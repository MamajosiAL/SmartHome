package com.ucll.smarthome.view.dialogs;


import com.ucll.smarthome.functions.BeanUtil;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.FlexLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;

public class WarningDialog extends Dialog {

    private Label lblTitle;
    @Autowired
    private MessageSource msgSrc;

    private HorizontalLayout hlButtons;
    private VerticalLayout vrlLayout;
    private Button btnCancel;
    private Button btnOk;

    private boolean okClicked;

    public WarningDialog(String description) {
        vrlLayout = new VerticalLayout();
        vrlLayout.setSizeFull();
        vrlLayout.setPadding(false);
        msgSrc = BeanUtil.getBean(MessageSource.class);

        hlButtons = new HorizontalLayout();
        lblTitle = new Label(description);
        vrlLayout.add(lblTitle);

        btnCancel = new Button(msgSrc.getMessage("rview.buttonCa",null,getLocale()));
        btnCancel.addClickListener(event -> {
            okClicked = false;
            this.close();
        });
        btnOk = new Button("Ok");
        btnOk.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        btnOk.addClickListener(event ->{
            okClicked = true;
            this.close();
        });

        hlButtons.add(btnOk,btnCancel);
        hlButtons.setJustifyContentMode(FlexComponent.JustifyContentMode.END);
        hlButtons.setWidthFull();

        FlexLayout wrapper = new FlexLayout();
        wrapper.setWidthFull();
        wrapper.setAlignItems(FlexComponent.Alignment.END);
        wrapper.add(hlButtons);
        vrlLayout.add(wrapper);
        vrlLayout.expand(wrapper);

        this.add(vrlLayout);
    }

    public boolean wasOkButtonClicked(){return okClicked;}
}
