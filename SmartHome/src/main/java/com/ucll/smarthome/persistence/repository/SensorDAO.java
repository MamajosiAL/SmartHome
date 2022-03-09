package com.ucll.smarthome.persistence.repository;

import com.ucll.smarthome.persistence.entities.Room;
import com.ucll.smarthome.persistence.entities.SensorDevice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SensorDAO extends JpaRepository<SensorDevice,Long> {
    List<SensorDevice> findAllByRoom(Room room);
}
