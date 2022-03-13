package be.ucll.java.mobile.smarthome_mobile;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import be.ucll.java.mobile.smarthome_mobile.api.Connection;
import be.ucll.java.mobile.smarthome_mobile.api.device.DeviceApiInterface;
import be.ucll.java.mobile.smarthome_mobile.exception.DataNotFoundException;
import be.ucll.java.mobile.smarthome_mobile.pojo.Device;
import be.ucll.java.mobile.smarthome_mobile.pojo.DeviceAllParams;
import be.ucll.java.mobile.smarthome_mobile.util.AuthorizationManager;
import be.ucll.java.mobile.smarthome_mobile.util.DeviceCategory;
import be.ucll.java.mobile.smarthome_mobile.util.NavigationManager;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class DeviceActivity extends AppCompatActivity implements Callback<DeviceAllParams> {
    private final String TAG = this.getClass().getSimpleName();
    private ProgressDialog progressDialog;
    private String deviceCategory;
    private DeviceAllParams device;

    private TextView name;
    private TextView status;
    private TextView room;
    private TextView category;
    private TextView type;
    private TextView program;
    private TextView temperature;
    private TextView timer;
    private TextView volume;
    private TextView sensortype;
    private TextView sensordata;

    public void getDeviceData(){
        progressDialog = new ProgressDialog(DeviceActivity.this);
        progressDialog.create();

        try {
            Gson gson = new GsonBuilder()
                    .setLenient()
                    .create();

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(Connection.getUrl())
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build();

            DeviceApiInterface deviceApi = retrofit.create(DeviceApiInterface.class);

            int deviceId = this.getIntent().getIntExtra("deviceId", 0);


            Call<DeviceAllParams> call;

            if (DeviceCategory.nameEqualToCategory(DeviceCategory.BIG_ELECTRO, deviceCategory)) {
                call = deviceApi.getBigElektroById(deviceId, AuthorizationManager.getInstance(this).getSessionId());
            } else if (DeviceCategory.nameEqualToCategory(DeviceCategory.GENERIC, deviceCategory)){
                call = deviceApi.getDeviceById(deviceId, AuthorizationManager.getInstance(this).getSessionId());
            } else if (DeviceCategory.nameEqualToCategory(DeviceCategory.MEDIA,deviceCategory)) {
                call = deviceApi.getMediaById(deviceId, AuthorizationManager.getInstance(this).getSessionId());
            } else if (DeviceCategory.nameEqualToCategory(DeviceCategory.SENSOR,deviceCategory)) {
                call = deviceApi.getSensorById(deviceId, AuthorizationManager.getInstance(this).getSessionId());
            }else {
                throw new DataNotFoundException("selected item is not valid or empty");
            }

            call.enqueue(this);
        }catch (Exception e){
            throw new DataNotFoundException(e.getMessage());

        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device);

        NavigationManager.initialise(this);

        //GENERIC
        name = findViewById(R.id.txtTitleDevice);
        status = findViewById(R.id.txtDeviceStatus);
        room = findViewById(R.id.txtDeviceRoom);
        category = findViewById(R.id.txtDeviceCategory);
        // BIG ELEK
        type = findViewById(R.id.txtBigElekType);
        program = findViewById(R.id.txtBigElekProgram);
        temperature = findViewById(R.id.txtBigElekTemperature);
        timer = findViewById(R.id.txtBigElekTimer);
        // MEDIA
        volume = findViewById(R.id.txtMediaVolume);
        // SENSOR
        sensortype = findViewById(R.id.txtSensorType);
        sensordata = findViewById(R.id.txtSensorData);

        //DATA WE ALREADY HAVE
        String roomName = getIntent().getStringExtra("roomName");
        deviceCategory = this.getIntent().getStringExtra("deviceCategory");
        room.setText(roomName);
        category.setText(String.valueOf(deviceCategory));

        // category.setText(deviceCategory);
        if (DeviceCategory.nameEqualToCategory(DeviceCategory.BIG_ELECTRO, deviceCategory)) {
            type.setVisibility(View.VISIBLE);
            program.setVisibility(View.VISIBLE);
            temperature.setVisibility(View.VISIBLE);
            timer.setVisibility(View.VISIBLE);
            volume.setVisibility(View.GONE);
            sensortype.setVisibility(View.GONE);
            sensordata.setVisibility(View.GONE);
        } else if (DeviceCategory.nameEqualToCategory(DeviceCategory.MEDIA,deviceCategory)) {
            type.setVisibility(View.GONE);
            program.setVisibility(View.GONE);
            temperature.setVisibility(View.GONE);
            timer.setVisibility(View.GONE);
            volume.setVisibility(View.VISIBLE);
            sensortype.setVisibility(View.GONE);
            sensordata.setVisibility(View.GONE);
        } else if (DeviceCategory.nameEqualToCategory(DeviceCategory.SENSOR,deviceCategory)) {
            type.setVisibility(View.GONE);
            program.setVisibility(View.GONE);
            temperature.setVisibility(View.GONE);
            timer.setVisibility(View.GONE);
            volume.setVisibility(View.GONE);
            sensortype.setVisibility(View.VISIBLE);
            sensordata.setVisibility(View.VISIBLE);
        }else if(DeviceCategory.nameEqualToCategory(DeviceCategory.GENERIC,deviceCategory)){
            type.setVisibility(View.GONE);
            program.setVisibility(View.GONE);
            temperature.setVisibility(View.GONE);
            timer.setVisibility(View.GONE);
            volume.setVisibility(View.GONE);
            sensortype.setVisibility(View.GONE);
            sensordata.setVisibility(View.GONE);
        }else{
            throw new DataNotFoundException("selected item is not valid or empty2");
        }

        getDeviceData();
    }

    public void setDevice(DeviceAllParams device){

        // GENERIC
        name.setText(device.getName());
        if(device.getStatus()){
            status.setText("Aan");
        }else{
            status.setText("Uit");
        }

        // BIG ELECTRO
        if (device.getType() != null ) {
            type.setText(device.getType().getName());
        }

        temperature.setText(String.valueOf(device.getTempature()));
        timer.setText(device.getTimer());
        program.setText(String.valueOf(device.getProgramid()));

        // MEDIA
        volume.setText(String.valueOf(device.getVolume()));

        // SENSOR
        sensortype.setText(device.getSensorType());
        sensordata.setText(String.valueOf(device.getSensordata()));
    }

    @Override
    public void onResponse(Call<DeviceAllParams> call, Response<DeviceAllParams> response) {
        if (response != null && response.body() != null) {
            if (response.isSuccessful()) {
                Log.d(TAG, "onResponse: code: " + response.code());
                device = response.body();
                setDevice(device);
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
    public void onFailure(Call<DeviceAllParams> call, Throwable t) {
        Toast.makeText(this, t.toString(), Toast.LENGTH_LONG).show();
        Log.e(TAG,t.getMessage());
        progressDialog.dismiss();
    }
}