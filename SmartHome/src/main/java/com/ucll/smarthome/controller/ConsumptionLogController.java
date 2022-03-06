package com.ucll.smarthome.controller;

import com.ucll.smarthome.persistence.entities.Consumption;
import com.ucll.smarthome.persistence.entities.ConsumptionLog;
import com.ucll.smarthome.persistence.repository.ConsumptionDAO;
import com.ucll.smarthome.persistence.repository.ConsumptionLogDAO;
import org.springframework.stereotype.Controller;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Controller
public class ConsumptionLogController {

    private final ConsumptionLogDAO consumptionLogDAO;
    private final ConsumptionDAO consumptionDAO;

    public ConsumptionLogController(ConsumptionLogDAO consumptionLogDAO, ConsumptionDAO consumptionDAO) {
        this.consumptionLogDAO = consumptionLogDAO;
        this.consumptionDAO = consumptionDAO;
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

}
