package be.ucll.java.mobile.smarthome_mobile.api.user;

import java.util.List;
import be.ucll.java.mobile.smarthome_mobile.pojo.User;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;


public interface UserApiInterface {
    // For POST request
    @POST("/users")
    Call<String> postUser(@Body User user);

    // for GET request
    @GET("/users")
    Call<User> getUserById(@Query("id") Integer id);

}
