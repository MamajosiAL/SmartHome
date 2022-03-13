package com.ucll.smarthome.view.forms;


import com.ucll.smarthome.functions.BeanUtil;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.textfield.TextField;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;

public class HouseForm  extends FormLayout {

    @Autowired
    private MessageSource msgSrc;
    public Label lblId;
    public TextField txtnaamhuis;

    public HouseForm(){
        super();
        msgSrc = BeanUtil.getBean(MessageSource.class);
        lblId = new Label("");
        txtnaamhuis = new TextField();
        txtnaamhuis.setRequired(true);
        txtnaamhuis.setMaxLength(128);
        txtnaamhuis.setErrorMessage(msgSrc.getMessage("rview.errormessage",null,getLocale()));

        addFormItem(txtnaamhuis, msgSrc.getMessage("hform.housename",null,getLocale()));
    }

    public void resetForm(){
        lblId.setText("");
        txtnaamhuis.clear();
        txtnaamhuis.setInvalid(false);
    }

    public boolean isformValid(){
        boolean result = true;
        if(txtnaamhuis.getValue() == null){
            txtnaamhuis.setInvalid(true);
            result = false;
        }
        return result;
    }
}
