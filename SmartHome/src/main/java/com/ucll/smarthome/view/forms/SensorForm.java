package com.ucll.smarthome.view.forms;

import com.ucll.smarthome.functions.BeanUtil;
import com.vaadin.flow.component.AbstractField;
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
        sensorType = new Select<>();
        sensorType.setItems("Thermostat","Motion Sensor","Water Leak/Freeze Sensor","Window/Door Sensor","Smart Smoke Sensor");
        sensorType.setEmptySelectionAllowed(true);
        sensorType.setLabel("Sensor type");
        sensorType.setLabel("Sensor type");
       // sensorType.addValueChangeListener(e-> handleValueChange(e));
        sensorData = new NumberField();
        sensorData.setValue(0.00);
        sensorData.setMin(0.00);
        sensorData.setLabel("Sensor data");
        addFormItem(deviceForm.txtNaamDevice, "Device naam");
        addFormItem(sensorType, "");
        addFormItem(sensorData, "");
    }

  /*  private void handleValueChange(AbstractField.ComponentValueChangeEvent<Select<String>, String> e) {

        if (e.getValue().equals("Thermostat")){
            sensorData.setVisible(true);
        }else  {
            sensorData.setVisible(false);
        }
    }*/

    public void resetForm(){
        deviceForm.lblid.setText("");
        deviceForm.txtNaamDevice.clear();
        deviceForm.txtNaamDevice.setInvalid(false);
        sensorType.clear();
        sensorData.setValue(0.00);
    }
}
