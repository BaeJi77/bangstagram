package com.bangstagram.user.controller.dto.response;

import com.bangstagram.user.domain.model.user.User;
import lombok.Getter;

import java.util.StringJoiner;

/***
 * author: Hyo-Jin Kim
 * Date: 2020.05.01
 */

@Getter
public class JoinResponseDto {
    private final User user;

    public JoinResponseDto(User user) {
        this.user = user;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", JoinResponseDto.class.getSimpleName() + "[", "]")
                .add("user=" + user)
                .toString();
    }
}
