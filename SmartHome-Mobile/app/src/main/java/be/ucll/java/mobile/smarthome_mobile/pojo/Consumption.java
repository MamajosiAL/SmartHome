package be.ucll.java.mobile.smarthome_mobile.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Consumption {
    @SerializedName("consumptionId")
    @Expose
    private long consumptionId;
    @SerializedName("deviceId")
    @Expose
    private long deviceId;
    @SerializedName("aantalMinuten")
    @Expose
    private long aantalMinuten;
    @SerializedName("unit")
    @Expose
    private String unit;
    @SerializedName("consumptionPerHour")
    @Expose
    private long consumptionPerHour;

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
    public Consumption(long consumptionId, long deviceId, long aantalMinuten, String unit, long consumptionPerHour) {
        super();
        this.consumptionId = consumptionId;
        this.deviceId = deviceId;
        this.aantalMinuten = aantalMinuten;
        this.unit = unit;
        this.consumptionPerHour = consumptionPerHour;
    }

    public long getConsumptionId() {
        return consumptionId;
    }

    public void setConsumptionId(long consumptionId) {
        this.consumptionId = consumptionId;
    }

    public Consumption withConsumptionId(long consumptionId) {
        this.consumptionId = consumptionId;
        return this;
    }

    public long getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(long deviceId) {
        this.deviceId = deviceId;
    }

    public Consumption withDeviceId(long deviceId) {
        this.deviceId = deviceId;
        return this;
    }

    public long getAantalMinuten() {
        return aantalMinuten;
    }

    public void setAantalMinuten(long aantalMinuten) {
        this.aantalMinuten = aantalMinuten;
    }

    public Consumption withAantalMinuten(long aantalMinuten) {
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

    public long getConsumptionPerHour() {
        return consumptionPerHour;
    }

    public void setConsumptionPerHour(long consumptionPerHour) {
        this.consumptionPerHour = consumptionPerHour;
    }

    public Consumption withConsumptionPerHour(long consumptionPerHour) {
        this.consumptionPerHour = consumptionPerHour;
        return this;
    }
}
