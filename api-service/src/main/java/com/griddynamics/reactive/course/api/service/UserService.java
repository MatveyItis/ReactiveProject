package com.griddynamics.reactive.course.api.service;

import com.griddynamics.reactive.course.api.persistence.User;
import com.griddynamics.reactive.course.api.repository.UserInfoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserInfoRepository userInfoRepository;

    public Mono<User> getUserInfoById(String userId) {
        return userInfoRepository.findById(userId)
                .onErrorMap(e -> {
                    log.error("Got error while retrieving userInfo by Id");
                    return e;
                });
    }
}
