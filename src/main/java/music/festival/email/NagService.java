package music.festival.email;

import music.festival.ConfigurationService;
import music.festival.user.Account;
import music.festival.user.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

/**
 * Created by bryce_fisher on 1/20/17.
 */
@Service
public class NagService {
    @Autowired
    ConfigurationService configurationService;
    @Autowired
    EmailService emailService;
    @Autowired
    AccountService accountService;
    private Thread nagger;

    @PostConstruct
    private void init() {
        nagger = new Thread(buildNagger());
        nagger.setName("NaggerThread");
        nagger.setDaemon(true);
        nagger.start();
    }

    private Runnable buildNagger() {
        return () -> {
            try {
                while (true) {
                    Thread.sleep(configurationService.timeBetweenCheckingToNag());
                    sendNags();
                }
            } catch (InterruptedException e) {
                return;
            }
        };
    }

    private void sendNags() throws InterruptedException {
        while (!Thread.currentThread().isInterrupted()) {
            //TODO
            // emailService.send(account, "Registration", buildNagEmail(account));
            break;
        }
    }

    private String buildNagEmail(Account account) {
        //TODO
        return "";
    }

    @PreDestroy
    private void destroy() {
        if (nagger != null && nagger.isAlive())
            nagger.interrupt();
    }
}
