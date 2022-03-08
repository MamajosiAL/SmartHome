package be.ucll.java.mobile.smarthome_mobile.api.house;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import be.ucll.java.mobile.smarthome_mobile.HouseActivity;
import be.ucll.java.mobile.smarthome_mobile.R;
import be.ucll.java.mobile.smarthome_mobile.pojo.House;
import be.ucll.java.mobile.smarthome_mobile.pojo.Room;

public class HousesAdapter extends RecyclerView.Adapter<HousesViewHolder>{
    Context context;
    List<House> houses;

    public HousesAdapter(Context context, List<House> houses) {
        this.houses = houses;
        this.context = context;
    }

    @Override
    public HousesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.houses_list_item, null);
        return new HousesViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(HousesViewHolder holder, final int position) {
        // set the data
        holder.name.setText(houses.get(position).getName());
        // implement setONCLickListener on itemView
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, HouseActivity.class);
                intent.putExtra("houseId", houses.get(position).getId());
                intent.putExtra("houseName", houses.get(position).getName());
                context.startActivity(intent);

            }
        });
    }

    @Override
    public int getItemCount() {
        return houses!=null?houses.size():0; // size of the list items
    }


}
