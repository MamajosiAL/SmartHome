package com.ucll.smarthome.view;

import com.ucll.smarthome.controller.ConsumptionController;
import com.ucll.smarthome.controller.HouseController;
import com.ucll.smarthome.dto.HouseDTO;
import com.ucll.smarthome.functions.BeanUtil;
import com.vaadin.flow.component.AbstractField;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.charts.Chart;
import com.vaadin.flow.component.charts.model.*;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.router.Route;


@Route(value = "consumption",layout = MainView.class)
@JsModule("@vaadin/vaadin-charts/theme/vaadin-chart-default-theme")
public class ConsumtionView extends VerticalLayout  {

    private HouseController houseController;
    private ConsumptionController consumptionController;
    private HorizontalLayout horizontalLayout;
    private Select<HouseDTO> houseDTOSelect;
    private Chart chart;
    private DataSeries ds;

    public ConsumtionView() {
        houseController = BeanUtil.getBean(HouseController.class);
        consumptionController = BeanUtil.getBean(ConsumptionController.class);

        horizontalLayout = new HorizontalLayout();
        houseDTOSelect = new Select<>();
        houseDTOSelect.setItems(houseController.getHousesByUser());
        houseDTOSelect.setItemLabelGenerator(HouseDTO::getName);
        houseDTOSelect.addValueChangeListener(e -> handleChangeSelect(e));
        horizontalLayout.add(houseDTOSelect);
        horizontalLayout.setWidth("40%");
        horizontalLayout.setAlignItems(Alignment.CENTER);



        add(horizontalLayout,createGraph());

        setHeightFull();
        setAlignItems(Alignment.CENTER);


    }
    private Component createGraph(){
        chart = new Chart(ChartType.LINE);
        Configuration conf = chart.getConfiguration();
        conf.setTitle("Verbruik in kWh");

        YAxis yAxis = conf.getyAxis();
        yAxis.setTitle("kWh");
        conf.getxAxis().setCategories("Jan", "Feb", "Mar", "Apr",
                "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec");

        PlotOptionsLine plotOptions = new PlotOptionsLine();
        plotOptions.setEnableMouseTracking(false);
        conf.setPlotOptions(plotOptions);

        DataSeries ds = new DataSeries();
        ds.setName("Test");
        ds.setData(7.0);

        conf.addSeries(ds);
        Legend legend = conf.getLegend();
        legend.setLayout(LayoutDirection.VERTICAL);
        legend.setVerticalAlign(VerticalAlign.MIDDLE);
        legend.setAlign(HorizontalAlign.RIGHT);

        conf.setSeries(loadData());
        return chart;
    }

    private void handleChangeSelect(AbstractField.ComponentValueChangeEvent<Select<HouseDTO>, HouseDTO> e) {
    }

    public DataSeries loadData() {
        ds= new DataSeries();

        return ds;
    }

}
