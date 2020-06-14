package com.bangstagram.user.service;

import com.bangstagram.common.exception.DoNotExistException;
import com.bangstagram.user.configure.security.JWT;
import com.bangstagram.user.controller.dto.request.AuthRequestDto;
import com.bangstagram.user.controller.dto.request.JoinRequestDto;
import com.bangstagram.user.controller.dto.response.AuthResponseDto;
import com.bangstagram.user.controller.dto.response.CheckEmailResponseDto;
import com.bangstagram.user.controller.dto.response.JoinResponseDto;
import com.bangstagram.user.domain.model.user.User;
import com.bangstagram.user.domain.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.google.common.base.Preconditions.checkNotNull;

/***
 * author: Hyo-Jin Kim
 * Date: 2020.05.08
 */

@Slf4j
@Service
public class UserService {
    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final JWT jwt;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, JWT jwt) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwt = jwt;
    }

    @Transactional
    public JoinResponseDto join(JoinRequestDto joinRequestDto) {
        User user = save(joinRequestDto.newUser(passwordEncoder));

        return new JoinResponseDto(user);
    }

    private User save(User user) {
        return userRepository.save(user);
    }

    @Transactional(readOnly = true)
    public CheckEmailResponseDto existsByEmail(String email) {
        checkNotNull(email, "email must be provided.");

        return new CheckEmailResponseDto(userRepository.existsByEmail(email));
    }

    @Transactional(readOnly = true)
    public User findByEmail(String email) {
        return userRepository.findByEmail(email).orElseThrow(() -> new DoNotExistException("user not found."));
    }

    @Transactional
    public AuthResponseDto login(AuthRequestDto authRequestDto) {
        String email = authRequestDto.getPrincipal();
        String password = authRequestDto.getCredentials();

        User user = findByEmail(email);
        user.login(passwordEncoder, password);
        user.afterLoginSuccess();

        String jwtToken = user.newJwtToken(jwt, new String[]{"ROLE_USER"});

        return new AuthResponseDto(user, jwtToken);
    }

    @Transactional
    public AuthResponseDto authLogin(String email) {
        checkNotNull(email, "email must be provided.");

        User user = findByEmail(email);
        user.afterLoginSuccess();

        String jwtToken = user.newJwtToken(jwt, new String[]{"ROLE_USER"});

        return new AuthResponseDto(user, jwtToken);
    }
}
