package be.ucll.java.mobile.smarthome_mobile;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import androidx.appcompat.app.AppCompatActivity;

import android.util.Base64;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.IOException;
import java.net.CookieHandler;
import java.net.CookieManager;
import java.util.HashSet;
import java.util.List;
import java.util.concurrent.TimeUnit;

import be.ucll.java.mobile.smarthome_mobile.api.Connection;
import be.ucll.java.mobile.smarthome_mobile.api.user.UserApiInterface;
import be.ucll.java.mobile.smarthome_mobile.pojo.Login;
import be.ucll.java.mobile.smarthome_mobile.util.AuthorizationManager;
import be.ucll.java.mobile.smarthome_mobile.util.BottomNavigationManager;
import be.ucll.java.mobile.smarthome_mobile.util.ReceivedCookiesInterceptor;
import okhttp3.Interceptor;
import okhttp3.JavaNetCookieJar;
import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static be.ucll.java.mobile.smarthome_mobile.util.TxtValidator.validate;

@SuppressWarnings("deprecation")
public class LoginActivity extends AppCompatActivity implements Callback<String> {
    private final String TAG = this.getClass().getSimpleName();
    private EditText username, password;
    private ProgressDialog progressDialog;
    private final AuthorizationManager authorizationManager= AuthorizationManager.getInstance(LoginActivity.this);

    public Retrofit getClient(){
        CookieHandler cookieHandler = new CookieManager();
        OkHttpClient client = new okhttp3.OkHttpClient.Builder().addNetworkInterceptor(new ReceivedCookiesInterceptor(this))
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
        setContentView(R.layout.activity_login);

        // init the EditText and Button
        username = findViewById(R.id.txtLogInUsername);
        password = findViewById(R.id.txtLogInPassword);
        Button logIn = findViewById(R.id.btnLogInForm);

        // implement setOnClickListener event on sign up Button
        logIn.setOnClickListener(view -> {
            // validate the fields and call sign method to implement the api
            if (validate(username) && validate(password)) {
                login();
            }
        });
        BottomNavigationManager.initialise(this);
    }

    private void login() {
        // display a progress dialog
        progressDialog = new ProgressDialog(LoginActivity.this);
        progressDialog.setCancelable(false); // set cancelable to false
        progressDialog.setMessage("Please Wait"); // set message
        progressDialog.show(); // show progress dialog
        startRequest();
    }

    private void startRequest() {
        String passwordString = "";
        String usernameString = "";
        try {
            usernameString = username.getText().toString().trim();
            passwordString = password.getText().toString().trim();
        }catch (Exception e){
            Log.e(TAG,e.getMessage());
        }

        UserApiInterface userApi = getClient().create(UserApiInterface.class);

        String base = usernameString + ":" + passwordString;

        String authHeader = "Basic " + Base64.encodeToString(base.getBytes(), Base64.NO_WRAP);

        Call<String> call = userApi.login(new Login(usernameString,passwordString), authHeader);
        call.enqueue(this);
    }

    @Override
    public void onResponse(Call<String> call, Response<String> response) {
        if (response != null && response.body() != null) {
            if (response.isSuccessful()) {
                startActivity(new Intent(getApplicationContext(), ManageActivity.class));
                overridePendingTransition(0, 0);
                String cookie = response.headers().get("Set-Cookie");
                Log.d(TAG,"Succes! Session-cookie: "+ cookie);
                authorizationManager.signIn(cookie);
            }
        }
    }


    @Override
    public void onFailure(Call<String> call, Throwable t) {
        Toast.makeText(LoginActivity.this, t.toString(), Toast.LENGTH_LONG).show();
        Log.e(TAG,t.getMessage());
        progressDialog.dismiss();
    }
}