package com.ucll.smarthome.view;

import com.ucll.smarthome.functions.BeanUtil;
import com.ucll.smarthome.functions.UserSecurityFunc;
import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.Html;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.dom.DomEvent;
import com.vaadin.flow.router.*;
import com.vaadin.flow.server.StreamResource;
import com.vaadin.flow.server.VaadinServletRequest;
import com.vaadin.flow.server.VaadinSession;
import com.vaadin.flow.theme.Theme;
import com.vaadin.flow.theme.lumo.Lumo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;

import javax.annotation.PostConstruct;
import java.util.Locale;

@Route("")
@PageTitle("Home")
@Theme(value = Lumo.class,variant = Lumo.LIGHT)
@CssImport(value = "./styles/main-view.css",themeFor = "vaadin-app-layout")
public class MainView extends AppLayout  implements BeforeEnterObserver{
    @Autowired
    private MessageSource msgSrc;
    private Locale loc;
    private final UserSecurityFunc sec;

    private HouseView hview;
    private PersenalInformationView pView;
    private ConsumtionView bView;

    private Button logoutButton;
    private Tab tab1;
    private static final String tabname1 = "Huizen";
    private Tab tab2;
    private static final String tabname2 = "Acount";
    private Tab tab3;
    private static final String tabname3 = "Verbruik";
    private Tabs tabs;

    public MainView() {
        sec = BeanUtil.getBean(UserSecurityFunc.class);
        msgSrc = BeanUtil.getBean(MessageSource.class);
        loc = VaadinSession.getCurrent().getLocale();

        //misschien nog een + met de naam van de user
        H3 header = new H3("SmartHome" );
        header.setId("header-layout");

        //hier nog misschien een image
        StreamResource imageResource = new StreamResource("SmartHome-logo.svg",
                () -> getClass().getResourceAsStream("/img/SmartHome-logo.svg"));
       ;

        logoutButton = new Button(msgSrc.getMessage("mview.logout",null,getLocale()));
        logoutButton.addClickListener(buttonClickEvent -> handleclickEvent(buttonClickEvent));
        logoutButton.setHeight("30px");
        logoutButton.setId("aligneer-rechts");
        Image image = new Image("frontend/styles/SmartHome-logo.svg" , "Image not found");

        addToNavbar(new DrawerToggle(),
                new Html("<span>&nbsp;&nbsp;</span>"),
                header,
                new Html("<span>&nbsp;&nbsp;</span>")
               ,/*img,*/ logoutButton);
        tab1 = new Tab(tabname1);
        tab2 = new Tab(tabname2);
        //hier misschien nog dat ge tab 3 alleen ziet als admin?
        tab3 = new Tab(tabname3);
        tabs = new Tabs(tab1,tab2,tab3);
        tab1.getElement().addEventListener("click", event ->{
            handleclickHouses(event);
        });
        tab2.getElement().addEventListener("click", event ->{
            handleClickAcoutn(event);
        });
        tab3.getElement().addEventListener("click", event ->{
            handleClickConsuption(event);
        });
        tabs.setOrientation(Tabs.Orientation.VERTICAL);
        addToDrawer(tabs);
    }

    private void handleClickConsuption(DomEvent event) {
        setContent(bView);
        getUI().ifPresent(ui -> ui.navigate("consumption"));
    }

    private void handleClickAcoutn(DomEvent event) {
        setContent(pView);
        getUI().ifPresent(ui -> ui.navigate("me"));
    }

    private void handleclickHouses(DomEvent event) {

        setContent(hview);
        getUI().ifPresent(ui -> ui.navigate("houses"));
    }



    @PostConstruct
    private void setMainViewContent() {
        hview = new HouseView();
        hview.loadData();

        pView = new PersenalInformationView();

        bView = new ConsumtionView();

        this.setContent(hview);
    }

    private void handleclickEvent(ClickEvent<Button> buttonClickEvent) {
        getUI().ifPresent(ui -> ui.navigate("login"));

        SecurityContextLogoutHandler logoutHandler = new SecurityContextLogoutHandler();
        logoutHandler.logout(VaadinServletRequest.getCurrent().getHttpServletRequest(), null, null);
    }


    @Override
    public void beforeEnter(BeforeEnterEvent beforeEnterEvent){
         hview.loadData();
        //if(){
           // BeforeEnterEvent.rerouteTo("login");
        //}
    }



}
