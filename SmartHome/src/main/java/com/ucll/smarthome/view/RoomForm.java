package com.ucll.smarthome.view;

import com.ucll.smarthome.functions.BeanUtil;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.textfield.TextField;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;

public class RoomForm extends FormLayout {
    @Autowired
    private MessageSource msgSrc;
    public Label lblid;
    public TextField txtNameRoom;
    public Label lblidHouse;

    public RoomForm(){
        super();
        msgSrc = BeanUtil.getBean(MessageSource.class);
        lblid = new Label("");
        lblidHouse = new Label("");
        txtNameRoom = new TextField();
        txtNameRoom.setRequired(true);
        txtNameRoom.setMaxLength(128);
        txtNameRoom.setErrorMessage(msgSrc.getMessage("rview.errormessage",null,getLocale()));

        addFormItem(txtNameRoom, msgSrc.getMessage("rform.roomname",null,getLocale()));
    }

    public void resetForm(){
        lblid.setText("");
        lblidHouse.setText("");
        txtNameRoom.clear();
        txtNameRoom.setInvalid(false);
    }

    public boolean isforValid(){
        boolean result = true;
        if(txtNameRoom.getValue() == null){
            txtNameRoom.setInvalid(true);
            result = false;
        }
        return result;
    }
}
