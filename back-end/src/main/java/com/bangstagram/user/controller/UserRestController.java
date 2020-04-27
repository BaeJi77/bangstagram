package com.bangstagram.user.controller;

import com.bangstagram.user.domain.model.api.request.JoinRequest;
import com.bangstagram.user.domain.model.api.response.JoinResult;
import com.bangstagram.user.domain.model.user.User;
import com.bangstagram.user.service.UserService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("api")
public class UserRestController {
    private final UserService userService;

    public UserRestController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping(path = "user/exists")
    public Boolean checkExistedEmail(@RequestBody Map<String,String> request) {
        // Email
        String requestEmail = request.get("email");
        return userService.findByEmail(requestEmail).isPresent();
    }

    @PostMapping(path = "user/join")
    public JoinResult join(@RequestBody JoinRequest joinRequest) {
        User user = userService.join(joinRequest.getName(), joinRequest.getLoginEmail(), joinRequest.getLoginPassword());

        String apiToken = "";

        return new JoinResult(user, apiToken);
    }


}
