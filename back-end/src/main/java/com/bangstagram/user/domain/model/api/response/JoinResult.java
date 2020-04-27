package com.bangstagram.user.domain.model.api.response;

import com.bangstagram.user.domain.model.user.User;
import lombok.Getter;

@Getter
public class JoinResult {
    private final User user;

    private final String apiToken;

    public JoinResult(User user, String apiToken) {
        this.user = user;
        this.apiToken = apiToken;
    }
}
