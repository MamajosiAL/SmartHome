package be.ucll.java.mobile.smarthome_mobile;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import be.ucll.java.mobile.smarthome_mobile.api.Connection;
import be.ucll.java.mobile.smarthome_mobile.api.device.DeviceApiInterface;
import be.ucll.java.mobile.smarthome_mobile.exception.DataNotFoundException;
import be.ucll.java.mobile.smarthome_mobile.pojo.BigElectro;
import be.ucll.java.mobile.smarthome_mobile.pojo.Device;
import be.ucll.java.mobile.smarthome_mobile.pojo.Media;
import be.ucll.java.mobile.smarthome_mobile.pojo.Sensor;
import be.ucll.java.mobile.smarthome_mobile.pojo.Type;
import be.ucll.java.mobile.smarthome_mobile.util.AuthorizationManager;
import be.ucll.java.mobile.smarthome_mobile.util.BigElectroType;
import be.ucll.java.mobile.smarthome_mobile.util.DeviceCategory;
import be.ucll.java.mobile.smarthome_mobile.util.NavigationManager;
import be.ucll.java.mobile.smarthome_mobile.util.SensorType;
import be.ucll.java.mobile.smarthome_mobile.util.TxtValidator;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

@SuppressWarnings("deprecation")
public class AddDeviceActivity extends AppCompatActivity implements Callback<String> {
    private final String TAG = this.getClass().getSimpleName();
    private Spinner dropdownListCategory, dropdownListType;
    private ProgressDialog progressDialog;
    private final AuthorizationManager authorizationManager = AuthorizationManager.getInstance(this);

    private ArrayAdapter spinnerAdapterCat;
    private ArrayAdapter spinnerAdapterType;

    private EditText name;
    private Button addButton;
    private TextView typeTitle;

    public void initialiseForm(DeviceCategory category){
        typeTitle =findViewById(R.id.txtAddDeviceTypes);
        switch (category){
            case BIG_ELECTRO:
                typeTitle.setText(getText(R.string.deviceType));
                typeTitle.setVisibility(View.VISIBLE);
                dropdownListType.setVisibility(View.VISIBLE);
                break;
            case SENSOR:
                typeTitle.setText(getText(R.string.sensorType));
                typeTitle.setVisibility(View.VISIBLE);
                dropdownListType.setVisibility(View.VISIBLE);
                break;
            default:
                typeTitle.setVisibility(View.GONE);
                dropdownListType.setVisibility(View.GONE);
                break;
        }
    }

