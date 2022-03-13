package com.ucll.smarthome.view.forms;


import com.ucll.smarthome.functions.BeanUtil;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.textfield.TextField;
import org.springframework.context.MessageSource;

public class HouseForm extends FormLayout {

    private MessageSource msg;
    public Label lblId;
    public TextField txtnaamhuis;

    public HouseForm(){
        super();

        msg = BeanUtil.getBean(MessageSource.class);


        lblId = new Label("");
        txtnaamhuis = new TextField();
        txtnaamhuis.setRequired(true);
        txtnaamhuis.setMaxLength(128);
        txtnaamhuis.setErrorMessage( msg.getMessage("rview.errormessage",null,getLocale()));

        addFormItem(txtnaamhuis, "hform.housename");
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
