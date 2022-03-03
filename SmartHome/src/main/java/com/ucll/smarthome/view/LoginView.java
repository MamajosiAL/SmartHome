package com.ucll.smarthome.view;

import com.ucll.smarthome.controller.UserController;
import com.ucll.smarthome.dto.UserDTO;
import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

@Route("")
@PageTitle("login")
@CssImport("styles/main-view.css")
public class LoginView extends VerticalLayout {
    private TextField username = new TextField("Username");
    private PasswordField password = new PasswordField("Password");
    private Button buttonLogin = new Button("Login");
    private Button buttonRegister = new Button("Register");
    private UserController userController;

    FormLayout formLayout = new FormLayout();

    public LoginView(){
        buttonRegister.addClickListener(buttonClickEvent -> handelclickEventRegister(buttonClickEvent));
        buttonLogin.addClickListener(buttonClickEvent -> handelclickEventLogin(buttonClickEvent));
        formLayout.add(
                username,
                password,
                buttonRegister,buttonLogin
        );
        formLayout.setResponsiveSteps(
                // Use one column by default
                new FormLayout.ResponsiveStep("0", 1),
                // Use two columns, if layout's width exceeds 500px
                new FormLayout.ResponsiveStep("500px", 2)
        );

    }

    private void handelclickEventLogin(ClickEvent<Button> buttonClickEvent) {
        UserDTO userDTO = new UserDTO();
        if(userController.getUserById(userDTO.getId())== null){
            Notification.show("User bestaat niet!");
        }else{
            getUI().ifPresent(ui -> ui.navigate("home"));
        }


    }

    private void handelclickEventRegister(ClickEvent<Button> buttonClickEvent) {
        getUI().ifPresent(ui -> ui.navigate("register"));
    }
}
