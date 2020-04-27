package com.bangstagram.user.domain.model.api.request;

import lombok.Getter;

@Getter
public class JoinRequest {
    private String name;
    private String loginEmail;
    private String loginPassword;

    public JoinRequest(String name, String loginEmail, String loginPassword) {
        this.name = name;
        this.loginEmail = loginEmail;
        this.loginPassword = loginPassword;
    }
}
