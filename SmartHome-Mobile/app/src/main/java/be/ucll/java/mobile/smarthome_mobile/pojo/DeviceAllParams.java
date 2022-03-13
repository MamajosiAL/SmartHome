package be.ucll.java.mobile.smarthome_mobile.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Timer;

import be.ucll.java.mobile.smarthome_mobile.util.DeviceCategory;

public class DeviceAllParams {

    @SerializedName("categoryid")
    @Expose
    private Integer categoryid;

    @SerializedName("id")
    @Expose
    private Integer id;

    @SerializedName("name")
    @Expose
    private String name;

    @SerializedName("roomid")
    @Expose
    private Integer roomid;

    @SerializedName("status")
    @Expose
    private Boolean status;

    @SerializedName("volume")
    @Expose
    private Integer Volume;

    @SerializedName("sensorType")
    @Expose
    private String sensorType;

    @SerializedName("sensordata")
    @Expose
    private Integer sensordata;

    @SerializedName("type")
    @Expose
    private Type type;

    @SerializedName("programid")
    @Expose
    private Integer programid;

    @SerializedName("tempature")
    @Expose
    private Integer tempature;

    @SerializedName("timer")
    @Expose
    private String timer;

    public Integer getCategoryid() {
        return categoryid;
    }

    public void setCategoryid(Integer categoryid) {
        this.categoryid = categoryid;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getRoomid() {
        return roomid;
    }

    public void setRoomid(Integer roomid) {
        this.roomid = roomid;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public Integer getVolume() {
        return Volume;
    }

    public void setVolume(Integer volume) {
        Volume = volume;
    }

    public String getSensorType() {
        return sensorType;
    }

    public void setSensorType(String sensorType) {
        this.sensorType = sensorType;
    }

    public Integer getSensordata() {
        return sensordata;
    }

    public void setSensordata(Integer sensordata) {
        this.sensordata = sensordata;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public Integer getProgramid() {
        return programid;
    }

    public void setProgramid(Integer programid) {
        this.programid = programid;
    }

    public Integer getTempature() {
        return tempature;
    }

    public void setTempature(Integer tempature) {
        this.tempature = tempature;
    }

    public String getTimer() {
        return timer;
    }

    public void setTimer(String timer) {
        this.timer = timer;
    }


}
