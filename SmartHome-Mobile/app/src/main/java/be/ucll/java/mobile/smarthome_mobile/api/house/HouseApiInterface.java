package be.ucll.java.mobile.smarthome_mobile.api.house;

import java.util.List;

import be.ucll.java.mobile.smarthome_mobile.pojo.House;
import be.ucll.java.mobile.smarthome_mobile.pojo.House_User;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface HouseApiInterface {
    // For POST request
    @POST("/houses/create")
    Call<String> addHouse(@Body House house, @Header("Cookie") String authHeader);

    // for GET request
    @GET("/houses/user")
    Call<List<House>> getHousesWithAccessForUserInSession(@Header("Cookie") String authHeader);

    @POST("/register")
    Call<String> addUserToHouseByUsername(@Body House house, @Query("username") String username, @Header("Cookie") String authHeader);

    @PUT("/houses/setadmin")
    Call<String> setUserAsAdminInHouse(@Body House_User house_user, @Header("Cookie") String authHeader);
}
