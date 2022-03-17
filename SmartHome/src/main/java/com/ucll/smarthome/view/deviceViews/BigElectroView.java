package com.ucll.smarthome.view.deviceViews;


import com.flowingcode.vaadin.addons.simpletimer.SimpleTimer;
import com.ucll.smarthome.controller.*;
import com.ucll.smarthome.dto.BigElectronicDTO;
import com.ucll.smarthome.dto.RoomDTO;
import com.ucll.smarthome.functions.BeanUtil;
import com.ucll.smarthome.functions.UserSecurityFunc;
import com.ucll.smarthome.view.MainView;
import com.ucll.smarthome.view.dialogs.WarningDialog;
import com.ucll.smarthome.view.forms.BigElectroForm;
import com.ucll.smarthome.view.forms.DeviceForm;
import com.vaadin.componentfactory.ToggleButton;
import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.grid.ColumnTextAlign;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H5;
import com.vaadin.flow.component.html.Span;
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
import org.springframework.context.MessageSource;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.security.access.AccessDeniedException;


import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;

import java.util.List;

@Route(value = "big_electronic's",layout = MainView.class)
public class BigElectroView extends VerticalLayout implements HasUrlParameter<Long> {


    private DeviceController deviceController;
    private BigElectronicController bigElectronicController;
    private ProgrammeConttoller programmeConttoller;
    private final UserSecurityFunc sec;
    private final RoomController roomController;


    private MessageSource msgSrc;

    private Grid<BigElectronicDTO> grid;
    private long roomid;
    private VerticalLayout vrlDeviceGrid;
    private VerticalLayout verticalLayoutrh;
    private HorizontalLayout horizontalLayoutrh;
    private HorizontalLayout hrlDeviceGrid;
    private SplitLayout splitLayout;
    private BigElectroForm bigElectroForm;
    private ToggleButton aSwitch;
    private H5 txtErrorMessage;
    private Button btnDelete;
    private Button btnCancel;
    private Button btnCreate;
    private Button btnUpdate;
    private Button btnBack;
    private SimpleTimer simpleTimer;

    public BigElectroView() {
        deviceController = BeanUtil.getBean(DeviceController.class);
        programmeConttoller = BeanUtil.getBean(ProgrammeConttoller.class);
        bigElectronicController = BeanUtil.getBean(BigElectronicController.class);
        sec = BeanUtil.getBean(UserSecurityFunc.class);
        roomController = BeanUtil.getBean(RoomController.class);
        msgSrc = BeanUtil.getBean(MessageSource.class);
        this.setSizeFull();
        this.setPadding(false);

        splitLayout = new SplitLayout();
        splitLayout.setSizeFull();
        splitLayout.addToPrimary(createGridLayout());
        splitLayout.addToSecondary(createEditorLayout());
        add(splitLayout);

    }

    private Component createEditorLayout() {
        verticalLayoutrh = new VerticalLayout();

        horizontalLayoutrh = new HorizontalLayout();
        horizontalLayoutrh.setWidthFull();
        horizontalLayoutrh.setSpacing(true);


        bigElectroForm = new BigElectroForm();
        txtErrorMessage = new H5();
        txtErrorMessage.setVisible(false);

        btnCancel = new Button(msgSrc.getMessage("rview.buttonCa",null,getLocale()));
        btnCancel.addClickListener(this:: handleClickCancel);


        btnCreate = new Button(msgSrc.getMessage("hview.buttonCr",null,getLocale()));
        btnCreate.addClickListener(this:: handleClickCreate);

        btnUpdate = new Button("Start");
        btnUpdate.addClickListener(this::handleClickUpdate);
        btnUpdate.setVisible(false);

        horizontalLayoutrh.add(btnCancel,btnCreate,btnUpdate);
        verticalLayoutrh.add(txtErrorMessage,bigElectroForm);
        verticalLayoutrh.add(horizontalLayoutrh);
        verticalLayoutrh.setWidth("20%");
        return verticalLayoutrh;
    }


