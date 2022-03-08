package com.ucll.smarthome.persistence.repository;

import com.ucll.smarthome.persistence.entities.Programme;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProgrammeDAO extends JpaRepository<Programme, Long> {

    List<Programme> findAllByTypeTypeid(long typeId);
}
