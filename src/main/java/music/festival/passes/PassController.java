package music.festival.passes;

import music.festival.ConfigurationService;
import music.festival.user.Account;
import music.festival.user.AccountService;
import org.apache.tomcat.util.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.*;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
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
    private RestTemplate restTemplate;
    private HttpHeaders headers;

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
        // Build rest template
        RestTemplateBuilder restTemplateBuilder =
                new RestTemplateBuilder().rootUri(CITY_WEEKLY_URI);
        restTemplateBuilder.setConnectTimeout(configurationService.defaultTimeout());
        restTemplateBuilder.setReadTimeout(configurationService.defaultTimeout());
        restTemplate = restTemplateBuilder.build();

        // Allow server to send text/html and treat it like JSON
        List<HttpMessageConverter<?>> converters = restTemplate.getMessageConverters();
        for (HttpMessageConverter<?> converter : converters) {
            if (converter instanceof MappingJackson2HttpMessageConverter) {
                try {
                    List<MediaType> supportedMediaTypes = converter.getSupportedMediaTypes();
                    supportedMediaTypes = new ArrayList<>(supportedMediaTypes);
                    supportedMediaTypes.add(MediaType.TEXT_HTML);
                    ((MappingJackson2HttpMessageConverter) converter).setSupportedMediaTypes(supportedMediaTypes);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        }

        // Build headers
        headers = new HttpHeaders();
        if (configurationService.isBasicAuthSet()) {
            String rawBasicAuthenticationString = configurationService.cityWeeklyBasicAuthUsername() + ':' +
                    configurationService.cityWeeklyBasicAuthPassword();
            byte[] base64AuthenticationBytes = Base64.encodeBase64(rawBasicAuthenticationString.getBytes());
            headers.add(HttpHeaders.AUTHORIZATION, "Basic " + new String(base64AuthenticationBytes));
        }
        headers.add(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE);
        headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
    }

    @GetMapping
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<List<Pass>> get(@AuthenticationPrincipal Account account) {
        //Reload account from database, don't depend on data from security context
        account = accountService.findById(account.getId());
        List<Pass> passes = passRepository.findByAccountAndTicketTypeNotNull(account);
        if (passes != null && !passes.isEmpty())
            return new ResponseEntity<>(passes, HttpStatus.OK);
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PostMapping("/{ticketId}")
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<Pass> swapBadge(@PathVariable Long ticketId, @AuthenticationPrincipal Account account) {
        //Reload account from database, don't depend on data from security context
        account = accountService.findById(account.getId());

        //Require ticketId to be a long so that Spring will sanitize the input for us.
        Pass pass = passRepository.findByCityWeeklyTicketId(ticketId + "");
        if (pass == null) {
            pass = new Pass();
            pass.setAccount(accountService.findById(account.getId()));
            pass.setCityWeeklyTicketId(ticketId + "");
            pass = passRepository.save(pass); //Save so we generate a Badge ID.
        }
        //Get from City Weekly
        HttpEntity<SwapPassRequest> swapPassRequest = new HttpEntity<>(buildSwapPassRequest(pass), headers);
        ResponseEntity<SwapPassResponse> swapPassResponseEntity = restTemplate.exchange("/doSwap/",
                HttpMethod.POST, swapPassRequest, SwapPassResponse.class);

        if (swapPassResponseEntity.getStatusCode() == HttpStatus.OK) {
            SwapPassResponse swapPassResponse = swapPassResponseEntity.getBody();
            if (swapPassResponse.getSuccess() == 1) {
                pass.setTicketType(swapPassResponse.getTicketType());
                pass = passRepository.save(pass);
                return new ResponseEntity<>(pass, HttpStatus.ACCEPTED);
            } else {
                logger.info("POST swapPassResponse return success == 0 (failed): " + swapPassResponse);
            }
        } else {
            logger.info("POST swapPassResponseEntity failed: " + swapPassResponseEntity);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @GetMapping("/{ticketId}")
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<Pass> swapStatus(@PathVariable Long ticketId, @AuthenticationPrincipal Account account) {
        //Reload account from database, don't depend on data from security context
        account = accountService.findById(account.getId());

        //Require ticketId to be a long so that Spring will sanitize the input for us.
        // Check if we already have this pass' swap status, no reason to recheck if we have it.
        Pass pass = passRepository.findByCityWeeklyTicketId(ticketId + "");

        if (pass == null || pass.getTicketType() == null) {
            //Get from City Weekly
            HttpEntity<SwapPassRequest> swapPassRequest = new HttpEntity<>(headers);
            ResponseEntity<SwapPassResponse> swapPassResponseEntity = restTemplate.exchange("/swapStatus/" + ticketId,
                    HttpMethod.GET, swapPassRequest, SwapPassResponse.class);

            if (swapPassResponseEntity.getStatusCode() == HttpStatus.OK) {
                SwapPassResponse swapPassResponse = swapPassResponseEntity.getBody();
                switch (swapPassResponse.getStatus()) {
                    case -1:
                    case 0:
                        logger.info("GET swapPassResponse return failure status: " + swapPassResponse);
                        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
                    case 1:
                        logger.info("GET swapPassResponse return valid not swapped status: " + swapPassResponse);
                        return swapBadge(ticketId, account);
                    case 2:
                        //Go down to after switch statement and continue from there
                        break;
                    case 3:
                    default:
                        logger.info("GET swapPassResponse return redeemed, but not swapped status: " + swapPassResponse);
                        return swapBadge(ticketId, account);
                }
                pass = new Pass();
                pass.setAccount(accountService.findById(account.getId()));
                pass.setCityWeeklyTicketId(ticketId + "");
                pass.setWristbandBadgeId(swapPassResponse.getBadgeNumber());
                pass.setTicketType(swapPassResponse.getTicketType());
                pass = passRepository.save(pass);
            } else {
                logger.info("GET swapPassResponse failed: " + swapPassResponseEntity);
            }
        }
        if (pass != null && pass.getTicketType() != null) {
            return new ResponseEntity<>(pass, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }
}
