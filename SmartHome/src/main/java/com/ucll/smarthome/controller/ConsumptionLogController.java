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
import java.util.Date;
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
        List<ConsumptionLogDTO> clogDTOList =  getConsumptionLogsByConsumptionList(consumptionDTOList);

        for (ConsumptionLogDTO clogDTO:clogDTOList) {
            clogDTO.setTotalConsumption((int) Math.round(clogDTO.getAantalMinuten() * clogDTO.getConsumptionPerHour()));
        }
        return clogDTOList;
    }

    public List<ConsumptionLogDTO> getConsumptionLogByHouse(long houseid){
        List<ConsumptionDTO> consumptionDTOList = consumptionController.getConsumptionsByHouse(houseid);
        List<ConsumptionLogDTO> clDTOList =  getConsumptionLogsByConsumptionList(consumptionDTOList);
        List<ConsumptionLogDTO> sumList = new ArrayList<>();
        List<Long> consId = new ArrayList<>();

        // loop through all consumption logs
        for (ConsumptionLogDTO clDTO: clDTOList) {
            ConsumptionLogDTO addclDTO = new ConsumptionLogDTO.Builder()
                    .consumptionId(clDTO.getConsumptionId()).consumptionLogId(clDTO.getConsumptionLogId()).consumptionPerHour(clDTO.getConsumptionPerHour())
                    .date(clDTO.getDate()).aantalMinuten(clDTO.getAantalMinuten()).deviceId(clDTO.getDeviceId()).houseId(clDTO.getHouseId())
                    .roomId(clDTO.getRoomId()).unit(clDTO.getUnit()).build();


            double totalConsSum = clDTO.getAantalMinuten() * clDTO.getConsumptionPerHour();

            // check if consumption has already been used
            if(!consId.contains(clDTO.getConsumptionLogId())){

                // add the consumptionlog id of the first loop
                consId.add(clDTO.getConsumptionId());

                // run second loop to compare data, if date & house are the same, make total sum and after loop add this to sumlist
                for (ConsumptionLogDTO clDTO2 : clDTOList) {
                    if(clDTO.getDeviceId() != clDTO2.getDeviceId() && clDTO.getRoomId() == clDTO2.getRoomId() && clDTO.getDate().isEqual(clDTO2.getDate())){
                        totalConsSum += (clDTO2.getAantalMinuten() * clDTO2.getConsumptionPerHour());
                        consId.add(clDTO2.getConsumptionLogId());
                    }
                }
                addclDTO.setTotalConsumption((int) Math.round(totalConsSum));
                sumList.add(addclDTO);
            }

        }
        return sumList;
    }

    public List<ConsumptionLogDTO> getConsumptionLogByUser(){
        List<ConsumptionDTO> consumptionDTOList = consumptionController.getConsumptionsByUser();
        List<ConsumptionLogDTO> clDTOList =  getConsumptionLogsByConsumptionList(consumptionDTOList);
        List<ConsumptionLogDTO> sumList = new ArrayList<>();
        List<Long> consId = new ArrayList<>();

        // loop through all consumption logs
        for (ConsumptionLogDTO clDTO: clDTOList) {
            ConsumptionLogDTO addclDTO = new ConsumptionLogDTO.Builder()
                    .consumptionId(clDTO.getConsumptionId()).consumptionLogId(clDTO.getConsumptionLogId()).consumptionPerHour(clDTO.getConsumptionPerHour())
                    .date(clDTO.getDate()).aantalMinuten(clDTO.getAantalMinuten()).deviceId(clDTO.getDeviceId()).houseId(clDTO.getHouseId())
                    .roomId(clDTO.getRoomId()).unit(clDTO.getUnit()).build();


            double totalConsSum = clDTO.getAantalMinuten() * clDTO.getConsumptionPerHour();

            // check if consumption has already been used
            if(!consId.contains(clDTO.getConsumptionLogId())){

                // add the consumptionlog id of the first loop
                consId.add(clDTO.getConsumptionId());

                // run second loop to compare data, if date & house are the same, make total sum and after loop add this to sumlist
                for (ConsumptionLogDTO clDTO2 : clDTOList) {
                    if(clDTO.getDeviceId() != clDTO2.getDeviceId() && clDTO.getHouseId() == clDTO2.getHouseId() && clDTO.getDate().isEqual(clDTO2.getDate())){
                        totalConsSum += (clDTO2.getAantalMinuten() * clDTO2.getConsumptionPerHour());
                        consId.add(clDTO2.getConsumptionLogId());
                    }
                }
                addclDTO.setTotalConsumption((int) Math.round(totalConsSum));
                sumList.add(addclDTO);
            }

        }
        return sumList;
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
