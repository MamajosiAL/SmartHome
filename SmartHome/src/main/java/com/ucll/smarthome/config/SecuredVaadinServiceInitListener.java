package com.ucll.smarthome.config;

import com.ucll.smarthome.view.LoginView;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.server.ServiceInitEvent;
import com.vaadin.flow.server.VaadinServiceInitListener;
import com.vaadin.flow.router.BeforeEnterEvent;
import org.springframework.stereotype.Component;

@Component
public class SecuredVaadinServiceInitListener implements VaadinServiceInitListener {
    @Override
    public void serviceInit(ServiceInitEvent serviceInitEvent) {
        serviceInitEvent.getSource().addUIInitListener(uiInitEvent -> {
            final UI ui = uiInitEvent.getUI();
            ui.addBeforeEnterListener(this::authenticateNavigation);
        });

    }
    private void authenticateNavigation(BeforeEnterEvent event) {
        if (!LoginView.class.equals(event.getNavigationTarget()) && !SecurityUtils.isUserLoggedIn()) {
            event.rerouteTo(LoginView.class);
        }
    }
}
