package be.ucll.java.mobile.smarthome_mobile.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ConsumptionLog {

    @SerializedName("consumptionLogId")
    @Expose
    private Integer consumptionLogId;

    @SerializedName("consumptionId")
    @Expose
    private Integer consumptionId;

    @SerializedName("deviceId")
    @Expose
    private Integer deviceId;

    @SerializedName("unit")
    @Expose
    private String unit;

    @SerializedName("houseId")
    @Expose
    private Integer houseId;

    @SerializedName("roomId")
    @Expose
    private Integer roomId;

    @SerializedName("date")
    @Expose
    private String date;

    @SerializedName("totalConsumption")
    @Expose
    private Integer totalConsumption;

    @SerializedName("houseName")
    @Expose
    private String houseName;

    @SerializedName("roomName")
    @Expose
    private String roomName;


    public Integer getConsumptionLogId() {
        return consumptionLogId;
    }

    public void setConsumptionLogId(Integer consumptionLogId) {
        this.consumptionLogId = consumptionLogId;
    }

    public Integer getConsumptionId() {
        return consumptionId;
    }

    public void setConsumptionId(Integer consumptionId) {
        this.consumptionId = consumptionId;
    }

    public Integer getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(Integer deviceId) {
        this.deviceId = deviceId;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public Integer getHouseId() {
        return houseId;
    }

    public void setHouseId(Integer houseId) {
        this.houseId = houseId;
    }

    public Integer getRoomId() {
        return roomId;
    }

    public void setRoomId(Integer roomId) {
        this.roomId = roomId;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Integer getTotalConsumption() {
        return totalConsumption;
    }

    public void setTotalConsumption(Integer totalConsumption) {
        this.totalConsumption = totalConsumption;
    }

    public String getHouseName() {
        return houseName;
    }

    public void setHouseName(String houseName) {
        this.houseName = houseName;
    }

    public String getRoomName() {
        return roomName;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }
}
