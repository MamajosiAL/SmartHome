package com.ucll.smarthome.view;

import com.ucll.smarthome.controller.ConsumptionController;
import com.ucll.smarthome.controller.ConsumptionLogController;
import com.ucll.smarthome.controller.HouseController;
import com.ucll.smarthome.dto.ConsumptionLogDTO;
import com.ucll.smarthome.dto.HouseDTO;
import com.ucll.smarthome.functions.BeanUtil;
import com.vaadin.flow.component.AbstractField;
import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.charts.Chart;
import com.vaadin.flow.component.charts.model.*;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.router.Route;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;


@Route(value = "consumption",layout = MainView.class)
@JsModule("@vaadin/vaadin-charts/theme/vaadin-chart-default-theme")
public class ConsumtionView extends VerticalLayout  {

    private HouseController houseController;
    private ConsumptionLogController consumptionLogController;
    private HorizontalLayout horizontalLayout;

    private Chart chart;
    private Button btnClear;
    private Configuration conf;
    private Legend legend;

    public ConsumtionView() {
        houseController = BeanUtil.getBean(HouseController.class);
        consumptionLogController = BeanUtil.getBean(ConsumptionLogController.class);

        add(createGraph());

        setHeightFull();
        setAlignItems(Alignment.CENTER);


    }


    private Component createGraph(){
        chart = new Chart(ChartType.LINE);
        conf = chart.getConfiguration();
        conf.setTitle("Verbruik in kW");


        conf.getxAxis().setType(AxisType.DATETIME);
        conf.getxAxis().setDateTimeLabelFormats(
                new DateTimeLabelFormats("%e. %b", "%b"));
        YAxis yAxis = conf.getyAxis();
        yAxis.setTitle(new AxisTitle("Verbruik(kW)"));
        yAxis.setMin(0);

        conf
                .getTooltip()
                .setFormatter(
                        "'<b>'+ this.series.name +'</b><br/>\'+ Highcharts.dateFormat('%e. %b', this.x) +': '+ this.y +' m'");


        for (HouseDTO houseDTO : houseController.getHousesByUser()){
            DataSeries series = new DataSeries();
            series.setPlotOptions(new PlotOptionsLine());
            series.setName(houseDTO.getName());
            List<ConsumptionLogDTO> lst = consumptionLogController.getConsumptionLogByUser();
            for (ConsumptionLogDTO cdto : lst){
                if (cdto.getHouseId() == houseDTO.getId()){
                    DataSeriesItem item = new DataSeriesItem((d(cdto.getDate().toString())), cdto.getTotalConsumption());
                    series.add(item);
                }
            }
            conf.addSeries(series);
        }

        PlotOptionsLine plotOptions = new PlotOptionsLine();
        plotOptions.setEnableMouseTracking(false);
        conf.setPlotOptions(plotOptions);


        legend = conf.getLegend();
        legend.setLayout(LayoutDirection.VERTICAL);
        legend.setVerticalAlign(VerticalAlign.MIDDLE);
        legend.setAlign(HorizontalAlign.RIGHT);

        chart.drawChart(true);
        chart.setHeight("40%");
        return chart;
    }



    private final DateFormat df = new SimpleDateFormat("yyyy-MM-dd");

    private Date d(String dateString) {
        df.setTimeZone(TimeZone.getTimeZone("EET"));
        try {
            return df.parse(dateString);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

}
