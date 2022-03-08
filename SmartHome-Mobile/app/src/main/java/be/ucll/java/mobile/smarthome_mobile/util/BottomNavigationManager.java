package be.ucll.java.mobile.smarthome_mobile.util;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import be.ucll.java.mobile.smarthome_mobile.ConsumptionActivity;
import be.ucll.java.mobile.smarthome_mobile.MainActivity;
import be.ucll.java.mobile.smarthome_mobile.ManageActivity;
import be.ucll.java.mobile.smarthome_mobile.R;

public class BottomNavigationManager {
    static BottomNavigationView bottomNavigationView;

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
    }
}
