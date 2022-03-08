package be.ucll.java.mobile.smarthome_mobile;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import be.ucll.java.mobile.smarthome_mobile.util.AuthorizationManager;
import be.ucll.java.mobile.smarthome_mobile.util.BottomNavigationManager;

public class ManageActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage);
//        startActivity(new Intent(getApplicationContext(), ManageActivity.class));
//        overridePendingTransition(0, 0);

        if (savedInstanceState == null) {
            Fragment body;
            if (AuthorizationManager.getInstance(ManageActivity.this).isSignedIn()) {
                body = new ManageLoggedInFragment();
            } else {
                body = new ManageLoggedOutFragment();
            }
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragmentManage_container_view, body, null)
                    .commit();

            BottomNavigationManager.initialise(this);
        }
    }
}