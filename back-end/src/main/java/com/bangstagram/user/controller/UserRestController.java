package com.bangstagram.user.controller;

import com.bangstagram.user.configure.security.JwtAuthenticationToken;
import com.bangstagram.user.controller.dto.request.AuthRequestDto;
import com.bangstagram.user.controller.dto.request.CheckEmailRequestDto;
import com.bangstagram.user.controller.dto.request.JoinRequestDto;
import com.bangstagram.user.controller.dto.response.AuthResponseDto;
import com.bangstagram.user.controller.dto.response.CheckEmailResponseDto;
import com.bangstagram.user.controller.dto.response.JoinResponseDto;
import com.bangstagram.user.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/***
 * author: Hyo-Jin Kim
 * Date: 2020.05.08
 */

@RestController
@Slf4j
public class UserRestController {
    private final AuthenticationManager authenticationManager;

    private final UserService userService;

    public UserRestController(AuthenticationManager authenticationManager, UserService userService) {
        this.authenticationManager = authenticationManager;
        this.userService = userService;
    }

    @PostMapping("/users/exists")
    public CheckEmailResponseDto checkUserExists(@RequestBody @Valid CheckEmailRequestDto request) {
        log.info("[Users Exists]: {}", request.getEmail());

        return userService.existsByEmail(request.getEmail());
    }

    @PostMapping("/users/join")
    public JoinResponseDto join(@RequestBody @Valid JoinRequestDto joinRequestDto) {
        log.info("[Users Join]: {}", joinRequestDto.toString());

        return userService.join(joinRequestDto);
    }

    @PostMapping("/users/login")
    public AuthResponseDto login(@RequestBody @Valid AuthRequestDto authRequestDto) {
        log.info("[Users Login]: {}", authRequestDto.toString());

        JwtAuthenticationToken authToken = new JwtAuthenticationToken(authRequestDto.getPrincipal(), authRequestDto.getCredentials());
        Authentication authentication = authenticationManager.authenticate(authToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        return (AuthResponseDto) authentication.getDetails();
    }
}
