package com.ucll.smarthome.view;

import com.ucll.smarthome.controller.HouseController;
import com.vaadin.flow.component.ClickEvent;
import com.ucll.smarthome.dto.HouseDTO;
import com.vaadin.flow.component.button.Button;
import com.ucll.smarthome.functions.BeanUtil;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.splitlayout.SplitLayout;

import java.util.ArrayList;
import java.util.List;

public class HuisView extends VerticalLayout {

        private final HouseController hc;
        private SplitLayout splitLayout;
        private VerticalLayout verticalLayoutlf;
        private VerticalLayout verticalLayoutrh;
        private HorizontalLayout horizontalLayoutrh;
        private HuisForm hfrm;

        private Grid<HouseDTO> grid;



        private Button btnCancel = new Button("Annuleren");;
        private Button btnCreate = new Button("Toevoegen");;
        private Button btnUpdate = new Button("Opslaan");;
        private Button btnDelete = new Button("Verwijderen");

    public HuisView(){
        super();
        hc = BeanUtil.getBean(HouseController.class);

        this.setSizeFull();
        this.setPadding(false);

        splitLayout = new SplitLayout();
        splitLayout.setSizeFull();
        splitLayout.addToPrimary(CreateGridLayout());
        splitLayout.addToSecondary(CreateEditorLayout());
        add(splitLayout);

    }



    private Component CreateGridLayout() {
        verticalLayoutlf = new VerticalLayout();
        verticalLayoutlf.setWidthFull();


        grid = new Grid<>();
        grid.setItems(new ArrayList<HouseDTO>(0));
        grid.addColumn(HouseDTO::getName).setHeader("Naam Huis");
        grid.setHeightFull();
        verticalLayoutlf.add(grid);
        verticalLayoutlf.setWidth("70%");
        return  verticalLayoutlf;
    }
    private Component CreateEditorLayout() {

        verticalLayoutrh = new VerticalLayout();
        hfrm = new HuisForm();

        horizontalLayoutrh = new HorizontalLayout();
        horizontalLayoutrh.setWidthFull();
        horizontalLayoutrh.setSpacing(true);



        
        btnCancel.addClickListener(e -> handleClickCancel(e));
        btnCreate.addClickListener(e -> handleClickCreate(e));
        btnUpdate.addClickListener(e -> handleClickUpdate(e));
        btnUpdate.setVisible(false);
        btnDelete.addClickListener(e -> handleClickDelete(e));
        btnDelete.setVisible(false);
        
        horizontalLayoutrh.add(btnCancel,btnCreate,btnUpdate,btnDelete);

        verticalLayoutrh.add(hfrm);
        verticalLayoutrh.add(horizontalLayoutrh);
        verticalLayoutrh.setWidth("30%");
        return verticalLayoutrh;
    }

    private void handleClickCancel(ClickEvent<Button> e) {
        grid.asSingleSelect().clear();
        hfrm.resetForm();
        btnCreate.setVisible(true);
        btnUpdate.setVisible(false);
        btnDelete.setVisible(false);
    }

    private void handleClickCreate(ClickEvent<Button> e) {
        if(!hfrm.isformValid()){
            Notification.show("Validatie fout", 3000, Notification.Position.MIDDLE);
            return;
        }
        try {
            HouseDTO houseDTO = new HouseDTO.Builder().name(hfrm.txtnaamhuis.getValue()).build();
            hc.createHouse(houseDTO);
            Notification.show("huis aangemaakt",3000,Notification.Position.MIDDLE);
            hfrm.resetForm();
        } catch (IllegalArgumentException event){
            Notification.show(event.getMessage(), 3000, Notification.Position.MIDDLE);
        }


    }
    private void handleClickUpdate(ClickEvent<Button> e) {
        if(!hfrm.isformValid()){
            Notification.show("Validatie fout", 3000, Notification.Position.MIDDLE);
        }
        try{
            HouseDTO houseDTO = new HouseDTO.Builder().name(hfrm.txtnaamhuis.getValue()).build();
            hc.updateHouse(houseDTO);
            Notification.show("huis aangepast",3000,Notification.Position.MIDDLE);
            hfrm.resetForm();
        } catch (IllegalArgumentException event){
            Notification.show(event.getMessage(), 3000, Notification.Position.MIDDLE);
        }
    }

    private void handleClickDelete(ClickEvent<Button> e) {
    }



    public void loadData() {
        if(hc != null){
            List<HouseDTO> lst = hc.getAllHouses();
            grid.setItems(lst);
        }else{
            System.err.println("Autowiring failed");
        }



    }
}
