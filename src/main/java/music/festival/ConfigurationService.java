package music.festival;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.time.Duration;

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
    private static final long timeBetweenCheckingToNag = Duration.ofMinutes(15).toMillis();
    @Value("${music.festival.admin.password:#{null}}")
    private String defaultAdminPassword;

    @Value("${music.festival.cityweekly.rest.username:#{null}}")
    private String cityWeeklyBasicAuthUsername;

    @Value("${music.festival.cityweekly.rest.password:#{null}}")
    private String cityWeeklyBasicAuthPassword;

    private int minLengthOfUsername = 4;
    private int minLengthOfPassword = 8;

    public long timeBetweenCheckingToNag() {
        return timeBetweenCheckingToNag;
    }

    public String defaultAdminPassword() {
        if (defaultAdminPassword == null) {
            return generateDefaultPassword(20);
        }
        return defaultAdminPassword;
    }

    /**
     * Generate a random alphanumeric password with dashes between every 5 characters
     *
     * @param maxLength
     * @return
     */
    private String generateDefaultPassword(int maxLength) {
        SecureRandom random = new SecureRandom();
        String password = new BigInteger(130, random).toString(Character.MAX_RADIX);
        if (password.length() > maxLength)
            password = password.substring(0, maxLength);
        // Insert dashes every 5 characters
        return String.join("-", password.split("(?<=\\G.{5})"));
    }

    public int minLengthOfUsername() {
        return minLengthOfUsername;
    }

    public int minLengthOfPassword() {
        return minLengthOfPassword;
    }

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
}
