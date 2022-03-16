package be.ucll.java.mobile.smarthome_mobile.api.user;

import java.util.List;

import be.ucll.java.mobile.smarthome_mobile.pojo.Login;
import be.ucll.java.mobile.smarthome_mobile.pojo.User;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;


public interface UserApiInterface {
    // For POST request
    @POST("/users/create")
    Call<String> postUser(@Body User user);

    @POST("/login")
    Call<String> login(@Body Login credentials , @Header("Authorization") String authHeader);

    @PUT("/users/update")
    Call<String> updateUser(@Body User user, @Header("Cookie") String authHeader);

    @GET("/users/user")
    Call<String> getUser(@Header("Cookie") String authHeader);

    @GET("/users/house/{id}")
    Call<List<User>> getUsersByHouseId(@Path("id") Integer houseid, @Header("Cookie") String authHeader);

    // for GET request
    @GET("/users")
    Call<User> getUserById(@Query("id") Integer id);

    @DELETE("users/delete")
    Call<User> deleteCurrentLoggedInUser(@Header("Cookie") String authHeader);
}
