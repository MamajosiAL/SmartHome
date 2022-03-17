package com.ucll.smarthome.controller;

import com.ucll.smarthome.dto.ProgrammeDTO;
import com.ucll.smarthome.dto.TypeDTO;
import com.ucll.smarthome.persistence.entities.Programme;
import com.ucll.smarthome.persistence.entities.Type;
import com.ucll.smarthome.persistence.repository.ProgrammeDAO;
import com.ucll.smarthome.persistence.repository.TypeDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import javax.transaction.Transactional;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;


@Controller
@Transactional
public class ProgrammeConttoller {

    private final ProgrammeDAO programmeDAO;
    private final TypeDAO  typeDAO;

    @Autowired
    public ProgrammeConttoller(ProgrammeDAO programmeDAO, TypeDAO typeDAO) {
        this.programmeDAO = programmeDAO;
        this.typeDAO = typeDAO;
    }

    public List<ProgrammeDTO> getProgramsByTypeId(long typeId) throws IllegalArgumentException{
        Stream<ProgrammeDTO> stream = programmeDAO.findAllByTypeTypeid(typeId).stream()
                .map(rec -> new ProgrammeDTO.Builder()
                        .id(rec.getProgramid()).name(rec.getName()).temp(rec.getTempature())
                        .timer(rec.getTimer()).typeId(rec.getType().getTypeid()).build());
        return stream.collect(Collectors.toList());
    }

    public List<TypeDTO> getTypes(){
        Stream<TypeDTO> stream = typeDAO.findAll().stream()
                .sorted(Comparator.comparing(Type::getTypeid))
                .map(rec -> new TypeDTO.Builder().typeid(rec.getTypeid()).typeName(rec.getName()).build());
        return stream.collect(Collectors.toList());
    }
    public ProgrammeDTO getProgramById(long id) throws  IllegalArgumentException{
        if (programmeDAO.findById(id).isEmpty()) throw new IllegalArgumentException("Programme is not found");
        Programme programme = programmeDAO.getById(id);

        return new ProgrammeDTO.Builder().id(programme.getProgramid()).name(programme.getName()).temp(programme.getTempature()).build();
    }
}