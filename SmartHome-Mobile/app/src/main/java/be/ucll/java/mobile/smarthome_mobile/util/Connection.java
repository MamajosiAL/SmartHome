package be.ucll.java.mobile.smarthome_mobile.util;

public class Connection {
    //local IP
    //private static final String URL = "http://192.168.68.110:8080/";
    //Emulator gateway
    private static final String URL = "http://10.0.2.2:8080/";

    public static String getUrl(){
        return URL;
    }
}
