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
    public BigElectro(Long categoryid, Long id, String name, Long roomid, Boolean status, DeviceCategory type, long programid, long tempature, Timer timer) {
        super(categoryid, id, name, roomid, status);
        this.type = type;
        this.programid = programid;
        this.tempature = tempature;
        this.timer = timer;
    }

    public DeviceCategory getType() {
        return type;
    }

    public void setType(DeviceCategory type) {
        this.type = type;
    }

    public BigElectro withType(DeviceCategory type) {
        this.type = type;
        return this;
    }

    public long getProgramid() {
        return programid;
    }

    public void setProgramid(long programid) {
        this.programid = programid;
    }

    public BigElectro withProgramid(long programid) {
        this.programid = programid;
        return this;
    }

    public long getTempature() {
        return tempature;
    }

    public void setTempature(long tempature) {
        this.tempature = tempature;
    }

    public BigElectro withTempature(long tempature) {
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