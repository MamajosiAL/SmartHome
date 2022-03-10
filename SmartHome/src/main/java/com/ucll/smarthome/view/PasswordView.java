package com.ucll.smarthome.view;

import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

@Route("/new_password")
@PageTitle("New password")
@CssImport("styles/main-view.css")
public class PasswordView extends VerticalLayout {

    private PasswordField password = new PasswordField("New Password");
    private PasswordField confirmPassword = new PasswordField("Confirm password");
    private Button buttonCreate = new Button("Create");
    private Button buttonCancel = new Button("Cancel");

    public PasswordView(){
        buttonCancel.addClickListener(buttonClickEvent -> handelclickEventCancel(buttonClickEvent));
        buttonCreate.addClickListener(buttonClickEvent -> handelclickEventCreate(buttonClickEvent));

        HorizontalLayout buttons = new HorizontalLayout();
        buttons.add(buttonCancel,buttonCreate);
        addClassName("Password-view");
        setSizeFull();;
        setAlignItems(Alignment.CENTER);
        setJustifyContentMode(JustifyContentMode.CENTER);
        add(password,confirmPassword,
                buttons);

    }

    private void handelclickEventCreate(ClickEvent<Button> buttonClickEvent) {
        getUI().ifPresent(ui -> ui.navigate("login"));
    }

    private void handelclickEventCancel(ClickEvent<Button> buttonClickEvent) {
        getUI().ifPresent(ui -> ui.navigate("login"));
    }
}
