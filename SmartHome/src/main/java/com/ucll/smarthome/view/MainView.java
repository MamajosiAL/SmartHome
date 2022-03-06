package com.ucll.smarthome.view;

import com.ucll.smarthome.controller.UserController;
import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.Html;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Header;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinServletRequest;
import liquibase.pro.packaged.I;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;

import javax.annotation.PostConstruct;

@Route("")
@PageTitle("Home")
@CssImport("styles/main-view.css")
public class MainView extends AppLayout  implements BeforeEnterObserver{
    private HuisView hview;
    private ConsumptieView cView;
    private BeheerView bView;

    private UserController userController;
    private Button logoutButton;
    private Tab tab1;
    private static final String tabname1 = "huizen";
    private Tab tab2;
    private static final String tabname2 = "consumptie";
    private Tab tab3;
    private static final String tabname3 = "beheer";
    private Tabs tabs;
    public MainView() {
        //misschien nog een + met de naam van de user
        H3 header = new H3("SmartHome van" );
        header.setId("header-layout");

        //hier nog misschien een image
        Image img = new Image("images/imagesmarthome.jpg","smarthome logo");
        img.setHeight("30px");
        img.setId("aligneer-rechts");

        logoutButton = new Button("log out");
        logoutButton.addClickListener(buttonClickEvent -> handleclickEvent(buttonClickEvent));
        logoutButton.setHeight("30px");
        logoutButton.setId("aligneer-rechts");

        addToNavbar(new DrawerToggle(),
                new Html("<span>&nbsp;&nbsp;</span>"),
                header,
                new Html("<span>&nbsp;&nbsp;</span>"),
                new Icon(VaadinIcon.HOME),/*img,*/ logoutButton);
        tab1 = new Tab(tabname1);
        tab2 = new Tab(tabname2);
        //hier misschien nog dat ge tab 3 alleen ziet als admin?
        tab3 = new Tab(tabname3);
        tabs = new Tabs(tab1,tab2,tab3);
        tabs.setOrientation(Tabs.Orientation.VERTICAL);
        tabs.addSelectedChangeListener(event ->{
            handleTabClicked(event);
        });
        addToDrawer(tabs);
    }

    private void handleTabClicked(Tabs.SelectedChangeEvent event) {
        Tab selTab = tabs.getSelectedTab();
        if(selTab.getLabel() != null){
            if(selTab.getLabel().equals(tabname1)){
                setContent(hview);
            }else if(selTab.getLabel().equals(tabname2)){
                setContent(cView);
            }else{
                setContent(bView);
            }
        }
    }


    @PostConstruct
    private void setMainViewContent() {
        hview = new HuisView();
        hview.loadData();

        cView = new ConsumptieView();
        cView.loadData();

        bView = new BeheerView();
        cView.loadData();

        this.setContent(hview);
    }

    private void handleclickEvent(ClickEvent<Button> buttonClickEvent) {
        getUI().ifPresent(ui -> ui.navigate("login"));

        SecurityContextLogoutHandler logoutHandler = new SecurityContextLogoutHandler();
        logoutHandler.logout(VaadinServletRequest.getCurrent().getHttpServletRequest(), null, null);
    }


    @Override
    public void beforeEnter(BeforeEnterEvent beforeEnterEvent){
        //if(){
           // BeforeEnterEvent.rerouteTo("login");
        //}
    }



}
