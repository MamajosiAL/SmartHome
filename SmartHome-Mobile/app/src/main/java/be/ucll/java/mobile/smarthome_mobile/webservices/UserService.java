package be.ucll.java.mobile.smarthome_mobile.webservices;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

import org.json.JSONObject;

import be.ucll.java.mobile.smarthome_mobile.HousesActivity;
import be.ucll.java.mobile.smarthome_mobile.model.UserPOJO;

public class UserService implements Response.Listener, Response.ErrorListener {
    private static final String TAG = "UserService";
    private static final String SMARTHOME_API_URL_USER_PREFIX = "http://localhost:8080/users";
    private RequestQueue queue;
    private HousesActivity activity;

    public UserService(HousesActivity housesActivity){
        this.activity = housesActivity;
    }

    public void getUsers(){
        // Instantiate the RequestQueue for asynchronous operations
        queue = Volley.newRequestQueue(activity);

        String url = SMARTHOME_API_URL_USER_PREFIX;
        Log.d(TAG, "URL: " + url);
    }

    /**
     * Callback method that an error has been occurred with the provided error code and optional
     * user-readable message.
     *
     * @param error
     */
    @Override
    public void onErrorResponse(VolleyError error) {
        // This is when the call upon the web service remains unanswered or in error
        Toast.makeText(activity, error.getMessage(), Toast.LENGTH_SHORT).show();
        Log.e(TAG, error.getMessage());
    }

    /**
     * Called when a response is received.
     *
     * @param response
     */
    @Override
    public void onResponse(Object response) {
        // Cast into Gson JSONObject
        JSONObject jsonObject = (JSONObject) response;

        // Log the output as debug information
        Log.d(TAG, jsonObject.toString());

        // Convert REST String to Pojo's using GSON libraries
        UserPOJO respo = new Gson().fromJson(jsonObject.toString(), UserPOJO.class);

    }
}
