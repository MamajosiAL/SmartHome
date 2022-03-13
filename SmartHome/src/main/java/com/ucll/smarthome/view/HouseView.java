package com.ucll.smarthome.view;

import com.ucll.smarthome.controller.HouseController;
import com.ucll.smarthome.functions.UserSecurityFunc;
import com.ucll.smarthome.view.dialogs.WarningDialog;
import com.ucll.smarthome.view.forms.HouseForm;
import com.vaadin.flow.component.ClickEvent;
import com.ucll.smarthome.dto.HouseDTO;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.ucll.smarthome.functions.BeanUtil;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.grid.ColumnTextAlign;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.splitlayout.SplitLayout;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.NotFoundException;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.security.access.AccessDeniedException;

import java.util.ArrayList;
import java.util.List;

@Route(value = "houses" , layout = MainView.class)
public class HouseView extends VerticalLayout implements BeforeEnterObserver {

        @Autowired
        private MessageSource msgSrc;
        private final HouseController hc;
        private ManageUsersView mvw;
        private final UserSecurityFunc sec;

        private SplitLayout splitLayout;
        private VerticalLayout verticalLayoutlf;
        private HorizontalLayout lphLayout;
        private VerticalLayout verticalLayoutrh;
        private HorizontalLayout horizontalLayoutrh;
        private HouseForm hfrm;

        private Grid<HouseDTO> grid;
        private Span role;
        private Button btnManageUsers;
        private Button btnRooms;
        private Button btnCancel;
        private Button btnCreate;
        private Button btnUpdate;
        private Button btnDelete;

    public HouseView(){
        super();
        sec = BeanUtil.getBean(UserSecurityFunc.class);
        msgSrc = BeanUtil.getBean(MessageSource.class);

        hc = BeanUtil.getBean(HouseController.class);

        this.setSizeFull();
        this.setPadding(false);

        splitLayout = new SplitLayout();
        splitLayout.setSizeFull();
        splitLayout.addToPrimary(createGridLayout());
        splitLayout.addToSecondary(createEditorLayout());
        add(splitLayout);

    }



    private Component createGridLayout() {
        verticalLayoutlf = new VerticalLayout();
        verticalLayoutlf.setWidthFull();
        lphLayout = new HorizontalLayout();

        grid = new Grid<>();
        grid.setItems(new ArrayList<HouseDTO>(0));
        grid.addColumn(HouseDTO::getName).setHeader(msgSrc.getMessage("hform.housename",null,getLocale()));
        grid.addColumn(new ComponentRenderer<>(houseDTO -> {
            role = new Span();
            if (houseDTO.isIsowner()){
                 role.setText(msgSrc.getMessage("hview.roleO",null,getLocale()));
                 role.getElement().getStyle().set("color", "green");
            }else if (houseDTO.isAdmin()){
                 role.setText("Admin");
                 role.getElement().getStyle().set("color", "Orange");
            }else{
                role.setText(msgSrc.getMessage("hview.roleU",null,getLocale()));
            }
            return role;
        })).setHeader("Rol");
        grid.addColumn(new ComponentRenderer<>(houseDTO -> {
            if (houseDTO.isIsowner()){
            btnManageUsers = new Button(new Icon(VaadinIcon.FOLDER));
            btnManageUsers.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
            btnManageUsers.addClickListener(e -> handleClicManageUsersHouse(e,houseDTO));
            return btnManageUsers;
            }
            return new Span();
        })).setHeader(msgSrc.getMessage("hview.beheer",null,getLocale())).setTextAlign(ColumnTextAlign.CENTER);
        grid.addColumn(new ComponentRenderer<>(houseDTO -> {
            btnRooms = new Button(new Icon(VaadinIcon.ANGLE_RIGHT));
            btnRooms.addClickListener(e -> handleClicToRooms(e,houseDTO));
            return btnRooms;

        })).setHeader("Kamers").setTextAlign(ColumnTextAlign.CENTER);
        grid.setHeightFull();

        grid.asSingleSelect().addValueChangeListener(event -> populateHouseForm(event.getValue()));
        verticalLayoutlf.add(lphLayout);
        verticalLayoutlf.add(grid);
        verticalLayoutlf.setWidth("80%");
        return  verticalLayoutlf;
    }

