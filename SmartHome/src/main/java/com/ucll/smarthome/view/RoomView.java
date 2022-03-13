package com.ucll.smarthome.view;

import com.ucll.smarthome.controller.HouseController;
import com.ucll.smarthome.controller.RoomController;
import com.ucll.smarthome.dto.HouseDTO;
import com.ucll.smarthome.dto.RoomDTO;
import com.ucll.smarthome.functions.BeanUtil;
import com.ucll.smarthome.functions.UserSecurityFunc;
import com.ucll.smarthome.persistence.entities.Room;
import com.ucll.smarthome.view.dialogs.WarningDialog;
import com.ucll.smarthome.view.forms.HouseForm;
import com.ucll.smarthome.view.forms.RoomForm;
import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.grid.ColumnTextAlign;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.H5;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.splitlayout.SplitLayout;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.router.BeforeEvent;
import com.vaadin.flow.router.HasUrlParameter;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.security.access.AccessDeniedException;

import java.util.ArrayList;
import java.util.List;


@Route(value = "rooms", layout = MainView.class)
public class RoomView extends VerticalLayout implements HasUrlParameter<Long> {


    @Autowired
    private MessageSource msgSrc;
    @Autowired
    private final UserSecurityFunc sec;
    @Autowired
    private final RoomController roomController;
    @Autowired
    private final HouseController houseController;
    private H5 txtErrorMessage;
    private long houseid;
    private SplitLayout splitLayout;
    private VerticalLayout verticalLayoutlf;
    private HorizontalLayout lphLayout;
    private VerticalLayout verticalLayoutrh;
    private HorizontalLayout horizontalLayoutrh;
    private RoomForm rfm;
    private Grid<RoomDTO> grid;
    private H3 houseTitle;

    private Button btnCancel;
    private Button btnCreate;
    private Button btnUpdate;
    private Button btnDelete;
    private Button btnBigElectro;
    private Button btnMedia;
    private Button btnSensor;
    private Button btnDevice;


    public RoomView() {
        sec = BeanUtil.getBean(UserSecurityFunc.class);
        msgSrc = BeanUtil.getBean(MessageSource.class);
        roomController = BeanUtil.getBean(RoomController.class);
        houseController = BeanUtil.getBean(HouseController.class);


        this.setSizeFull();
        this.setPadding(false);

        splitLayout = new SplitLayout();
        splitLayout.setSizeFull();
        splitLayout.addToPrimary(creatGridLayout());
        splitLayout.addToSecondary(createEditorLayout());
        add(splitLayout);
    }

    private Component creatGridLayout(){
        verticalLayoutlf = new VerticalLayout();
        verticalLayoutlf.setWidthFull();
        lphLayout = new HorizontalLayout();
        grid = new Grid<>();
        grid.setItems(new ArrayList<RoomDTO>(0));
        grid.addColumn(RoomDTO::getName).setHeader("Kamer");
        grid.addColumn(new ComponentRenderer<>(roomDTO -> {
            btnBigElectro = new Button(new Icon(VaadinIcon.AUTOMATION));
            return btnBigElectro;
        })).setHeader("Groot electro").setTextAlign(ColumnTextAlign.CENTER);
        grid.addColumn(new ComponentRenderer<>(roomDTO -> {
            btnMedia = new Button(new Icon(VaadinIcon.CAMERA));
            btnMedia.addClickListener(e->handleClickMedia(e,roomDTO.getId()));
            return btnMedia;
        })).setHeader("Media").setTextAlign(ColumnTextAlign.CENTER);
        grid.addColumn(new ComponentRenderer<>(roomDTO -> {
            btnSensor = new Button(new Icon(VaadinIcon.FLASH));
            return btnSensor;
        })).setHeader("Sensor").setTextAlign(ColumnTextAlign.CENTER);
        grid.addColumn(new ComponentRenderer<>(roomDTO -> {
            btnDevice = new Button(new Icon(VaadinIcon.TAB_A));
            btnDevice.addClickListener(e -> handleClickDevice(e, roomDTO.getId()));
            return btnDevice;
        })).setHeader("Niet gecategorizeerd").setTextAlign(ColumnTextAlign.CENTER);
        grid.addColumn(new ComponentRenderer<>(roomDTO -> {
            btnDelete = new Button(new Icon(VaadinIcon.TRASH));
            btnDelete.addThemeVariants(ButtonVariant.LUMO_ICON,ButtonVariant.LUMO_ERROR,ButtonVariant.LUMO_TERTIARY);
            btnDelete.addClickListener(e-> handleClickDelete(e,roomDTO.getId()));
            return btnDelete;
        })).setKey("delete");
        grid.setHeightFull();
        grid.asSingleSelect().addValueChangeListener(event -> populateRoomForm(event.getValue()));
        verticalLayoutlf.add(lphLayout);
        verticalLayoutlf.add(grid);
        verticalLayoutlf.setWidth("80%");
        return  verticalLayoutlf;
    }

