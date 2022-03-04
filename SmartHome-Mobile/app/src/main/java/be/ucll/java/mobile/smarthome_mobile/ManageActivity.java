package be.ucll.java.mobile.smarthome_mobile;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import be.ucll.java.mobile.smarthome_mobile.util.AuthorizationManager;

public class ManageActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage);
//        startActivity(new Intent(getApplicationContext(), ManageActivity.class));
//        overridePendingTransition(0, 0);

        if (savedInstanceState == null) {
            Fragment body;
            if (AuthorizationManager.getInstance(ManageActivity.this).signedIn()) {
                body = new ManageLoggedInFragment();
            } else {
                body = new ManageLoggedOutFragment();
            }
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragmentManage_container_view, body, null)
                    .commit();
            
            initialiseNavigation();
        }
    }
    private void initialiseNavigation () {
        //initialise navigation variable
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigation);
        //set selected
        bottomNavigationView.setSelectedItemId(R.id.navManage);
        //perform ItemSelectedListener
        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            switch (item.getItemId()) {

                case R.id.navConsumption:
                    startActivity(new Intent(getApplicationContext(), ConsumptionActivity.class));
                    overridePendingTransition(0, 0);
                    return true;

                case R.id.navManage:
                    startActivity(new Intent(getApplicationContext(), ManageActivity.class));
                    overridePendingTransition(0, 0);
                    return true;

                case R.id.navHouses:
                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                    overridePendingTransition(0, 0);
                    return true;

            }
            return false;
        });
    }
}