    private Component createGridLayout(){
        vrlDeviceGrid = new VerticalLayout();
        vrlDeviceGrid.setSizeFull();
        hrlDeviceGrid = new HorizontalLayout();
        btnBack = new Button("Kamers");
        btnBack.addClickListener(e->handleClickBack(e));
        grid = new Grid<>();
        grid.setItems(new ArrayList<BigElectronicDTO>(0));
        grid.addColumn(BigElectronicDTO::getName).setHeader(msgSrc.getMessage("Bview.Naam",null,getLocale()));
        grid.addColumn(new ComponentRenderer<>(bigElectronicDTO -> {
            aSwitch = new ToggleButton();
            aSwitch.setValue(bigElectronicDTO.isStatus());
            if (bigElectronicDTO.isStatus()){
                aSwitch.setValue(true);
                aSwitch.addClickListener(e->handleClickOnOf(e,bigElectronicDTO));
            }else {
                aSwitch.setValue(false);
                aSwitch.setReadOnly(true);
            }
            return aSwitch;

        })).setHeader("I/O").setTextAlign(ColumnTextAlign.CENTER);
        grid.addColumn(bigElectronicDTO -> bigElectronicDTO.getType().getTypeName()).setHeader("Type");
        grid.addColumn(new ComponentRenderer<>( bigElectronicDTO -> {
            if (bigElectronicDTO.isStatus()){
                return  new Span( programmeConttoller.getProgramById(bigElectronicDTO.getProgramid()).getName());
            }
            return new Span();
        })).setHeader(msgSrc.getMessage("bview.programma",null,getLocale()));
        grid.addColumn(BigElectronicDTO::getTempature).setHeader("Temp °C");
        grid.addColumn(new ComponentRenderer<>(bigElectronicDTO -> {
            if (bigElectronicDTO.isStatus()){
                simpleTimer = new SimpleTimer();
                simpleTimer.setHours(true);
                simpleTimer.setMinutes(true);
                simpleTimer.setStartTime(calculateSeconds(bigElectronicDTO.getEindeProgramma()));
                simpleTimer.start();
                simpleTimer.addTimerEndEvent(e->handleTimeEnd(e,bigElectronicDTO));
                return simpleTimer;
            }
           return new Span();

        })).setHeader(msgSrc.getMessage( "bview.timer",null,getLocale()));
        grid.addColumn(new ComponentRenderer<>(bigElectronicDTO -> {
            if (!bigElectronicDTO.isStatus()){
                btnDelete = new Button(new Icon(VaadinIcon.TRASH));
                btnDelete.addThemeVariants(ButtonVariant.LUMO_ICON,ButtonVariant.LUMO_ERROR,ButtonVariant.LUMO_TERTIARY);
                btnDelete.addClickListener(e-> handleClickDelete(e,bigElectronicDTO.getId()));
                return btnDelete;
            }
           return new Span();
        })).setKey("delete");
        grid.setHeightFull();
        grid.asSingleSelect().addValueChangeListener(event -> populateRoomForm(event.getValue()));
        vrlDeviceGrid.add(hrlDeviceGrid);
        vrlDeviceGrid.add(btnBack,grid);
        vrlDeviceGrid.setWidth("80%");
        return vrlDeviceGrid;
    }

    private void handleClickOnOf(ClickEvent<ToggleButton> e,BigElectronicDTO bigElectronicDTO) {
        handleStart(bigElectronicDTO.getId());
        bigElectronicController.beSetToOf(bigElectronicDTO.getId());
        loadData();
    }

    private void handleTimeEnd(SimpleTimer.TimerEndedEvent e, BigElectronicDTO bigElectronicDTO) {
        loadData();
    }

    private void handleStart(long deviceid) {
        try {

            deviceController.changeStatus(deviceid);
            setButtonsToDefault();
            loadData();
        }catch (IllegalArgumentException ex){
            txtErrorMessage.setText(ex.getMessage());
            txtErrorMessage.setVisible(true);
        }
    }
    private void handleClickBack(ClickEvent<Button> e) {
        getUI().ifPresent(ui -> ui.navigate("rooms/" + getRoom().getHouseid()));
    }

