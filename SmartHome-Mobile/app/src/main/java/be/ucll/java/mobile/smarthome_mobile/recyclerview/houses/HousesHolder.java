package be.ucll.java.mobile.smarthome_mobile.recyclerview.houses;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import be.ucll.java.mobile.smarthome_mobile.R;
import be.ucll.java.mobile.smarthome_mobile.model.HousePOJO;

public class HousesHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    private static final String TAG = "UsersHolder";
    private TextView txtName;
    private TextView txtOwner;
    private HousePOJO housePOJO;

    public HousesHolder(@NonNull View itemView) {
        super(itemView);
        txtName = itemView.findViewById(R.id.house_name);
    }

    @Override
    public void onClick(View v) {

    }

    public void setHouse(@NonNull HousePOJO house) {
        this.housePOJO = house;
        // Set the name
        txtName.setText(housePOJO.getName());
        // Set the owner id
        txtOwner.setText(housePOJO.getUserid());
    }
}
