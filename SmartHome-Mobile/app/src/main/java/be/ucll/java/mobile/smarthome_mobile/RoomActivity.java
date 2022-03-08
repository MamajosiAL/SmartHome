package be.ucll.java.mobile.smarthome_mobile;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import be.ucll.java.mobile.smarthome_mobile.util.NavigationManager;

public class RoomActivity extends AppCompatActivity {
    private final String TAG = this.getClass().getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room);

        NavigationManager.initialise(this);
    }
}