    private void handleClickDelete(ClickEvent<Button> e, long id) {
        WarningDialog w = new WarningDialog(msgSrc.getMessage("bview.warn",null,getLocale()));
        w.setCloseOnEsc(false);
        w.setCloseOnOutsideClick(false);
        w.addOpenedChangeListener(event -> {
            if (!event.isOpened() && w.wasOkButtonClicked()) {
                try {
                     deviceController.deleteDeviceById(id);
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
    private void handleClickCreate(ClickEvent<Button> buttonClickEvent) {
        try {
            bigElectronicController.createApplianceDevice(new BigElectronicDTO.Builder().name(bigElectroForm.deviceForm.txtNaamDevice.getValue())
                    .status(false).type(bigElectroForm.typeDTOSelect.getValue()).roomid(roomid).build());
            setButtonsToDefault();
            loadData();
        }catch (IllegalArgumentException e){
            txtErrorMessage.setText(e.getMessage());
            txtErrorMessage.setVisible(true);
        }
    }

    private void handleClickUpdate(ClickEvent<Button> buttonClickEvent) {
        try {
            long programid;
            if (bigElectroForm.programmeDTOSelect.getValue() == null){
                programid = 0;
            }else {
                programid = bigElectroForm.programmeDTOSelect.getValue().getId();
            }
            LocalTime time = bigElectroForm.timer.getValue();
            int devicid = Integer.parseInt(bigElectroForm.deviceForm.lblid.getText());
            bigElectronicController.updateBeDeviceDevice(new BigElectronicDTO.Builder().id(devicid)
                    .status(false).name(bigElectroForm.deviceForm.txtNaamDevice.getValue()).tempature(bigElectroForm.temperature.getValue())
                    .programid(programid)
                    .timer(time).type(bigElectroForm.typeDTOSelect.getValue())
                    .roomid(roomid).build());
            handleStart(devicid);

            setButtonsToDefault();
            loadData();
        }catch (IllegalArgumentException e){
            txtErrorMessage.setText(e.getMessage());
            txtErrorMessage.setVisible(true);
        }
    }

    private void handleClickCancel(ClickEvent<Button> buttonClickEvent) {
        grid.asSingleSelect().clear();
        setButtonsToDefault();
    }

    private RoomDTO getRoom(){
        return roomController.getRoomById(roomid);
    }
    private void loadData(){
        List<BigElectronicDTO> devices = bigElectronicController.getApplianceDevicesByRoom(roomid);
        grid.setItems(devices);
    }
    private void setButtonsToDefault(){
        bigElectroForm.resetForm();
        txtErrorMessage.setVisible(false);
        if (sec.checkCurrentUserIsAdmin(getRoom().getHouseid())){
            btnCreate.setVisible(true);
            bigElectroForm.deviceForm.txtNaamDevice.setVisible(true);
        }else {
            bigElectroForm.setVisible(false);
            btnCancel.setVisible(false);
        }
        btnUpdate.setVisible(false);
    }
    private void populateRoomForm(BigElectronicDTO bigElectronicDTO) {
        setButtonsToDefault();
        if (bigElectronicDTO != null) {
            if (!bigElectronicDTO.isStatus()){
                aSwitch.setReadOnly(true);
                btnCreate.setVisible(false);
                btnUpdate.setVisible(true);
                bigElectroForm.deviceForm.lblid.setText("" + bigElectronicDTO.getId());
                bigElectroForm.deviceForm.txtNaamDevice.setValue(bigElectronicDTO.getName());
                bigElectroForm.typeDTOSelect.setValue(bigElectronicDTO.getType());
                bigElectroForm.typeDTOSelect.setVisible(false);
                if (!bigElectronicDTO.getType().getTypeName().equals("Cooling Device")){
                    bigElectroForm.programmeDTOSelect.setVisible(true);
                    bigElectroForm.timer.setVisible(true);
                }
                bigElectroForm.setVisible(true);
                bigElectroForm.deviceForm.txtNaamDevice.setVisible(false);
                btnCancel.setVisible(true);
                bigElectroForm.temperature.setVisible(true);
                bigElectroForm.temperature.setValue(bigElectronicDTO.getTempature());
            }else {
                setButtonsToDefault();
            }
        }
    }
    private long calculateSeconds(LocalDateTime endTime){
        if (endTime.isBefore(LocalDateTime.now())){
            return 0;
        }
         LocalDateTime now =  LocalDateTime.now();
         long seconds = ChronoUnit.SECONDS.between(now,endTime);
         return seconds;
    }
    @Override
    public void setParameter(BeforeEvent beforeEvent, Long id) {
        try {
            roomid = id;
            loadData();
            if (!sec.checkCurrentUserIsAdmin(getRoom().getHouseid())){
                grid.removeColumnByKey("delete");
                btnCreate.setVisible(false);
                bigElectroForm.setVisible(false);
                btnCancel.setVisible(false);
            }
        } catch (IllegalArgumentException e) {
            txtErrorMessage.setText(e.getMessage());
            txtErrorMessage.setVisible(true);
        }

    }
}
