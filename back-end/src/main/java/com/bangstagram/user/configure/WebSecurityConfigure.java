package com.bangstagram.user.configure;

import com.bangstagram.user.security.JWT;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
public class WebSecurityConfigure extends WebSecurityConfigurerAdapter {

    @Value("${jwt.token.issuer}")
    String issuer;
    @Value("${jwt.token.secret}")
    String secret;
    @Value("${jwt.token.expirySeconds}")
    int expirySeconds;

    @Bean
    public JWT jwt() {
        return new JWT(issuer, secret, expirySeconds);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf()
                .disable()
                .headers()
                .disable()
                .exceptionHandling()
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests()
                .antMatchers("/api/user/join").permitAll()
                .antMatchers("/api/user/exists").permitAll()
                .antMatchers("/api/user/login").permitAll()
                .antMatchers("/api/**").hasRole("USER")
                .anyRequest().permitAll()
                .and()
                .formLogin()
                .disable();
    }

    @Override
    public void configure(WebSecurity web) {
        web.ignoring().antMatchers("/swagger-resources", "/webjars/**", "/templates/**", "/h2/**");
    }

}
