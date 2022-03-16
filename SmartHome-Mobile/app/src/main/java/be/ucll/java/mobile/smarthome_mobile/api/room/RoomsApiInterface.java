package be.ucll.java.mobile.smarthome_mobile.api.room;

import java.util.List;

import be.ucll.java.mobile.smarthome_mobile.R;
import be.ucll.java.mobile.smarthome_mobile.pojo.House;
import be.ucll.java.mobile.smarthome_mobile.pojo.Room;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface RoomsApiInterface {
    // For POST request
    @POST("/rooms/create")
    Call<String> addRoomToHouse(@Body Room roomForHouse, @Header("Cookie") String authHeader);

    @PUT("/rooms/update")
    Call<String> updateRoom(@Body Room updatedRoom, @Header("Cookie") String authHeader);

    // for GET request
    @GET("/rooms/house/{id}")
    Call<List<Room>> getRoomsFromHouseWithAccessForUserInSession(@Path("id") Integer houseid, @Header("Cookie") String authHeader);

    @GET("/rooms")
    Call<Room> getRoomById(@Query("id") Integer roomid, @Header("Cookie") String authHeader);

    @DELETE("/rooms/{id}/delete")
    Call<String> deleteRoomById(@Path("id") Integer roomid, @Header("Cookie") String authHeader);
}

