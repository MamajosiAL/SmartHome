package be.ucll.java.mobile.smarthome_mobile.util;

import android.content.Context;
import android.widget.Toast;

import androidx.annotation.NonNull;

import be.ucll.java.mobile.smarthome_mobile.R;
import be.ucll.java.mobile.smarthome_mobile.exception.SignupException;
import be.ucll.java.mobile.smarthome_mobile.pojo.User;

public class AuthorizationManager {
    private static AuthorizationManager instance;
    private final Context context;
    private User currentUser;
    private User userTemp;

    private AuthorizationManager(@NonNull Context context){
        this.context= context;
    }

    public static AuthorizationManager getInstance(@NonNull Context context){
        if(instance==null) instance = new AuthorizationManager(context);
        return instance;
    }

    public void signIn(@NonNull User user) throws SignupException{
        if(currentUser!=null) {
            throw new SignupException("already logged in!");
        }else {
            currentUser = user;
            Toast.makeText(context, context.getString(R.string.signinSucces), Toast.LENGTH_LONG).show();
        }


    }

    public void signOut(){
        currentUser = null;
        Toast.makeText(context, context.getString(R.string.signoutSucces), Toast.LENGTH_LONG).show();
    }

    public void setTempUser(@NonNull User user){
        this.userTemp = user;
    }

    public User getUserTemp() {
        return userTemp;
    }

    public boolean signedIn(){
        return currentUser!=null;
    }
}
