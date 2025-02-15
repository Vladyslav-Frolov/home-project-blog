package com.softserveinc.ita.homeprojectblog.config;

import lombok.AccessLevel;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Qualifier("userDetailsServiceImpl")
    @NonNull
    UserDetailsService userDetailsService;

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .csrf().disable()
                .authorizeRequests()
                .antMatchers(HttpMethod.POST, "/api/*/users").permitAll()
                .antMatchers(HttpMethod.GET, "/api/*/tags").permitAll()
                .antMatchers(HttpMethod.GET, "/api/*/tags/{Id:\\d+}").permitAll()
                .antMatchers(HttpMethod.GET, "/api/*/posts/{Id:\\d+}").permitAll()
                .antMatchers(HttpMethod.GET, "/api/*/posts").permitAll()
                .antMatchers(HttpMethod.GET, "/api/*/comments/{Id:\\d+}").permitAll()
                .antMatchers(HttpMethod.GET, "/api/*/comments").permitAll()
                .anyRequest()
                .authenticated()
                .and()
                .httpBasic();
    }

    @Bean
    protected PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(12);
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) {
        auth.authenticationProvider(daoAuthenticationProvider());
    }

    @Bean
    protected DaoAuthenticationProvider daoAuthenticationProvider() {
        var daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());
        daoAuthenticationProvider.setUserDetailsService(userDetailsService);
        return daoAuthenticationProvider;
    }

}
