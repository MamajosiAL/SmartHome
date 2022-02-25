package be.ucll.java.mobile.smarthome_mobile;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //initialise navigation variable
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigation);
        //set selected
        bottomNavigationView.setSelectedItemId(R.id.navHouses);
        //perform ItemSelectedListener
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){

                    case R.id.navConsumption:
                        startActivity(new Intent(getApplicationContext(),ConsumptionActivity.class));
                        overridePendingTransition(0,0);
                        return true;

                    case R.id.navManage:
                        startActivity(new Intent(getApplicationContext(),ManageActivity.class));
                        overridePendingTransition(0,0);
                        return true;

                    case R.id.navHouses:
                        return true;

                }
                return false;
            }
        });
    }

}