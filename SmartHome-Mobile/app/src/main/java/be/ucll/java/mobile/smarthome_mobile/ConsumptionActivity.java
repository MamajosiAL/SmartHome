package be.ucll.java.mobile.smarthome_mobile;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import be.ucll.java.mobile.smarthome_mobile.util.BottomNavigationManager;

public class ConsumptionActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consumption);

        BottomNavigationManager.initialise(this);
    }
}