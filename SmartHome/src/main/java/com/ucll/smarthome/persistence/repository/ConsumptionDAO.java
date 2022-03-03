package com.ucll.smarthome.persistence.repository;

import com.ucll.smarthome.persistence.entities.Consumption;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ConsumptionDAO extends JpaRepository<Consumption,Long> {

}
