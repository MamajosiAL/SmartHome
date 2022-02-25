package com.ucll.smarthome.persistence.repository;

import com.ucll.smarthome.persistence.entities.House;
import com.ucll.smarthome.persistence.entities.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HouseDAO extends JpaRepository<House,Long> {

}
