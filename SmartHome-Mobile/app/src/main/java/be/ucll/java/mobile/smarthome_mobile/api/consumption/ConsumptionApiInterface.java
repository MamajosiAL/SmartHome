package be.ucll.java.mobile.smarthome_mobile.api.consumption;

import java.util.List;

import be.ucll.java.mobile.smarthome_mobile.pojo.Consumption;
import be.ucll.java.mobile.smarthome_mobile.pojo.ConsumptionLog;
import be.ucll.java.mobile.smarthome_mobile.pojo.Device;
import be.ucll.java.mobile.smarthome_mobile.pojo.DeviceAllParams;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface ConsumptionApiInterface {
    @GET("/consumptions/device/{id}")
    Call<List<Consumption>> getConsumptionsByDevice(@Path("id") Integer deviceId, @Header("Cookie") String authHeader);

    @GET("/consumptions/room/{id}")
    Call<List<Consumption>> getConsumptionsByRoom(@Path("id") Integer roomId, @Header("Cookie") String authHeader);

    @GET("/consumptions/house/{id}")
    Call<List<Consumption>> getConsumptionsByHouse(@Path("id") Integer houseId, @Header("Cookie") String authHeader);

    @GET("/consumptions/user")
    Call<List<Consumption>> getConsumptionsByuser(@Header("Cookie") String authHeader);

    @GET("/consumptionlogs/device/{id}")
    Call<List<ConsumptionLog>> getConsumptionlogsByDevice(@Path("id") Integer deviceId, @Header("Cookie") String authHeader);

    @GET("/consumptionlogs/room/{id}")
    Call<List<ConsumptionLog>> getConsumptionlogsByRoom(@Path("id") Integer roomId, @Header("Cookie") String authHeader);

    @GET("/consumptionlogs/house/{id}")
    Call<List<ConsumptionLog>> getConsumptionlogsByHouse(@Path("id") Integer houseId, @Header("Cookie") String authHeader);

    @GET("/consumptionlogs/user")
    Call<List<ConsumptionLog>> getConsumptionlogsByuser(@Header("Cookie") String authHeader);

}
