package com.ucll.smarthome.view.deviceViews;

import com.ucll.smarthome.controller.ConsumptionController;
import com.ucll.smarthome.controller.DeviceController;
import com.ucll.smarthome.controller.MediaController;
import com.ucll.smarthome.controller.RoomController;
import com.ucll.smarthome.dto.DeviceDTO;
import com.ucll.smarthome.dto.MediaDTO;
import com.ucll.smarthome.dto.RoomDTO;
import com.ucll.smarthome.functions.BeanUtil;
import com.ucll.smarthome.functions.UserSecurityFunc;
import com.ucll.smarthome.persistence.entities.MediaDevice;
import com.ucll.smarthome.view.MainView;
import com.ucll.smarthome.view.customComponent.PaperSlider;
import com.ucll.smarthome.view.dialogs.WarningDialog;
import com.ucll.smarthome.view.forms.DeviceForm;
import com.ucll.smarthome.view.forms.MediaForm;
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
import com.vaadin.flow.component.splitlayout.SplitLayout;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.BeforeEvent;
import com.vaadin.flow.router.HasUrlParameter;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.security.access.AccessDeniedException;

import java.util.ArrayList;
import java.util.List;

@Route(value = "media's",layout = MainView.class)
public class MediaView extends VerticalLayout implements HasUrlParameter<Long> {

    @Autowired
    private DeviceController deviceController;
    @Autowired
    private MediaController mediaController;
    @Autowired
    private ConsumptionController consumptionController;


    @Autowired
    private final UserSecurityFunc sec;
    @Autowired
    private final RoomController roomController;

    @Autowired
    private MessageSource msgSrc;

    private Grid<MediaDTO> grid;
    private long roomid;
    private VerticalLayout vrlDeviceGrid;
    private VerticalLayout verticalLayoutrh;
    private HorizontalLayout horizontalLayoutrh;
    private HorizontalLayout hrlDeviceGrid;
    private SplitLayout splitLayout;
    private MediaForm mediaForm;
    private ToggleButton aSwitch;
    private H5 txtErrorMessage;
    private Button btnDelete;
    private Button btnCancel;
    private Button btnCreate;
    private Button btnUpdate;

    public PaperSlider paperSlider;
    public IntegerField integerField;

    public MediaView() {
        deviceController = BeanUtil.getBean(DeviceController.class);
        mediaController = BeanUtil.getBean(MediaController.class);
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

        mediaForm = new MediaForm();
        txtErrorMessage = new H5();
        txtErrorMessage.setVisible(false);

        btnCancel = new Button(msgSrc.getMessage("rview.buttonCa",null,getLocale()));
        btnCancel.addClickListener(this:: handleClickCancel);


        btnCreate = new Button(msgSrc.getMessage("hview.buttonCr",null,getLocale()));
        btnCreate.addClickListener(this:: handleClickCreate);

        btnUpdate = new Button(msgSrc.getMessage("hview.save",null,getLocale()));
        btnUpdate.addClickListener(this::handleClickUpdate);
        btnUpdate.setVisible(false);

        horizontalLayoutrh.add(btnCancel,btnCreate,btnUpdate);
        verticalLayoutrh.add(txtErrorMessage,mediaForm);
        verticalLayoutrh.add(horizontalLayoutrh);
        verticalLayoutrh.setWidth("20%");
        return verticalLayoutrh;
    }


    private Component createGridLayout(){
        vrlDeviceGrid = new VerticalLayout();
        vrlDeviceGrid.setSizeFull();
        hrlDeviceGrid = new HorizontalLayout();
        grid = new Grid<>();
        grid.setItems(new ArrayList<MediaDTO>(0));
        grid.addColumn(MediaDTO::getName).setHeader(msgSrc.getMessage("Bview.Naam",null,getLocale()));
        grid.addColumn(new ComponentRenderer<>(mediaDTO -> {
            aSwitch = new ToggleButton();
            aSwitch.setValue(mediaDTO.isStatus());
            aSwitch.addClickListener(e-> handleClickOnOf(e,mediaDTO));
            return aSwitch;
        })).setHeader("I/O").setTextAlign(ColumnTextAlign.CENTER);
        grid.addColumn(new ComponentRenderer<>(mediaDTO -> {
            paperSlider = new PaperSlider();
            paperSlider.setMax(100);
            paperSlider.setMin(0);
            paperSlider.setValue(mediaDTO.getVolume());
            if (!mediaDTO.isStatus()){
                paperSlider.setReadOnly(true);
            }
            paperSlider.addValueChangeListener(e->handleChangeVolume(e,mediaDTO));
            return paperSlider;
        })).setHeader("Volume");
        grid.addColumn(new ComponentRenderer<>(mediaDTO -> {
            integerField = new IntegerField();
            integerField.setHasControls(true);
            integerField.setMin(1);
            integerField.setMax(999);
            integerField.setValue(mediaDTO.getZender());
            if (!mediaDTO.isStatus()){
                integerField.setReadOnly(true);
            }
            integerField.addValueChangeListener(e->handleChangeZender(e,mediaDTO));
            return integerField;
        })).setHeader(msgSrc.getMessage("bview.Zender",null,getLocale()));
        grid.addColumn(new ComponentRenderer<>(roomDTO -> {
            btnDelete = new Button(new Icon(VaadinIcon.TRASH));
            btnDelete.addThemeVariants(ButtonVariant.LUMO_ICON,ButtonVariant.LUMO_ERROR,ButtonVariant.LUMO_TERTIARY);
            btnDelete.addClickListener(e-> handleClickDelete(e,roomDTO.getId()));
            return btnDelete;
        })).setKey("delete");
        grid.setHeightFull();
        grid.asSingleSelect().addValueChangeListener(event -> populateRoomForm(event.getValue()));
        vrlDeviceGrid.add(hrlDeviceGrid);
        vrlDeviceGrid.add(grid);
        vrlDeviceGrid.setWidth("80%");
        return vrlDeviceGrid;
    }

