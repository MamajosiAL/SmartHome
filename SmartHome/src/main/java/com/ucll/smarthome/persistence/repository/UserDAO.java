package com.ucll.smarthome.persistence.repository;

import com.ucll.smarthome.persistence.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserDAO extends JpaRepository<User,Long> {

    Optional<User> findUserByUsername(String username);

}
