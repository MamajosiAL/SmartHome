package com.ucll.smarthome.persistence.repository;


import com.ucll.smarthome.persistence.entities.Appliances;
import com.ucll.smarthome.persistence.entities.Device;
import com.ucll.smarthome.persistence.entities.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface AppliancesDAO extends JpaRepository<Appliances , Long> {
    List<Appliances> findAllByRoom(Room room);

}
