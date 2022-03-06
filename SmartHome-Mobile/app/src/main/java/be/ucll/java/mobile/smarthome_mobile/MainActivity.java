package be.ucll.java.mobile.smarthome_mobile;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.util.List;
import be.ucll.java.mobile.smarthome_mobile.api.Connection;
import be.ucll.java.mobile.smarthome_mobile.api.house.HouseApiInterface;
import be.ucll.java.mobile.smarthome_mobile.api.house.HousesAdapter;
import be.ucll.java.mobile.smarthome_mobile.exception.DataNotFoundException;
import be.ucll.java.mobile.smarthome_mobile.pojo.House;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity implements Callback<List<House>> {
    private final String TAG = this.getClass().getSimpleName();
    private RecyclerView recyclerViewHouses;
    private ProgressDialog progressDialog;
    List<House> houses;

    public void getHousesListData() {
        progressDialog = new ProgressDialog(MainActivity.this);
        progressDialog.create();
        try {
            Gson gson = new GsonBuilder()
                    .setLenient()
                    .create();

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(Connection.getUrl())
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build();

            HouseApiInterface houseApi = retrofit.create(HouseApiInterface.class);

            Call<List<House>> call = houseApi.getHousesWithAccessForUserWithId(1);
            call.enqueue(this);
        }catch (Exception e){
            throw new DataNotFoundException(e.getCause());
        }


    }

    private void setDataInRecyclerView() {
        // set a LinearLayoutManager with default vertical orientation
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(MainActivity.this);
        recyclerViewHouses.setLayoutManager(linearLayoutManager);
        // call the constructor of housesAdapter to send the reference and data to Adapter
        HousesAdapter housesAdapter = new HousesAdapter(MainActivity.this, houses);
        recyclerViewHouses.setAdapter(housesAdapter); // set the Adapter to RecyclerView
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        recyclerViewHouses = findViewById(R.id.recyclerViewHouses);
        try {
            getHousesListData();
        }catch (Exception e){
            e.printStackTrace();
        }


        //fabAddHouse for adding a new house
        FloatingActionButton fab = findViewById(R.id.fabAddHouse);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, getText(R.string.notImplementedMessage), Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        initialiseNavigation();
    }

    @Override
    public void onResponse(Call<List<House>> call, Response<List<House>> response) {
        houses = response.body();
        setDataInRecyclerView();
    }

    /**
     * Invoked when a network exception occurred talking to the server or when an unexpected
     * exception occurred creating the request or processing the response.
     *
     * @param call
     * @param t
     */
    @Override
    public void onFailure(Call<List<House>> call, Throwable t) {
        Toast.makeText(MainActivity.this, t.toString(), Toast.LENGTH_LONG).show();
        Log.e(TAG,t.getMessage());
        progressDialog.dismiss();
    }

    private void initialiseNavigation() {

        //disable darkmode
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        //initialise navigation variable
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigation);
        //set selected
        bottomNavigationView.setSelectedItemId(R.id.navHouses);
        //perform ItemSelectedListener
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){

                    case R.id.navConsumption:
                        startActivity(new Intent(getApplicationContext(), ConsumptionActivity.class));
                        overridePendingTransition(0,0);
                        return true;

                    case R.id.navManage:
                        startActivity(new Intent(getApplicationContext(), ManageActivity.class));
                        overridePendingTransition(0,0);
                        return true;

                    case R.id.navHouses:
                        return true;

                }
                return false;
            }
        });
    }

}