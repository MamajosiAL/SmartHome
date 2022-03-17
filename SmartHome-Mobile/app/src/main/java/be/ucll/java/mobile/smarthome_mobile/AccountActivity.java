package be.ucll.java.mobile.smarthome_mobile;

import android.app.ProgressDialog;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.List;

import be.ucll.java.mobile.smarthome_mobile.api.Connection;
import be.ucll.java.mobile.smarthome_mobile.api.user.UserApiInterface;
import be.ucll.java.mobile.smarthome_mobile.exception.DataNotFoundException;
import be.ucll.java.mobile.smarthome_mobile.pojo.User;
import be.ucll.java.mobile.smarthome_mobile.util.AuthorizationManager;
import be.ucll.java.mobile.smarthome_mobile.util.NavigationManager;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class AccountActivity extends AppCompatActivity implements Callback<User> {
    private final String TAG = this.getClass().getSimpleName();
    private TextView username,firstname,name,email;

    public void getCurrentUserData() {

        try {
            Gson gson = new GsonBuilder()
                    .setLenient()
                    .create();

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(Connection.getUrl())
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build();

            UserApiInterface userApi = retrofit.create(UserApiInterface.class);

            Call<User> call = userApi.getUser(AuthorizationManager.getInstance(this).getSessionId());
            call.enqueue(this);
        }catch (Exception e){
            throw new DataNotFoundException(e.getCause());
        }
    }

    private void fillViews(User userdata) {
        username.setText(userdata.getUsername());
        firstname.setText(userdata.getFirstname());
        name.setText(userdata.getName());
        email.setText(userdata.getEmail());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);

        NavigationManager.initialise(this);

        //set views
        username = findViewById(R.id.txtAccountUsername);
        firstname = findViewById(R.id.txtuser_firstname);
        name = findViewById(R.id.txtuser_name);
        email = findViewById(R.id.txtuser_email);

        try {
            getCurrentUserData();
        }catch (Exception e){
            Log.e(TAG, e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public void onResponse(Call<User> call, Response<User> response) {
        if (response != null && response.body() != null) {
            if (response.isSuccessful()) {
                fillViews(response.body());
            }else {
                Log.e(TAG, getString(R.string.responseErrorCode) + response.code());

            }
        }else{
            Log.e(TAG, getString(R.string.responseErrorCode) + response.code());
        }
    }

    @Override
    public void onFailure(Call<User> call, Throwable t) {
        Toast.makeText(this, t.toString(), Toast.LENGTH_LONG).show();
        Log.e(TAG,t.getMessage());

    }
}