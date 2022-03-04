package com.ucll.smarthome.persistence.repository;

import com.ucll.smarthome.persistence.entities.Room;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RoomDAO  extends JpaRepository<Room,Long> {

    Optional<List<Room>> findAllByHouseHouseId(long id);
}
