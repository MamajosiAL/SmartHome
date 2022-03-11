package com.ucll.smarthome.view;


import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.textfield.TextField;

public class HuisForm  extends FormLayout {
    public Label lblId;
    public TextField txtnaamhuis;

    public HuisForm(){
        super();
        lblId = new Label("");
        txtnaamhuis = new TextField();
        txtnaamhuis.setRequired(true);
        txtnaamhuis.setMaxLength(128);
        txtnaamhuis.setErrorMessage("rview.errormessage");

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
