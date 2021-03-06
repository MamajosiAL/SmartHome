package be.ucll.java.mobile.smarthome_mobile;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.List;
import java.util.Locale;

import be.ucll.java.mobile.smarthome_mobile.api.Connection;
import be.ucll.java.mobile.smarthome_mobile.api.device.DeviceApiInterface;
import be.ucll.java.mobile.smarthome_mobile.api.device.DevicesAdapter;
import be.ucll.java.mobile.smarthome_mobile.api.house.HouseDeleter;
import be.ucll.java.mobile.smarthome_mobile.api.room.RoomDeleter;
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
    private ImageView editButton , deletebutton;
    private Spinner dropdownList;
    private ProgressDialog progressDialog;
    private String selectedItem;
    List<Device> devicesInRoomFromHouse;
    private int roomId, houseId;

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
            roomId = this.getIntent().getIntExtra("roomId",0);
            houseId = this.getIntent().getIntExtra("houseId",0);
            //check if roomId is not null

            if(roomId==0){
                throw new NullPointerException("The id of the selected room is not found");
            }
            Call<List<Device>> call;
            selectedItem  = (String) dropdownList.getSelectedItem();

            if(selectedItem==null||selectedItem.isEmpty()){
                selectedItem = DeviceCategory.GENERIC.getName();
           } 

            // define call depending on selectedItem
                  if (DeviceCategory.nameEqualToCategory(DeviceCategory.BIG_ELECTRO,selectedItem)) {
                call = deviceApiInterface.getBigElektroInRoomFromHouseWithAccessForUserInSession(roomId, AuthorizationManager.getInstance(this).getSessionId());
            } else if (DeviceCategory.nameEqualToCategory(DeviceCategory.GENERIC,selectedItem)) {

                call = deviceApiInterface.getDevicesInRoomFromHouseWithAccessForUserInSession(roomId, AuthorizationManager.getInstance(this).getSessionId());
            } else if (DeviceCategory.nameEqualToCategory(DeviceCategory.MEDIA,selectedItem)) {

                call = deviceApiInterface.getMediaInRoomFromHouseWithAccessForUserInSession(roomId, AuthorizationManager.getInstance(this).getSessionId());
            } else if (DeviceCategory.nameEqualToCategory(DeviceCategory.SENSOR,selectedItem)) {

                call = deviceApiInterface.getSensorInRoomFromHouseWithAccessForUserInSession(roomId, AuthorizationManager.getInstance(this).getSessionId());
            }else {
                throw new DataNotFoundException("selected item is not valid or empty");
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

        DevicesAdapter roomsAdapter = new DevicesAdapter(this, devicesInRoomFromHouse, selectedItem.toString(), this.getIntent().getStringExtra("roomName"), roomId);
        recyclerViewDevices.setAdapter(roomsAdapter); // set the Adapter to RecyclerView
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room);

        NavigationManager.initialise(this);

        title = findViewById(R.id.titleRoom);

        deletebutton = findViewById(R.id.imgDeleteRoom);

        if (AuthorizationManager.getInstance(this).isSignedIn()) {
            dropdownList = findViewById(R.id.spinDeviceCategory);
            ArrayAdapter spinnerAdapter = null;

            //set dropdownList depending on language
            if(Locale.getDefault().getLanguage().equals("BE")){
                spinnerAdapter = new ArrayAdapter(getApplicationContext(),R.layout.spinner_item, R.id.txtSpinnerItem, DeviceCategory.getNamesBE());
            }else{
                spinnerAdapter = new ArrayAdapter(getApplicationContext(),R.layout.spinner_item, R.id.txtSpinnerItem, DeviceCategory.getNames());
            }
            spinnerAdapter.setDropDownViewResource(R.layout.spinner_item);
            dropdownList.setAdapter(spinnerAdapter);

            dropdownList.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    getRoomsListData();
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });

            deletebutton.setOnClickListener(v -> {
                new AlertDialog.Builder(this)
                        .setTitle(R.string.deleteConfirmation)
                        .setMessage(R.string.deleteConfMessage)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setPositiveButton(R.string.yes, (dialog, whichButton) -> deleteRoom())
                        .setNegativeButton(R.string.no, null).show();
            });



            recyclerViewDevices = findViewById(R.id.recyclerViewDevices);
            title.setText(this.getIntent().getStringExtra("roomName"));
            try {
                //fabAddRoom for adding a new room to house
                FloatingActionButton fab = findViewById(R.id.fabAddDevice);
                fab.setOnClickListener(view -> {
                    Intent intent = new Intent(this, AddDeviceActivity.class);
                    intent.putExtra("houseId", this.getIntent().getIntExtra("houseId",0));
                    intent.putExtra("roomId", this.getIntent().getIntExtra("roomId",0));
                    intent.putExtra("roomName", this.getIntent().getStringExtra("roomName"));
                    intent.putExtra("houseName", this.getIntent().getStringExtra("houseName"));
                    startActivity(intent);
                    overridePendingTransition(0, 0);
                });
                getRoomsListData();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void deleteRoom(){
        try{

            String roomName = this.getIntent().getStringExtra("roomName");
            new RoomDeleter(this).delete();
        } catch (Exception e){
            Toast.makeText(this, e.toString(), Toast.LENGTH_LONG).show();
            Log.e(TAG,e.getMessage());
        }
    }

    @Override
    public void onResponse(Call<List<Device>> call, Response<List<Device>> response) {
        if (response != null && response.body() != null) {
            Log.d(TAG, "onResponse: code: " + response.code());
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