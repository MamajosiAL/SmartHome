package be.ucll.java.mobile.smarthome_mobile.recyclerview.houses;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import be.ucll.java.mobile.smarthome_mobile.R;
import be.ucll.java.mobile.smarthome_mobile.model.HousePOJO;

public class HousesAdapter extends RecyclerView.Adapter<HousesHolder> {
    private static final String TAG = "UsersAdapter";
    LayoutInflater inflater;
    private List<HousePOJO> list;

    public HousesAdapter(@NonNull Context context, @NonNull List<HousePOJO> list) {
        this.list = list;
        this.inflater = LayoutInflater.from(context);
    }


    @NonNull
    @Override
    public HousesHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.house_item, parent,false);
        return new HousesHolder(view);
    }


    // Voor elk item in de List van de adapter wordt deze method aangeroepen
    // om de 'Data' te visualiseren als 1 lijn of kaartje in de recyclerview
    @Override
    public void onBindViewHolder(@NonNull HousesHolder holder, int position) {
        Log.d(TAG, "onBindViewHolder: " + list.get(position));
        //bind data to single row
        holder.setHouse(list.get(position));
    }

    /**
     * Returns the total number of items in the data set held by the adapter.
     *
     * @return The total number of items in this adapter.
     */
    @Override
    public int getItemCount() {
        return 0;
    }
}
