package com.ucll.smarthome.controller;

import com.ucll.smarthome.dto.ConsumptionDTO;
import com.ucll.smarthome.dto.ConsumptionLogDTO;
import com.ucll.smarthome.dto.RoomDTO;
import com.ucll.smarthome.persistence.entities.Consumption;
import com.ucll.smarthome.persistence.entities.ConsumptionLog;
import com.ucll.smarthome.persistence.entities.Room;
import com.ucll.smarthome.persistence.repository.ConsumptionDAO;
import com.ucll.smarthome.persistence.repository.ConsumptionLogDAO;
import org.springframework.stereotype.Controller;

import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
        LocalDateTime end = LocalDateTime.of(today.getYear(),today.getMonth(),today.getDayOfMonth(),23,59,0,0);
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

        return ConsumptionSortByDate(getConsumptionLogsByConsumptionList(cDTO));
    }

    public List<ConsumptionLogDTO> getConsumptionLogByRoom(long roomid){
        List<ConsumptionDTO> consumptionDTOList = consumptionController.getConsumptionsByRoom(roomid);
        List<ConsumptionLogDTO> clogDTOList =  getConsumptionLogsByConsumptionList(consumptionDTOList);

        for (ConsumptionLogDTO clogDTO:clogDTOList) {
            clogDTO.setTotalConsumption((int) Math.round(clogDTO.getAantalMinuten() * clogDTO.getConsumptionPerHour()));
        }
        return ConsumptionSortByDate(clogDTOList);
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
                    .roomId(clDTO.getRoomId()).unit(clDTO.getUnit()).houseName(clDTO.getHouseName()).roomName(clDTO.getRoomName()).build();

            double totalConsSum = clDTO.getAantalMinuten()/ 60f * clDTO.getConsumptionPerHour();


            // check if consumption has already been used
            if(!consId.contains(clDTO.getConsumptionLogId())){

                // add the consumptionlog id of the first loop
                consId.add(clDTO.getConsumptionId());

                // run second loop to compare data, if date & house are the same, make total sum and after loop add this to sumlist
                for (ConsumptionLogDTO clDTO2 : clDTOList) {
                    if(clDTO.getDeviceId() != clDTO2.getDeviceId() && clDTO.getRoomId() == clDTO2.getRoomId() && clDTO.getDate().isEqual(clDTO2.getDate())){
                        totalConsSum += (clDTO2.getAantalMinuten()/60f  * clDTO2.getConsumptionPerHour());
                        consId.add(clDTO2.getConsumptionLogId());
                    }
                }

                totalConsSum = totalConsSum * 100;
                totalConsSum = Math.round(totalConsSum);
                totalConsSum = totalConsSum / 100;
                addclDTO.setTotalConsumption(totalConsSum);

                addclDTO.setTotalConsumption(totalConsSum);
                sumList.add(addclDTO);
            }

        }
        return ConsumptionSortByDate(sumList);
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
                    .roomId(clDTO.getRoomId()).unit(clDTO.getUnit()).houseName(clDTO.getHouseName()).roomName(clDTO.getRoomName()).build();

            double totalConsSum = (clDTO.getAantalMinuten() / 60f) * clDTO.getConsumptionPerHour();

            // check if consumption has already been used
            if(!consId.contains(clDTO.getConsumptionLogId())){

                // add the consumptionlog id of the first loop
                consId.add(clDTO.getConsumptionId());

                // run second loop to compare data, if date & house are the same, make total sum and after loop add this to sumlist
                for (ConsumptionLogDTO clDTO2 : clDTOList) {
                    if(clDTO.getDeviceId() != clDTO2.getDeviceId() && clDTO.getHouseId() == clDTO2.getHouseId() && clDTO.getDate().isEqual(clDTO2.getDate())){
                        totalConsSum += (clDTO2.getAantalMinuten() / 60f  * clDTO2.getConsumptionPerHour());
                        consId.add(clDTO2.getConsumptionLogId());
                    }
                }

                totalConsSum = totalConsSum * 100;
                totalConsSum = Math.round(totalConsSum);
                totalConsSum = totalConsSum / 100;
                addclDTO.setTotalConsumption(totalConsSum);
                sumList.add(addclDTO);
            }

        }

        return ConsumptionSortByDate(sumList);
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
                        .roomName(consumptionDTO.getRoomName())
                        .houseName(consumptionDTO.getHouseName())
                        .build();

                consumptionLogDTOList.add(consumptionLogDTO);
            }

        }
        return consumptionLogDTOList;
    }


    private List<ConsumptionLogDTO> ConsumptionSortByDate(List<ConsumptionLogDTO> sumList){

        Stream<ConsumptionLogDTO> stream = sumList.stream()
                .sorted(Comparator.comparing(ConsumptionLogDTO::getDate))
                .map(rec -> new ConsumptionLogDTO.Builder()
                        .deviceId(rec.getDeviceId())
                        .consumptionLogId(rec.getConsumptionLogId())
                        .date(rec.getDate())
                        .consumptionId(rec.getConsumptionId())
                        .aantalMinuten(rec.getAantalMinuten())
                        .unit(rec.getUnit())
                        .consumptionPerHour(rec.getConsumptionPerHour())
                        .houseId(rec.getHouseId())
                        .roomId(rec.getRoomId())
                        .roomName(rec.getRoomName())
                        .houseName(rec.getHouseName())
                        .totalConsumption(rec.getTotalConsumption())
                        .build());

        return stream.collect(Collectors.toList());
    }


    public void createData(){
        int min = 1;
        int max = 1000;

        LocalDate date = LocalDate.now();
        List<Consumption> clist = consumptionDAO.findAll();

        System.out.println(clist.size());
        for (int i = 0; i < 60; i++){
            for(Consumption c : clist){
                int random =  (int)Math.floor(Math.random()*(max-min+1)+min);
                ConsumptionLog consumptionLog = new ConsumptionLog.Builder()
                        .consumption(c)
                        .minutesPerDay(random)
                        .date(date)
                        .build();
                consumptionLogDAO.save(consumptionLog);
            }
            date = date.minusDays(1);
        }
    }
}
