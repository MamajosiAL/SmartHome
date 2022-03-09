package be.ucll.java.mobile.smarthome_mobile.api.house;

import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import be.ucll.java.mobile.smarthome_mobile.R;
import be.ucll.java.mobile.smarthome_mobile.api.ViewLinker;

public class HousesViewHolder extends RecyclerView.ViewHolder implements ViewLinker {
    // init the item view's
    TextView name;

    public HousesViewHolder(View itemView) {
        super(itemView);
        // get the reference of item view's
        linkView(itemView);
    }

    public void linkView(View itemView) {
        name = itemView.findViewById(R.id.name);
    }

}
