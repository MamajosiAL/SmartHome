package be.ucll.java.mobile.smarthome_mobile.api.device;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import be.ucll.java.mobile.smarthome_mobile.R;
import be.ucll.java.mobile.smarthome_mobile.pojo.Device;

public class DevicesAdapter extends RecyclerView.Adapter<DevicesViewHolder> {
    Context context;
    List<Device> devices;

    public DevicesAdapter(Context context, List<Device> devices){
        this.context = context;
        this.devices = devices;
    }

    @Override
    public DevicesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType){
        View view = LayoutInflater.from(context).inflate(R.layout.devices_list_item, null);
        return new DevicesViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(DevicesViewHolder holder, final int position){
        holder.name.setText(devices.get(position).getName());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return devices!=null?devices.size():0;
    }
}
