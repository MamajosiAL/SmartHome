package be.ucll.java.mobile.smarthome_mobile;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.List;

import be.ucll.java.mobile.smarthome_mobile.api.Connection;
import be.ucll.java.mobile.smarthome_mobile.api.room.RoomsAdapter;
import be.ucll.java.mobile.smarthome_mobile.api.room.RoomsApiInterface;
import be.ucll.java.mobile.smarthome_mobile.api.user.UserApiInterface;
import be.ucll.java.mobile.smarthome_mobile.api.user.UsersAdapter;
import be.ucll.java.mobile.smarthome_mobile.exception.DataNotFoundException;
import be.ucll.java.mobile.smarthome_mobile.pojo.Room;
import be.ucll.java.mobile.smarthome_mobile.pojo.User;
import be.ucll.java.mobile.smarthome_mobile.util.AuthorizationManager;
import be.ucll.java.mobile.smarthome_mobile.util.NavigationManager;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

@SuppressWarnings("deprecation")
public class UserInHouseActivity extends AppCompatActivity implements Callback<List<User>> {
    private final String TAG = this.getClass().getSimpleName();
    private RecyclerView recyclerViewUsers;
    private ProgressDialog progressDialog;
    private TextView title;
    List<User> usersFromHouse;


    public void getUsersInHouseData() {
        progressDialog = new ProgressDialog(UserInHouseActivity.this);
        progressDialog.create();
        try {
            Gson gson = new GsonBuilder()
                    .setLenient()
                    .create();

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(Connection.getUrl())
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build();

            UserApiInterface userApi = retrofit.create(UserApiInterface.class);
            //fetch houseId
            int houseId = this.getIntent().getIntExtra("houseId",0);

            //check if houseId is not null
            if(houseId==0){
                throw new NullPointerException("The id of the selected house is 0");
            }

            Call<List<User>> call = userApi.getUsersByHouseId(houseId, AuthorizationManager.getInstance(this).getSessionId());
            call.enqueue(this);
        }catch (Exception e){
            throw new DataNotFoundException(e.getCause());
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users_in_house);

        NavigationManager.initialise(this);

        title = findViewById(R.id.titleUsersInHouse);
        usersFromHouse = new ArrayList<>();

        if (AuthorizationManager.getInstance(this).isSignedIn()){
            recyclerViewUsers = findViewById(R.id.recyclerViewUsers);
            title.setText(this.getIntent().getStringExtra("houseName"));
            try {

                FloatingActionButton fab = findViewById(R.id.fabAddUserToHouse);
                fab.setOnClickListener(view -> {
                    Intent intent = new Intent(this, AddUserToHouseActivity.class);
                    intent.putExtra("houseId", this.getIntent().getIntExtra("houseId",0));
                    intent.putExtra("houseName", this.getIntent().getStringExtra("houseName"));
                    startActivity(intent);
                    overridePendingTransition(0, 0);
                });
                getUsersInHouseData();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    private void setDataInRecyclerView() {
        // set a LinearLayoutManager with default vertical orientation
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerViewUsers.setLayoutManager(linearLayoutManager);
        // call the constructor of housesAdapter to send the reference and data to Adapter
        UsersAdapter usersAdapter = new UsersAdapter(this,usersFromHouse);
        recyclerViewUsers.setAdapter(usersAdapter); // set the Adapter to RecyclerView
    }

    @Override
    public void onResponse(Call<List<User>> call, Response<List<User>> response) {
        if (response != null && response.body() != null) {
            if (response.isSuccessful()) {
                Log.e(TAG, "onResponse: " + response.body().get(0).getName());
                usersFromHouse = response.body();
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
    public void onFailure(Call<List<User>> call, Throwable t) {
        Toast.makeText(UserInHouseActivity.this, t.toString(), Toast.LENGTH_LONG).show();
        Log.e(TAG,t.getMessage());
        progressDialog.dismiss();
    }
}
