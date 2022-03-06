package com.ucll.smarthome.view;

import com.ucll.smarthome.controller.HouseController;
import com.ucll.smarthome.dto.HouseDTO;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

import java.text.SimpleDateFormat;

public class ConsumptieView extends VerticalLayout {

    private Grid<HouseDTO> grid;

    private SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyyy");

    public ConsumptieView(){
        super();
        grid = new Grid<>();

        //grid.addColumn().setHeader("")

    }

    public void loadData() {

        System.err.println("Autowiring failed");

    }
}
