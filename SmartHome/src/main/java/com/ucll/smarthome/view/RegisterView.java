package com.ucll.smarthome.view;

import com.ucll.smarthome.dto.UserDTO;
import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

@Route("/register")
@PageTitle("register")
@CssImport("styles/main-view.css")
public class RegisterView extends VerticalLayout {
    private TextField firstName = new TextField("First name");
    private TextField lastName = new TextField("Last name");
    private TextField username = new TextField("Username");
    private TextField email = new TextField("Email");
    private PasswordField password = new PasswordField("Password");
    private PasswordField confirmPassword = new PasswordField("Confirm password");
    private Button buttonCreate = new Button("Create");

    private Button buttonCancel = new Button("Cancel");


    FormLayout formLayout = new FormLayout();
    public RegisterView() {
        addClassName("Register-view");
        setSizeFull();
        setAlignItems(Alignment.CENTER);
        setJustifyContentMode(JustifyContentMode.CENTER);
        buttonCancel.addClickListener(buttonClickEvent -> handelclickEventCancel(buttonClickEvent));
        buttonCreate.addClickListener(buttonClickEvent -> handelclickEventCreate(buttonClickEvent));
        formLayout.add(
                firstName, lastName,
                username,
                email,
                password, confirmPassword,
                buttonCancel, buttonCreate
        );


        add(formLayout);
    }

    private void handelclickEventCreate(ClickEvent<Button> buttonClickEvent) {
        getUI().ifPresent(ui -> ui.navigate("login"));
    }

    private void handelclickEventCancel(ClickEvent<Button> buttonClickEvent) {
        getUI().ifPresent(ui -> ui.navigate("login"));
    }
}


