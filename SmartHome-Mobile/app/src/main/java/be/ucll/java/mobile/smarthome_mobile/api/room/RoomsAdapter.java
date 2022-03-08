package be.ucll.java.mobile.smarthome_mobile.api.room;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;
import be.ucll.java.mobile.smarthome_mobile.HouseActivity;
import be.ucll.java.mobile.smarthome_mobile.R;
import be.ucll.java.mobile.smarthome_mobile.pojo.Room;

public class RoomsAdapter extends RecyclerView.Adapter<RoomsViewHolder>{
    Context context;
    List<Room> roomsFromHouse;

    public RoomsAdapter(Context context, List<Room> roomsFromHouse) {
        this.roomsFromHouse = roomsFromHouse;
        this.context = context;
    }

    @NonNull
    @Override
    public RoomsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        @SuppressLint("InflateParams")
        View view = LayoutInflater.from(context).inflate(R.layout.houses_list_item, null);
        return new RoomsViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(RoomsViewHolder holder, final int position) {
        // set the data
        holder.name.setText(roomsFromHouse.get(position).getName());
        // implement setONCLickListener on itemView
        holder.itemView.setOnClickListener(view -> {
            Intent intent = new Intent(context, HouseActivity.class);
            intent.putExtra("houseId", roomsFromHouse.get(position).getHouseid());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return roomsFromHouse !=null? roomsFromHouse.size():0; // size of the list items
    }


}
