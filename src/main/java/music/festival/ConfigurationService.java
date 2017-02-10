package music.festival;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * Using a service to control properties is better than @Value and properties file
 * because it provides compile time checking and the ide can do usage checks as needed.
 * <p>
 * https://tuhrig.de/why-using-springs-value-annotation-is-bad/
 * <p>
 * Created by bryce_fisher on 1/20/17.
 */
@Service
public class ConfigurationService {
    @Value("${music.festival.admin.password:#{null}}")
    private String defaultAdminPassword;

    @Value("${music.festival.cityweekly.rest.username:#{null}}")
    private String cityWeeklyBasicAuthUsername;

    @Value("${music.festival.cityweekly.rest.password:#{null}}")
    private String cityWeeklyBasicAuthPassword;

    @Value("${music.festival.cityweekly.default.timeout:60000}")
    private int defaultTimeout;
    @Value("${music.festival.cityweekly.hostname:#{null}}")
    private String cityWeeklyHostname;

    public boolean isBasicAuthSet() {
        return cityWeeklyBasicAuthPassword() != null &&
                cityWeeklyBasicAuthUsername() != null;
    }

    public String cityWeeklyBasicAuthUsername() {
        return cityWeeklyBasicAuthUsername;
    }

    public String cityWeeklyBasicAuthPassword() {
        return cityWeeklyBasicAuthPassword;
    }

    public int defaultTimeout() {
        return defaultTimeout;
    }

    public String cityWeeklyHostname() {
        if (cityWeeklyHostname == null)
            throw new NullPointerException("Expected music.festival.cityweekly.hostname in properties");
        return cityWeeklyHostname;
    }
}
