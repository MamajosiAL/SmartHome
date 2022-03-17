package be.ucll.java.mobile.smarthome_mobile;

import androidx.appcompat.app.AppCompatActivity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.material.slider.RangeSlider;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.util.HashMap;
import be.ucll.java.mobile.smarthome_mobile.api.Connection;
import be.ucll.java.mobile.smarthome_mobile.api.device.DeviceApiInterface;
import be.ucll.java.mobile.smarthome_mobile.api.device.DeviceDeleter;
import be.ucll.java.mobile.smarthome_mobile.api.device.DeviceNameEditor;
import be.ucll.java.mobile.smarthome_mobile.api.device.DeviceStatusToggler;
import be.ucll.java.mobile.smarthome_mobile.api.media.VolumeChangeAdapter;
import be.ucll.java.mobile.smarthome_mobile.exception.DataNotFoundException;
import be.ucll.java.mobile.smarthome_mobile.pojo.BigElectro;
import be.ucll.java.mobile.smarthome_mobile.pojo.Device;
import be.ucll.java.mobile.smarthome_mobile.pojo.DeviceAllParams;
import be.ucll.java.mobile.smarthome_mobile.pojo.Media;
import be.ucll.java.mobile.smarthome_mobile.pojo.Sensor;
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
    private int roomId;
    private boolean edit = false;
    private boolean hasResponded;
    private HashMap<TextView, String> initialTexts = new HashMap<>();
    private RangeSlider volumeSlider;
    private Switch toggleStatusSwitch;
    private ImageView editDeviceButton, deleteDeviceButton;
    private TextView name,room,category,type,program,temperature,timer
            ,sensortype,sensordata,lbltype,lblprogram,lbltemperature,lbltimer,lblvolume
            ,lblsensordata,lblsensortype;

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
            roomId = this.getIntent().getIntExtra("roomId",0);

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

            // volumeslider updated by db volume, now we can update the volume again. check the volumeSlider.addOnChangeListener
            hasResponded = true;

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
        room = findViewById(R.id.txtDeviceRoom);
        category = findViewById(R.id.txtDeviceCategory);
        // BIG ELECTRO
        type = findViewById(R.id.txtBigElekType);
        program = findViewById(R.id.txtBigElekProgram);
        temperature = findViewById(R.id.txtBigElekTemperature);
        timer = findViewById(R.id.txtBigElekTimer);
        lbltype = findViewById(R.id.lblBigElekType);
        lblprogram = findViewById(R.id.lblBigElekProgram);
        lbltemperature = findViewById(R.id.lblBigElekTemperature);
        lbltimer = findViewById(R.id.lblBigElekTimer);
        // MEDIA
        volumeSlider = findViewById(R.id.sliderVolume);
        volumeSlider.setValueFrom(0f);
        volumeSlider.setValueTo(100f);
        volumeSlider.setStepSize(1f);
        lblvolume = findViewById(R.id.lblMediaVolume);
        // SENSOR
        sensortype = findViewById(R.id.txtSensorType);
        sensordata = findViewById(R.id.txtSensorData);
        lblsensordata = findViewById(R.id.lblSensorData);
        lblsensortype = findViewById(R.id.lblSensorType);

        //BUTTONS
        deleteDeviceButton = findViewById(R.id.imgDeleteDevice);
        editDeviceButton = findViewById(R.id.imgEditDevice);
        toggleStatusSwitch = findViewById(R.id.swtchToggleStatusDevice);

        //DATA WE ALREADY HAVE
        String roomName = getIntent().getStringExtra("roomName");
        deviceCategory = this.getIntent().getStringExtra("deviceCategory");
        room.setText(roomName);
        category.setText(String.valueOf(deviceCategory));

        //visibility of textviews by device category
        if (DeviceCategory.nameEqualToCategory(DeviceCategory.BIG_ELECTRO, deviceCategory)) {

            type.setVisibility(View.VISIBLE);
            program.setVisibility(View.VISIBLE);
            temperature.setVisibility(View.VISIBLE);
            timer.setVisibility(View.VISIBLE);
            volumeSlider.setVisibility(View.GONE);
            sensortype.setVisibility(View.GONE);
            sensordata.setVisibility(View.GONE);

            lbltype.setVisibility(View.VISIBLE);
            lblprogram.setVisibility(View.VISIBLE);
            lbltemperature.setVisibility(View.VISIBLE);
            lbltimer.setVisibility(View.VISIBLE);
            lblvolume.setVisibility(View.GONE);
            lblsensortype.setVisibility(View.GONE);
            lblsensordata.setVisibility(View.GONE);
        } else if (DeviceCategory.nameEqualToCategory(DeviceCategory.MEDIA,deviceCategory)) {
            type.setVisibility(View.GONE);
            program.setVisibility(View.GONE);
            temperature.setVisibility(View.GONE);
            timer.setVisibility(View.GONE);
            volumeSlider.setVisibility(View.VISIBLE);
            sensortype.setVisibility(View.GONE);
            sensordata.setVisibility(View.GONE);

            lbltype.setVisibility(View.GONE);
            lblprogram.setVisibility(View.GONE);
            lbltemperature.setVisibility(View.GONE);
            lbltimer.setVisibility(View.GONE);
            lblvolume.setVisibility(View.VISIBLE);
            lblsensortype.setVisibility(View.GONE);
            lblsensordata.setVisibility(View.GONE);


        } else if (DeviceCategory.nameEqualToCategory(DeviceCategory.SENSOR,deviceCategory)) {
            type.setVisibility(View.GONE);
            program.setVisibility(View.GONE);
            temperature.setVisibility(View.GONE);
            timer.setVisibility(View.GONE);
            volumeSlider.setVisibility(View.GONE);
            sensortype.setVisibility(View.VISIBLE);
            sensordata.setVisibility(View.VISIBLE);

            lbltype.setVisibility(View.GONE);
            lblprogram.setVisibility(View.GONE);
            lbltemperature.setVisibility(View.GONE);
            lbltimer.setVisibility(View.GONE);
            lblvolume.setVisibility(View.GONE);
            lblsensortype.setVisibility(View.VISIBLE);
            lblsensordata.setVisibility(View.VISIBLE);
        }else if(DeviceCategory.nameEqualToCategory(DeviceCategory.GENERIC,deviceCategory)){
            type.setVisibility(View.GONE);
            program.setVisibility(View.GONE);
            temperature.setVisibility(View.GONE);
            timer.setVisibility(View.GONE);
            volumeSlider.setVisibility(View.GONE);
            sensortype.setVisibility(View.GONE);
            sensordata.setVisibility(View.GONE);

            lbltype.setVisibility(View.GONE);
            lblprogram.setVisibility(View.GONE);
            lbltemperature.setVisibility(View.GONE);
            lbltimer.setVisibility(View.GONE);
            lblvolume.setVisibility(View.GONE);
            lblsensortype.setVisibility(View.GONE);
            lblsensordata.setVisibility(View.GONE);
        }else{
            throw new DataNotFoundException("selected item is not valid or empty2");
        }

        getDeviceData();

        //initialise delete button
        deleteDeviceButton.setOnClickListener(v -> {
            new AlertDialog.Builder(this)
                    .setTitle(R.string.deleteConfirmation)
                    .setMessage(R.string.deleteConfMessage)
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setPositiveButton(R.string.yes, (dialog, whichButton) -> deleteDevice())
                    .setNegativeButton(R.string.no, null).show();
        });

        editDeviceButton.setOnClickListener(v -> {
            editVisibility();
        });

        toggleStatusSwitch.setOnClickListener(v -> {
            try{
                new DeviceStatusToggler(this).toggleStatus(device.getId());

            } catch (Exception e){
                Toast.makeText(this, e.toString(), Toast.LENGTH_LONG).show();
                Log.e(TAG,e.getMessage());
            }
        });

        volumeSlider.addOnChangeListener((slider, value, fromUser) -> {
            // check if the editvolume has called getdevicedata if not, it doesn't update the volume
            // otherwise the volumeslider will break.
            if(hasResponded){
                device.setVolume(Math.round(slider.getValues().get(0)));
                editVolume();
            }
            hasResponded = false;
        });

    }

    private void editVolume() {
        try{
            //Media media = deviceAllParamsToMedia(device);
            new VolumeChangeAdapter(this).changeVolume(device);
        } catch (Exception e){
            Toast.makeText(this, e.toString(), Toast.LENGTH_LONG).show();
            Log.e(TAG,e.getMessage());
        }
    }

    private Media deviceAllParamsToMedia(DeviceAllParams device){
        Media media = new Media();
        media.setId(device.getId());
        media.setName(device.getName());
        media.setRoomid(device.getRoomid());
        media.setVolume(device.getVolume());
        media.setStatus(device.getStatus());
        media.setCategoryid(device.getCategoryid());
        return media;
    }

    private void deleteDevice() {
        try{
            new DeviceDeleter(this).delete(device.getId(), roomId);
        } catch (Exception e){
            Toast.makeText(this, e.toString(), Toast.LENGTH_LONG).show();
            Log.e(TAG,e.getMessage());
        }
    }

    public void setDevice(DeviceAllParams device){

        // GENERIC
        String deviceName = device.getName();
        this.name.setText(deviceName);
        initialTexts.put(name, deviceName);


        toggleStatusSwitch.setChecked(device.getStatus());

        // BIG ELECTRO
        if (device.getType() != null ) {
            type.setText(device.getType().getName());
        }

        temperature.setText(String.valueOf(device.getTempature()));
        timer.setText(device.getTimer());
        program.setText(String.valueOf(device.getProgramid()));

        // MEDIA
        if (DeviceCategory.nameEqualToCategory(DeviceCategory.MEDIA,deviceCategory)) {
            volumeSlider.setValues((float)device.getVolume());
        }


        // SENSOR
        sensortype.setText(device.getSensorType());
        sensordata.setText(String.valueOf(device.getSensordata()));

    }

    @Override
    public void onResponse(Call<DeviceAllParams> call, Response<DeviceAllParams> response) {
        if (response != null && response.body() != null) {
            if (response.isSuccessful()) {
                device = response.body();
                device.setRoomid(this.getIntent().getIntExtra("roomId",0));
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


    public void editVisibility(){
        toggleTextViewEditable(name);
    }

    // TODO : edit device
    public void toggleTextViewEditable(TextView textView){
        if(edit){

            //check if text has changed, if so submit changes
            if(initialTexts.containsKey(textView)&&!initialTexts.get(textView).isEmpty()){
                if (!initialTexts.get(textView).equals(textView.getText())){
                    DeviceAllParams updatedDevice = device;
                    updatedDevice.setName(String.valueOf(textView.getText()));
                    new DeviceNameEditor(this).editName(device, deviceCategory);
                }
            }else {
                throw new DataNotFoundException("No textview exists in initialTexts");
            }

            // remove editable
            textView.setCursorVisible(false);
            textView.setFocusableInTouchMode(false);
            textView.setEnabled(false);
            textView.requestFocus();

            // remove underline
            textView.setText(device.getName());

            // edit off
            edit = false;
        }else{
            // editable
            textView.setCursorVisible(true);
            textView.setFocusableInTouchMode(true);
            textView.setEnabled(true);
            textView.requestFocus();

            // underline
            SpannableString content = new SpannableString(textView.getText());
            content.setSpan(new UnderlineSpan(), 0, content.length(), 0);
            textView.setText(content);

            edit = true;
        }
    }
}