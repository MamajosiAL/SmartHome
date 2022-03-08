package be.ucll.java.mobile.smarthome_mobile.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class House_User {
    @SerializedName("id")
    @Expose
    private Integer id;

    @SerializedName("houseid")
    @Expose
    private Integer houseid;

    @SerializedName("userid")
    @Expose
    private Integer userid;

    @SerializedName("isadmin")
    @Expose
    private boolean isadmin;

    public House_User(){

    }

    public House_User(Integer id, Integer houseid, Integer userid, boolean isadmin) {
        this.id = id;
        this.houseid = houseid;
        this.userid = userid;
        this.isadmin = isadmin;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getHouseid() {
        return houseid;
    }

    public void setHouseid(Integer houseid) {
        this.houseid = houseid;
    }

    public Integer getUserid() {
        return userid;
    }

    public void setUserid(Integer userid) {
        this.userid = userid;
    }

    public boolean isIsadmin() {
        return isadmin;
    }

    public void setIsadmin(boolean isadmin) {
        this.isadmin = isadmin;
    }
}
