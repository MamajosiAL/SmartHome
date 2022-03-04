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
import be.ucll.java.mobile.smarthome_mobile.util.AuthorizationManager;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static be.ucll.java.mobile.smarthome_mobile.util.TxtValidator.validate;

public class RegisterActivity extends AppCompatActivity implements Callback<String> {
    private final String TAG = "RegisterActivity";
    private EditText name, username, firstName,email, password;
    private String nameString = "leeg";
    private Button signUp;
    private ProgressDialog progressDialog;
    private final AuthorizationManager  authorizationManager= AuthorizationManager.getInstance(RegisterActivity.this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        initialiseNavigation();

        // init the EditText and Button
        username = findViewById(R.id.txtRegisterusername);
        firstName = findViewById(R.id.txtRegisterFirstName);
        name = findViewById(R.id.txtRegisterName);
        email = findViewById(R.id.txtRegisterEmail);
        password = findViewById(R.id.txtRegisterPassword);
        signUp = findViewById(R.id.btnRegister);

        // implement setOnClickListener event on sign up Button
        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // validate the fields and call sign method to implement the api
                if (validate(name) && validate(email) && validate(password)) {
                    signUp();
                }
            }
        });
    }

    private void signUp() {
        // display a progress dialog
        progressDialog = new ProgressDialog(RegisterActivity.this);
        progressDialog.setCancelable(false); // set cancelable to false
        progressDialog.setMessage("Please Wait"); // set message
        progressDialog.show(); // show progress dialog
        try {
            nameString = name.getText().toString().trim();
            authorizationManager.setTempUser(new User(0,username.getText().toString().trim(),name.getText().toString().trim(),firstName.getText().toString().trim(),
                    email.getText().toString().trim(),
                    password.getText().toString().trim()));
            startRequest(authorizationManager.getUserTemp());

        }catch (Exception e){
            Log.e(TAG,e.getMessage());
        }

    }



    private void initialiseNavigation() {
        //initialise navigation variable
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigation);
        //set selected
        bottomNavigationView.setSelectedItemId(R.id.navManage);
        //perform ItemSelectedListener
        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            switch (item.getItemId()){

                case R.id.navConsumption:
                    startActivity(new Intent(getApplicationContext(), ConsumptionActivity.class));
                    overridePendingTransition(0,0);
                    return true;

                case R.id.navManage:
                    startActivity(new Intent(getApplicationContext(), ManageActivity.class));
                    overridePendingTransition(0,0);
                    return true;

                case R.id.navHouses:
                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                    overridePendingTransition(0,0);
                    return true;

            }
            return false;
        });
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

    /**
     * Invoked for a received HTTP response.
     * <p>
     * Note: An HTTP response may still indicate an application-level failure such as a 404 or 500.
     * Call {@link Response#isSuccessful()} to determine if the response indicates success.
     *  @param call
     * @param response
     */
    @Override
    public void onResponse(Call<String> call, Response<String> response) {
            startActivity(new Intent(getApplicationContext(), ManageActivity.class));
            overridePendingTransition(0,0);
            authorizationManager.signIn(authorizationManager.getUserTemp());
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
        Toast.makeText(RegisterActivity.this, t.toString(), Toast.LENGTH_LONG).show();
        Log.e(TAG,t.getMessage());
        progressDialog.dismiss();
    }
}