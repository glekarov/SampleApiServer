package org.sample.server.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@EnableWebSecurity
@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    AuthEntryPoint authEntryPoint;

    @Autowired
    AuthRequestFilter authRequestFilter;

    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {
        // We don't need CSRF for this example
        httpSecurity.csrf().disable() // disable CSRF
                .authorizeRequests().antMatchers("/user/**").permitAll() // don't authenticate these requests
                .anyRequest().authenticated() // the rest need to be authenticated
                .and().exceptionHandling().authenticationEntryPoint(authEntryPoint).and().sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS); // make the session stateless, so it doesn't
        // store any user state

        // Add a filter to validate the tokens with every request
        httpSecurity.addFilterBefore(authRequestFilter, UsernamePasswordAuthenticationFilter.class);
    }
}
