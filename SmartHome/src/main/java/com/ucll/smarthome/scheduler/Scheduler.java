package com.ucll.smarthome.scheduler;

import com.ucll.smarthome.controller.ConsumptionLogController;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@EnableScheduling
public class Scheduler {

    private final ConsumptionLogController consumptionLogController;

    public Scheduler(ConsumptionLogController consumptionLogController) {
        this.consumptionLogController = consumptionLogController;
    }

    // runs every day at 23:59
    @Scheduled(cron = "0 59 23 * * *", zone = "Europe/Brussels")
    public void dailySchedule(){
        System.out.println("Daily Schedule run... ");
        consumptionLogController.createDailyConsumptionLog();
    }
}
