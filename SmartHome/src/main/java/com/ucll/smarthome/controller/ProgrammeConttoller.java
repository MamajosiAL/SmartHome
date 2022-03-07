package com.ucll.smarthome.controller;

import com.ucll.smarthome.dto.ProgrammeDTO;
import com.ucll.smarthome.persistence.repository.ProgrammeDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;


@Controller
@Transactional
public class ProgrammeConttoller {

    private final ProgrammeDAO programmeDAO;

    @Autowired
    public ProgrammeConttoller(ProgrammeDAO programmeDAO) {
        this.programmeDAO = programmeDAO;
    }

    public List<ProgrammeDTO> getProgramsByTypeId(long typeId) throws IllegalArgumentException{
        Stream<ProgrammeDTO> stream = programmeDAO.findAllByTypeTypeid(typeId).stream()
                .map(rec -> new ProgrammeDTO.Builder()
                        .id(rec.getProgramid()).name(rec.getName()).temp(rec.getTempature())
                        .timer(rec.getTimer()).typeId(rec.getType().getTypeid()).build());
        return stream.collect(Collectors.toList());
    }
}