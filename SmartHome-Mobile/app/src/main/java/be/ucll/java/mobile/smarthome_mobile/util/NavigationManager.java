package be.ucll.java.mobile.smarthome_mobile.util;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import be.ucll.java.mobile.smarthome_mobile.ConsumptionActivity;
import be.ucll.java.mobile.smarthome_mobile.LoginActivity;
import be.ucll.java.mobile.smarthome_mobile.MainActivity;
import be.ucll.java.mobile.smarthome_mobile.ManageActivity;
import be.ucll.java.mobile.smarthome_mobile.R;
import be.ucll.java.mobile.smarthome_mobile.RegisterActivity;

public class NavigationManager {
    private static final String TAG = "NavigationManager";
    static BottomNavigationView bottomNavigationView;

    private static void redirectToLogin(AppCompatActivity context) {
        Toast.makeText(context, context.getText(R.string.redirectedToLogin), Toast.LENGTH_LONG).show();
        Log.d(TAG, (String) context.getText(R.string.redirectedToLogin));

        context.startActivity(new Intent(context, LoginActivity.class));
        context.overridePendingTransition(0, 0);
    }

    @SuppressLint("NonConstantResourceId")
    public static void initialise(AppCompatActivity context) {
        //disable darkmode
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        //initialise navigation variable
        bottomNavigationView = context.findViewById(R.id.bottomNavigation);

        //set selected
        if (context instanceof MainActivity) {
            bottomNavigationView.setSelectedItemId(R.id.navHouses);
        } else if (context instanceof ConsumptionActivity){
            bottomNavigationView.setSelectedItemId(R.id.navConsumption);
        }else{
            bottomNavigationView.setSelectedItemId(R.id.navManage);
        }

        //perform ItemSelectedListener
        //noinspection deprecation
        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            switch (item.getItemId()) {

                case R.id.navConsumption:
                    context.startActivity(new Intent(context, ConsumptionActivity.class));
                    context.overridePendingTransition(0, 0);
                    return true;

                case R.id.navManage:
                    context.startActivity(new Intent(context, ManageActivity.class));
                    context.overridePendingTransition(0, 0);
                    return true;

                case R.id.navHouses:
                    context.startActivity(new Intent(context, MainActivity.class));
                    context.overridePendingTransition(0, 0);
                    return true;

            }
            return false;
        });

        //redirect if user is not logged and current context(Activity) is not equal to Register or Login
        if ((!AuthorizationManager.getInstance(context).isSignedIn()) && !((context instanceof LoginActivity) || (context instanceof RegisterActivity))){
            redirectToLogin(context);
        }
    }
}
