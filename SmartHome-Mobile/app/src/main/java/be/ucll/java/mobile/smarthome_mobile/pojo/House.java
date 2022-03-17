package be.ucll.java.mobile.smarthome_mobile.pojo;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
public class House {
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("userid")
    @Expose
    private Integer userid;

    @SerializedName("username")
    @Expose
    private String username;

    /**
     * No args constructor for use in serialization
     *
     */
    public House() {
    }

    /**
     *
     * @param name
     * @param id
     * @param userid
     */
    public House(Integer id, String name, Integer userid, String username) {
        super();
        this.id = id;
        this.name = name;
        this.userid = userid;
        this.username = username;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public House withId(Integer id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public House withName(String name) {
        this.name = name;
        return this;
    }

    public Integer getUserid() {
        return userid;
    }

    public void setUserid(Integer userid) {
        this.userid = userid;
    }

    public House withUserid(Integer userid) {
        this.userid = userid;
        return this;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
