package music.festival;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;

@SpringBootApplication
public class DowntownApplication extends SpringBootServletInitializer {
    public static void main(String[] args) {
        DEV_MODE_DisableSSLVerification.disable();
        SpringApplication.run(DowntownApplication.class, args);
    }

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        return builder.sources(DowntownApplication.class);
    }
}
