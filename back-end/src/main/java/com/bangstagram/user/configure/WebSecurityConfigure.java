package com.bangstagram.user.configure;

import com.bangstagram.user.domain.model.oauth.kakao.KakaoLoginApi;
import com.bangstagram.user.domain.model.oauth.kakao.KakaoProfileApi;
import com.bangstagram.user.domain.model.oauth.naver.NaverLoginApi;
import com.bangstagram.user.domain.model.oauth.naver.NaverProfileApi;
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
    private String issuer;

    @Value("${jwt.token.secret}")
    private String secret;

    @Value("${jwt.token.expirySeconds}")
    private int expirySeconds;

    @Bean
    public JWT jwt() {
        return new JWT(issuer, secret, expirySeconds);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Value("${oauth.naver.clientId}")
    private String naverClientId;

    @Value("${oauth.naver.clientSecret}")
    private String naverClientSecret;

    @Value("${oauth.naver.tokenRequestUrl}")
    private String naverTokenRequestUrl;

    @Value("${oauth.naver.profileRequestUrl}")
    private String naverProfileRequestUrl;

    @Bean
    public NaverLoginApi naverLoginApi() {
        return new NaverLoginApi(naverClientId,naverClientSecret,naverTokenRequestUrl);
    }

    @Bean
    public NaverProfileApi naverProfileApi() {
        return new NaverProfileApi(naverProfileRequestUrl);
    }


    @Value("${oauth.kakao.clientId}")
    private String kakaoClientId;

    @Value("${oauth.kakao.tokenRequestUrl}")
    private String kakaoLoginTokenUrl;

    @Value("${oauth.kakao.profileRequestUrl}")
    private String kakaoProfileInfoUrl;

    @Bean
    public KakaoLoginApi kakaoLoginApi() {
        return new KakaoLoginApi(kakaoClientId,kakaoLoginTokenUrl);
    }

    @Bean
    public KakaoProfileApi kakaoProfileApi() {
        return new KakaoProfileApi(kakaoProfileInfoUrl);
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
                .antMatchers("/**").hasRole("USER_ROLE")
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
