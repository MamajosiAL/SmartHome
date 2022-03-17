package com.ucll.smarthome.view.forms;

import com.ucll.smarthome.controller.ProgrammeConttoller;
import com.ucll.smarthome.dto.ProgrammeDTO;
import com.ucll.smarthome.dto.TypeDTO;
import com.ucll.smarthome.functions.BeanUtil;
import com.ucll.smarthome.persistence.entities.Type;
import com.vaadin.flow.component.AbstractField;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.timepicker.TimePicker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;

import java.time.Duration;
import java.util.List;

public class BigElectroForm extends FormLayout {
    @Autowired
    private MessageSource msgSrc;
    @Autowired
    private ProgrammeConttoller programmeConttoller;
    public DeviceForm deviceForm;
    public NumberField temperature;
    public TimePicker timer;
    public Select<TypeDTO> typeDTOSelect;
    public Select<ProgrammeDTO> programmeDTOSelect;

    public BigElectroForm() {
        super();
        msgSrc = BeanUtil.getBean(MessageSource.class);
        programmeConttoller = BeanUtil.getBean(ProgrammeConttoller.class);
        deviceForm = new DeviceForm();
        temperature = new NumberField();
        temperature.setLabel("Temperatuur °C");
        temperature.setVisible(false);
        timer = new TimePicker();
        //timer.setStep(Duration.ofSeconds(1));
        timer.setLabel("Duur(HH:mm)");
        timer.setPlaceholder("HH::MM");
        timer.setVisible(false);
        typeDTOSelect = new Select<>();
        typeDTOSelect.setLabel("Type");
        typeDTOSelect.setRequiredIndicatorVisible(true);
        List<TypeDTO> list = programmeConttoller.getTypes();
        typeDTOSelect.setItems(list);
        typeDTOSelect.setItemLabelGenerator(typeDTO -> {
            if (typeDTO == null){
                return "";
            }else {
                return typeDTO.getTypeName();
            }
        });
        typeDTOSelect.addValueChangeListener(e->handleChangeType(e));
        programmeDTOSelect = new Select<>();
        programmeDTOSelect.setLabel(msgSrc.getMessage("bview.programma",null,getLocale()));
        programmeDTOSelect.setEmptySelectionAllowed(true);
        programmeDTOSelect.setVisible(false);
        programmeDTOSelect.addValueChangeListener(e -> handleChangeProgramme(e));
        addFormItem(deviceForm.txtNaamDevice, "");
        addFormItem(typeDTOSelect,"");
        addFormItem(programmeDTOSelect,"");
        addFormItem(temperature,"");
        addFormItem(timer,"");


    }

    private void handleChangeProgramme(AbstractField.ComponentValueChangeEvent<Select<ProgrammeDTO>, ProgrammeDTO> e) {
        if (e.getValue()!=null){
            temperature.setValue(e.getValue().getTemp());
            timer.setValue(e.getValue().getTimer());
        }else {
            temperature.clear();
            timer.clear();
        }
    }

    private void handleChangeType(AbstractField.ComponentValueChangeEvent<Select<TypeDTO>, TypeDTO> e) {
        if (e.getValue() != null){
            List<ProgrammeDTO> list = programmeConttoller.getProgramsByTypeId(e.getValue().getTypeid());
            programmeDTOSelect.setItems(list);
            programmeDTOSelect.setItemLabelGenerator(programmeDTO -> {
                if (programmeDTO == null){
                    return "";
                }else {
                    return programmeDTO.getName();
                }
            });
        }
    }

    public void resetForm(){
        deviceForm.lblid.setText("");
        deviceForm.txtNaamDevice.clear();
        deviceForm.txtNaamDevice.setInvalid(false);
        typeDTOSelect.clear();
        timer.clear();
        temperature.clear();
        programmeDTOSelect.clear();
        programmeDTOSelect.setVisible(false);
        typeDTOSelect.setVisible(true);
        temperature.setVisible(false);
        timer.setVisible(false);
    }

}
