package music.festival;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.HttpMessageConverters;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.http.converter.xml.MappingJackson2XmlHttpMessageConverter;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;

/**
 * Created by bryce_fisher on 1/16/17.
 */
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    ConfigurationService configurationService;

    @Override
    public void configure(final HttpSecurity http) throws Exception {
        http.authorizeRequests().anyRequest().permitAll();
        http.csrf().csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse());

        if (configurationService.isViewRegistrationAuthSet()) {
            http.antMatcher("/list/**").authorizeRequests().anyRequest().hasRole("REPORTER")
                    .and().httpBasic().realmName("Utah Music Festival");
        } else {
            http.antMatcher("/list/**").authorizeRequests().anyRequest().denyAll();
        }
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        if (configurationService.isViewRegistrationAuthSet()) {
            auth.inMemoryAuthentication()
                    .withUser(configurationService.viewRegistrationUsername())
                    .password(configurationService.viewRegistrationPassword())
                    .roles("REPORTER");
        }
    }

    @Bean
    public HttpMessageConverters customConverters() {
        return new HttpMessageConverters(
                new MappingJackson2HttpMessageConverter(),
                new MappingJackson2XmlHttpMessageConverter());
    }
}
