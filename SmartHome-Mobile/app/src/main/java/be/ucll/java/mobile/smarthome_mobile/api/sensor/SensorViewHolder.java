package be.ucll.java.mobile.smarthome_mobile.api.sensor;

import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import be.ucll.java.mobile.smarthome_mobile.R;
import be.ucll.java.mobile.smarthome_mobile.api.ViewLinker;

public class SensorViewHolder extends RecyclerView.ViewHolder implements ViewLinker {
    TextView name;

    public SensorViewHolder(View itemView) {
        super(itemView);
        // get the reference of item view's
        linkView(itemView);
    }

    public void linkView(View itemView) {
        name = itemView.findViewById(R.id.name);
    }

}
