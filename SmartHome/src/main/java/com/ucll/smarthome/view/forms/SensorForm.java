package com.ucll.smarthome.view.forms;

import com.ucll.smarthome.functions.BeanUtil;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextField;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;

public class SensorForm extends FormLayout {
    @Autowired
    private MessageSource msgSrc;
    public DeviceForm deviceForm;
    public TextField sensorType;
    public NumberField sensorData;

    public SensorForm() {
        super();
        msgSrc = BeanUtil.getBean(MessageSource.class);
        deviceForm = new DeviceForm();
        deviceForm.lblid = new Label("");
        deviceForm.txtNaamDevice = new TextField();
        deviceForm.txtNaamDevice.setRequired(true);
        deviceForm.txtNaamDevice.setErrorMessage(msgSrc.getMessage("rview.errormessage",null,getLocale()));
        sensorType = new TextField();
        sensorData = new NumberField();
        addFormItem(deviceForm.txtNaamDevice, "Device naam");
        addFormItem(sensorType, "Senor type");
        addFormItem(sensorData, "Sensor data");
    }

    public void resetForm(){
        deviceForm.lblid.setText("");
        deviceForm.txtNaamDevice.clear();
        deviceForm.txtNaamDevice.setInvalid(false);
        sensorType.clear();
        sensorData.clear();
    }
}
