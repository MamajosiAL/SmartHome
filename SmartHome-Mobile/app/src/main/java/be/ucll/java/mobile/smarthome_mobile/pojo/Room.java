package be.ucll.java.mobile.smarthome_mobile.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Room {
    @SerializedName("id")
    @Expose
    private long id;

    @SerializedName("name")
    @Expose
    private String name;

    @SerializedName("houseid")
    @Expose
    private long houseid;

    /**
     * No args constructor for use in serialization
     *
     */
    public Room() {
    }

    /**
     *
     * @param houseid
     * @param name
     * @param id
     */
    public Room(long id, String name, long houseid) {
        super();
        this.id = id;
        this.name = name;
        this.houseid = houseid;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Room withId(long id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Room withName(String name) {
        this.name = name;
        return this;
    }

    public long getHouseid() {
        return houseid;
    }

    public void setHouseid(long houseid) {
        this.houseid = houseid;
    }

    public Room withHouseid(long houseid) {
        this.houseid = houseid;
        return this;
    }
}
