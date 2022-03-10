package be.ucll.java.mobile.smarthome_mobile.api.Sensor;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import be.ucll.java.mobile.smarthome_mobile.R;
import be.ucll.java.mobile.smarthome_mobile.api.Media.MediaViewHolder;
import be.ucll.java.mobile.smarthome_mobile.pojo.Media;
import be.ucll.java.mobile.smarthome_mobile.pojo.Sensor;

public class SensorAdapter extends RecyclerView.Adapter<SensorViewHolder> {
    Context context;
    List<Sensor> sensorList;

    public SensorAdapter(Context context, List<Sensor> sensorList){
        this.context = context;
        this.sensorList = sensorList;
    }

    @Override
    public SensorViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType){
        View view = LayoutInflater.from(context).inflate(R.layout.sensor_list_item, null);
        return new SensorViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(SensorViewHolder holder, final int position){
        holder.name.setText(sensorList.get(position).getName());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return sensorList!=null?sensorList.size():0;
    }

}
