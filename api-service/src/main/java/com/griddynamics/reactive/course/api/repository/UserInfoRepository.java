package com.griddynamics.reactive.course.api.repository;

import com.griddynamics.reactive.course.api.persistence.User;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserInfoRepository extends ReactiveMongoRepository<User, String> {

}
