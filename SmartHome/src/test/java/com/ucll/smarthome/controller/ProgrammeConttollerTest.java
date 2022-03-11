package com.ucll.smarthome.controller;

import com.ucll.smarthome.AbstractIntegrationTest;
import com.ucll.smarthome.dto.ProgrammeDTO;
import com.ucll.smarthome.persistence.entities.Type;
import com.ucll.smarthome.persistence.repository.ProgrammeDAO;
import com.ucll.smarthome.persistence.repository.TypeDAO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.test.context.support.TestExecutionEvent;
import org.springframework.security.test.context.support.WithUserDetails;

import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

class ProgrammeConttollerTest extends AbstractIntegrationTest {

    @Autowired
    private ProgrammeConttoller programmeConttoller;

    @Autowired
    private TypeDAO typeDAO;

    private Type dishwasher;

    @BeforeEach
    void setUp() {
        dishwasher = typeDAO.findAll().stream()
                .filter(p -> p.getName().equals("Dryer"))
                .findFirst()
                .orElse(null);
    }

    @Test
    void getProgramsByTypeId() {
        List<ProgrammeDTO> programmes = programmeConttoller.getProgramsByTypeId(dishwasher.getTypeid());

        List<String> programmeName = programmes.stream()
                .map(ProgrammeDTO::getName)
                .collect(Collectors.toList());

        assertTrue(programmeName.contains("Very Dry"));
        assertTrue(programmeName.contains("Cupboard Dry"));
        assertTrue(programmeName.contains("Iron Dry"));
        assertTrue(programmeName.contains("Cotton Dry"));
    }
}