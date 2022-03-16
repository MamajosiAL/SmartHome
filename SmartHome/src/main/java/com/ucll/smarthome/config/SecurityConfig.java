package com.ucll.smarthome.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;


@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private static final String LOGIN_URL = "/vaadin/login";
    private static final String LOGIN_FAILURE_URL = "/vaadin/login?error";
    private static final String LOGOUT_SUCCESS_URL = "/vaadin/login";

    private static final String HOME_URL = "/vaadin/houses";
    // pw encoder voor eventueel de in-memory Auth indien nodig
    private final BCryptPasswordEncoder passwordEncoder;
    private final UserDetailService userDetailService;

    @Autowired
    public SecurityConfig(BCryptPasswordEncoder passwordEncoder, UserDetailService userDetailService ) {
        this.passwordEncoder = passwordEncoder;
        this.userDetailService = userDetailService;
    }
    @Override
    public void configure(WebSecurity web) {
        // BYPASS Spring Security for all these:
        web.ignoring().antMatchers(
                // HTML files (index, swagger-ui, offline, accessdenied, ...) located at the root
                "/*.html",
                // For Vaadin assets.
                "/VAADIN/**",
                "/favicon.ico",
                "/robots.txt",
                "/manifest.webmanifest",
                "/sw.js",
                "/icons/**",
                "/img/**",
                "/styles/**",
                "/frontend/**",
                "/webjars/**",
                // Health checks
                "/health",
                "/health/**",
                // For Swagger/OpenAPI V3
                "/v3/api-docs/**",
                "/swagger-ui/**");
    }

    // authorization moet nog toegepast worden.
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.httpBasic().and()
                .cors()
                .and().authorizeRequests().antMatchers("/login").permitAll()
                .and().logout().logoutUrl("/login/logout").invalidateHttpSession(true).deleteCookies("JSESSIONID")
                .and()
                .formLogin()
                .loginPage(LOGIN_URL).permitAll()
                .loginProcessingUrl(LOGIN_URL)
                .failureUrl(LOGIN_FAILURE_URL)
                .defaultSuccessUrl(HOME_URL)
                .and()
                .logout()
                .logoutSuccessUrl(LOGOUT_SUCCESS_URL)
                .and().csrf()
                .disable();
    }


    @Override
    public void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailService);
    }
}
