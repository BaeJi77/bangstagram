package com.bangstagram.user.service;

import com.bangstagram.common.exception.AlreadyExistsException;
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

import java.util.Optional;

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
        User user = findByEmail(joinRequestDto.getEmail()).orElse(null);
        if( user != null )
            throw new AlreadyExistsException("이미 가입한 계정이 있습니다. userEmail: " + user.getEmail());

        user = save(joinRequestDto.newUser(passwordEncoder));

        return new JoinResponseDto(user);
    }

    private User save(User user) {
        return userRepository.save(user);
    }

    @Transactional(readOnly = true)
    public CheckEmailResponseDto existsByEmail(String email) {
        return new CheckEmailResponseDto(userRepository.existsByEmail(email));
    }

    @Transactional(readOnly = true)
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Transactional
    public AuthResponseDto login(AuthRequestDto authRequestDto) {
        String email = authRequestDto.getPrincipal();
        String password = authRequestDto.getCredentials();

        User user = findByEmail(email).orElseThrow(() -> new DoNotExistException("요청하신 사용자가 없습니다."));
        user.login(passwordEncoder, password);
        user.afterLoginSuccess();

        String jwtToken = user.newJwtToken(jwt, new String[]{"ROLE_USER"});

        return new AuthResponseDto(user, jwtToken);
    }

    @Transactional
    public AuthResponseDto authLogin(String email) {
        User user = findByEmail(email).orElseThrow(() -> new DoNotExistException("요청하신 사용자가 없습니다."));
        user.afterLoginSuccess();

        String jwtToken = user.newJwtToken(jwt, new String[]{"ROLE_USER"});

        return new AuthResponseDto(user, jwtToken);
    }
}
