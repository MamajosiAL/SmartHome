package com.ucll.smarthome.view;

import com.ucll.smarthome.controller.UserController;
import com.ucll.smarthome.dto.UserDTO;
import com.ucll.smarthome.functions.BeanUtil;
import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H5;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;

@Route(value = "me" ,layout = MainView.class)
public class PersenalInformationView extends VerticalLayout {

    @Autowired
    private final UserController userController;

    @Autowired
    private MessageSource msgSrc;

    private RegisterView rv;

    private VerticalLayout vrl;
    private FormLayout personalForm;
    private H1 hTitel;
    private H5 txtErrorMessage;
    private TextField txtFirstname;
    private TextField txtLasname;
    private TextField txtUsername;
    private EmailField emailField;
    private Button btnUpdate;
    private Button btnCancel;
    private Button btnSubmit;


    public PersenalInformationView() {
        msgSrc = BeanUtil.getBean(MessageSource.class);
        userController = BeanUtil.getBean(UserController.class);

        vrl = new VerticalLayout();
        vrl.setMaxWidth("40em");

        hTitel = new H1("Persoonlijke informatie");

        txtErrorMessage = new H5();
        txtErrorMessage.setVisible(false);

        vrl.add(hTitel,txtErrorMessage ,createRegisterForm());
        populateHouseForm();
        setHorizontalComponentAlignment(Alignment.CENTER,vrl);
        add(vrl);
    }

    private Component createRegisterForm(){
        personalForm = new FormLayout();

        txtFirstname = new TextField("Voornaam");
        txtFirstname.setRequired(true);
        txtFirstname.setErrorMessage("rview.errormessage");
        txtFirstname.setReadOnly(true);

        txtLasname = new TextField("rview.lastname");
        txtLasname.setRequired(true);
        txtLasname.setErrorMessage("rview.errormessage");
        txtLasname.setReadOnly(true);

        txtUsername = new TextField("lview.username");
        txtUsername.setRequired(true);
        txtUsername.setErrorMessage("Verplicht veld");
        txtUsername.setReadOnly(true);
        emailField = new EmailField("Email");
        emailField.setRequiredIndicatorVisible(true);
        emailField.setPattern("[^@\\s]+@[^@\\s]+\\.[^@\\s]+");
        emailField.setErrorMessage("Foutieve email");
        emailField.setReadOnly(true);

        btnUpdate = new Button(new Icon(VaadinIcon.EDIT));
        btnUpdate.addClickListener(e-> handleClickEdit(e));

        btnCancel = new Button(new Icon(VaadinIcon.CLOSE_SMALL));
        btnCancel.setVisible(false);
        btnCancel.addClickListener(e-> handleCancelClick(e));

        btnSubmit = new Button(new Icon(VaadinIcon.CHECK));
        btnSubmit.setVisible(false);
        btnSubmit.addClickListener(e->handleSubmit(e));


        personalForm.add(
                txtFirstname, txtLasname, txtUsername, emailField,btnCancel,btnSubmit,btnUpdate
        );
        personalForm.setResponsiveSteps(new FormLayout.ResponsiveStep("0",2));

        return personalForm;
    }

    private void handleSubmit(ClickEvent<Button> e) {
        try {
            UserDTO userDTO = new UserDTO.Builder().username(txtUsername.getValue()).firstname(txtFirstname.getValue()).name(txtLasname.getValue())
                    .email(emailField.getValue()).build();
            userController.updateUser(userDTO);
            resetReadOnly();
            populateHouseForm();

        } catch (IllegalArgumentException ex) {
           txtErrorMessage.setVisible(true);
           txtErrorMessage.setText(ex.getMessage());
        }


    }
    private void resetReadOnly(){
        btnCancel.setVisible(false);
        btnUpdate.setVisible(true);
        btnSubmit.setVisible(false);
        txtFirstname.setReadOnly(true);
        txtLasname.setReadOnly(true);
        txtUsername.setReadOnly(true);
        emailField.setReadOnly(true);
        txtErrorMessage.setVisible(false);
    }
    private void handleCancelClick(ClickEvent<Button> e) {
        resetReadOnly();
        populateHouseForm();

    }

    private void handleClickEdit(ClickEvent<Button> e) {
        btnUpdate.setVisible(false);
        btnCancel.setVisible(true);
        btnSubmit.setVisible(true);
        txtFirstname.setReadOnly(false);
        txtLasname.setReadOnly(false);
        txtUsername.setReadOnly(false);
        emailField.setReadOnly(false);
    }

    private void populateHouseForm( ){
        UserDTO userDTO = userController.getUser();

        txtFirstname.setValue(userDTO.getFirstname());
        txtLasname.setValue(userDTO.getName());
        txtUsername.setValue(userDTO.getUsername());
        emailField.setValue(userDTO.getEmail());

    }
}
