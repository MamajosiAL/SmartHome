package com.ucll.smarthome.persistence.repository;

import com.ucll.smarthome.persistence.entities.Consumption;
import com.ucll.smarthome.persistence.entities.Device;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ConsumptionDAO extends JpaRepository<Consumption,Long> {
    Optional<List<Consumption>> findAllByDevice(Device device);
}
