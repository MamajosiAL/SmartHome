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
        buttonCancel.addClickListener(buttonClickEvent -> handelclickEventCancel(buttonClickEvent));
        buttonCreate.addClickListener(buttonClickEvent -> handelclickEventCreate(buttonClickEvent));
        formLayout.add(
                firstName, lastName,
                username,
                email,
                password, confirmPassword,
                buttonCancel, buttonCreate
        );
        formLayout.setResponsiveSteps(
                // Use one column by default
                new FormLayout.ResponsiveStep("0", 1),
                // Use two columns, if layout's width exceeds 500px
                new FormLayout.ResponsiveStep("500px", 2)
        );
// Stretch the username field over 2 columns
        formLayout.setColspan(username, 2);
    }

    private void handelclickEventCreate(ClickEvent<Button> buttonClickEvent) {
        getUI().ifPresent(ui -> ui.navigate(""));
    }

    private void handelclickEventCancel(ClickEvent<Button> buttonClickEvent) {
        getUI().ifPresent(ui -> ui.navigate(""));
    }
}


