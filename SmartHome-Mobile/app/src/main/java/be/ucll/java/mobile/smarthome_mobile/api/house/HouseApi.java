package be.ucll.java.mobile.smarthome_mobile.api.house;

import be.ucll.java.mobile.smarthome_mobile.api.Connection;
import retrofit.RestAdapter;

public class HouseApi {
    public static HouseApiInterface getClient() {

        // change your base URL
        RestAdapter adapter = new RestAdapter.Builder()
                .setEndpoint(Connection.getUrl()) //Set the Root URL
                .build(); //Finally building the adapter

        //Creating object for our interface
        HouseApiInterface api = adapter.create(HouseApiInterface.class);
        return api; // return the APIInterface object
    }
}
