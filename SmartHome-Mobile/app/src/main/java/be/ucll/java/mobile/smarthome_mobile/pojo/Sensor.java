
package be.ucll.java.mobile.smarthome_mobile.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Sensor extends Device{

    @SerializedName("sensorType")
    @Expose
    private String sensorType;

    @SerializedName("sensordata")
    @Expose
    private Integer sensordata;

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

}
