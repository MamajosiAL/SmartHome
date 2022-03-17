package com.ucll.smarthome.view;


import com.ucll.smarthome.controller.HouseController;
import com.ucll.smarthome.controller.House_UserController;
import com.ucll.smarthome.controller.UserController;
import com.ucll.smarthome.dto.HouseDTO;
import com.ucll.smarthome.dto.House_UserDTO;
import com.ucll.smarthome.dto.UserDTO;
import com.ucll.smarthome.functions.BeanUtil;
import com.ucll.smarthome.functions.UserSecurityFunc;
import com.ucll.smarthome.view.dialogs.WarningDialog;
import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.Component;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.grid.Grid;

import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.H5;
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
import liquibase.pro.packaged.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.security.access.AccessDeniedException;

import java.util.ArrayList;
import java.util.List;

@Route(value = "users" , layout = MainView.class)
public class ManageUsersView extends VerticalLayout implements HasUrlParameter<Long> {
    private long houseid;
    @Autowired
    private  UserController usc;
    @Autowired
    private HouseController hsc;

    @Autowired
    private House_UserController huC;
    @Autowired
    private final UserSecurityFunc sec;
    private MessageSource msgSrc;


    private H5 txtErrorMessage;
    private SplitLayout splitLayout;
    private VerticalLayout verticalLayoutlf;
    private HorizontalLayout lphLayout;
    private VerticalLayout verticalLayoutrh;
    private HorizontalLayout horizontalLayoutrh;
    private Checkbox checkbox;
    private H3 houseTitle;
    private ComboBox<UserDTO> users;
    private Button btnDelete;
    private Button btnAdd;
    private Grid<UserDTO> grid;
    private Button btnBack;


    public ManageUsersView() {
        super();
        sec = BeanUtil.getBean(UserSecurityFunc.class);
        msgSrc = BeanUtil.getBean(MessageSource.class);
        usc = BeanUtil.getBean(UserController.class);
        hsc = BeanUtil.getBean(HouseController.class);

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

        btnBack = new Button("Woningen");
        btnBack.addClickListener(e->handleClickBack(e));

        grid = new Grid<>();
        grid.setItems(new ArrayList<UserDTO>(0));
        grid.addColumn(UserDTO::getUsername).setHeader(msgSrc.getMessage("lview.username",null,getLocale()));
        grid.addColumn(UserDTO::getFirstname).setHeader(msgSrc.getMessage("rview.firstname",null,getLocale()));
        grid.addColumn(UserDTO::getName).setHeader(msgSrc.getMessage("rview.lastname",null,getLocale()));
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
                btnDelete.addClickListener(e-> handleClickDelete(e,userDTO.getId()));
                return btnDelete;
        }));

        grid.setHeightFull();


        verticalLayoutlf.add(lphLayout);
        verticalLayoutlf.add(btnBack,grid);
        verticalLayoutlf.setWidth("80%");

        return verticalLayoutlf;
    }

    private void handleClickBack(ClickEvent<Button> e) {
        getUI().ifPresent(ui -> ui.navigate("houses"));
    }

    private void handleClickDelete(ClickEvent<Button> e, long userid) {
        WarningDialog w = new WarningDialog("Weet u zeker dat u deze gebruiker wilt verwijderen van u woning");
        w.setCloseOnEsc(false);
        w.setCloseOnOutsideClick(false);
        w.addOpenedChangeListener(event -> {
            if (!event.isOpened() && w.wasOkButtonClicked()) {
                try {
                    huC.deleteSingleHouseUser(huC.getHouseUserByHouseIdAndUserId(houseid,userid));
                }catch (IllegalArgumentException | AccessDeniedException ex){
                    txtErrorMessage.setText(ex.getMessage());
                    txtErrorMessage.setVisible(true);
                }
                grid.asSingleSelect().clear();
                loadData();
            }
        });
        w.open();

    }

    private Component createAddUserToHouseLayout(){
        verticalLayoutrh = new VerticalLayout();
        horizontalLayoutrh = new HorizontalLayout();
        horizontalLayoutrh.setWidthFull();
        horizontalLayoutrh.setSpacing(true);

        houseTitle = new H3();
        txtErrorMessage = new H5();
        txtErrorMessage.setVisible(false);
        users = new ComboBox<>( msgSrc.getMessage("mview.search",null,getLocale()));
        List<UserDTO> lstUsers = usc.getAllUsers();
        users.setItems(lstUsers);
        users.setItemLabelGenerator(UserDTO::getUsername);

        btnAdd = new Button(msgSrc.getMessage("hview.buttonCr",null,getLocale()));
        btnAdd.addClickListener(this::handleClickAdd);

        horizontalLayoutrh.add(btnAdd);
        verticalLayoutrh.add(houseTitle,txtErrorMessage,users);
        verticalLayoutrh.add(horizontalLayoutrh);
        verticalLayoutrh.setWidth("20%");
        return verticalLayoutrh;
    }

    private void handleClickAdd(ClickEvent<Button> buttonClickEvent) {
        try {
            UserDTO userDTO =  users.getValue();
            if (userDTO == null) throw new IllegalArgumentException(msgSrc.getMessage("mview.exc",null,getLocale()));
            huC.registerUserToHouseNotOwner(new HouseDTO.Builder().id(houseid).username(userDTO.getUsername()).build());
            users.clear();
            loadData();
            txtErrorMessage.setVisible(false);
        }catch (IllegalArgumentException ex){
            txtErrorMessage.setText(ex.getMessage());
            txtErrorMessage.setVisible(true);
        }
    }

    private void handleClickCheckbox(ClickEvent<Checkbox> checkboxClickEvent,long userid) {
        try {
            House_UserDTO house_userDTO = new House_UserDTO.Builder().houseid(houseid).userid(userid).isadmin(checkbox.getValue()).build();
            huC.updateUserSetAdmin(house_userDTO);
            loadData();
        }catch (IllegalArgumentException ex){

            txtErrorMessage.setVisible(true);
            txtErrorMessage.setText(ex.getMessage());
        }
    }

    private HouseDTO getHouse(){
       return  hsc.getHouseById(houseid);
    }

    public void loadData(){
        try {
            if (sec.checkCurrentUserIsOwner(getHouse().getId())) {
                List<UserDTO> users = usc.getUsersByHouse(houseid);
                grid.setItems(users);
            }
        } catch (IllegalArgumentException e) {
            Notification.show(e.getMessage() ,3000, Notification.Position.TOP_CENTER);
        }
    }


    @Override
    public void setParameter(BeforeEvent beforeEvent, Long id) {
        try {
            houseid = id;
            houseTitle.setText(getHouse().getName());

            if (sec.checkCurrentUserIsOwner(getHouse().getId())){

                List<UserDTO> users = usc.getUsersByHouse(id);
                grid.setItems(users);
            }
            if (!sec.checkCurrentUserIsOwner(getHouse().getId())){
                txtErrorMessage.setText("Je bent niet de eigenaar van dit huis. Je kan hier niks doen.");
                txtErrorMessage.setVisible(true);
                users.setItems(new ArrayList<>());
                btnAdd.setVisible(false);

                //Notification.show("Je hebt hier geen toegang toe" ,3000, Notification.Position.TOP_CENTER);
                getUI().ifPresent(ui -> ui.navigate(HouseView.class));
            }

        } catch (IllegalArgumentException e) {
           txtErrorMessage.setText(e.getMessage());
           txtErrorMessage.setVisible(true);
        }
    }
}
