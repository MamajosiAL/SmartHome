package be.ucll.java.mobile.smarthome_mobile;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ManageLoggedOutFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ManageLoggedOutFragment extends Fragment {
    private final String TAG = this.getClass().getSimpleName();
    Button logIn;
    Button register;

    public ManageLoggedOutFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static ManageLoggedOutFragment newInstance() {
        return new ManageLoggedOutFragment();
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.fragment_manage_logged_out, null);

        //set onclicklisteners
        try {
            logIn = root.findViewById(R.id.btnLogIn);
            register = root.findViewById(R.id.btnRegisterPage);
            logIn.setOnClickListener(v -> {
                Log.d(TAG, "btnLogIn pressed");
                startActivity(new Intent(getContext(), LoginActivity.class));
            });

            register.setOnClickListener(v -> {
                Log.d(TAG, "btnRegisterPage pressed");
                startActivity(new Intent(getContext(), RegisterActivity.class));
            });
        }catch (Exception e){
            Log.e(TAG, e.getMessage());
            e.printStackTrace();
        }

        return root;
    }
}