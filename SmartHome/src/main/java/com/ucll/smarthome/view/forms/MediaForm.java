package com.ucll.smarthome.view.forms;

import com.ucll.smarthome.functions.BeanUtil;
import com.ucll.smarthome.view.customComponent.PaperSlider;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextField;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;

public class MediaForm extends FormLayout {
    @Autowired
    private MessageSource msgSrc;
    public DeviceForm deviceForm;
    public PaperSlider volume;
    public IntegerField zender;
    public MediaForm() {
        super();
        msgSrc = BeanUtil.getBean(MessageSource.class);
        deviceForm = new DeviceForm();
        volume = new PaperSlider();
        volume.setMax(100);
        volume.setMin(0);
        zender = new IntegerField();
        zender.setHasControls(true);
        zender.setMin(1);
        zender.setMax(999);
        addFormItem(deviceForm.txtNaamDevice,"Apparaat naam");

    }

    public void resetForm(){
        deviceForm.lblid.setText("");
        deviceForm.txtNaamDevice.clear();
        deviceForm.txtNaamDevice.setInvalid(false);
        volume.clear();
        zender.clear();
    }

}
