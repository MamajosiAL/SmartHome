package com.ucll.smarthome.persistence.repository;

import com.ucll.smarthome.persistence.entities.Audio;
import com.ucll.smarthome.persistence.entities.Device;
import com.ucll.smarthome.persistence.entities.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AudioDAO extends JpaRepository<Audio,Long> {
    List<Audio> findAllByRoom(Room room);
}
