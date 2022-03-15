package be.ucll.java.mobile.smarthome_mobile.api.device;

import android.app.ProgressDialog;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.net.CookieHandler;
import java.net.CookieManager;
import java.util.concurrent.TimeUnit;

import be.ucll.java.mobile.smarthome_mobile.DeviceActivity;
import be.ucll.java.mobile.smarthome_mobile.R;
import be.ucll.java.mobile.smarthome_mobile.RoomActivity;
import be.ucll.java.mobile.smarthome_mobile.api.Connection;
import be.ucll.java.mobile.smarthome_mobile.exception.DataNotFoundException;
import be.ucll.java.mobile.smarthome_mobile.pojo.BigElectro;
import be.ucll.java.mobile.smarthome_mobile.pojo.Device;
import be.ucll.java.mobile.smarthome_mobile.pojo.DeviceAllParams;
import be.ucll.java.mobile.smarthome_mobile.pojo.Media;
import be.ucll.java.mobile.smarthome_mobile.pojo.Sensor;
import be.ucll.java.mobile.smarthome_mobile.util.AuthorizationManager;
import be.ucll.java.mobile.smarthome_mobile.util.DeviceCategory;
import be.ucll.java.mobile.smarthome_mobile.util.ReceivedCookiesInterceptor;
import okhttp3.JavaNetCookieJar;
import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

@SuppressWarnings("ALL")
public class DeviceNameEditor implements Callback<String> {
    private final String TAG = this.getClass().getSimpleName();
    private DeviceActivity context;
    private ProgressDialog progressDialog;

    public DeviceNameEditor(DeviceActivity context) {
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

    private void startRequest(DeviceAllParams device, DeviceCategory category) {
        DeviceApiInterface deviceApi = getClient().create(DeviceApiInterface.class);
        Call<String> call;
        if (category ==DeviceCategory.BIG_ELECTRO) {
            call = deviceApi.updateBigElektroInRoom(device, AuthorizationManager.getInstance(context).getSessionId());
        } else if (category ==DeviceCategory.MEDIA){
            call = deviceApi.updateMediaInRoom(device, AuthorizationManager.getInstance(context).getSessionId());
        } else if (category ==DeviceCategory.GENERIC) {
            call = deviceApi.updateDeviceInRoom(device, AuthorizationManager.getInstance(context).getSessionId());
        } else if (category ==DeviceCategory.SENSOR) {
            call = deviceApi.updateSensorInRoom(device, AuthorizationManager.getInstance(context).getSessionId());
        }else {
            throw new DataNotFoundException("selected item is not valid or empty");
        }

        call.enqueue(this);
    }

    public void editName(@NonNull DeviceAllParams device, @NonNull String deviceCategory) {
        if(DeviceCategory.matchedAny(deviceCategory)){
            if(device.getRoomid()<=0) throw new IllegalArgumentException("devicehas no valid roomId");
            startRequest(device, DeviceCategory.getCategoryFromName(deviceCategory));
        }else {
            throw new IllegalArgumentException("deviceCategory string is invalid or empty");
        }
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
                context.getDeviceData();
                Toast.makeText(context, context.getText(R.string.deviceNameChangeSucces), Toast.LENGTH_LONG).show();
            }else {
                Log.e(TAG, context.getString(R.string.responseErrorCode) + response.code());
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
        Toast.makeText(context, t.toString(), Toast.LENGTH_LONG).show();
        Log.e(TAG,t.getMessage());
        progressDialog.dismiss();
    }


}
