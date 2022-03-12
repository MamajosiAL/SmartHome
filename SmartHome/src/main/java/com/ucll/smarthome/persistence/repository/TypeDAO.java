package com.ucll.smarthome.persistence.repository;

import com.ucll.smarthome.persistence.entities.Type;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface TypeDAO extends JpaRepository<Type,Long> {
    Optional<Type> findByName(String name);
}
