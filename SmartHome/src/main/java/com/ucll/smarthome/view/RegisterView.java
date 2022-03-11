package com.ucll.smarthome.view;

import com.ucll.smarthome.controller.UserController;
import com.ucll.smarthome.dto.UserDTO;
import com.ucll.smarthome.functions.BeanUtil;
import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.H5;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.*;
import com.vaadin.flow.server.VaadinSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;

import java.util.Locale;


@Route("register")
@PageTitle("register")
@CssImport("styles/main-view.css")
public class RegisterView extends VerticalLayout {

    @Autowired
    private final UserController userController;

    @Autowired
    private MessageSource msgSrc;
    private Locale loc;

    private VerticalLayout vrl;
    private FormLayout registerForm;
    private H1 hTitel;
    private H5 txtErrorMessage;
    private TextField txtFirstname;
    private TextField txtLasname;
    private TextField txtUsername;
    private EmailField emailField;
    private PasswordField password;
    private PasswordField confirmPassword;
    private Button buttonCreate;

    private Button buttonCancel;


    public RegisterView() {
        msgSrc = BeanUtil.getBean(MessageSource.class);
        loc = VaadinSession.getCurrent().getLocale();

        userController = BeanUtil.getBean(UserController.class);
        vrl = new VerticalLayout();
        addClassName("Register-view");

        vrl.setMaxWidth("40em");
        hTitel = new H1(msgSrc.getMessage("rview.titel",null,getLocale()));
        txtErrorMessage = new H5();
        txtErrorMessage.setVisible(false);
        vrl.add(hTitel,txtErrorMessage,createRegisterForm());
        setHorizontalComponentAlignment(Alignment.CENTER,vrl);
        add(vrl);

    }


    private Component createRegisterForm(){
        registerForm = new FormLayout();


        txtFirstname = new TextField(msgSrc.getMessage("rview.firstname",null,getLocale()));
        txtFirstname.setRequired(true);
        txtFirstname.setErrorMessage(msgSrc.getMessage("rview.errormessage",null,getLocale()));

        txtLasname = new TextField(msgSrc.getMessage("rview.lastname",null,getLocale()));
        txtLasname.setRequired(true);
        txtLasname.setErrorMessage(msgSrc.getMessage("rview.errormessage",null,getLocale()));

        txtUsername = new TextField(msgSrc.getMessage("lview.username",null,getLocale()));
        txtUsername.setRequired(true);
        txtUsername.setErrorMessage(msgSrc.getMessage("rview.errormessage",null,getLocale()));
        emailField = new EmailField("Email");
        emailField.setRequiredIndicatorVisible(true);
        emailField.setPattern("[^@\\s]+@[^@\\s]+\\.[^@\\s]+");
        emailField.setErrorMessage(msgSrc.getMessage("rview.errormessage",null,getLocale()),"Foutieve email");
        password = new PasswordField(msgSrc.getMessage("lview.password",null,getLocale()));
        password.setRequired(true);
        password.setErrorMessage(msgSrc.getMessage("rview.errormessage",null,getLocale()));
        confirmPassword = new PasswordField(msgSrc.getMessage("rview.confirmpassword",null,getLocale()));
        confirmPassword.setRequired(true);
        confirmPassword.setErrorMessage(msgSrc.getMessage("rview.errormessage",null,getLocale()));
        buttonCreate = new Button(msgSrc.getMessage("rview.buttonCr",null,getLocale()));
        buttonCancel = new Button(msgSrc.getMessage("rview.buttonCa", null,getLocale()));
        buttonCancel.addClickListener(this::handelclickEventCancel);
        buttonCreate.addClickListener(this::handelclickEventCreate);

        registerForm.add(
             txtFirstname, txtLasname, txtUsername, emailField,password,confirmPassword,buttonCancel,buttonCreate
        );
        registerForm.setResponsiveSteps(new FormLayout.ResponsiveStep("0",2));

        return registerForm;

    }

    private void handelclickEventCreate(ClickEvent<Button> buttonClickEvent) {

        try{
            if (!password.getValue().equals(confirmPassword.getValue())) throw new IllegalArgumentException(msgSrc.getMessage("rview.exception",null,getLocale()));
                UserDTO userDTO = new UserDTO.Builder().username(txtUsername.getValue()).firstname(txtFirstname.getValue()).name(txtLasname.getValue())
                    .email(emailField.getValue()).password(password.getValue()).build();

            userController.createUser(userDTO);
            getUI().ifPresent(ui -> ui.navigate("login"));
        }catch (IllegalArgumentException ex){
            txtErrorMessage.setVisible(true);
            txtErrorMessage.setText(ex.getMessage());

        }

    }

    private void handelclickEventCancel(ClickEvent<Button> buttonClickEvent) {
        getUI().ifPresent(ui -> ui.navigate("login"));
    }
}


