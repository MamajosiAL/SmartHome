package com.ucll.smarthome.view;

import com.ucll.smarthome.dto.HouseDTO;
import com.vaadin.flow.component.accordion.Accordion;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.splitlayout.SplitLayout;

import java.awt.*;

public class HuisView extends VerticalLayout {

        private SplitLayout splitLayout;
        private VerticalLayout verticalLayoutlf;
        private HorizontalLayout horizontalLayoutlf;
        private VerticalLayout verticalLayoutrh;
        private HorizontalLayout horizontalLayoutrh;

        private Grid<HouseDTO> grid;

        private Button btnCancel;
        private Button btnCreate;
        private Button btnUpdate;
        private Button btnDelete;
    public HuisView(){


    }





    public void loadData() {

            System.err.println("Autowiring failed");

    }
}
