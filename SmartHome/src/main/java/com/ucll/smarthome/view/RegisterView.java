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
import com.vaadin.flow.component.html.H5;
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
        hTitel = new H1("Registreer");
        txtErrorMessage = new H5();
        txtErrorMessage.setVisible(false);
        vrl.add(hTitel,txtErrorMessage,createRegisterForm());
        setHorizontalComponentAlignment(Alignment.CENTER,vrl);
        add(vrl);
    }


    private Component createRegisterForm(){
        registerForm = new FormLayout();


        txtFirstname = new TextField("First name");
        txtFirstname.setRequired(true);
        txtFirstname.setErrorMessage("Verplicht veld");

        txtLasname = new TextField("Last name");
        txtLasname.setRequired(true);
        txtLasname.setErrorMessage("Verplicht veld");

        txtUsername = new TextField("Username");
        txtUsername.setRequired(true);
        txtUsername.setErrorMessage("Verplicht veld");
        emailField = new EmailField("Email");
        emailField.setRequiredIndicatorVisible(true);
        emailField.setPattern("[^@\\s]+@[^@\\s]+\\.[^@\\s]+");
        emailField.setErrorMessage("Foutieve email");
        password = new PasswordField("Password");
        password.setRequired(true);
        password.setErrorMessage("Verplicht veld");
        confirmPassword = new PasswordField("Confirm password");
        confirmPassword.setRequired(true);
        confirmPassword.setErrorMessage("Verplicht veld");
        buttonCreate = new Button("Create");
        buttonCancel = new Button("Cancel");
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
            if (!password.getValue().equals(confirmPassword.getValue())) throw new IllegalArgumentException("Wachtwoorden zijn niet het zelfde");
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


