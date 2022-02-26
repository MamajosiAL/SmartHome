package be.ucll.java.mobile.smarthome_mobile.recyclerview.users;

import android.util.Log;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import be.ucll.java.mobile.smarthome_mobile.model.UserPOJO;

public class UsersAdapter extends RecyclerView.Adapter<UsersHolder> {
    private static final String TAG = "UsersAdapter";

    private List<UserPOJO> list;

    public UsersAdapter(List<UserPOJO> list) {
        this.list = list;
    }


    @NonNull
    @Override
    public UsersHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }


    // Voor elk item in de List van de adapter wordt deze method aangeroepen
    // om de 'Data' te visualiseren als 1 lijn of kaartje in de recyclerview
    @Override
    public void onBindViewHolder(@NonNull UsersHolder holder, int position) {
        Log.d(TAG, "onBindViewHolder: " + list.get(position));
        UserPOJO m = list.get(position);
        holder.setUser(m);
    }

    /**
     * Returns the total number of items in the data set held by the adapter.
     *
     * @return The total number of items in this adapter.
     */
    @Override
    public int getItemCount() {
        return list.size();
    }
}