    public void addDevice() {
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
            int roomId = this.getIntent().getIntExtra("roomId",0);

            //check if roomId is not null
            if(roomId==0){
                throw new NullPointerException("The id of the selected room is not found");
            }
            Call<String> call;
            String selectedItem  = (String) dropdownListCategory.getSelectedItem();

            if(selectedItem==null||selectedItem.isEmpty()){
                selectedItem = DeviceCategory.GENERIC.getName();
            }
            Device newDevice;

            String selectedType = (String) dropdownListType.getSelectedItem();

            // define call depending on selectedItem
            if (DeviceCategory.nameEqualToCategory(DeviceCategory.BIG_ELECTRO,selectedItem)) {
                newDevice = new BigElectro();
                newDevice.setName( String.valueOf(name.getText()));
                newDevice.setRoomid(roomId);
                ((BigElectro) newDevice).setType(new Type(selectedType));
                call = deviceApiInterface.createBigElektroInRoom((BigElectro) newDevice,AuthorizationManager.getInstance(this).getSessionId());
            } else if (DeviceCategory.nameEqualToCategory(DeviceCategory.GENERIC,selectedItem)) {
                newDevice = new Device();
                newDevice.setName( String.valueOf(name.getText()));
                newDevice.setRoomid(roomId);
                call = deviceApiInterface.createDeviceInRoom(newDevice, AuthorizationManager.getInstance(this).getSessionId());
            } else if (DeviceCategory.nameEqualToCategory(DeviceCategory.MEDIA,selectedItem)) {
                newDevice = new Media();
                newDevice.setName( String.valueOf(name.getText()));
                newDevice.setRoomid(roomId);
                call = deviceApiInterface.createMediaInRoom((Media) newDevice, AuthorizationManager.getInstance(this).getSessionId());
            } else if (DeviceCategory.nameEqualToCategory(DeviceCategory.SENSOR,selectedItem)) {
                newDevice = new Sensor();
                newDevice.setName(String.valueOf(name.getText()));
                newDevice.setRoomid(roomId);
                ((Sensor) newDevice).setSensorType(selectedType);
                call = deviceApiInterface.createSensorInRoom((Sensor) newDevice, AuthorizationManager.getInstance(this).getSessionId());
            }else {
                throw new DataNotFoundException("selected item is not valid or empty");
            }

            call.enqueue(this);
        }catch (Exception e){
            throw new DataNotFoundException(e.getMessage());
        }


    }

    public void populateDropdownType(){
        List<String> dropdownItems = new ArrayList<>();
        if(dropdownListCategory != null  && !String.valueOf(dropdownListCategory.getSelectedItem()).isEmpty()){
            String selected = (String) dropdownListCategory.getSelectedItem();
            if(selected == null) selected = DeviceCategory.GENERIC.getNameBE();
            switch (DeviceCategory.getCategoryFromName(selected)){
                case BIG_ELECTRO: dropdownItems = Locale.getDefault().getLanguage().equals("BE") ?  BigElectroType.getNamesBE() : BigElectroType.getNames();
                    break;
                case SENSOR: dropdownItems = Locale.getDefault().getLanguage().equals("BE") ?  SensorType.getNamesBE() : SensorType.getNames();
                    break;
                default:
                    break;
            }
        }

        spinnerAdapterType = new ArrayAdapter(getApplicationContext(), R.layout.spinner_item, R.id.txtSpinnerItem, dropdownItems);

        spinnerAdapterType.setDropDownViewResource(R.layout.spinner_item);
        dropdownListType.setAdapter(spinnerAdapterType);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_device);

        NavigationManager.initialise(this);
        dropdownListCategory = findViewById(R.id.spinAddDeviceCategory);
        dropdownListType = findViewById(R.id.spinAddDeviceTpe);
        name = findViewById(R.id.txtAddDeviceName);
        addButton = findViewById(R.id.btnAddDevice);

        addButton.setOnClickListener(v -> {
            if(TxtValidator.validate(name) && authorizationManager.isSignedIn()){
                addDevice();
            }
        });


        if (AuthorizationManager.getInstance(this).isSignedIn()) {

            List<String> dropdownItems = new ArrayList<>();
            if(dropdownListCategory != null  && !String.valueOf(dropdownListCategory.getSelectedItem()).isEmpty()){
                String selected = (String) dropdownListCategory.getSelectedItem();
                if(selected == null) selected = DeviceCategory.GENERIC.getNameBE();
                switch (DeviceCategory.getCategoryFromName(selected)){
                    case BIG_ELECTRO: dropdownItems = Locale.getDefault().getLanguage().equals("BE") ?  BigElectroType.getNamesBE() : BigElectroType.getNames();
                    break;
                    case SENSOR: dropdownItems = Locale.getDefault().getLanguage().equals("BE") ?  SensorType.getNamesBE() : SensorType.getNames();
                    break;
                    default:
                    break;
                }
            }

            spinnerAdapterType = new ArrayAdapter(getApplicationContext(), R.layout.spinner_item, R.id.txtSpinnerItem, dropdownItems);

            //set dropdownLists depending on language
            if (Locale.getDefault().getLanguage().equals("BE")) {
                spinnerAdapterCat = new ArrayAdapter(getApplicationContext(), R.layout.spinner_item, R.id.txtSpinnerItem, DeviceCategory.getNamesBE());

            } else {
                spinnerAdapterCat = new ArrayAdapter(getApplicationContext(), R.layout.spinner_item, R.id.txtSpinnerItem, DeviceCategory.getNames());

            }

            spinnerAdapterType.setDropDownViewResource(R.layout.spinner_item);
            dropdownListType.setAdapter(spinnerAdapterType);

            //hide type dropdownlist by default
            dropdownListType.setVisibility(View.GONE);

            spinnerAdapterCat.setDropDownViewResource(R.layout.spinner_item);
            dropdownListCategory.setAdapter(spinnerAdapterCat);

            dropdownListCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    initialiseForm(DeviceCategory.getCategoryFromName((String)dropdownListCategory.getSelectedItem()));
                    populateDropdownType();
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
        }
        initialiseForm(DeviceCategory.BIG_ELECTRO);

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
    public void onResponse(Call<String> call, Response<String> response) {
        if (response != null && response.body() != null) {
            if (response.isSuccessful()) {
                Intent intent = new Intent(this, RoomActivity.class);
                intent.putExtra("roomId", this.getIntent().getIntExtra("roomId",0));
                intent.putExtra("roomName", this.getIntent().getStringExtra("roomName"));
                startActivity(intent);
                overridePendingTransition(0, 0);
            }else {
                Log.e(TAG, getString(R.string.responseErrorCode) + response.code());
                progressDialog.dismiss();
            }
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
    public void onFailure(Call<String> call, Throwable t) {
        Toast.makeText(this, t.toString(), Toast.LENGTH_LONG).show();
        Log.e(TAG,t.getMessage());
        progressDialog.dismiss();
    }
}

