package com.ucll.smarthome.view.deviceViews;

import com.ucll.smarthome.controller.ConsumptionController;
import com.ucll.smarthome.controller.DeviceController;
import com.ucll.smarthome.controller.RoomController;
import com.ucll.smarthome.controller.SensorController;
import com.ucll.smarthome.dto.DeviceDTO;
import com.ucll.smarthome.dto.RoomDTO;
import com.ucll.smarthome.dto.SensorDTO;
import com.ucll.smarthome.functions.BeanUtil;
import com.ucll.smarthome.functions.UserSecurityFunc;
import com.ucll.smarthome.persistence.entities.enums.SensorType;
import com.ucll.smarthome.persistence.entities.enums.SensorTypeConverter;
import com.ucll.smarthome.view.MainView;
import com.ucll.smarthome.view.dialogs.WarningDialog;
import com.ucll.smarthome.view.forms.DeviceForm;
import com.ucll.smarthome.view.forms.MediaForm;
import com.ucll.smarthome.view.forms.SensorForm;
import com.vaadin.componentfactory.ToggleButton;
import com.vaadin.flow.component.AbstractField;
import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.grid.ColumnTextAlign;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H5;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.select.Select;
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

@Route(value = "sensors",layout = MainView.class)
public class SensorView extends VerticalLayout implements HasUrlParameter<Long> {

    @Autowired
    private DeviceController deviceController;
    @Autowired
    private ConsumptionController consumptionController;
    @Autowired
    private SensorController sensorController;


    @Autowired
    private final UserSecurityFunc sec;
    @Autowired
    private final RoomController roomController;

    @Autowired
    private MessageSource msgSrc;

    private Grid<SensorDTO> grid;
    private long roomid;
    private VerticalLayout vrlDeviceGrid;
    private VerticalLayout verticalLayoutrh;
    private HorizontalLayout horizontalLayoutrh;
    private HorizontalLayout hrlDeviceGrid;
    private SplitLayout splitLayout;
    private SensorForm sensorForm;
    private ToggleButton aSwitch;
    private H5 txtErrorMessage;
    private Button btnDelete;
    private Button btnCancel;
    private Button btnCreate;
    private Button btnUpdate;
    private Button btnBack;

    public SensorView() {
        deviceController = BeanUtil.getBean(DeviceController.class);
        sensorController = BeanUtil.getBean(SensorController.class);
        consumptionController = BeanUtil.getBean(ConsumptionController.class);
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

        sensorForm = new SensorForm();
        txtErrorMessage = new H5();
        txtErrorMessage.setVisible(false);

        sensorForm.sensorType.addValueChangeListener(e -> handleChangeType(e));

        btnCancel = new Button(msgSrc.getMessage("rview.buttonCa",null,getLocale()));
        btnCancel.addClickListener(this:: handleClickCancel);


        btnCreate = new Button(msgSrc.getMessage("hview.buttonCr",null,getLocale()));
        btnCreate.addClickListener(this:: handleClickCreate);

        btnUpdate = new Button(msgSrc.getMessage("hview.save",null,getLocale()));
        btnUpdate.addClickListener(this::handleClickUpdate);
        btnUpdate.setVisible(false);

        horizontalLayoutrh.add(btnCancel,btnCreate,btnUpdate);
        verticalLayoutrh.add(txtErrorMessage,sensorForm);
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
        grid.setItems(new ArrayList<SensorDTO>(0));
        grid.addColumn(SensorDTO::getName).setHeader(msgSrc.getMessage("Bview.Naam",null,getLocale()));
        grid.addColumn(new ComponentRenderer<>(deviceDTO -> {
            aSwitch = new ToggleButton();
            aSwitch.setValue(deviceDTO.isStatus());
            aSwitch.addClickListener(e-> handleClickOnOf(e,deviceDTO));
            return aSwitch;
        })).setHeader("I/O").setTextAlign(ColumnTextAlign.CENTER);
        grid.addColumn(SensorDTO::getSensorType).setHeader("Sensor Type");
        grid.addColumn(SensorDTO::getSensordata).setHeader("Sensor Data");
        grid.addColumn(new ComponentRenderer<>(roomDTO -> {
            btnDelete = new Button(new Icon(VaadinIcon.TRASH));
            btnDelete.addThemeVariants(ButtonVariant.LUMO_ICON,ButtonVariant.LUMO_ERROR,ButtonVariant.LUMO_TERTIARY);
            btnDelete.addClickListener(e-> handleClickDelete(e,roomDTO.getId()));
            return btnDelete;
        })).setKey("delete");
        grid.setHeightFull();
        grid.asSingleSelect().addValueChangeListener(event -> populateRoomForm(event.getValue()));
        vrlDeviceGrid.add(hrlDeviceGrid);
        vrlDeviceGrid.add(btnBack,grid);
        vrlDeviceGrid.setWidth("80%");
        return vrlDeviceGrid;
    }

