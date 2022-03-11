package com.ucll.smarthome.view;


import com.ucll.smarthome.controller.House_UserController;
import com.ucll.smarthome.controller.UserController;
import com.ucll.smarthome.dto.HouseDTO;
import com.ucll.smarthome.dto.House_UserDTO;
import com.ucll.smarthome.dto.UserDTO;
import com.ucll.smarthome.functions.BeanUtil;
import com.ucll.smarthome.functions.UserSecurityFunc;
import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.Component;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.grid.Grid;

import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.splitlayout.SplitLayout;

import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.router.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

@Route(value = "users" , layout = MainView.class)
public class ManageUsersView extends VerticalLayout implements HasUrlParameter<Long> {
    private long houseid;
    @Autowired
    private  UserController usc;

    @Autowired
    private House_UserController huC;

    private SplitLayout splitLayout;
    private VerticalLayout verticalLayoutlf;
    private HorizontalLayout lphLayout;
    private VerticalLayout verticalLayoutrh;
    private HorizontalLayout horizontalLayoutrh;
    private Checkbox checkbox;



    private TextField txtUsername;
    private Button btnDelete;
    private Button btnAdd;
    private Grid<UserDTO> grid;

    private final UserSecurityFunc sec;
    public ManageUsersView(UserSecurityFunc sec) {
        super();
        this.sec = sec;


        usc = BeanUtil.getBean(UserController.class);

        setSizeFull();
        splitLayout = new SplitLayout();
        splitLayout.setSizeFull();
        splitLayout.addToPrimary(createGridLayout());
        splitLayout.addToSecondary(createAddUserToHouseLayout());

        add(splitLayout);

    }


    private Component createGridLayout(){
        verticalLayoutlf = new VerticalLayout();
        verticalLayoutlf.setWidthFull();
        lphLayout = new HorizontalLayout();


        grid = new Grid<>();
        grid.setItems(new ArrayList<UserDTO>(0));
        grid.addColumn(UserDTO::getUsername).setHeader("Gebruikersnaam");
        grid.addColumn(UserDTO::getFirstname).setHeader("Voornaam");
        grid.addColumn(UserDTO::getName).setHeader("Naam");
        grid.addColumn(UserDTO::getEmail).setHeader("Email");
        grid.addColumn(new ComponentRenderer<>(userDTO -> {
            checkbox = new Checkbox();
            checkbox.setValue(userDTO.isIsadmin());
            checkbox.addClickListener(e -> handleClickCheckbox(e, userDTO.getId()));
            return checkbox;
        })).setHeader("Admin");
        grid.addColumn(new ComponentRenderer<>( userDTO -> {
                btnDelete = new Button(new Icon(VaadinIcon.TRASH));
                btnDelete.addThemeVariants(ButtonVariant.LUMO_ICON,ButtonVariant.LUMO_ERROR,ButtonVariant.LUMO_TERTIARY);
                return btnDelete;
        }));

        grid.setHeightFull();


        verticalLayoutlf.add(lphLayout);
        verticalLayoutlf.add(grid);
        verticalLayoutlf.setWidth("80%");

        return verticalLayoutlf;
    }

    private Component createAddUserToHouseLayout(){
        verticalLayoutrh = new VerticalLayout();
        horizontalLayoutrh = new HorizontalLayout();
        horizontalLayoutrh.setWidthFull();
        horizontalLayoutrh.setSpacing(true);

        txtUsername = new TextField();
        txtUsername.setPlaceholder("Gebruikersnaam");
        txtUsername.setPrefixComponent(VaadinIcon.SEARCH.create());
        txtUsername.setClearButtonVisible(true);

        btnAdd = new Button("Toevoegen");
        btnAdd.addClickListener(this::handleClickAdd);

        horizontalLayoutrh.add(btnAdd);
        verticalLayoutrh.add(txtUsername);
        verticalLayoutrh.add(horizontalLayoutrh);
        verticalLayoutrh.setWidth("20%");
        return verticalLayoutrh;
    }

    private void handleClickAdd(ClickEvent<Button> buttonClickEvent) {
        try {
            huC.registerUserToHouseNotOwner(new HouseDTO.Builder().id(houseid).username(txtUsername.getValue()).build());
            txtUsername.clear();
            loadData();
        }catch (IllegalArgumentException ex){
            Notification.show(ex.getMessage() ,3000, Notification.Position.TOP_CENTER);
        }
    }

    private void handleClickCheckbox(ClickEvent<Checkbox> checkboxClickEvent,long userid) {
        try {
            House_UserDTO house_userDTO = new House_UserDTO.Builder().houseid(houseid).userid(userid).isadmin(checkbox.getValue()).build();
            huC.updateUserSetAdmin(house_userDTO);
        }catch (IllegalArgumentException ex){
            loadData();
            Notification.show(ex.getMessage() ,3000, Notification.Position.TOP_CENTER);
        }
    }

    public void loadData(){
        try {
            List<UserDTO> users = usc.getUsersByHouse(houseid);
            grid.setItems(users);
        } catch (IllegalArgumentException e) {
            Notification.show(e.getMessage() ,3000, Notification.Position.TOP_CENTER);
        }
    }


    @Override
    public void setParameter(BeforeEvent beforeEvent, Long id) {
        try {
            houseid = id;
            List<UserDTO> users = usc.getUsersByHouse(id);
            grid.setItems(users);
        } catch (IllegalArgumentException e) {
           Notification.show(e.getMessage() ,3000, Notification.Position.TOP_CENTER);
        }
    }
}
