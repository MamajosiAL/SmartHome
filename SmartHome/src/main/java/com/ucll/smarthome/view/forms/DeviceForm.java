package com.ucll.smarthome.view.forms;

import com.ucll.smarthome.functions.BeanUtil;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.textfield.TextField;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;

public class DeviceForm extends FormLayout {
    @Autowired
    private MessageSource msgSrc;
    public Label lblid;
    public TextField txtNaamDevice;

    public DeviceForm() {
        super();
        msgSrc = BeanUtil.getBean(MessageSource.class);
        lblid = new Label("");
        txtNaamDevice = new TextField();
        txtNaamDevice.setRequired(true);
        txtNaamDevice.setErrorMessage(msgSrc.getMessage("rview.errormessage",null,getLocale()));
        addFormItem(txtNaamDevice, msgSrc.getMessage("rform.roomname",null,getLocale()));
    }

    public void resetForm(){
        lblid.setText("");
        txtNaamDevice.clear();
        txtNaamDevice.setInvalid(false);
    }

}
