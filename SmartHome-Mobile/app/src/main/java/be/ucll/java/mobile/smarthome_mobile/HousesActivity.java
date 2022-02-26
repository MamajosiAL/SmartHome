package be.ucll.java.mobile.smarthome_mobile;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import be.ucll.java.mobile.smarthome_mobile.model.HousePOJO;
import be.ucll.java.mobile.smarthome_mobile.recyclerview.houses.HousesAdapter;
import be.ucll.java.mobile.smarthome_mobile.webservices.UserService;

public class HousesActivity extends AppCompatActivity {
    private static final String SMARTHOME_API_URL_HOUSE_PREFIX = "http://127.0.0.1:8080/houses";
    private static final String TAG = "HousesActivity";
    //private HousesFragment housesFragment;
    private HousesAdapter housesAdapter;
    private List<HousePOJO> houses;
    private RecyclerView recyclerHouses;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_houses);
        checkPermission(Manifest.permission.INTERNET, Integer.parseInt(Manifest.permission.INTERNET));
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
                        startActivity(new Intent(getApplicationContext(),ConsumptionActivity.class));
                        overridePendingTransition(0,0);
                        return true;

                    case R.id.navManage:
                        startActivity(new Intent(getApplicationContext(),ManageActivity.class));
                        overridePendingTransition(0,0);
                        return true;

                    case R.id.navHouses:
                        return true;

                }
                return false;
            }
        });
            recyclerHouses = findViewById(R.id.recyclerviewHouses);
            houses = new ArrayList<>();
            getHousesFromService();
            recyclerHouses.setLayoutManager(new LinearLayoutManager(this));
            housesAdapter = new HousesAdapter(this, houses);
            recyclerHouses.setAdapter(housesAdapter);
//        housesFragment = new HousesFragment();
//        getSupportFragmentManager().beginTransaction().replace(R.id.container_a, housesFragment).commit();
    }

    private void getHousesFromService() {
        Log.d(TAG,"getHousesFromService was called! With "+ houses.size()+ " houses in list");
        RequestQueue queue = Volley.newRequestQueue(this);
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, SMARTHOME_API_URL_HOUSE_PREFIX, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                for (int i = 0; i < response.length(); i++) {
                    try {
                        JSONObject houseObject = response.getJSONObject(i);
                        HousePOJO house = new Gson().fromJson(houseObject.toString(), HousePOJO.class);
                        houses.add(house);
                    } catch (Exception e) {
                        Log.e(TAG, e.getMessage());
                    }

                }
            }
        }, error -> Log.e(TAG, error.getMessage()));
    }

    // Function to check and request permission
    public void checkPermission(String permission, int requestCode)
    {
        // Checking if permission is not granted
        if (ContextCompat.checkSelfPermission(HousesActivity.this, permission) == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(HousesActivity.this, new String[] { permission }, requestCode);
        }
        else {
            Toast.makeText(HousesActivity.this, "Permission already granted", Toast.LENGTH_SHORT).show();
        }
    }

    // This request code is provided when user is prompt for permission.
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults)
    {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == Integer.parseInt(Manifest.permission.INTERNET)) {

            // Checking whether user granted the permission or not.
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                // Showing the toast message
                Toast.makeText(HousesActivity.this, getString(R.string.InternetGrantedMessage), Toast.LENGTH_SHORT).show();
            }
            else {
                Toast.makeText(HousesActivity.this, getString(R.string.InternetNotGrantedMessage), Toast.LENGTH_SHORT).show();
            }
        }
        else if (requestCode == Integer.parseInt(Manifest.permission.WAKE_LOCK)) {
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(HousesActivity.this, getString(R.string.WakeLockGranted), Toast.LENGTH_SHORT).show();
            }
            else {
                Toast.makeText(HousesActivity.this, getString(R.string.WakeLockNotGranted), Toast.LENGTH_SHORT).show();
            }
        }
    }

}