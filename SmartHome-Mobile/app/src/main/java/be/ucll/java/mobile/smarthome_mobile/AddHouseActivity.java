package be.ucll.java.mobile.smarthome_mobile;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import be.ucll.java.mobile.smarthome_mobile.util.NavigationManager;

public class AddHouseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_house);

        NavigationManager.initialise(this);
    }
}