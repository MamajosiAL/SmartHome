package com.ucll.smarthome.persistence.repository;

import com.ucll.smarthome.persistence.entities.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoomDAO  extends JpaRepository<Room,Long> {

}