    private Component createEditorLayout() {

        verticalLayoutrh = new VerticalLayout();
        hfrm = new HouseForm();

        horizontalLayoutrh = new HorizontalLayout();
        horizontalLayoutrh.setWidthFull();
        horizontalLayoutrh.setSpacing(true);



        btnCancel = new Button(msgSrc.getMessage("rview.buttonCa",null,getLocale()));
        btnCancel.addClickListener(this:: handleClickCancel);

        btnCreate = new Button("Toevoegen");
        btnCreate.addClickListener(this::handleClickCreate);

        btnCreate = new Button(msgSrc.getMessage("hview.buttonCr",null,getLocale()));
        btnCreate.addClickListener(this:: handleClickCreate);

        btnUpdate = new Button(msgSrc.getMessage("hview.save",null,getLocale()));
        btnUpdate.addClickListener(this::handleClickUpdate);
        btnUpdate.setVisible(false);

        btnDelete = new Button(msgSrc.getMessage("hview.delete",null,getLocale()));
        btnDelete.addClickListener(this::handleClickDelete);
        btnDelete.setVisible(false);
        
        horizontalLayoutrh.add(btnCancel,btnCreate,btnUpdate,btnDelete);

        verticalLayoutrh.add(hfrm);
        verticalLayoutrh.add(horizontalLayoutrh);
        verticalLayoutrh.setWidth("20%");
        return verticalLayoutrh;
    }

    private void handleClicManageUsersHouse(ClickEvent<Button> e,HouseDTO houseDTO){

       getUI().ifPresent(ui -> ui.navigate("users/" + houseDTO.getId()));

    }
    private void handleClicToRooms(ClickEvent<Button> e, HouseDTO houseDTO) {
        getUI().ifPresent(ui -> ui.navigate("rooms/" + houseDTO.getId()));
    }

    private void handleClickCancel(ClickEvent<Button> e) {
        grid.asSingleSelect().clear();

        setButtonsToDefault();
    }

    private void handleClickCreate(ClickEvent<Button> e) {
        if(!hfrm.isformValid()){
            Notification.show(msgSrc.getMessage("hview.validationerror",null,getLocale()), 3000, Notification.Position.MIDDLE);
            return;
        }
        try {
            HouseDTO houseDTO = new HouseDTO.Builder().name(hfrm.txtnaamhuis.getValue()).build();
            hc.createHouse(houseDTO);
            Notification.show(msgSrc.getMessage("hview.createhouse",null,getLocale()),3000,Notification.Position.TOP_CENTER);
            hfrm.resetForm();
            loadData();
        } catch (IllegalArgumentException event){
            Notification.show(event.getMessage(), 3000, Notification.Position.MIDDLE);
        }


    }
    private void handleClickUpdate(ClickEvent<Button> e) {
        if(!hfrm.isformValid()){
            Notification.show(msgSrc.getMessage("hview.validationerror",null,getLocale()), 3000, Notification.Position.MIDDLE);
        }
        try{
            HouseDTO houseDTO = new HouseDTO.Builder().id(Integer.parseInt(hfrm.lblId.getText())).name(hfrm.txtnaamhuis.getValue()).build();
            hc.updateHouse(houseDTO);
            Notification.show(msgSrc.getMessage("hview.houseadjusted",null,getLocale()),3000,Notification.Position.TOP_CENTER);
            loadData();
            setButtonsToDefault();
        } catch (IllegalArgumentException | NotFoundException event){
            Notification.show(event.getMessage(), 3000, Notification.Position.TOP_CENTER);
        }
    }

    private void handleClickDelete(ClickEvent<Button> e) {
        WarningDialog w = new WarningDialog(msgSrc.getMessage("hview.error",null,getLocale()));
        w.setCloseOnEsc(false);
        w.setCloseOnOutsideClick(false);
        w.addOpenedChangeListener(event -> {
            if (!event.isOpened() && w.wasOkButtonClicked()) {
                try {
                    hc.deleteHouse(Integer.parseInt(hfrm.lblId.getText()));
                }catch (IllegalArgumentException | AccessDeniedException ex){
                    Notification.show(ex.getMessage(), 3000, Notification.Position.TOP_CENTER);
                }
                grid.asSingleSelect().clear();
                setButtonsToDefault();
                loadData();
            }
        });
        w.open();
    }

    public void loadData() {
        if(hc != null){
            List<HouseDTO> lst = hc.getHousesByUser();
            grid.setItems(lst);
        }else{
            System.err.println("Autowiring failed");
        }

    }


    private void setButtonsToDefault(){
        hfrm.resetForm();
        btnCreate.setVisible(true);
        btnUpdate.setVisible(false);
        btnDelete.setVisible(false);
    }
    private void populateHouseForm(HouseDTO houseDTO){
        setButtonsToDefault();
        if (houseDTO != null){
            if(sec.checkCurrentUserIsAdmin(houseDTO.getId())){
                btnCreate.setVisible(false);
                btnUpdate.setVisible(true);
                if (sec.checkCurrentUserIsOwner(houseDTO.getId())){
                    btnDelete.setVisible(true);
                }
                hfrm.lblId.setText("" + houseDTO.getId());
                hfrm.txtnaamhuis.setValue(houseDTO.getName());
            }

        }
    }

    @Override
    public void beforeEnter(BeforeEnterEvent beforeEnterEvent) {
        loadData();
    }
}
