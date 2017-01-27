package music.festival.passes;

import music.festival.ConfigurationService;
import music.festival.user.Account;
import music.festival.user.AccountService;
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
    @Autowired
    PassRepository passRepository;
    @Autowired
    AccountService accountService;
    @Autowired
    ConfigurationService configurationService;

    RestTemplate restTemplate;

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
    public List<Pass> get(@AuthenticationPrincipal Account account) {
        //Reload account from database, don't depend on data from security context
        account = accountService.findById(account.getId());
        return passRepository.findByAccount(account.getId());
    }

    @PostMapping("/{ticketId}")
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<Pass> swapBadge(@PathVariable String ticketId) {
        Pass pass = passRepository.findByCityWeeklyTicketId(ticketId);
        if (pass == null) {
            //Get from City Weekly
            SwapPassRequest swapPassRequest = new SwapPassRequest();
            ResponseEntity<SwapPassResponse> swapPassResponseEntity =
                    restTemplate.postForEntity("/doSwap", swapPassRequest, SwapPassResponse.class);

            if (swapPassResponseEntity.getStatusCode() == HttpStatus.OK) {
                //TODO handle attachPassResponse
            }
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(pass, HttpStatus.ALREADY_REPORTED);
    }

    @GetMapping("/{ticketId}")
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<Pass> swapStatus(@PathVariable String ticketId) {

        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }
}
