package com.bangstagram.user.configure.security;

import com.bangstagram.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/***
 * author: Hyo-Jin Kim
 * Date: 2020.05.01
 */

@Configuration
@EnableConfigurationProperties({JwtProperty.class})
@EnableWebSecurity
public class WebSecurityConfigure extends WebSecurityConfigurerAdapter {
    private final JwtProperty jwtProperty;

    public WebSecurityConfigure(JwtProperty jwtProperty) {
        this.jwtProperty = jwtProperty;
    }

    @Bean
    public JWT jwt() {
        return new JWT(jwtProperty.getIssuer(), jwtProperty.getSecret(), jwtProperty.getExpirySeconds());
    }

    @Autowired
    public void configureAuthentication(AuthenticationManagerBuilder builder, JwtAuthenticationProvider jwtAuthenticationProvider) {
        builder.authenticationProvider(jwtAuthenticationProvider);
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Bean
    public JwtAuthenticationProvider jwtAuthenticationProvider(UserService userService) {
        return new JwtAuthenticationProvider(userService);
    }

    @Bean
    public JwtAuthenticationTokenFilter jwtAuthenticationTokenFilter(AuthenticationManager authenticationManager, JWT jwt) {
        return new JwtAuthenticationTokenFilter(authenticationManager, jwt);
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
                    .antMatchers("/users/join").permitAll()
                    .antMatchers("/users/exists").permitAll()
                    .antMatchers("/users/login").permitAll()
                    .antMatchers("/oauth/naver").permitAll()
                    .antMatchers("/oauth/kakao").permitAll()
                    .antMatchers("/rooms/**").permitAll()
                    .antMatchers("/**").hasRole("USER")
                    .anyRequest().permitAll()
                    .and()
                .formLogin()
                    .disable();
         http
                .addFilterBefore(jwtAuthenticationTokenFilter(authenticationManagerBean(), jwt()), UsernamePasswordAuthenticationFilter.class);
    }

    @Override
    public void configure(WebSecurity web) {
        web.ignoring().antMatchers("/swagger-resources", "/webjars/**", "/templates/**", "/h2/**");
    }

}
