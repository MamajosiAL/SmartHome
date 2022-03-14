package com.ucll.smarthome.controller;

import com.ucll.smarthome.dto.ConsumptionDTO;
import com.ucll.smarthome.dto.ConsumptionLogDTO;
import com.ucll.smarthome.persistence.entities.Consumption;
import com.ucll.smarthome.persistence.entities.ConsumptionLog;
import com.ucll.smarthome.persistence.repository.ConsumptionDAO;
import com.ucll.smarthome.persistence.repository.ConsumptionLogDAO;
import org.springframework.stereotype.Controller;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

@Controller
public class ConsumptionLogController {

    private final ConsumptionLogDAO consumptionLogDAO;
    private final ConsumptionDAO consumptionDAO;
    private final ConsumptionController consumptionController;

    public ConsumptionLogController(ConsumptionLogDAO consumptionLogDAO, ConsumptionDAO consumptionDAO, ConsumptionController consumptionController) {
        this.consumptionLogDAO = consumptionLogDAO;
        this.consumptionDAO = consumptionDAO;
        this.consumptionController = consumptionController;
    }

    // this runs every 24hours at 23:59 with the scheduler
    public void createDailyConsumptionLog(){
        List<Consumption> consumptionList = consumptionDAO.findAll();

        LocalDate today = LocalDate.now();
        LocalDateTime end = LocalDateTime.of(today.getYear(),today.getMonth(),today.getDayOfMonth(),0,0,0,0);
        LocalDateTime resetdatetime = LocalDateTime.of(today.getYear(),today.getMonth(),today.getDayOfMonth(),0,0,0,0).plusDays(1);

        for(Consumption c : consumptionList){
            ConsumptionLog consumptionLog = new ConsumptionLog();
            consumptionLog.setDate(today);
            consumptionLog.setConsumption(c);
            if(c.getDevice().isStatus()){
                LocalDateTime start = c.getStartDatumEnTijd();
                long minutes = ChronoUnit.MINUTES.between(start,end);
                c.setAantalMinuten(c.getAantalMinuten() + (int)minutes);
            }

            consumptionLog.setMinutesPerDay(c.getAantalMinuten());
            c.setAantalMinuten(0);
            c.setStartDatumEnTijd(resetdatetime);
            consumptionLogDAO.save(consumptionLog);
            consumptionDAO.save(c);
        }
    }

    public List<ConsumptionLogDTO> getConsumptionLogByDevice(long deviceid){
        List<ConsumptionDTO> cDTO = consumptionController.getConsumptionsByDeviceId(deviceid);

        return getConsumptionLogsByConsumptionList(cDTO);
    }

    public List<ConsumptionLogDTO> getConsumptionLogByRoom(long roomid){
        List<ConsumptionDTO> consumptionDTOList = consumptionController.getConsumptionsByRoom(roomid);
        return getConsumptionLogsByConsumptionList(consumptionDTOList);
    }

    public List<ConsumptionLogDTO> getConsumptionLogByHouse(long houseid){
        List<ConsumptionDTO> consumptionDTOList = consumptionController.getConsumptionsByHouse(houseid);
        return getConsumptionLogsByConsumptionList(consumptionDTOList);
    }

    public List<ConsumptionLogDTO> getConsumptionLogByUser(){
        List<ConsumptionDTO> consumptionDTOList = consumptionController.getConsumptionsByUser();
        return getConsumptionLogsByConsumptionList(consumptionDTOList);
    }

    private List<ConsumptionLogDTO> getConsumptionLogsByConsumptionList(List<ConsumptionDTO> consumptionDTOList) {
        List<ConsumptionLogDTO> consumptionLogDTOList = new ArrayList<>();
        for (ConsumptionDTO consumptionDTO: consumptionDTOList) {
            List<ConsumptionLog> temp = consumptionLogDAO.findAllByConsumptionConsumptionId(consumptionDTO.getConsumptionId()).get();
            for (ConsumptionLog consumptionLog : temp) {
                ConsumptionLogDTO consumptionLogDTO = new ConsumptionLogDTO.Builder()
                        .deviceId(consumptionDTO.getDeviceId())
                        .consumptionLogId(consumptionLog.getId())
                        .date(consumptionLog.getDate())
                        .consumptionId(consumptionLog.getConsumption().getConsumptionId())
                        .aantalMinuten(consumptionLog.getMinutesPerDay())
                        .unit(consumptionDTO.getUnit())
                        .consumptionPerHour(consumptionDTO.getConsumptionPerHour())
                        .houseId(consumptionDTO.getHouseId())
                        .roomId(consumptionDTO.getRoomId())
                        .build();

                consumptionLogDTOList.add(consumptionLogDTO);
            }

        }
        return consumptionLogDTOList;
    }

}
