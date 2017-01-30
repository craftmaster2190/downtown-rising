package music.festival.passes;

import music.festival.ConfigurationService;
import music.festival.user.Account;
import music.festival.user.AccountService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;
import java.util.List;

/**
 * Created by bryce_fisher on 1/13/17.
 */
@RestController
@RequestMapping("/pass")
public class PassController {
    private static final String CITY_WEEKLY_URI = "https://textizi.com/api/merchant/badge/";
    private static final Logger logger = LoggerFactory.getLogger(PassController.class);
    @Autowired
    PassRepository passRepository;
    @Autowired
    AccountService accountService;
    @Autowired
    ConfigurationService configurationService;
    RestTemplate restTemplate;

    private static SwapPassRequest buildSwapPassRequest(Pass pass) {
        SwapPassRequest swapPassRequest = new SwapPassRequest();
        swapPassRequest.setSerialNumber(pass.getCityWeeklyTicketId());
        swapPassRequest.setBadgeNumber(pass.getWristbandBadgeId());
        Account account = pass.getAccount();
        swapPassRequest.setFirstName(account.getFirstName());
        swapPassRequest.setMiddleInitial(account.getMiddleInitial());
        swapPassRequest.setLastName(account.getLastName());
        swapPassRequest.setAddress1(account.getAddress1());
        swapPassRequest.setAddress2(account.getAddress2());
        swapPassRequest.setCity(account.getCity());
        swapPassRequest.setState(account.getState());
        swapPassRequest.setZip(account.getZip());
        swapPassRequest.setEmail(account.getEmail());
        swapPassRequest.setPhone(account.getPhone());
        swapPassRequest.setPhoneType(account.getPhoneType());
        logger.info("buildSwapPassRequest: " + swapPassRequest);
        return swapPassRequest;
    }

    @PostConstruct
    private void configureBasicAuth() {
        RestTemplateBuilder restTemplateBuilder = new RestTemplateBuilder().rootUri(CITY_WEEKLY_URI);
        if (configurationService.isBasicAuthSet()) {
            restTemplateBuilder.basicAuthorization(
                    configurationService.cityWeeklyBasicAuthUsername(),
                    configurationService.cityWeeklyBasicAuthPassword());
        }

        restTemplate = restTemplateBuilder.build();
    }

    @GetMapping
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<List<Pass>> get(@AuthenticationPrincipal Account account) {
        //Reload account from database, don't depend on data from security context
        account = accountService.findById(account.getId());
        List<Pass> passes = passRepository.findByAccount(account);
        if (passes != null && !passes.isEmpty())
            return new ResponseEntity<>(passes, HttpStatus.OK);
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PostMapping("/{ticketId}")
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<Pass> swapBadge(@PathVariable Long ticketId, @AuthenticationPrincipal Account account) {
        //Require ticketId to be a long so that Spring will sanitize the input for us.
        Pass pass = passRepository.findByCityWeeklyTicketId(ticketId + "");
        if (pass == null) {
            pass = new Pass();
            pass.setAccount(accountService.findById(account.getId()));
            pass.setCityWeeklyTicketId(ticketId + "");
            pass = passRepository.save(pass);
        }
        //Get from City Weekly
        SwapPassRequest swapPassRequest = buildSwapPassRequest(pass);
        ResponseEntity<String> swapPassResponseEntity =
                restTemplate.postForEntity("/doSwap", swapPassRequest, String.class);

        logger.info("POST swapPassResponse: " + swapPassResponseEntity);

        if (swapPassResponseEntity.getStatusCode() == HttpStatus.OK) {
            //TODO handle attachPassResponse
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @GetMapping("/{ticketId}")
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<Pass> swapStatus(@PathVariable Long ticketId, @AuthenticationPrincipal Account account) {
        //Get from City Weekly
        ResponseEntity<String> swapPassResponseEntity =
                restTemplate.getForEntity("/swapStatus/" + ticketId, String.class);

        logger.info("GET swapPassResponse: " + swapPassResponseEntity);

        if (swapPassResponseEntity.getStatusCode() == HttpStatus.OK) {
            //TODO handle attachPassResponse
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }
}
