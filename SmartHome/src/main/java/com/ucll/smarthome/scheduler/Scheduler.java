package com.ucll.smarthome.scheduler;

import com.ucll.smarthome.controller.*;
import com.ucll.smarthome.dto.BigElectronicDTO;
import com.ucll.smarthome.dto.HouseDTO;
import com.ucll.smarthome.dto.RoomDTO;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@EnableScheduling
public class Scheduler {

    private final ConsumptionLogController consumptionLogController;
    private final BigElectronicController bigElectronicController;
    private final DeviceController deviceController;
    private final RoomController roomController;
    private final HouseController houseController;

    public Scheduler(ConsumptionLogController consumptionLogController, BigElectronicController bigElectronicController, DeviceController deviceController, RoomController roomController, HouseController houseController) {
        this.consumptionLogController = consumptionLogController;
        this.bigElectronicController = bigElectronicController;
        this.deviceController = deviceController;
        this.roomController = roomController;

        this.houseController = houseController;
    }

    // runs every day at 23:59
    @Scheduled(cron = "0 59 23 * * *", zone = "Europe/Brussels")
    public void dailySchedule(){
        System.out.println("Daily Schedule run... ");
        consumptionLogController.createDailyConsumptionLog();
    }

    @Scheduled(fixedDelay = 1000)
    public void checkIfTimeOfBigElectroIsOver(){
            for (HouseDTO houseDTO: houseController.getAllHouses()){
                for (RoomDTO roomDTO: roomController.getRoomsByHouseSchedule(houseDTO.getId())){
                    for (BigElectronicDTO beDTO: bigElectronicController.getBigelectronicScheduled(roomDTO.getId())){
                        if (!beDTO.getType().getTypeName().equals("Cooling Device") && beDTO.isStatus() && beDTO.getEindeProgramma().isBefore(LocalDateTime.now())){
                            deviceController.changeStatus(beDTO.getId());
                            bigElectronicController.beSetToOf(beDTO.getId());
                        }
                    }
                }
            }

    }
}
