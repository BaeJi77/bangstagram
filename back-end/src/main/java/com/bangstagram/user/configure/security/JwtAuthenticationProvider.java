package com.bangstagram.user.configure.security;

import com.bangstagram.common.exception.DoNotExistException;
import com.bangstagram.user.controller.dto.request.AuthRequestDto;
import com.bangstagram.user.controller.dto.response.AuthResponseDto;
import com.bangstagram.user.service.UserService;
import org.apache.commons.lang3.ClassUtils;
import org.springframework.dao.DataAccessException;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public class JwtAuthenticationProvider implements AuthenticationProvider {
    private final UserService userService;

    public JwtAuthenticationProvider(UserService userService) {
        this.userService = userService;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        try {
            // 로그인 인증 전
            JwtAuthenticationToken authenticationToken = (JwtAuthenticationToken) authentication;
            String principal = (String) authenticationToken.getPrincipal();
            String credential = authenticationToken.getCredentials();

            AuthResponseDto authResponseDto = userService.login(new AuthRequestDto(principal, credential));

            // 로그인 인증 후
            JwtAuthenticationToken authenticated
                    = new JwtAuthenticationToken(new JwtAuthentication(authResponseDto.getUser()), null, AuthorityUtils.createAuthorityList("ROLE_USER"));
            authenticated.setDetails(authResponseDto);

            return authenticated;
        } catch(DoNotExistException e) {
            throw new UsernameNotFoundException(e.getMessage());
        } catch(IllegalArgumentException e) {
            throw new BadCredentialsException(e.getMessage());
        } catch(DataAccessException e) {
            throw new AuthenticationServiceException(e.getMessage());
        }
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return ClassUtils.isAssignable(JwtAuthenticationToken.class, authentication);
    }
}
