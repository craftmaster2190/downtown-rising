package music.festival;

import music.festival.file.EditablePage;
import music.festival.file.EditablePageRepository;
import music.festival.user.Account;
import music.festival.user.AccountService;
import music.festival.user.RoleService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * Created by bryce_fisher on 1/26/17.
 */
@Component
public class InitialConfiguration {

    private static final Logger logger = LoggerFactory.getLogger(InitialConfiguration.class);

    @Autowired
    AccountService accountService;

    @Autowired
    RoleService roleService;

    @Autowired
    ConfigurationService configurationService;

    @Autowired
    EditablePageRepository editablePageRepository;

    @PostConstruct
    private void configureDefaultAdminAccount() {
        String adminName = "admin";
        if (!accountService.userExists(adminName)) {
            Account adminAccount = new Account();
            adminAccount.setName(adminName);
            adminAccount.setPassword(configurationService.defaultAdminPassword());
            adminAccount.setEmail(adminName);
            adminAccount.setRoles(roleService.getAdminRoles());
            logger.info("\n\t[\n\t\tAdmin user created with name / password: " +
                    adminAccount.getUsername() + " / " + adminAccount.getPassword() +
                    "\n\t]");
            accountService.saveAndUpdatePassword(adminAccount);
        }
    }

    @PostConstruct
    private void configureDefaultHomePage() {
        EditablePage home = editablePageRepository.findByPath("home");
        if (home == null) {
            home = new EditablePage();
            home.setPath("home");
            home.setHtml("<div class=\"col-xs-12 col-md-8 col-md-offset-2\">\n" +
                    "\t<img src=\"images/music-logo-trans-full.png\" class=\"rounded-block col-xs-12\">\n" +
                    "</div>");
            logger.info("Created default home page");
            editablePageRepository.save(home);
        }
    }
}
