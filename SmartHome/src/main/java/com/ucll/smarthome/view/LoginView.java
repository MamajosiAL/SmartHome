package com.ucll.smarthome.view;

import com.ucll.smarthome.controller.UserController;
import com.ucll.smarthome.dto.UserDTO;
import com.ucll.smarthome.functions.BeanUtil;
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
import com.vaadin.flow.server.VaadinSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;

import java.util.Locale;

@Route("login")
@PageTitle("login")
@CssImport("styles/main-view.css")
public class LoginView extends VerticalLayout implements BeforeEnterObserver {

    @Autowired
    private MessageSource msgSrc;
    private Locale loc;


    private VerticalLayout vrl;
    private Button buttonRegister;
    private LoginI18n i18n;
    private LoginForm frmLogin;

    public LoginView(){
        msgSrc = BeanUtil.getBean(MessageSource.class);
        loc = VaadinSession.getCurrent().getLocale();

        addClassName("login-view");

        i18n = LoginI18n.createDefault();
        i18n.getForm().setTitle("Smarthome Login");
        i18n.getForm().setUsername(msgSrc.getMessage("lview.username",null,getLocale()));
        i18n.getForm().setPassword(msgSrc.getMessage("lview.password",null,getLocale()));
        i18n.getErrorMessage().setTitle(msgSrc.getMessage("lview.loginfail",null,getLocale()));
        i18n.getErrorMessage().setMessage(msgSrc.getMessage("lview.loginfailmessage",null,getLocale()));


        frmLogin = new LoginForm(i18n);
        frmLogin.setAction("login");
        frmLogin.setForgotPasswordButtonVisible(false);

        buttonRegister = new Button(msgSrc.getMessage("lview.regbutton",null,getLocale()));

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
            frmLogin.setError(true);

        }
    }

    private void handelclickEventRegister(ClickEvent<Button> buttonClickEvent) {
        getUI().ifPresent(ui -> ui.navigate("register"));
    }
}
