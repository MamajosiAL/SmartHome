package com.ucll.smarthome.view;

import com.ucll.smarthome.controller.UserController;
import com.ucll.smarthome.dto.UserDTO;
import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.login.LoginForm;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

@Route("login")
@PageTitle("login")
@CssImport("styles/main-view.css")
public class LoginView extends VerticalLayout implements BeforeEnterObserver {
    private final LoginForm login = new LoginForm();

    private Button buttonRegister = new Button("Register");

    public LoginView(){
        addClassName("login-view");
        setSizeFull();
        setAlignItems(Alignment.CENTER);
        setJustifyContentMode(JustifyContentMode.CENTER);

        login.setAction("login");
        add(new H1("Smarthome Login"), login, buttonRegister);
        buttonRegister.addClickListener(buttonClickEvent -> handelclickEventRegister(buttonClickEvent));
        

    }
    @Override
    public void beforeEnter(BeforeEnterEvent beforeEnterEvent){
        if(beforeEnterEvent.getLocation().getQueryParameters().getParameters().containsKey("error"))
        {
            login.setError(true);

        }
    }

    private void handelclickEventRegister(ClickEvent<Button> buttonClickEvent) {
        getUI().ifPresent(ui -> ui.navigate("register"));
    }
}
