package be.ucll.java.mobile.smarthome_mobile.api;

import be.ucll.java.mobile.smarthome_mobile.api.house.HouseApiInterface;
import retrofit.RestAdapter;

public class Api {
    public static HouseApiInterface getClient() {

        // change your base URL
        RestAdapter adapter = new RestAdapter.Builder()
                .setEndpoint("http://192.168.68.110:8080/") //Set the Root URL
                .build(); //Finally building the adapter

        //Creating object for our interface
        HouseApiInterface api = adapter.create(HouseApiInterface.class);
        return api; // return the APIInterface object
    }
}
