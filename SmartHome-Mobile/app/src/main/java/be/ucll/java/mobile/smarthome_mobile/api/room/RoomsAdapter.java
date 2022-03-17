package be.ucll.java.mobile.smarthome_mobile.api.room;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;
import be.ucll.java.mobile.smarthome_mobile.R;
import be.ucll.java.mobile.smarthome_mobile.RoomActivity;
import be.ucll.java.mobile.smarthome_mobile.pojo.Room;

public class RoomsAdapter extends RecyclerView.Adapter<RoomsViewHolder>{
    private final String TAG = this.getClass().getSimpleName();
    AppCompatActivity context;
    List<Room> roomsFromHouse;

    public RoomsAdapter(AppCompatActivity context, List<Room> roomsFromHouse) {
        this.roomsFromHouse = roomsFromHouse;
        this.context = context;
    }

    @NonNull
    @Override
    public RoomsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        @SuppressLint("InflateParams")
        View view = LayoutInflater.from(context).inflate(R.layout.rooms_list_item, null);
        return new RoomsViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(RoomsViewHolder holder, final int position) {
        // set the data
        holder.name.setText(roomsFromHouse.get(position).getName());
        // implement setONCLickListener on itemView
        holder.itemView.setOnClickListener(view -> {
            Intent intent = new Intent(context, RoomActivity.class);
            intent.putExtra("roomId", roomsFromHouse.get(position).getId());
            intent.putExtra("roomName", roomsFromHouse.get(position).getName());
            intent.putExtra("houseId", roomsFromHouse.get(position).getHouseid());
            intent.putExtra("houseName", context.getIntent().getStringExtra("houseName"));
            context.startActivity(intent);
            // display a toast with user name
            //Toast.makeText(context, roomsFromHouse.get(position).getName(), Toast.LENGTH_SHORT).show();
        });
    }

    @Override
    public int getItemCount() {
        return roomsFromHouse !=null ? roomsFromHouse.size():0; // size of the list items
    }


}
