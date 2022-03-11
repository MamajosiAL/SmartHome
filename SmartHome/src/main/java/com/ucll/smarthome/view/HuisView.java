package com.ucll.smarthome.view;

import com.ucll.smarthome.controller.HouseController;
import com.ucll.smarthome.functions.UserSecurityFunc;
import com.ucll.smarthome.view.dialogs.WarningDialog;
import com.vaadin.flow.component.ClickEvent;
import com.ucll.smarthome.dto.HouseDTO;
import com.vaadin.flow.component.button.Button;
import com.ucll.smarthome.functions.BeanUtil;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.splitlayout.SplitLayout;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.router.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.context.MessageSource;
import org.springframework.security.access.AccessDeniedException;
import java.util.ArrayList;
import java.util.List;

public class HuisView extends VerticalLayout {

        @Autowired
        private MessageSource msgSrc;
        private final HouseController hc;


        private final UserSecurityFunc sec;

        private SplitLayout splitLayout;
        private VerticalLayout verticalLayoutlf;
        private HorizontalLayout lphLayout;
        private VerticalLayout verticalLayoutrh;
        private HorizontalLayout horizontalLayoutrh;
        private HuisForm hfrm;

        private Grid<HouseDTO> grid;
        private Span role;

        private Button btnCancel;
        private Button btnCreate;
        private Button btnUpdate;
        private Button btnDelete;

    public HuisView(UserSecurityFunc sec){
        super();
        this.sec = sec;
        msgSrc = BeanUtil.getBean(MessageSource.class);


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
        grid.setHeightFull();

        grid.asSingleSelect().addValueChangeListener(event -> populateHouseForm(event.getValue()));
        verticalLayoutlf.add(lphLayout);
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



        btnCancel = new Button(msgSrc.getMessage("rview.buttonCa",null,getLocale()));
        btnCancel.addClickListener(e -> handleClickCancel(e));

        btnCreate = new Button(msgSrc.getMessage("rview.buttonCr",null,getLocale()));
        btnCreate.addClickListener(e -> handleClickCreate(e));

        btnUpdate = new Button(msgSrc.getMessage("hview.save",null,getLocale()));
        btnUpdate.addClickListener(e -> handleClickUpdate(e));
        btnUpdate.setVisible(false);

        btnDelete = new Button(msgSrc.getMessage("hview.delete",null,getLocale()));
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
            hfrm.resetForm();
            loadData();
            btnCreate.setVisible(true);
            btnUpdate.setVisible(false);
            btnDelete.setVisible(false);
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
                hfrm.resetForm();
                btnCreate.setVisible(true);
                btnUpdate.setVisible(false);
                btnDelete.setVisible(false);
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
}
