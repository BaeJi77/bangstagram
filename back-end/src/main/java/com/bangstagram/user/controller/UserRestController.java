package com.bangstagram.user.controller;

import com.bangstagram.user.controller.dto.request.AuthRequestDto;
import com.bangstagram.user.controller.dto.request.JoinRequestDto;
import com.bangstagram.user.controller.dto.response.AuthResponseDto;
import com.bangstagram.user.controller.dto.response.JoinResponseDto;
import com.bangstagram.user.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.Map;

@RestController
@Slf4j
public class UserRestController {
    private final UserService userService;

    public UserRestController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/users/exists")
    public boolean checkUserExists(@RequestBody Map<String, String> request) {
        return userService.existsByEmail(request.get("email"));
    }

    @PostMapping("/users/join")
    public JoinResponseDto join(@RequestBody @Valid JoinRequestDto joinRequestDto) {
        return userService.join(joinRequestDto);
    }

    @PostMapping("/users/login")
    public AuthResponseDto login(@RequestBody @Valid AuthRequestDto authRequestDto) {
        log.info("[login]: {}", authRequestDto.toString());
        return userService.login(authRequestDto);
    }
}
