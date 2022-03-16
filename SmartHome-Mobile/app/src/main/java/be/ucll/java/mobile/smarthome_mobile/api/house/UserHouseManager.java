package be.ucll.java.mobile.smarthome_mobile.api.house;

import android.app.ProgressDialog;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.net.CookieHandler;
import java.net.CookieManager;
import java.util.concurrent.TimeUnit;

import be.ucll.java.mobile.smarthome_mobile.R;
import be.ucll.java.mobile.smarthome_mobile.UserInHouseActivity;
import be.ucll.java.mobile.smarthome_mobile.api.Connection;
import be.ucll.java.mobile.smarthome_mobile.pojo.House_User;
import be.ucll.java.mobile.smarthome_mobile.util.AuthorizationManager;
import be.ucll.java.mobile.smarthome_mobile.util.ReceivedCookiesInterceptor;
import okhttp3.JavaNetCookieJar;
import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

@SuppressWarnings("ALL")
public class UserHouseManager implements Callback<String> {
    private final String TAG = this.getClass().getSimpleName();
    private UserInHouseActivity context;
    private ProgressDialog progressDialog;

    public UserHouseManager(UserInHouseActivity context) {
        this.context = context;
    }

    public Retrofit getClient(){
        progressDialog = new ProgressDialog(context);
        progressDialog.create();
        CookieHandler cookieHandler = new CookieManager();
        OkHttpClient client = new OkHttpClient.Builder().addNetworkInterceptor(new ReceivedCookiesInterceptor(context))
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

    private void startPromoteRequest(House_User houseAdmin) {
        HouseApiInterface houseApi = getClient().create(HouseApiInterface.class);
        Call<String> call = houseApi.setUserAsAdminInHouse(houseAdmin, AuthorizationManager.getInstance(context).getSessionId());
        call.enqueue(this);
    }

    private void startRemoveRequest(House_User houseUser) {
        HouseApiInterface houseApi = getClient().create(HouseApiInterface.class);
        Call<String> call = houseApi.removeUserFromHouse(houseUser.getHouseid(),houseUser.getUserid(), AuthorizationManager.getInstance(context).getSessionId());
        call.enqueue(this);
    }

    @Override
    public void onResponse(Call<String> call, Response<String> response) {
        if (response != null && response.body() != null) {
            if (response.isSuccessful()) {
                context.getUsersInHouseData();
                Toast.makeText(context, context.getText(R.string.userAccessChangedSucces), Toast.LENGTH_LONG).show();
            }else {
                Log.e(TAG, context.getString(R.string.responseErrorCode) + response.code());
                progressDialog.dismiss();
            }
        }
    }

    @Override
    public void onFailure(Call<String> call, Throwable t) {
        Toast.makeText(context, t.toString(), Toast.LENGTH_LONG).show();
        Log.e(TAG,t.getMessage());
        progressDialog.dismiss();
    }

    public void promoteUserToAdminForHouse(Integer userId, int houseId) {
        startPromoteRequest(convert(userId, houseId));
    }

    private House_User convert(int userId, int houseId) {
        if (userId > 0 && houseId > 0) {
            House_User houseUser = new House_User();
            houseUser.setHouseid(houseId);
            houseUser.setUserid(userId);
            houseUser.setIsadmin(true);
            return houseUser;
        } else {
            throw new IllegalArgumentException("userid or houseid is 0 or invalid");
        }
    }

    public void removeUserFromHouse(int userId, int houseId) {
        startRemoveRequest(convert(userId, houseId));
    }
}
