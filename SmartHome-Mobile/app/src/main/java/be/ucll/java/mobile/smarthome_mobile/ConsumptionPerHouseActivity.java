package be.ucll.java.mobile.smarthome_mobile;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;

import com.anychart.APIlib;
import com.anychart.AnyChart;
import com.anychart.AnyChartView;
import com.anychart.chart.common.dataentry.DataEntry;
import com.anychart.chart.common.dataentry.ValueDataEntry;
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
import be.ucll.java.mobile.smarthome_mobile.api.house.HouseApiInterface;
import be.ucll.java.mobile.smarthome_mobile.exception.DataNotFoundException;
import be.ucll.java.mobile.smarthome_mobile.pojo.ConsumptionLog;
import be.ucll.java.mobile.smarthome_mobile.pojo.House;
import be.ucll.java.mobile.smarthome_mobile.util.AuthorizationManager;
import be.ucll.java.mobile.smarthome_mobile.util.NavigationManager;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ConsumptionPerHouseActivity extends AppCompatActivity {
    private final String TAG = this.getClass().getSimpleName();
    private List<ConsumptionLog> consumptionLogs;
    private List<House> houses;
    private Spinner dropdownListHouses;
    private ArrayAdapter spinnerAdapterHouses;
    private int selectedHouse;
    AnyChartView anyChartView;
    Cartesian cartLine;

    public void getConsumptionLogs(int houseid){

        try {
            Gson gson = new GsonBuilder()
                    .setLenient()
                    .create();

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(Connection.getUrl())
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build();

            ConsumptionApiInterface consumptionApiInterface = retrofit.create(ConsumptionApiInterface.class);

            Call<List<ConsumptionLog>> call = consumptionApiInterface.getConsumptionlogsByHouse(houseid, AuthorizationManager.getInstance(this).getSessionId());

            call.enqueue(new Callback<List<ConsumptionLog>>() {
                @Override
                public void onResponse(Call<List<ConsumptionLog>> call, Response<List<ConsumptionLog>> response) {
                    if (response != null && response.body() != null) {
                        if (response.isSuccessful()) {
                            consumptionLogs = response.body();
                            populateChart(consumptionLogs);
                        }else {
                            Log.e(TAG, getString(R.string.responseErrorCode) + response.code());
                        }
                    }else{
                        Log.e(TAG, getString(R.string.responseErrorCode) + response.code());

                    }
                }
                @Override
                public void onFailure(Call<List<ConsumptionLog>> call, Throwable t) {

                }
            });

        }catch (Exception e){
            throw new DataNotFoundException(e.getMessage());
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consumption_per_house);
        NavigationManager.initialise(this);

        anyChartView = findViewById(R.id.chartConsByHouse);
        dropdownListHouses = findViewById(R.id.spinConsumptionHouses);
        houses = new ArrayList<>();

        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Connection.getUrl())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        HouseApiInterface houseApi = retrofit.create(HouseApiInterface.class);

        Call<List<House>> call = houseApi.getHousesWithAccessForUserInSession(AuthorizationManager.getInstance(this).getSessionId());
        call.enqueue(new Callback<List<House>>() {
            @Override
            public void onResponse(Call<List<House>> call, Response<List<House>> response) {
                houses = response.body();
                spinnerAdapterHouses = new ArrayAdapter(getApplicationContext(), R.layout.spinner_item, R.id.txtSpinnerItem, houses);
                spinnerAdapterHouses.setDropDownViewResource(R.layout.spinner_item);
                dropdownListHouses.setAdapter(spinnerAdapterHouses);
            }
            @Override
            public void onFailure(Call<List<House>> call, Throwable t) {
                Log.e(TAG,t.getMessage());
            }
        });

        dropdownListHouses.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                House house = (House) parent.getSelectedItem();
                selectedHouse = house.getId();
                getConsumptionLogs(selectedHouse);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    public void populateChart(List<ConsumptionLog> cLogs){

        cartLine = AnyChart.line();

        cartLine.animation(true);
        cartLine.padding(10d, 20d, 5d, 20d);

        cartLine.crosshair().enabled(true);
        cartLine.crosshair()
                .yLabel(true)
                // TODO ystroke
                .yStroke((Stroke) null, null, null, (String) null, (String) null);

        cartLine.tooltip().positionMode(TooltipPositionMode.POINT);

        cartLine.yAxis(0).title("kW");
        cartLine.xAxis(0).labels().padding(5d, 5d, 5d, 5d);

        List<Integer> consDone = new ArrayList<>();
        List<String> datesCheck = new ArrayList<>();

        // put's all the dates we are going through in a list
        for (ConsumptionLog c: cLogs) {
            if(!datesCheck.contains(c.getDate())){
                datesCheck.add(c.getDate());
            }
        }

        List<DataEntry> seriesData = new ArrayList<>();

        for (ConsumptionLog cl : cLogs){
            List<String> datesDone = new ArrayList<>();

            //check if the consumptionlog is already done for this house (housid)
            if(!consDone.contains(cl.getRoomId())){
                for(ConsumptionLog cl1 : cLogs){
                    if(cl.getRoomId().equals(cl1.getRoomId()))  {
                        seriesData.add(new ValueDataEntry(cl1.getDate(),cl1.getTotalConsumption()));
                        // if succesful add dates to a list of done dates
                        datesDone.add(cl1.getDate());

                        Log.e(TAG, "populateChart: ");
                    }
                }

                // check if the done dates contains all dates, if it doesn't contain the date we add a empty data for
                // the specific date
                for (String d : datesCheck) {
                    if(!datesDone.contains(d)){
                        seriesData.add(new ValueDataEntry(d,null));
                    }
                }

                APIlib.getInstance().setActiveAnyChartView(anyChartView);
                Set set = Set.instantiate();
                set.data(seriesData);
                Mapping seriesMapping = set.mapAs("{ x: 'x', value: 'value' }");

                Line series1 = cartLine.line(seriesMapping);
                series1.name(cl.getRoomName());
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
                consDone.add(cl.getRoomId());
            }
        }

        cartLine.legend().enabled(true);
        cartLine.legend().fontSize(13d);
        cartLine.legend().padding(0d, 0d, 10d, 0d);
        anyChartView.setChart(cartLine);
    }
}
