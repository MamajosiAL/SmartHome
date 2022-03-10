package com.ucll.smarthome.view;

import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.textfield.TextField;

public class RoomForm extends FormLayout {
    public Label lblid;
    public TextField txtNameRoom;
    public Label lblidHouse;

    public RoomForm(){
        super();
        lblid = new Label("");
        lblidHouse = new Label("");
        txtNameRoom = new TextField();
        txtNameRoom.setRequired(true);
        txtNameRoom.setMaxLength(128);
        txtNameRoom.setErrorMessage("rview.errormessage");

        addFormItem(txtNameRoom, "rform.roomname");
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
