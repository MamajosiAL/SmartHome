package be.ucll.java.mobile.smarthome_mobile.api.device;

import java.util.List;

import be.ucll.java.mobile.smarthome_mobile.pojo.Device;
import be.ucll.java.mobile.smarthome_mobile.pojo.House;
import be.ucll.java.mobile.smarthome_mobile.pojo.Room;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface DeviceApiInterface {

    // for GET request
    @GET("/rooms/house/{id}")
    Call<List<Room>> getDevicesInRoomFromHouseWithAccessForUserInSession(@Path("id") Integer houseid, @Header("Cookie") String authHeader);

    @POST("/devices/create")
    Call<String> createDeviceInRoom(@Body Device device, @Header("Cookie") String authHeader);

    @PUT("/devices/update")
    Call<String> updateDeviceInRoom(@Body Device device, @Header("Cookie") String authHeader);
}
