package be.ucll.java.mobile.smarthome_mobile.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Timer;

import be.ucll.java.mobile.smarthome_mobile.util.DeviceCategory;

public class BigElectro extends Device {
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
    private Timer timer;

    /**
     * No args constructor for use in serialization
     *
     */
    public BigElectro() {
        super();
    }

    /**
     *
     * @param timer
     * @param type
     * @param tempature
     * @param programid
     */
    public BigElectro(Integer categoryid, Integer id, String name, Integer roomid, Boolean status, Type type, Integer programid, Integer tempature, Timer timer) {
        super(categoryid, id, name, roomid, status);
        this.type = type;
        this.programid = programid;
        this.tempature = tempature;
        this.timer = timer;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public BigElectro withType(Type type) {
        this.type = type;
        return this;
    }

    public Integer getProgramid() {
        return programid;
    }

    public void setProgramid(Integer programid) {
        this.programid = programid;
    }

    public BigElectro withProgramid(Integer programid) {
        this.programid = programid;
        return this;
    }

    public Integer getTempature() {
        return tempature;
    }

    public void setTempature(Integer tempature) {
        this.tempature = tempature;
    }

    public BigElectro withTempature(Integer tempature) {
        this.tempature = tempature;
        return this;
    }

    public Timer getTimer() {
        return timer;
    }

    public void setTimer(Timer timer) {
        this.timer = timer;
    }

    public BigElectro withTimer(Timer timer) {
        this.timer = timer;
        return this;
    }

}