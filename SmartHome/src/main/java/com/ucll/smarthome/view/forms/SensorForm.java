package com.ucll.smarthome.view.forms;

import com.ucll.smarthome.functions.BeanUtil;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextField;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;

public class SensorForm extends FormLayout {
    @Autowired
    private MessageSource msgSrc;
    public DeviceForm deviceForm;
    public Select<String> sensorType;
    public NumberField sensorData;

    public SensorForm() {
        super();
        msgSrc = BeanUtil.getBean(MessageSource.class);
        deviceForm = new DeviceForm();
        deviceForm.lblid = new Label("");
        deviceForm.txtNaamDevice = new TextField();
        deviceForm.txtNaamDevice.setRequired(true);
        deviceForm.txtNaamDevice.setErrorMessage(msgSrc.getMessage("rview.errormessage",null,getLocale()));
        sensorType = new Select<>();
        sensorType.setItems("Thermostat","Motion Sensor","Water Leak/Freeze Sensor","Window/Door Sensor","Smart Smoke Sensor");
        sensorType.setEmptySelectionAllowed(true);
        sensorData = new NumberField();
        sensorData.setValue(0.00);
        sensorData.setMin(0.00);
        addFormItem(deviceForm.txtNaamDevice, "Device naam");
        addFormItem(sensorType, "Senor type");
        addFormItem(sensorData, "Sensor data");
    }

    public void resetForm(){
        deviceForm.lblid.setText("");
        deviceForm.txtNaamDevice.clear();
        deviceForm.txtNaamDevice.setInvalid(false);
        sensorType.clear();
        sensorData.setValue(0.00);
    }
}
