package com.ucll.smarthome.persistence.repository;

import com.ucll.smarthome.persistence.entities.Consumption;
import com.ucll.smarthome.persistence.entities.ConsumptionLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ConsumptionLogDAO extends JpaRepository<ConsumptionLog,Long> {
    Optional<List<ConsumptionLog>> findAllByConsumptionConsumptionId(long consumptionId);
}
