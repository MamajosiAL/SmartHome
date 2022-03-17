package be.ucll.java.mobile.smarthome_mobile.api.device;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import be.ucll.java.mobile.smarthome_mobile.DeviceActivity;
import be.ucll.java.mobile.smarthome_mobile.R;
import be.ucll.java.mobile.smarthome_mobile.RoomActivity;
import be.ucll.java.mobile.smarthome_mobile.pojo.Device;

public class DevicesAdapter extends RecyclerView.Adapter<DevicesViewHolder> {
    Context context;
    List<Device> devices;
    String deviceCategory;
    String roomName;
    int roomId;

    public DevicesAdapter(Context context, List<Device> devices, String deviceCategory, String roomName, int roomId){
        this.context = context;
        this.devices = devices;
        this.deviceCategory = deviceCategory;
        this.roomName = roomName;
        this.roomId = roomId;
    }

    @Override
    public DevicesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType){
        View view = LayoutInflater.from(context).inflate(R.layout.devices_list_item, null);
        return new DevicesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(DevicesViewHolder holder, final int position){
        holder.name.setText(devices.get(position).getName());


        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, DeviceActivity.class);
            intent.putExtra("deviceId", devices.get(position).getId());
            intent.putExtra("deviceName", devices.get(position).getName());
            intent.putExtra("roomId", roomId);
            intent.putExtra("deviceCategory", deviceCategory);
            intent.putExtra("roomName", roomName);
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return devices!=null?devices.size():0;
    }
}
