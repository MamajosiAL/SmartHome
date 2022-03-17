package be.ucll.java.mobile.smarthome_mobile.api.house;

import java.util.List;

import be.ucll.java.mobile.smarthome_mobile.pojo.House;
import be.ucll.java.mobile.smarthome_mobile.pojo.House_User;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
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

    // POST register a user to a house
    @POST("/houses/register")
    Call<String> addUserToHouseByUsername(@Body House house, @Header("Cookie") String authHeader);

    // set a user as admin of house
    @PUT("/houses/setadmin")
    Call<String> setUserAsAdminInHouse(@Body House_User house_user, @Header("Cookie") String authHeader);

    // UPDATE house (name)
    @PUT("/houses/update")
    Call<String> updateHouse(@Body House house, @Header("Cookie") String authHeader);

    // for GET request
    @GET("/houses/user")
    Call<List<House>> getHousesWithAccessForUserInSession( @Header("Cookie") String authHeader);

    // GET house by id
    @GET("/houses/{id}")
    Call<House> getHouseById(@Path("id") Integer houseid,@Header("Cookie") String authHeader);

    // DELETE house
    @DELETE("/houses/{id}/delete")
    Call<String> deleteHouse(@Path("id") Integer houseid, @Header("Cookie") String authHeader);

    // DELETE user from house
    @DELETE("/houses/{houseid}/user/{userid}")
    Call<String> removeUserFromHouse(@Path("houseid") Integer houseid, @Path("userid") Integer userid, @Header("Cookie") String authHeader);
}
