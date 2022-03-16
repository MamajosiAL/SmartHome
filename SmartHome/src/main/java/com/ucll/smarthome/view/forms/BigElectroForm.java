package com.ucll.smarthome.view.forms;

import com.ucll.smarthome.functions.BeanUtil;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.timepicker.TimePicker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;

public class BigElectroForm extends FormLayout {
    @Autowired
    private MessageSource msgSrc;
    public DeviceForm deviceForm;
    public NumberField temperature;
    public TimePicker timer;

    public BigElectroForm() {
        super();
        msgSrc = BeanUtil.getBean(MessageSource.class);
        deviceForm.lblid = new Label("");
        deviceForm.txtNaamDevice = new TextField();
        deviceForm.txtNaamDevice.setRequired(true);
        deviceForm.txtNaamDevice.setErrorMessage(msgSrc.getMessage("rview.errormessage",null,getLocale()));
        temperature = new NumberField();
        temperature.setLabel("Temperatuur °C");
        timer = new TimePicker();
        timer.setLabel("Duur");
        addFormItem(deviceForm.txtNaamDevice, "Device naam");
        addFormItem(temperature,"");
        addFormItem(timer,"");
    }

    public void resetForm(){
        deviceForm.lblid.setText("");
        deviceForm.txtNaamDevice.clear();
        deviceForm.txtNaamDevice.setInvalid(false);
        timer.clear();
        temperature.clear();
    }

}
