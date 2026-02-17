package com.matrimony.auth.repository;

import com.matrimony.auth.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, String> {

    Optional<User> findByUserName(String userName);

    Optional<User> findByMobileNo(String mobileNo);

}
