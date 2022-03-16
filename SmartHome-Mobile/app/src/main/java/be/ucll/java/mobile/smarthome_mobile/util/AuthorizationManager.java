package be.ucll.java.mobile.smarthome_mobile.util;

import android.annotation.SuppressLint;
import android.content.Context;
import android.widget.Toast;
import androidx.annotation.NonNull;
import be.ucll.java.mobile.smarthome_mobile.R;
import be.ucll.java.mobile.smarthome_mobile.exception.SignupException;
import be.ucll.java.mobile.smarthome_mobile.pojo.User;

public class AuthorizationManager {
    @SuppressLint("StaticFieldLeak")
    private static AuthorizationManager instance;
    private String sessionId;
    private final Context context;

    private AuthorizationManager(@NonNull Context context){
        this.context= context;
    }

    public static AuthorizationManager getInstance(@NonNull Context context){
        if(instance==null) instance = new AuthorizationManager(context);
        return instance;
    }

    public void signIn(String sessionId) throws SignupException{
        if(sessionId==null||sessionId.equals("")) {
            throw new SignupException("already logged in!");
        }else {
            setSessionId(sessionId);
            Toast.makeText(context, context.getString(R.string.signinSucces), Toast.LENGTH_LONG).show();
        }
    }

    public String getSessionId() {
        return sessionId;
    }

    private void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public boolean isSignedIn() {
        return sessionId!=null&&!sessionId.equals("");
    }

    public void signOut(){
        this.sessionId = null;
        Toast.makeText(context, context.getString(R.string.signoutSucces), Toast.LENGTH_LONG).show();
    }
}
