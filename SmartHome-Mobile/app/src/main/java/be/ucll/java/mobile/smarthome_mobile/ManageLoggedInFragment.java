package be.ucll.java.mobile.smarthome_mobile;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;


public class ManageLoggedInFragment extends Fragment {
    private final String TAG = this.getClass().getSimpleName();
    Button logOut;
    Button myAccount;

    public ManageLoggedInFragment() {
        // Required empty public constructor
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {

    }

    @Override
    public void onStart() {
        super.onStart();
    }


    // TODO: Rename and change types and number of parameters
    public static ManageLoggedInFragment newInstance() {
        return new ManageLoggedInFragment();
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
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.fragment_manage_logged_in, null);

        //set onclicklisteners
        try {
            Log.d(TAG, "initiating buttons");
            logOut = getView().findViewById(R.id.btnLogOut);
            myAccount = getView().findViewById(R.id.btnAccountDetails);
            Log.d(TAG, "buttons initiated, started adding listeners");
            logOut.setOnClickListener(v -> {
                Log.d(TAG, "Button " + logOut.getClass().getSimpleName()+" pressed");
                startActivity(new Intent(getContext(), LogOutActivity.class));
            });
            myAccount.setOnClickListener(v -> {
                Log.d(TAG, "Button " + myAccount.getClass().getSimpleName()+" pressed");
                startActivity(new Intent(getContext(), AccountActivity.class));
            });
            Log.d(TAG, "Added listeners");
        }catch (Exception e){
            Log.e(TAG, e.getMessage());
            e.printStackTrace();
        }

        return root;
    }
}