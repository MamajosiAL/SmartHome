package com.ucll.smarthome.view.forms;

import com.ucll.smarthome.functions.BeanUtil;
import com.ucll.smarthome.view.customComponent.PaperSlider;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.TextField;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;

public class MediaForm extends FormLayout {
    @Autowired
    private MessageSource msgSrc;
    public Label lblid;
    public TextField txtNaamDevice;
    public PaperSlider paperSlider;
    public IntegerField integerField;
    public MediaForm() {
        super();
        msgSrc = BeanUtil.getBean(MessageSource.class);
        lblid = new Label("");
        txtNaamDevice = new TextField();
        txtNaamDevice.setRequired(true);
        txtNaamDevice.setErrorMessage(msgSrc.getMessage("rview.errormessage",null,getLocale()));
        paperSlider = new PaperSlider();
        paperSlider.setMax(100);
        paperSlider.setMin(0);
        integerField = new IntegerField();
        integerField.setHasControls(true);
        integerField.setMin(1);
        integerField.setMax(999);
        addFormItem(txtNaamDevice,"Apparaat naam");

    }

    public void resetForm(){
        lblid.setText("");
        txtNaamDevice.clear();
        txtNaamDevice.setInvalid(false);
        paperSlider.clear();
        integerField.clear();
    }

}
