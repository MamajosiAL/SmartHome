package com.ucll.smarthome.view;

import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.BeforeEvent;
import com.vaadin.flow.router.HasUrlParameter;
import com.vaadin.flow.router.Route;


@Route(value = "rooms", layout = MainView.class)
public class RoomView extends VerticalLayout implements HasUrlParameter<Long> {


    private long houseid;
    public RoomView() {
    }
    @Override
    public void setParameter(BeforeEvent beforeEvent, Long id) {
        try {
            houseid = id;

        } catch (IllegalArgumentException e) {
            Notification.show(e.getMessage() ,3000, Notification.Position.TOP_CENTER);
        }
    }
}
