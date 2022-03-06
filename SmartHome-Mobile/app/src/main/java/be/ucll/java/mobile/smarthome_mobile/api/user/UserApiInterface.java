package be.ucll.java.mobile.smarthome_mobile.api.user;

import be.ucll.java.mobile.smarthome_mobile.pojo.Login;
import be.ucll.java.mobile.smarthome_mobile.pojo.User;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Query;


public interface UserApiInterface {
    // For POST request
    @POST("/users")
    Call<String> postUser(@Body User user);

    // for GET request
    @GET("/users")
    Call<User> getUserById(@Query("id") Integer id);

    @POST("/login")
    Call<String> login(@Body Login credentials , @Header("Authorization") String authHeader);


}
