package be.ucll.java.mobile.smarthome_mobile.webservices;

import android.util.Log;
import android.widget.ArrayAdapter;

import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.List;

import be.ucll.java.mobile.smarthome_mobile.HousesActivity;
import be.ucll.java.mobile.smarthome_mobile.model.HousePOJO;
import be.ucll.java.mobile.smarthome_mobile.recyclerview.houses.HousesAdapter;

public class HouseService implements Response.Listener, Response.ErrorListener {
    private static final String TAG = "HousesService";
    private RequestQueue queue;
    private HousesActivity houseActivity;
    private RecyclerView listHouses;
    private HousesAdapter adapter;

    public HouseService(HousesActivity houseActivity, RecyclerView listHouses, HousesAdapter adapter) {
        this.houseActivity = houseActivity;
        this.listHouses = listHouses;
        this.adapter = adapter;
    }

    /**
     * Callback method that an error has been occurred with the provided error code and optional
     * user-readable message.
     *
     * @param error
     */
    @Override
    public void onErrorResponse(VolleyError error) {

    }

    /**
     * Called when a response is received.
     *
     * @param response
     */
    @Override
    public void onResponse(Object response) {
        Gson gson = new Gson();
        TypeToken<List<HousePOJO>> token = new TypeToken<List<HousePOJO>>(){};
        List<HousePOJO> houses = gson.fromJson(String.valueOf(response), token.getType());
        // Log the output as debug information
        Log.d(TAG, String.valueOf(houses.size())+" houses in response");

        //TODO

        // Create an ArrayAdapter from List
        final ArrayAdapter<HousePOJO> arrayAdapter = new ArrayAdapter<HousePOJO>
                (houseActivity, android.R.layout.simple_list_item_1, houses);

        // DataBind ListView with items from ArrayAdapter
        listHouses.setAdapter(adapter);

        arrayAdapter.notifyDataSetChanged();
    }
}
