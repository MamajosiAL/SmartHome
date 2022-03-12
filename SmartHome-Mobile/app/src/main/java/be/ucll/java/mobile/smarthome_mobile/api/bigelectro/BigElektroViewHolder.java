package be.ucll.java.mobile.smarthome_mobile.api.bigelectro;

import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import be.ucll.java.mobile.smarthome_mobile.R;
import be.ucll.java.mobile.smarthome_mobile.api.ViewLinker;

public class BigElektroViewHolder extends RecyclerView.ViewHolder implements ViewLinker {
    TextView name;

    public BigElektroViewHolder(View itemView) {
        super(itemView);
        // get the reference of item view's
        linkView(itemView);
    }

    public void linkView(View itemView) {
        name = itemView.findViewById(R.id.txtDeviceName);
    }

}
