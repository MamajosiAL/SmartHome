package be.ucll.java.mobile.smarthome_mobile.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Consumption {
    @SerializedName("consumptionId")
    @Expose
    private Integer consumptionId;
    @SerializedName("deviceId")
    @Expose
    private Integer deviceId;
    @SerializedName("aantalMinuten")
    @Expose
    private Integer aantalMinuten;
    @SerializedName("unit")
    @Expose
    private String unit;
    @SerializedName("consumptionPerHour")
    @Expose
    private double consumptionPerHour;

    /**
     * No args constructor for use in serialization
     *
     */
    public Consumption() {
    }

    /**
     *
     * @param unit
     * @param consumptionId
     * @param aantalMinuten
     * @param consumptionPerHour
     * @param deviceId
     */
    public Consumption(Integer consumptionId, Integer deviceId, Integer aantalMinuten, String unit, double consumptionPerHour) {
        super();
        this.consumptionId = consumptionId;
        this.deviceId = deviceId;
        this.aantalMinuten = aantalMinuten;
        this.unit = unit;
        this.consumptionPerHour = consumptionPerHour;
    }

    public Integer getConsumptionId() {
        return consumptionId;
    }

    public void setConsumptionId(Integer consumptionId) {
        this.consumptionId = consumptionId;
    }

    public Consumption withConsumptionId(Integer consumptionId) {
        this.consumptionId = consumptionId;
        return this;
    }

    public Integer getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(Integer deviceId) {
        this.deviceId = deviceId;
    }

    public Consumption withDeviceId(Integer deviceId) {
        this.deviceId = deviceId;
        return this;
    }

    public Integer getAantalMinuten() {
        return aantalMinuten;
    }

    public void setAantalMinuten(Integer aantalMinuten) {
        this.aantalMinuten = aantalMinuten;
    }

    public Consumption withAantalMinuten(Integer aantalMinuten) {
        this.aantalMinuten = aantalMinuten;
        return this;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public Consumption withUnit(String unit) {
        this.unit = unit;
        return this;
    }

    public double getConsumptionPerHour() {
        return consumptionPerHour;
    }

    public void setConsumptionPerHour(double consumptionPerHour) {
        this.consumptionPerHour = consumptionPerHour;
    }

    public Consumption withConsumptionPerHour(double consumptionPerHour) {
        this.consumptionPerHour = consumptionPerHour;
        return this;
    }
}
