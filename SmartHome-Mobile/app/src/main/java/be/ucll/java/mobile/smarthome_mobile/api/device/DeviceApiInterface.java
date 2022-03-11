package be.ucll.java.mobile.smarthome_mobile.api.device;

import java.util.List;

import be.ucll.java.mobile.smarthome_mobile.pojo.BigElectro;
import be.ucll.java.mobile.smarthome_mobile.pojo.Device;

import be.ucll.java.mobile.smarthome_mobile.pojo.Media;
import be.ucll.java.mobile.smarthome_mobile.pojo.Sensor;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface DeviceApiInterface {

    // GENERIC DEVICES

    @GET("/devices/room/{id}")
    Call<List<Device>> getDevicesInRoomFromHouseWithAccessForUserInSession(@Path("id") Integer roomid, @Header("Cookie") String authHeader);

    @POST("/devices/create")
    Call<String> createDeviceInRoom(@Body Device device, @Header("Cookie") String authHeader);

    @PUT("/devices/update")
    Call<String> updateDeviceInRoom(@Body Device device, @Header("Cookie") String authHeader);

    @GET("/devices/{id}")
    Call<Device> getDeviceById(@Path("id") Integer deviceid, @Header("Cookie") String authHeader);

    @DELETE("/devices/{id}/deleteDevice")
    Call<String> deleteDeviceFromHouse(@Path("id") Integer deviceid, @Header("Cookie") String authHeader);

    @PUT("/devices/{id}/changestatus")
    Call<String> changeDeviceStatus(@Path("id") Integer deviceid, @Header("Cookie") String authHeader);

    // BIG ELECTRO

    @GET("/appliances/room/{id}")
    Call<List<Device>> getBigElektroInRoomFromHouseWithAccessForUserInSession(@Path("id") Integer roomid, @Header("Cookie") String authHeader);

    @POST("/appliances/create")
    Call<String> createBigElektroInRoom(@Body BigElectro bigElectro, @Header("Cookie") String authHeader);

    @PUT("/appliances/update")
    Call<String> updateBigElektroInRoom(@Body BigElectro bigElectro, @Header("Cookie") String authHeader);

    @GET("/appliances/{id}")
    Call<BigElectro> getBigElektroById(@Path("id") Integer bigElectroid, @Header("Cookie") String authHeader);

    @DELETE("/appliances/{id}/delete")
    Call<String> deleteBigElektroFromHouse(@Path("id") Integer bigElectroid, @Header("Cookie") String authHeader);

    @PUT("/appliances/{id}/changestatus")
    Call<String> changeBigElektroStatus(@Path("id") Integer bigElectroid, @Header("Cookie") String authHeader);

    // MEDIA

    @GET("/audios/room/{id}")
    Call<List<Device>> getMediaInRoomFromHouseWithAccessForUserInSession(@Path("id") Integer roomid, @Header("Cookie") String authHeader);

    @POST("/audios/create")
    Call<String> createMediaInRoom(@Body Media media, @Header("Cookie") String authHeader);

    @PUT("/audios/update")
    Call<String> updateMediaInRoom(@Body Media media, @Header("Cookie") String authHeader);

    @GET("/audios/{id}")
    Call<Media> getMediaById(@Path("id") Integer mediaid, @Header("Cookie") String authHeader);

    @DELETE("/audios/{id}/delete")
    Call<String> deleteMediaFromHouse(@Path("id") Integer mediaid, @Header("Cookie") String authHeader);

    @PUT("/audios/{id}/changestatus")
    Call<String> changeMediaStatus(@Path("id") Integer mediaid, @Header("Cookie") String authHeader);

    // SENSOR

    @GET("/sensors/room/{id}")
    Call<List<Device>> getSensorInRoomFromHouseWithAccessForUserInSession(@Path("id") Integer roomid, @Header("Cookie") String authHeader);

    @POST("/sensors/create")
    Call<String> createSensorInRoom(@Body Sensor sensor, @Header("Cookie") String authHeader);

    @PUT("/sensors/update")
    Call<String> updateSensorInRoom(@Body Sensor sensor, @Header("Cookie") String authHeader);

    @GET("/sensors/{id}")
    Call<Sensor> getSensorById(@Path("id") Integer sensorid, @Header("Cookie") String authHeader);

    @DELETE("/sensors/{id}/delete")
    Call<String> deleteSensorFromHouse(@Path("id") Integer sensorid, @Header("Cookie") String authHeader);

    @PUT("/sensors/{id}/changestatus")
    Call<String> changeSensorStatus(@Path("id") Integer sensorid, @Header("Cookie") String authHeader);

}
