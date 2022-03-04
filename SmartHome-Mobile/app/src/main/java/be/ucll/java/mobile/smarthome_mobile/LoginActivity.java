package be.ucll.java.mobile.smarthome_mobile;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import be.ucll.java.mobile.smarthome_mobile.api.Connection;
import be.ucll.java.mobile.smarthome_mobile.api.user.UserApiInterface;
import be.ucll.java.mobile.smarthome_mobile.pojo.User;
import be.ucll.java.mobile.smarthome_mobile.util.AuthorizationManager;
import be.ucll.java.mobile.smarthome_mobile.util.TxtValidator;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static be.ucll.java.mobile.smarthome_mobile.util.TxtValidator.validate;

public class LoginActivity extends AppCompatActivity implements Callback<String> {
    private final String TAG = this.getClass().getSimpleName();
    private Button logIn;
    private EditText username, password;
    private ProgressDialog progressDialog;
    private final AuthorizationManager authorizationManager= AuthorizationManager.getInstance(LoginActivity.this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // init the EditText and Button
        username = findViewById(R.id.txtLogInUsername);
        password = findViewById(R.id.txtLogInPassword);
        logIn = findViewById(R.id.btnLogInForm);

        // implement setOnClickListener event on sign up Button
        logIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // validate the fields and call sign method to implement the api
                if (validate(username) && validate(password)) {
                    signIn();
                }
            }
        });
    }

    private void signIn() {
        // display a progress dialog
        progressDialog = new ProgressDialog(LoginActivity.this);
        progressDialog.setCancelable(false); // set cancelable to false
        progressDialog.setMessage("Please Wait"); // set message
        progressDialog.show(); // show progress dialog
        try {
            String usernameString = username.getText().toString().trim();
            String passwordString = password.getText().toString().trim();
            authorizationManager.setTempUser(new User(0,usernameString
                    , "name", "dummy",
                    "dummy@gmail.com",
                    "*****"));
            startRequest(usernameString,passwordString);

        }catch (Exception e){
            Log.e(TAG,e.getMessage());
        }
    }

    private void startRequest(@NonNull String usernameString, @NonNull String passwordString) {
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Connection.getUrl())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        UserApiInterface userApi = retrofit.create(UserApiInterface.class);

        Call<String> call = userApi.login(usernameString,passwordString);
        call.enqueue(this);
    }


    private void initialiseNavigation () {
        //initialise navigation variable
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigation);

        //set selected
        bottomNavigationView.setSelectedItemId(R.id.navManage);

        //perform ItemSelectedListener
        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            switch (item.getItemId()) {

                case R.id.navConsumption:
                    startActivity(new Intent(getApplicationContext(), ConsumptionActivity.class));
                    overridePendingTransition(0, 0);
                    return true;

                case R.id.navManage:
                    startActivity(new Intent(getApplicationContext(), ManageActivity.class));
                    overridePendingTransition(0, 0);
                    return true;

                case R.id.navHouses:
                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                    overridePendingTransition(0, 0);
                    return true;

            }
            return false;
        });
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
        Toast.makeText(LoginActivity.this, t.toString(), Toast.LENGTH_LONG).show();
        Log.e(TAG,t.getMessage());
        progressDialog.dismiss();
    }
}