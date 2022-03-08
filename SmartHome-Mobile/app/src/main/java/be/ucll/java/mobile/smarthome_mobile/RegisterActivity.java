package be.ucll.java.mobile.smarthome_mobile;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import be.ucll.java.mobile.smarthome_mobile.api.Connection;
import be.ucll.java.mobile.smarthome_mobile.api.user.UserApiInterface;
import be.ucll.java.mobile.smarthome_mobile.pojo.User;
import be.ucll.java.mobile.smarthome_mobile.util.BottomNavigationManager;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static be.ucll.java.mobile.smarthome_mobile.util.TxtValidator.validate;

public class RegisterActivity extends AppCompatActivity implements Callback<String> {
    private final String TAG = "RegisterActivity";
    private EditText name, username, firstName,email, password;
    private Button register;
    private ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        BottomNavigationManager.initialise(this);

        // init the EditText and Button
        username = findViewById(R.id.txtRegisterusername);
        firstName = findViewById(R.id.txtRegisterFirstName);
        name = findViewById(R.id.txtRegisterName);
        email = findViewById(R.id.txtRegisterEmail);
        password = findViewById(R.id.txtRegisterPassword);
        register = findViewById(R.id.btnRegister);

        // implement setOnClickListener event on sign up Button
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // validate the fields and call sign method to implement the api
                if (validate(name) && validate(email) && validate(password)) {
                    register();
                }
            }
        });
    }

    private void register() {
        // display a progress dialog
        progressDialog = new ProgressDialog(RegisterActivity.this);
        progressDialog.setCancelable(false); // set cancelable to false
        progressDialog.setMessage("Please Wait"); // set message
        progressDialog.show(); // show progress dialog
        try {
            startRequest(new User(0,username.getText().toString().trim(),
                                        name.getText().toString().trim(),
                                        firstName.getText().toString().trim(),
                                        email.getText().toString().trim(),
                                        password.getText().toString().trim()));

        }catch (Exception e){
            Log.e(TAG,e.getMessage());
        }

    }

    public void startRequest(User user) {
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Connection.getUrl())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        UserApiInterface userApi = retrofit.create(UserApiInterface.class);

        Call<String> call = userApi.postUser(user);
        call.enqueue(this);

    }

    @Override
    public void onResponse(Call<String> call, Response<String> response) {
            startActivity(new Intent(getApplicationContext(), LoginActivity.class));
            overridePendingTransition(0,0);
    }

    @Override
    public void onFailure(Call<String> call, Throwable t) {
        Toast.makeText(RegisterActivity.this, t.toString(), Toast.LENGTH_LONG).show();
        Log.e(TAG,t.getMessage());
        progressDialog.dismiss();
    }
}