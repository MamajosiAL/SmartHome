package be.ucll.java.mobile.smarthome_mobile;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import be.ucll.java.mobile.smarthome_mobile.model.HousePOJO;
import be.ucll.java.mobile.smarthome_mobile.model.UserPOJO;

public class HousesFragment extends Fragment implements Response.Listener<JsonArray>, Response.ErrorListener{

    private static final String TAG = "HousesFragment";
    private static final String SMARTHOME_API_URL_HOUSE_PREFIX = "http://10.0.2.2:8080/houses";
    private RequestQueue queue;

    private List<HousePOJO> houses;

    private ListView listHouses;
    public HousesFragment() {
        houses = new ArrayList<>();
    }

    public void getHouses(){
        // Instantiate the RequestQueue for asynchronous operations
        queue = Volley.newRequestQueue(getContext());
        String url = SMARTHOME_API_URL_HOUSE_PREFIX;
        Log.d(TAG, "URL: " + url);

        // Prepare the request to be send out towards the REST service
        JsonArrayRequest req = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                Gson gson = new Gson();
                TypeToken<List<HousePOJO>> token = new TypeToken<List<HousePOJO>>(){};
                houses = gson.fromJson(String.valueOf(response), token.getType());
                // Log the output as debug information
                Log.d(TAG, String.valueOf(houses.size())+" houses in response");

                //TODO

                // Create an ArrayAdapter from List
                final ArrayAdapter<HousePOJO> arrayAdapter = new ArrayAdapter<HousePOJO>
                        (getContext(), android.R.layout.simple_list_item_1, houses);

                // DataBind ListView with items from ArrayAdapter
                listHouses.setAdapter(arrayAdapter);

                arrayAdapter.notifyDataSetChanged();

            }
        }, this);

        // Add the request to the RequestQueue for asynchronous retrieval on separate thread.
        queue.add(req);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        listHouses = container.findViewById(R.id.list_houses);
        getHouses();



        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_houses, container, false);
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
        Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
        Log.e(TAG, error.getMessage());
    }


    /**
     * Called when a response is received.
     *
     * @param response
     */
    @Override
    public void onResponse(JsonArray response) {
        Gson gson = new Gson();
        TypeToken<List<HousePOJO>> token = new TypeToken<List<HousePOJO>>(){};
        houses = gson.fromJson(response, token.getType());

        // Log the output as debug information
        Log.d(TAG, String.valueOf(houses.size())+" houses in response (2)");
    }
}