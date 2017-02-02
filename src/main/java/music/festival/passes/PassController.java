package music.festival.passes;

import music.festival.ConfigurationService;
import org.apache.tomcat.util.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.*;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
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
    private static final Logger logger = LoggerFactory.getLogger(PassController.class);
    @Autowired
    AccountRepository accountRepository;

    @Autowired
    ConfigurationService configurationService;
    private RestTemplate restTemplate;
    private HttpHeaders headers;


    private String cityWeeklyUri;

    private static SwapPassRequest buildSwapPassRequest(Account account) {
        SwapPassRequest swapPassRequest = new SwapPassRequest();
        swapPassRequest.setSerialNumber(account.getCityWeeklyTicketId());
        swapPassRequest.setBadgeNumber(account.getWristbandBadgeId());
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
        //Build URI
        cityWeeklyUri = "https://" + configurationService.cityWeeklyHostname() + "/api/merchant/badge/";

        // Build rest template
        RestTemplateBuilder restTemplateBuilder =
                new RestTemplateBuilder().rootUri(cityWeeklyUri);
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

    @PostMapping()
    public ResponseEntity<Account> swapBadge(@RequestBody Account account) {
        Account existingAccount = accountRepository.findByCityWeeklyTicketId(account.getCityWeeklyTicketId());
        if (existingAccount != null) {
            account.setId(existingAccount.getId());
        }

        //Get from City Weekly
        HttpEntity<SwapPassRequest> swapPassRequest = new HttpEntity<>(buildSwapPassRequest(account), headers);
        ResponseEntity<SwapPassResponse> swapPassResponseEntity = restTemplate.exchange("/doSwap/",
                HttpMethod.POST, swapPassRequest, SwapPassResponse.class);

        if (swapPassResponseEntity.getStatusCode() == HttpStatus.OK) {
            SwapPassResponse swapPassResponse = swapPassResponseEntity.getBody();
            if (swapPassResponse.getSuccess() == 1) {
                logger.info("POST swapPassResponse: " + swapPassResponse);
                account.setTicketType(swapPassResponse.getTicketType());
                account = accountRepository.save(account);
                return new ResponseEntity<>(account, HttpStatus.ACCEPTED);
            } else {
                logger.info("POST swapPassResponse return success == 0 (failed): " + swapPassResponse);
            }
        } else {
            logger.info("POST swapPassResponseEntity failed: " + swapPassResponseEntity);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @GetMapping("/{ticketId}")
    public ResponseEntity<Account> swapStatus(@PathVariable Long ticketId) {
        //Require ticketId to be a long so that Spring will sanitize the input for us.
        Account existingAccount = accountRepository.findByCityWeeklyTicketId(ticketId);
        Account account = new Account();
        if (existingAccount != null) {
            account.setId(existingAccount.getId());
        }

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
                    //Go down to after switch statement and continue from there
                    break;
                case 2:
                    logger.info("GET swapPassResponse return already swapped status: " + swapPassResponse);
                    return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
                case 3:
                default:
                    logger.info("GET swapPassResponse return redeemed, but not swapped status: " + swapPassResponse);
                    return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
            logger.info("GET swapPassResponse: " + swapPassResponse);
            account.setCityWeeklyTicketId(ticketId);
            account.setWristbandBadgeId(swapPassResponse.getBadgeNumber());
            account.setTicketType(swapPassResponse.getTicketType());
            account = accountRepository.save(account);
        } else {
            logger.info("GET swapPassResponse failed: " + swapPassResponseEntity);
        }

        if (account.getTicketType() != null && account.getWristbandBadgeId() != null) {
            account.setWristbandBadgeId(null); //Make this null so it does not get sent back to customer yet.
            return new ResponseEntity<>(account, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }
}
