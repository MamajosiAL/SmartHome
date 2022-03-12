package be.ucll.java.mobile.smarthome_mobile.api.consumption;

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
import be.ucll.java.mobile.smarthome_mobile.api.house.HousesViewHolder;
import be.ucll.java.mobile.smarthome_mobile.pojo.Consumption;
import be.ucll.java.mobile.smarthome_mobile.pojo.House;

public class ConsumptionAdapter extends RecyclerView.Adapter<ConsumptionViewHolder> {

    Context context;
    List<Consumption> consumptions;

    public ConsumptionAdapter(Context context, List<Consumption> consumptionList) {
        this.consumptions = consumptionList;
        this.context = context;
    }

    @Override
    public ConsumptionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //TODO
        View view = LayoutInflater.from(context).inflate(R.layout.houses_list_item, null);
        return new ConsumptionViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(ConsumptionViewHolder holder, final int position) {
        // set the data
        holder.name.setText(consumptions.get(position).getUnit());
        // implement setONCLickListener on itemView
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, HouseActivity.class);
                intent.putExtra("houseId", consumptions.get(position).getDeviceId());
                intent.putExtra("houseName", consumptions.get(position).getUnit());
                context.startActivity(intent);

            }
        });
    }

    @Override
    public int getItemCount() {
        return consumptions!=null?consumptions.size():0; // size of the list items
    }


}
