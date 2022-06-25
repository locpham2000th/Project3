package com.example.project3.repository;

import com.example.project3.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmailAndPassword(String login, String password);

    Optional<User> findByEmail(String login);

    Optional<User> findByCookie(String cookie);

}
