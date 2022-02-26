package be.ucll.java.mobile.smarthome_mobile;

import java.util.List;

import be.ucll.java.mobile.smarthome_mobile.pojo.House;
import retrofit.Callback;
import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.GET;
import retrofit.http.POST;

public interface ApiInterface {


// For POST request

    @FormUrlEncoded    // annotation that used with POST type request
    @POST("/houses") // specify the sub url for our base url
    public void addHouse(
            @Field("name") String name);
            //@Field("user_pass") String user_pass, Callback<House> callback);
            //name and user_pass are the post parameters and SignUpResponse is a POJO class which recieves the response of this API


// for GET request

    @GET("/houses") // specify the sub url for our base url
    public void getHouseList(Callback<List<House>> callback);
            // House is a POJO class which receives the response of this API

}
