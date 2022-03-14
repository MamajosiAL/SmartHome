package be.ucll.java.mobile.smarthome_mobile.api.media;

import android.app.ProgressDialog;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.net.CookieHandler;
import java.net.CookieManager;
import java.util.concurrent.TimeUnit;

import be.ucll.java.mobile.smarthome_mobile.DeviceActivity;
import be.ucll.java.mobile.smarthome_mobile.R;
import be.ucll.java.mobile.smarthome_mobile.api.Connection;
import be.ucll.java.mobile.smarthome_mobile.api.device.DeviceApiInterface;
import be.ucll.java.mobile.smarthome_mobile.pojo.Media;
import be.ucll.java.mobile.smarthome_mobile.pojo.Sensor;
import be.ucll.java.mobile.smarthome_mobile.util.AuthorizationManager;
import be.ucll.java.mobile.smarthome_mobile.util.ReceivedCookiesInterceptor;
import okhttp3.JavaNetCookieJar;
import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class VolumeChangeAdapter  implements Callback<String> {

    private final String TAG = this.getClass().getSimpleName();
    private DeviceActivity context;
    private ProgressDialog progressDialog;
    private int roomId;

    public VolumeChangeAdapter(DeviceActivity context) {
        this.context = context;
    }

    public Retrofit getClient(){
        progressDialog = new ProgressDialog(context);
        progressDialog.create();
        CookieHandler cookieHandler = new CookieManager();
        OkHttpClient client = new okhttp3.OkHttpClient.Builder().addNetworkInterceptor(new ReceivedCookiesInterceptor(context))
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

    private void startRequest(Media media) {
        Log.e(TAG, "startRequest: " + media.getRoomid());
        DeviceApiInterface deviceApi = getClient().create(DeviceApiInterface.class);
        Call<String> call = deviceApi.updateMediaInRoom(media, AuthorizationManager.getInstance(context).getSessionId());
        call.enqueue(this);
    }

    public void changeVolume(Media media) {
        if(media.getId() > 0){
            startRequest(media);
        }else {
            throw new IllegalArgumentException("id is 0 or invalid");
        }
    }

    @Override
    public void onResponse(Call<String> call, Response<String> response) {
        if (response != null && response.body() != null) {

            if (response.isSuccessful()) {
                context.getDeviceData();
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
}
