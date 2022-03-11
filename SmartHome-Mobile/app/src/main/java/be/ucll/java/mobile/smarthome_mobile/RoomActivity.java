package be.ucll.java.mobile.smarthome_mobile;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.List;

import be.ucll.java.mobile.smarthome_mobile.api.Connection;
import be.ucll.java.mobile.smarthome_mobile.api.device.DeviceApiInterface;
import be.ucll.java.mobile.smarthome_mobile.api.device.DevicesAdapter;
import be.ucll.java.mobile.smarthome_mobile.exception.DataNotFoundException;
import be.ucll.java.mobile.smarthome_mobile.pojo.BigElectro;
import be.ucll.java.mobile.smarthome_mobile.pojo.Device;
import be.ucll.java.mobile.smarthome_mobile.util.AuthorizationManager;
import be.ucll.java.mobile.smarthome_mobile.util.DeviceCategory;
import be.ucll.java.mobile.smarthome_mobile.util.NavigationManager;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

@SuppressWarnings("ALL")
public class RoomActivity extends AppCompatActivity implements Callback<List<Device>> {
    private final String TAG = this.getClass().getSimpleName();
    private RecyclerView recyclerViewDevices;
    private TextView title;
    private ImageView editButton;
    private Spinner dropdownList;
    private ProgressDialog progressDialog;
    List<Device> devicesInRoomFromHouse;

    public void getRoomsListData() {
        progressDialog = new ProgressDialog(this);
        progressDialog.create();
        try {
            Gson gson = new GsonBuilder()
                    .setLenient()
                    .create();

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(Connection.getUrl())
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build();

            DeviceApiInterface deviceApiInterface = retrofit.create(DeviceApiInterface.class);

            //fetch roomId
            int roomId = this.getIntent().getIntExtra("roomChineesId",0);
            //check if roomId is not null
            if(roomId==0){
                throw new NullPointerException("The id of the selected room is not found");
            }
            String deviceType = DeviceCategory.BIG_ELECTRO.getName();
            String selectedItem = "";
            Call<List<Device>> call = null;
            try {

                selectedItem = (String) dropdownList.getSelectedItem();
                if (DeviceCategory.BIG_ELECTRO.getName().equals(selectedItem)) {
                    DeviceCategory.BIG_ELECTRO.getName();
                    call = deviceApiInterface.getBigElektroInRoomFromHouseWithAccessForUserInSession(roomId, AuthorizationManager.getInstance(this).getSessionId());
                } else if (DeviceCategory.GENERIC.getName().equals(selectedItem)) {
                    DeviceCategory.GENERIC.getName();
                    call = deviceApiInterface.getDevicesInRoomFromHouseWithAccessForUserInSession(roomId, AuthorizationManager.getInstance(this).getSessionId());
                } else if (DeviceCategory.MEDIA.getName().equals(selectedItem)) {
                    DeviceCategory.MEDIA.getName();
                    call = deviceApiInterface.getBigElektroInRoomFromHouseWithAccessForUserInSession(roomId, AuthorizationManager.getInstance(this).getSessionId());
                } else if (DeviceCategory.SENSOR.getName().equals(selectedItem)) {
                    DeviceCategory.SENSOR.getName();
                    call = deviceApiInterface.getBigElektroInRoomFromHouseWithAccessForUserInSession(roomId, AuthorizationManager.getInstance(this).getSessionId());
                }else {
                    throw new DataNotFoundException("selected ielected item is not valid or empty");
                }
            }catch (Exception e){
                Log.e(TAG,e.getMessage());
            }
            call.enqueue(this);
        }catch (Exception e){
            throw new DataNotFoundException(e.getMessage());
        }


    }

    private void setDataInRecyclerView() {
        // set a LinearLayoutManager with default vertical orientation
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerViewDevices.setLayoutManager(linearLayoutManager);
        // call the constructor of housesAdapter to send the reference and data to Adapter
        DevicesAdapter roomsAdapter = new DevicesAdapter(this, devicesInRoomFromHouse);
        recyclerViewDevices.setAdapter(roomsAdapter); // set the Adapter to RecyclerView
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room);

        NavigationManager.initialise(this);

        title = findViewById(R.id.titleRoom);

        if (AuthorizationManager.getInstance(this).isSignedIn()) {
            dropdownList = findViewById(R.id.spinDeviceCategory);
            ArrayAdapter spinnerAdapter = new ArrayAdapter(getApplicationContext(),R.layout.spinner_item, DeviceCategory.values());
            dropdownList.setAdapter(spinnerAdapter);

            recyclerViewDevices = findViewById(R.id.recyclerViewDevices);
            title.setText(this.getIntent().getStringExtra("roomName"));
            try {
                //fabAddRoom for adding a new room to house
                FloatingActionButton fab = findViewById(R.id.fabAddDevice);
                fab.setOnClickListener(view -> {
                    Intent intent = new Intent(this, AddDeviceActivity.class);
                    intent.putExtra("houseId", this.getIntent().getIntExtra("houseId",0));
                    intent.putExtra("roomId", this.getIntent().getIntExtra("roomId",0));
                    startActivity(intent);
                    overridePendingTransition(0, 0);
                });
                getRoomsListData();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onResponse(Call<List<Device>> call, Response<List<Device>> response) {
        if (response != null && response.body() != null) {
            if (response.isSuccessful()) {
                devicesInRoomFromHouse = response.body();
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

    @Override
    public void onFailure(Call<List<Device>> call, Throwable t) {
        Toast.makeText(this, t.toString(), Toast.LENGTH_LONG).show();
        Log.e(TAG,t.getMessage());
        progressDialog.dismiss();
    }
}