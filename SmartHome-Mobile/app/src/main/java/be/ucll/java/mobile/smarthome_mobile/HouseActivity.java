package be.ucll.java.mobile.smarthome_mobile;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.List;

import be.ucll.java.mobile.smarthome_mobile.api.Connection;
import be.ucll.java.mobile.smarthome_mobile.api.house.HousesAdapter;
import be.ucll.java.mobile.smarthome_mobile.api.room.RoomsAdapter;
import be.ucll.java.mobile.smarthome_mobile.api.room.RoomsApiInterface;
import be.ucll.java.mobile.smarthome_mobile.exception.DataNotFoundException;
import be.ucll.java.mobile.smarthome_mobile.pojo.Device;
import be.ucll.java.mobile.smarthome_mobile.pojo.House;
import be.ucll.java.mobile.smarthome_mobile.pojo.Room;
import be.ucll.java.mobile.smarthome_mobile.util.AuthorizationManager;
import be.ucll.java.mobile.smarthome_mobile.util.NavigationManager;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

@SuppressWarnings("deprecation")
public class HouseActivity extends AppCompatActivity implements Callback<List<Room>> {
    private final String TAG = this.getClass().getSimpleName();
    private RecyclerView recyclerViewRooms;
    private TextView title;
    private ImageView editButton;
    private ProgressDialog progressDialog;
    List<Room> roomsFromHouse;

    public void getRoomsListData() {
        progressDialog = new ProgressDialog(HouseActivity.this);
        progressDialog.create();
        try {
            Gson gson = new GsonBuilder()
                    .setLenient()
                    .create();

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(Connection.getUrl())
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build();

            RoomsApiInterface roomsApi = retrofit.create(RoomsApiInterface.class);

            //fetch houseId
            int houseId = this.getIntent().getIntExtra("houseId",0);

            //check if houseId is not null
            if(houseId==0){
                throw new NullPointerException("The id of the selected house is 0");
            }

            Call<List<Room>> call = roomsApi.getRoomsFromHouseWithAccessForUserInSession(houseId, AuthorizationManager.getInstance(this).getSessionId());
            call.enqueue(this);
        }catch (Exception e){
            throw new DataNotFoundException(e.getCause());
        }
    }

    private void setDataInRecyclerView() {
        // set a LinearLayoutManager with default vertical orientation
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerViewRooms.setLayoutManager(linearLayoutManager);
        // call the constructor of housesAdapter to send the reference and data to Adapter
        RoomsAdapter roomsAdapter = new RoomsAdapter(this, roomsFromHouse);
        recyclerViewRooms.setAdapter(roomsAdapter); // set the Adapter to RecyclerView
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_house);

        NavigationManager.initialise(this);

        title = findViewById(R.id.titleHouse);
        roomsFromHouse = new ArrayList<>();

        if (AuthorizationManager.getInstance(this).isSignedIn()) {
            recyclerViewRooms = findViewById(R.id.recyclerViewRooms);
            title.setText(this.getIntent().getStringExtra("houseName"));
            try {
                //fabAddRoom for adding a new room to house
                FloatingActionButton fab = findViewById(R.id.fabAddRoomToHouse);
                fab.setOnClickListener(view -> {
                    Intent intent = new Intent(this, AddRoomActivity.class);
                    intent.putExtra("houseId", this.getIntent().getIntExtra("houseId",0));
                    startActivity(intent);
                    overridePendingTransition(0, 0);
                });
                /*editButton.setOnClickListener(view ->{
                    Intent intent = new Intent(this, AddRoomActivity.class);
                    intent.putExtra("houseId", this.getIntent().getIntExtra("houseId",0));
                    startActivity(intent);
                    overridePendingTransition(0, 0);
                });*/
                getRoomsListData();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Invoked for a received HTTP response.
     * <p>
     * Note: An HTTP response may still indicate an application-level failure such as a 404 or 500.
     * Call {@link Response#isSuccessful()} to determine if the response indicates success.
     *
     * @param call
     * @param response
     */
    @Override
    public void onResponse(Call<List<Room>> call, Response<List<Room>> response) {
        if (response != null && response.body() != null) {
            if (response.isSuccessful()) {
                roomsFromHouse = response.body();
                setDataInRecyclerView();
            }else {
                Log.e(TAG, getString(R.string.responseErrorCode) + response.code());
                progressDialog.dismiss();
            }
        }else{
            Log.e(TAG, getString(R.string.responseErrorCode) + response.code());
            progressDialog.dismiss();
        }
    }

    /**
     * Invoked when a network exception occurred talking to the server or when an unexpected
     * exception occurred creating the request or processing the response.
     *
     * @param call
     * @param t
     */
    @Override
    public void onFailure(Call<List<Room>> call, Throwable t) {
        Toast.makeText(HouseActivity.this, t.toString(), Toast.LENGTH_LONG).show();
        Log.e(TAG,t.getMessage());
        progressDialog.dismiss();
    }
}