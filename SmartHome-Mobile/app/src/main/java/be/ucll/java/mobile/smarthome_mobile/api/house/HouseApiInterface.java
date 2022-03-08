package be.ucll.java.mobile.smarthome_mobile.api.house;

import java.util.List;

import be.ucll.java.mobile.smarthome_mobile.pojo.House;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface HouseApiInterface {
    // For POST request
    @POST("/houses/create")
    Call<String> addHouse(@Body House house, @Header("Cookie") String authHeader);

    // for GET request
    @GET("/houses/user")
    Call<List<House>> getHousesWithAccessForUserInSession(@Header("Cookie") String authHeader);

}
