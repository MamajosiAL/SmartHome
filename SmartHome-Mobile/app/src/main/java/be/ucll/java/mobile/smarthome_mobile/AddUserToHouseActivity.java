package be.ucll.java.mobile.smarthome_mobile;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.net.CookieHandler;
import java.net.CookieManager;
import java.util.concurrent.TimeUnit;

import be.ucll.java.mobile.smarthome_mobile.api.Connection;
import be.ucll.java.mobile.smarthome_mobile.api.house.HouseApiInterface;
import be.ucll.java.mobile.smarthome_mobile.api.user.UserApiInterface;
import be.ucll.java.mobile.smarthome_mobile.pojo.House;
import be.ucll.java.mobile.smarthome_mobile.util.AuthorizationManager;
import be.ucll.java.mobile.smarthome_mobile.util.NavigationManager;
import be.ucll.java.mobile.smarthome_mobile.util.TxtValidator;
import okhttp3.JavaNetCookieJar;
import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

@SuppressWarnings("deprecation")
public class AddUserToHouseActivity extends AppCompatActivity implements Callback<String> {
    private final String TAG = this.getClass().getSimpleName();
    private ProgressDialog progressDialog;
    private EditText name;
    private final AuthorizationManager authorizationManager = AuthorizationManager.getInstance(this);

    public Retrofit getClient(){
        CookieHandler cookieHandler = new CookieManager();
        OkHttpClient client = new okhttp3.OkHttpClient.Builder()
                .cookieJar(new JavaNetCookieJar(cookieHandler))
                .connectTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(10, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .build();

        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        return new Retrofit.Builder()
                .baseUrl(Connection.getUrl())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(client)
                .build();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_user_to_house);
        NavigationManager.initialise(this);

        // init the EditText and Button
        name = findViewById(R.id.txtAddUserName);
        Button addUserButton = findViewById(R.id.btnAddUserToHouse);

        // implement setOnClickListener event on addHouse Button
        addUserButton.setOnClickListener(view -> {
            if(TxtValidator.validate(name) && authorizationManager.isSignedIn()){
                addUser();
            }
        });
    }

    private void addUser() {
        // display a progress dialog
        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false); // set cancelable to false
        progressDialog.setMessage("Please Wait"); // set message
        progressDialog.show(); // show progress dialog
        startRequest();
    }

    private void startRequest() {
        String nameString = "";
        try {
            nameString = name.getText().toString().trim();
        }catch (Exception e){
            Log.e(TAG,e.getMessage());
        }

        HouseApiInterface houseApi = getClient().create(HouseApiInterface.class);

        int houseId = this.getIntent().getIntExtra("houseId",0);
        House newHouse = new House();
        newHouse.setId(houseId);
        newHouse.setUsername(nameString);

        Call<String> call = houseApi.addUserToHouseByUsername(newHouse, AuthorizationManager.getInstance(this).getSessionId());
        call.enqueue(this);
    }


    @Override
    public void onResponse(Call<String> call, Response<String> response) {
        if (response != null && response.body() != null) {
            if (response.isSuccessful()) {
                Intent intent = new Intent(this, UserInHouseActivity.class);
                intent.putExtra("houseId", this.getIntent().getIntExtra("houseId",0));
                intent.putExtra("houseName",this.getIntent().getStringExtra("houseName"));
                startActivity(intent);
                overridePendingTransition(0, 0);
            }else {
                Log.e(TAG, getString(R.string.responseErrorCode) + response.code());
                progressDialog.dismiss();
            }
        }
    }

    @Override
    public void onFailure(Call<String> call, Throwable t) {
        Toast.makeText(this, t.toString(), Toast.LENGTH_LONG).show();
        Log.e(TAG,t.getMessage());
        progressDialog.dismiss();
    }
}