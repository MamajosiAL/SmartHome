package com.ucll.smarthome.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;


@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    // pw encoder voor eventueel de in-memory Auth indien nodig
    private final BCryptPasswordEncoder passwordEncoder;

    private final UserDetailService userDetailService;

    public SecurityConfig(BCryptPasswordEncoder passwordEncoder, UserDetailService userDetailService ) {
        this.passwordEncoder = passwordEncoder;
        this.userDetailService = userDetailService;
    }

    // authorization moet nog toegepast worden.
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.httpBasic();
    }

    @Override
    public void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailService);
    }
}