    private void handleClickBack(ClickEvent<Button> e) {
        getUI().ifPresent(ui -> ui.navigate("rooms/"+getRoom().getHouseid()));
    }
    private void handleChangeType(AbstractField.ComponentValueChangeEvent<Select<String>, String> e) {
        if (e.getValue()!=null){
            sensorForm.sensorData.setVisible(e.getValue().equals("Thermostat"));
        }else{
            sensorForm.sensorData.setVisible(true);
        }


    }
    private void handleClickOnOf(ClickEvent<ToggleButton> e,SensorDTO sensorDTO) {
        try {
            deviceController.changeStatus(sensorDTO.getId());
            setButtonsToDefault();
            loadData();
        }catch (IllegalArgumentException ex){
            txtErrorMessage.setText(ex.getMessage());
            txtErrorMessage.setVisible(true);
        }
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
            if (sensorForm.sensorData.getValue() == null){
                sensorForm.sensorData.setValue(0.00);
            }
            sensorController.createSensorDevice(new SensorDTO.Builder().name(sensorForm.deviceForm.txtNaamDevice.getValue())
                    .status(false).sensorType(sensorForm.sensorType.getValue()).sensordata(sensorForm.sensorData.getValue()).roomid(roomid).build());
            setButtonsToDefault();
            loadData();
        }catch (IllegalArgumentException e){
            txtErrorMessage.setText(e.getMessage());
            txtErrorMessage.setVisible(true);
        }
    }

    private void handleClickUpdate(ClickEvent<Button> buttonClickEvent) {
        try {
            if (sensorForm.sensorData.getValue() == null){
                sensorForm.sensorData.setValue(0.00);
            }
            sensorController.updateSensorDevice(new SensorDTO.Builder().id(Integer.parseInt(sensorForm.deviceForm.lblid.getText()))
                    .status(sensorForm.deviceForm.isStatus).name(sensorForm.deviceForm.txtNaamDevice.getValue())
                    .sensordata(sensorForm.sensorData.getValue()).roomid(roomid).build());
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
        List<SensorDTO> devices = sensorController.getSonsorDevicesByRoom(roomid);
        grid.setItems(devices);
    }
    private void setButtonsToDefault(){
        sensorForm.resetForm();
        sensorForm.sensorType.setVisible(true);
        sensorForm.sensorData.setVisible(true);
        txtErrorMessage.setVisible(false);
        if (sec.checkCurrentUserIsAdmin(getRoom().getHouseid())){
            btnCreate.setVisible(true);
        }
        btnUpdate.setVisible(false);
    }
    private void populateRoomForm(SensorDTO sensorDTO) {
        setButtonsToDefault();
        if (sensorDTO != null) {
            btnCreate.setVisible(false);
            btnUpdate.setVisible(true);
            sensorForm.deviceForm.lblid.setText("" + sensorDTO.getId());
            sensorForm.deviceForm.txtNaamDevice.setValue(sensorDTO.getName());
            sensorForm.sensorType.setValue(sensorDTO.getSensorType());
            sensorForm.sensorType.setVisible(false);
            sensorForm.sensorType.setLabel("");
            sensorForm.deviceForm.isStatus = sensorDTO.isStatus();
            if (sensorDTO.getSensorType() == null || sensorDTO.getSensorType().equals("Thermostat") ){
                sensorForm.sensorData.setVisible(true);
                sensorForm.sensorData.setValue(sensorDTO.getSensordata());
            }
            if (sensorDTO.getSensorType() != null ){
                if (!sensorDTO.getSensorType().equals("Thermostat")){
                sensorForm.sensorData.setVisible(false);}
            }


        }
    }
    @Override
    public void setParameter(BeforeEvent beforeEvent, Long id) {
        try {
            roomid = id;
            loadData();
            if (!sec.checkCurrentUserIsAdmin(getRoom().getHouseid())){
                grid.removeColumnByKey("delete");
                btnCreate.setVisible(false);
                btnCancel.setVisible(false);
                sensorForm.deviceForm.txtNaamDevice.setVisible(false);
                sensorForm.sensorType.setVisible(false);
                sensorForm.sensorData.setVisible(false);
            }
        } catch (IllegalArgumentException e) {
            txtErrorMessage.setText(e.getMessage());
            txtErrorMessage.setVisible(true);
        }

    }
}