    private void handleChangeZender(AbstractField.ComponentValueChangeEvent<IntegerField, Integer> e, MediaDTO mediaDTO) {
        try {
            mediaController.updateAudioDevice(new MediaDTO.Builder().id(mediaDTO.getId()).name(mediaDTO.getName())
                    .status(true).volume(mediaDTO.getVolume()).zender(e.getValue()).roomid(roomid).build());
            setButtonsToDefault();
            loadData();
        }catch (IllegalArgumentException ex){
            txtErrorMessage.setText(ex.getMessage());
            txtErrorMessage.setVisible(true);
        }
    }

    private void handleChangeVolume(AbstractField.ComponentValueChangeEvent<PaperSlider, Integer> e, MediaDTO mediaDTO) {
        try {
            mediaController.updateAudioDevice(new MediaDTO.Builder().id(mediaDTO.getId()).name(mediaDTO.getName())
                    .status(true).volume(e.getValue()).zender(mediaDTO.getZender()).roomid(roomid).build());
            setButtonsToDefault();
            loadData();
        }catch (IllegalArgumentException ex){
            txtErrorMessage.setText(ex.getMessage());
            txtErrorMessage.setVisible(true);
        }
    }


    private void handleClickOnOf(ClickEvent<ToggleButton> e,MediaDTO mediaDTO) {
        try {
            deviceController.changeStatus(mediaDTO.getId());
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
            mediaController.createAudioDevice(new MediaDTO.Builder().name(mediaForm.deviceForm.txtNaamDevice.getValue()).status(false)
                    .volume(0).zender(1).roomid(roomid).build());
            setButtonsToDefault();
            loadData();
        }catch (IllegalArgumentException e){
            txtErrorMessage.setText(e.getMessage());
            txtErrorMessage.setVisible(true);
        }
    }

    private void handleClickUpdate(ClickEvent<Button> buttonClickEvent) {
        try {
            mediaController.updateAudioDevice(new MediaDTO.Builder().id(Integer.parseInt(mediaForm.deviceForm.lblid.getText())).name(mediaForm.deviceForm.txtNaamDevice.getValue())
                    .status(mediaForm.deviceForm.isStatus).volume(mediaForm.volume.getValue()).zender(mediaForm.zender.getValue()).roomid(roomid).build());
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
        List<MediaDTO> devices = mediaController.getAdioDevicesByRoom(roomid);
        grid.setItems(devices);
    }
    private void setButtonsToDefault(){
        mediaForm.resetForm();
        txtErrorMessage.setVisible(false);
        if (sec.checkCurrentUserIsAdmin(getRoom().getHouseid())){
            btnCreate.setVisible(true);
        }
        btnUpdate.setVisible(false);
    }
    private void populateRoomForm(MediaDTO mediaDTO) {
        setButtonsToDefault();
        if (mediaDTO != null) {
            btnCreate.setVisible(false);
            btnUpdate.setVisible(true);
            mediaForm.deviceForm.lblid.setText("" + mediaDTO.getId());
            mediaForm.deviceForm.txtNaamDevice.setValue(mediaDTO.getName());
            mediaForm.volume.setValue(mediaDTO.getVolume());
            mediaForm.zender.setValue(mediaDTO.getZender());
            mediaForm.deviceForm.isStatus = mediaDTO.isStatus();
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
                mediaForm.setVisible(false);
            }
        } catch (IllegalArgumentException e) {
            Notification.show(e.getMessage() ,3000, Notification.Position.TOP_CENTER);
        }

    }
}
