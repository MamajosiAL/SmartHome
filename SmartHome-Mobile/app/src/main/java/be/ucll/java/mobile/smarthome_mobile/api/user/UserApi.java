package be.ucll.java.mobile.smarthome_mobile.api.user;

import be.ucll.java.mobile.smarthome_mobile.api.Connection;
import be.ucll.java.mobile.smarthome_mobile.api.house.HouseApiInterface;
import retrofit2.Retrofit;

public class UserApi {
    public static HouseApiInterface getClient() {

        // change your base URL
        Retrofit adapter = new Retrofit.Builder()
                .baseUrl(Connection.getUrl()) //Set the Root URL
                .build(); //Finally building the adapter

        //Creating object for our interface
        HouseApiInterface api = adapter.create(HouseApiInterface.class);
        return api; // return the APIInterface object
    }
}
