package be.ucll.java.mobile.smarthome_mobile.api.room;

import java.util.List;

import be.ucll.java.mobile.smarthome_mobile.pojo.House;
import be.ucll.java.mobile.smarthome_mobile.pojo.Room;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface RoomsApiInterface {
    // For POST request
    @POST("/rooms/create")
    Call<String> addRoomToHouse(@Body Room roomForHouse, @Header("Cookie") String authHeader);

    // for GET request
    @GET("/rooms/house")
    Call<List<Room>> getRoomsFromHouseWithAccessForUserInSession(@Query("id") Integer houseid, @Header("Cookie") String authHeader);


}
