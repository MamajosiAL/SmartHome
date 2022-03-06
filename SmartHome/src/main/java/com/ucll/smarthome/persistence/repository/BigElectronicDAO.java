package com.ucll.smarthome.persistence.repository;


import com.ucll.smarthome.persistence.entities.BigElectronicDevice;
import com.ucll.smarthome.persistence.entities.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface BigElectronicDAO extends JpaRepository<BigElectronicDevice, Long> {
    List<BigElectronicDevice> findAllByRoom(Room room);

}
