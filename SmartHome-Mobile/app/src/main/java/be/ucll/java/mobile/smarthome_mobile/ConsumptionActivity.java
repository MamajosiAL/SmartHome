package be.ucll.java.mobile.smarthome_mobile;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.anychart.AnyChart;
import com.anychart.AnyChartView;
import com.anychart.chart.common.dataentry.DataEntry;
import com.anychart.chart.common.dataentry.ValueDataEntry;
import com.anychart.chart.common.listener.Event;
import com.anychart.chart.common.listener.ListenersInterface;
import com.anychart.charts.Cartesian;
import com.anychart.core.cartesian.series.Line;
import com.anychart.data.Mapping;
import com.anychart.data.Set;
import com.anychart.enums.Anchor;
import com.anychart.enums.MarkerType;
import com.anychart.enums.TooltipPositionMode;
import com.anychart.graphics.vector.Stroke;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.List;

import be.ucll.java.mobile.smarthome_mobile.api.Connection;
import be.ucll.java.mobile.smarthome_mobile.api.consumption.ConsumptionApiInterface;
import be.ucll.java.mobile.smarthome_mobile.api.device.DeviceApiInterface;
import be.ucll.java.mobile.smarthome_mobile.exception.DataNotFoundException;
import be.ucll.java.mobile.smarthome_mobile.pojo.ConsumptionLog;
import be.ucll.java.mobile.smarthome_mobile.pojo.DeviceAllParams;
import be.ucll.java.mobile.smarthome_mobile.util.AuthorizationManager;
import be.ucll.java.mobile.smarthome_mobile.util.DeviceCategory;
import be.ucll.java.mobile.smarthome_mobile.util.NavigationManager;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ConsumptionActivity extends AppCompatActivity implements Callback<List<ConsumptionLog>> {
    private final String TAG = this.getClass().getSimpleName();
    private ProgressDialog progressDialog;
    List<ConsumptionLog> consumptionLogs;
    AnyChartView anyChartView;
    Button btnGoToConsPerHouse;

    public void getConsumptionLogs(){
        progressDialog = new ProgressDialog(ConsumptionActivity.this);
        progressDialog.create();

        try {
            Gson gson = new GsonBuilder()
                    .setLenient()
                    .create();

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(Connection.getUrl())
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build();

            ConsumptionApiInterface consumptionApiInterface = retrofit.create(ConsumptionApiInterface.class);

            Call<List<ConsumptionLog>> call = consumptionApiInterface.getConsumptionlogsByuser(AuthorizationManager.getInstance(this).getSessionId());

            call.enqueue(this);
        }catch (Exception e){
            throw new DataNotFoundException(e.getMessage());
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consumption);

        NavigationManager.initialise(this);
        anyChartView = findViewById(R.id.chartConsByUser);
        btnGoToConsPerHouse = findViewById(R.id.btnGetConsumptionByHouse);

        getConsumptionLogs();

        btnGoToConsPerHouse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(),ConsumptionPerHouseActivity.class);
                startActivity(intent);
            }
        });
    }

    public void populateChart(List<ConsumptionLog> cLogs){

        Cartesian cartLine = AnyChart.line();

        cartLine.animation(true);
        cartLine.padding(10d, 20d, 5d, 20d);

        cartLine.crosshair().enabled(true);
        cartLine.crosshair()
                .yLabel(true)
                // TODO ystroke
                .yStroke((Stroke) null, null, null, (String) null, (String) null);

        cartLine.tooltip().positionMode(TooltipPositionMode.POINT);

        cartLine.title(getResources().getString(R.string.byHouse));

        cartLine.yAxis(0).title("kW");
        cartLine.xAxis(0).labels().padding(5d, 5d, 5d, 5d);

        List<DataEntry> seriesData = new ArrayList<>();
        List<Integer> consDone = new ArrayList<>();
        List<String> datesCheck = new ArrayList<>();

        for (ConsumptionLog c: cLogs) {
            if(!datesCheck.contains(c.getDate())){
                datesCheck.add(c.getDate());
            }
        }

        Log.e(TAG, "populateChart: " + cLogs.size() );
        for (ConsumptionLog cl : cLogs){
            List<String> datesDone = new ArrayList<>();
            if(!consDone.contains(cl.getHouseId())){
            for(ConsumptionLog cl1 : cLogs){
                if(cl.getHouseId().equals(cl1.getHouseId()))   {
                    int i = 1;
                    seriesData.add(new ValueDataEntry(cl1.getDate(),cl1.getTotalConsumption()));
                    datesDone.add(cl1.getDate());
                }
            }
                for (String d : datesCheck) {
                    if(!datesDone.contains(d)){
                        seriesData.add(new ValueDataEntry(d,null));
                    }
                }

                for (DataEntry d: seriesData) {
                    Log.e(TAG, "populateChart: this is the value in dataentry: " + d.getValue("value") );
                    System.out.println();
                }

                Set set = Set.instantiate();
                set.data(seriesData);
                Mapping seriesMapping = set.mapAs("{ x: 'x', value: 'value' }");

                Line series1 = cartLine.line(seriesMapping);
                series1.name(cl.getHouseName());
                series1.hovered().markers().enabled(true);
                series1.hovered().markers()
                        .type(MarkerType.CIRCLE)
                        .size(4d);
                series1.tooltip()
                        .position("right")
                        .anchor(Anchor.LEFT_CENTER)
                        .offsetX(5d)
                        .offsetY(5d);

                // if cl is done put the house id in consDone list
                consDone.add(cl.getHouseId());
            }
        }

        cartLine.legend().enabled(true);
        cartLine.legend().fontSize(13d);
        cartLine.legend().padding(0d, 0d, 10d, 0d);

        anyChartView.setChart(cartLine);
    }

    @Override
    public void onResponse(Call<List<ConsumptionLog>> call, Response<List<ConsumptionLog>> response) {
        if (response != null && response.body() != null) {
            if (response.isSuccessful()) {
                consumptionLogs = response.body();
                populateChart(consumptionLogs);
            }else {
                Log.e(TAG, getString(R.string.responseErrorCode) + response.code());
                progressDialog.dismiss();
            }
        }else{
            Log.e(TAG, getString(R.string.responseErrorCode) + response.code());
            progressDialog.dismiss();
        }
    }

    @Override
    public void onFailure(Call<List<ConsumptionLog>> call, Throwable t) {
        Toast.makeText(this, t.toString(), Toast.LENGTH_LONG).show();
        Log.e(TAG,t.getMessage());
        progressDialog.dismiss();
    }
}