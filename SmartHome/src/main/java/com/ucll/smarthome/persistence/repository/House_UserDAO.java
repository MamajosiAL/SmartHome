package com.ucll.smarthome.persistence.repository;

import com.ucll.smarthome.persistence.entities.House;
import com.ucll.smarthome.persistence.entities.House_User;
import com.ucll.smarthome.persistence.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface House_UserDAO extends JpaRepository<House_User,Long> {

    List<House_User> findAllByUser(User user);
    List<House_User> findAllByHouse(House house);
}