    private void handleClickMedia(ClickEvent<Button> e, long roomid) {
        getUI().ifPresent(ui->ui.navigate("media's/" + roomid));
    }

    private void handleClickDevice(ClickEvent<Button> e,long roomid) {
        getUI().ifPresent(ui -> ui.navigate("devices/" + roomid));
    }

    private Component createEditorLayout(){

        verticalLayoutrh = new VerticalLayout();
        rfm = new RoomForm();

        horizontalLayoutrh = new HorizontalLayout();
        horizontalLayoutrh.setWidthFull();
        horizontalLayoutrh.setSpacing(true);
        txtErrorMessage = new H5();
        txtErrorMessage.setVisible(false);
        houseTitle = new H3();

        btnCancel = new Button(msgSrc.getMessage("rview.buttonCa",null,getLocale()));
        btnCancel.addClickListener(this:: handleClickCancel);


        btnCreate = new Button(msgSrc.getMessage("hview.buttonCr",null,getLocale()));
        btnCreate.addClickListener(this:: handleClickCreate);

        btnUpdate = new Button(msgSrc.getMessage("hview.save",null,getLocale()));
        btnUpdate.addClickListener(this::handleClickUpdate);
        btnUpdate.setVisible(false);


        horizontalLayoutrh.add(btnCancel,btnCreate,btnUpdate);

        verticalLayoutrh.add(houseTitle,txtErrorMessage,rfm);
        verticalLayoutrh.add(horizontalLayoutrh);
        verticalLayoutrh.setWidth("20%");
        return verticalLayoutrh;
    }

    private void handleClickDelete(ClickEvent<Button> buttonClickEvent,long roomid) {
        WarningDialog w = new WarningDialog("Weet u zeker dat u deze kamer wilt verwijderen");
        w.setCloseOnEsc(false);
        w.setCloseOnOutsideClick(false);
        w.addOpenedChangeListener(event -> {
            if (!event.isOpened() && w.wasOkButtonClicked()) {
                try {
                    roomController.deleteRoom(roomid);
                }catch (IllegalArgumentException | AccessDeniedException ex){
                    txtErrorMessage.setText(ex.getMessage());
                    txtErrorMessage.setVisible(true);
                }
                grid.asSingleSelect().clear();
                setButtonsToDefault();
                loadData();
            }
        });
        w.open();
    }

    private void handleClickUpdate(ClickEvent<Button> buttonClickEvent) {
        try {

            roomController.updateRoom(new RoomDTO.Builder().id(Integer.parseInt(rfm.lblid.getText())).name(rfm.txtNameRoom.getValue()).build());
            setButtonsToDefault();
            loadData();
        } catch (IllegalArgumentException | AccessDeniedException e) {
           txtErrorMessage.setText(e.getMessage());
           txtErrorMessage.setVisible(true);
        }
    }

    private void handleClickCreate(ClickEvent<Button> buttonClickEvent) {
        try {
            roomController.createRoom(new RoomDTO.Builder().name(rfm.txtNameRoom.getValue()).houseid(houseid).build());
            setButtonsToDefault();
            loadData();
        } catch (IllegalArgumentException | AccessDeniedException ex) {
           txtErrorMessage.setVisible(true);
           txtErrorMessage.setText(ex.getMessage());
        }
    }

    private void handleClickCancel(ClickEvent<Button> buttonClickEvent) {
        grid.asSingleSelect().clear();
        setButtonsToDefault();
    }

    private void populateRoomForm(RoomDTO roomDTO) {
        setButtonsToDefault();
        if (roomDTO != null) {
            btnCreate.setVisible(false);
            btnUpdate.setVisible(true);
            rfm.lblid.setText("" + roomDTO.getId());
            rfm.txtNameRoom.setValue(roomDTO.getName());
        }
    }

    private void setButtonsToDefault(){
        rfm.resetForm();
        txtErrorMessage.setVisible(false);
        btnCreate.setVisible(true);
        btnUpdate.setVisible(false);
    }
    private HouseDTO getHouse(){
        return  houseController.getHouseById(houseid);
    }

    private void loadData() {
         grid.setItems(roomController.getRoomsByHouse(houseid));
    }

    @Override
    public void setParameter(BeforeEvent beforeEvent, Long id) {
        try {
            houseid = id;
            List<RoomDTO> rooms = roomController.getRoomsByHouse(id);
            grid.setItems(rooms);
            houseTitle.setText(getHouse().getName());
            if (!sec.checkCurrentUserIsAdmin(id)){
                grid.removeColumnByKey("delete");
                rfm.removeAll();
                btnCancel.setVisible(false);
                btnCreate.setVisible(false);
            }

        } catch (IllegalArgumentException e) {
            Notification.show(e.getMessage() ,3000, Notification.Position.TOP_CENTER);
        }
    }
}
