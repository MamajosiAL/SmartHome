package com.ucll.smarthome.persistence.repository;

import com.ucll.smarthome.persistence.entities.House;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HouseDAO extends JpaRepository<House,Long> {
}
