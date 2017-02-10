package music.festival;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;

/**
 * Created by bryce_fisher on 1/27/17.
 */
enum DEV_MODE_DisableSSLVerification {
    ;
    private static final Logger logger =
            LoggerFactory.getLogger(DEV_MODE_DisableSSLVerification.class);

    static void disable() {
        try {
            // Create a trust manager that does not validate certificate chains
            final TrustManager[] trustAllCerts = {
                    new NaiveX509TrustManager()
            };

            // Install the all-trusting certificate manager
            final SSLContext sc = SSLContext.getInstance("SSL");
            sc.init(null, trustAllCerts, new SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
            logger.info("Trusting all SSL Certificates");

            // Create all-trusting host name verifier
            final HostnameVerifier allHostsValid = (hostname, session) -> true;

            // Install the all-trusting host verifier
            HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);
            logger.info("Trusting all SSL Hosts");
        } catch (NoSuchAlgorithmException | KeyManagementException e) {
            throw new RuntimeException(e);
        }
    }

    private static class NaiveX509TrustManager implements X509TrustManager {
        public X509Certificate[] getAcceptedIssuers() {
            return null;
        }

        public void checkClientTrusted(final X509Certificate[] x509Certificates, final String authType) {
        }

        public void checkServerTrusted(final X509Certificate[] x509Certificates, final String authType) {
        }
    }
}
