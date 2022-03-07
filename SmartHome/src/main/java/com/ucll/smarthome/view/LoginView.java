package com.ucll.smarthome.view;

import com.ucll.smarthome.controller.UserController;
import com.ucll.smarthome.dto.UserDTO;
import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.login.LoginForm;
import com.vaadin.flow.component.login.LoginI18n;
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

    private VerticalLayout vrl;
    private Button buttonRegister = new Button("Register");
    private LoginI18n i18n;
    private LoginForm frmLogin;

    public LoginView(){
        addClassName("login-view");

        i18n = LoginI18n.createDefault();
        i18n.getForm().setTitle("Smarthome Login");
        i18n.getForm().setUsername("Gebruikersnaam");
        i18n.getForm().setPassword("Wachtwoord");
        i18n.getErrorMessage().setTitle("Login niet gelukt");
        i18n.getErrorMessage().setMessage("Controlleer of u het juiste Gebruikersnaam en wachtwoord hebt ingevuld en probeer opnieuw");


        frmLogin = new LoginForm(i18n);
        frmLogin.setAction("login");
        frmLogin.setForgotPasswordButtonVisible(false);

        add(frmLogin,buttonRegister);
        this.setAlignItems(Alignment.CENTER);
        this.setSizeFull();
        this.setJustifyContentMode(JustifyContentMode.CENTER);
        buttonRegister.addClickListener(this::handelclickEventRegister);
        

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
