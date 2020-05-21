package com.bangstagram.user.configure.security;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.regex.Pattern;

import static java.util.stream.Collectors.toList;

/***
 * author: Hyo-Jin Kim
 * Date: 2020.05.20
 */

@Slf4j
public class JwtAuthenticationTokenFilter extends BasicAuthenticationFilter {
    @Value("${jwt.token.header}")
    private String tokenHeader;

    private static final Pattern BEARER = Pattern.compile("^Bearer$", Pattern.CASE_INSENSITIVE);

    private final JWT jwt;

    public JwtAuthenticationTokenFilter(AuthenticationManager authenticationManager, JWT jwt) {
        super(authenticationManager);
        this.jwt = jwt;
    }

    // endpoint every request hit with authorization
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        String jwtToken = getAuthenticationToken(request);

        if( jwtToken != null ) {
            JWT.Claims claims = jwt.verify(jwtToken);
            log.info("[JwtAuthenticationTokenFilter] claims: {}", claims);

            if( needRefresh(claims, 1000L * 60L) ) { // 10분
                log.info("[JwtAuthenticationTokenFilter] refresh token");
                String refreshJwtToken = jwt.refreshToken(jwtToken);
                response.setHeader(tokenHeader, refreshJwtToken);
            }

            Long userKey = claims.getUserKey();
            String email = claims.getEmail();
            String name = claims.getName();
            List<GrantedAuthority> authorities = getAuthorities(claims);

            JwtAuthenticationToken authentication =
                    new JwtAuthenticationToken(new JwtAuthentication(userKey,name,email),null, authorities);
            authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        // Continue filter execution
        chain.doFilter(request, response);
    }

    private String getAuthenticationToken(HttpServletRequest request)  {
        // Read the Authorization header, where the JWT Token should be
        String token = request.getHeader(tokenHeader);
        if( token != null ) {
            if( log.isDebugEnabled() )
                log.debug("[JwtAuthenticationTokenFiler] token: {} ", token);

            try {
                token = URLDecoder.decode(token, "UTF-8");
                String[] parts = token.split(" "); // Bearer ~encodedToken
                if( parts.length == 2 ) {
                    String bearer = parts[0]; // Bearer
                    String encodedToken = parts[1]; // encodedToken
                    return BEARER.matcher(bearer).matches() ? encodedToken : null;
                }

            } catch (UnsupportedEncodingException e) {
                log.error(e.getMessage(), e);
            }
        }

        return null;
    }

    private boolean needRefresh(JWT.Claims claims, long rangeOfRefreshMillis) {
        long exp = claims.getExpiresAt().getTime();
        if( exp > 0 ) {
            long remain = exp - System.currentTimeMillis();
            return remain < rangeOfRefreshMillis ? true : false;
        }
        return false;
    }

    private List<GrantedAuthority> getAuthorities(JWT.Claims claims) {
        String[] roles = claims.getRoles();
        return roles.length == 0 || roles == null ?
                Collections.emptyList() :
                Arrays.stream(roles).map(SimpleGrantedAuthority::new).collect(toList());
    }
}