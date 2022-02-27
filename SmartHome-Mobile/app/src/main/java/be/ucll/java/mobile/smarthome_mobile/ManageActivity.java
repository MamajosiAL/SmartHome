package be.ucll.java.mobile.smarthome_mobile;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class ManageActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage);
        Button registerButton = findViewById(R.id.buttonAccount);
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ManageActivity.this,RegisterActivity.class));
                overridePendingTransition(0,0);
            }
        });
        initialiseNavigation();
    }

    private void initialiseNavigation() {
        //initialise navigation variable
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigation);
        //set selected
        bottomNavigationView.setSelectedItemId(R.id.navManage);
        //perform ItemSelectedListener
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){

                    case R.id.navConsumption:
                        startActivity(new Intent(getApplicationContext(), ConsumptionActivity.class));
                        overridePendingTransition(0,0);
                        return true;

                    case R.id.navManage:
                        return true;

                    case R.id.navHouses:
                        startActivity(new Intent(getApplicationContext(), MainActivity.class));
                        overridePendingTransition(0,0);
                        return true;

                }
                return false;
            }
        });
    }
}