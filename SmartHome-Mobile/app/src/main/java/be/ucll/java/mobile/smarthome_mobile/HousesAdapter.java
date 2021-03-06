package be.ucll.java.mobile.smarthome_mobile;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import be.ucll.java.mobile.smarthome_mobile.pojo.House;

public class HousesAdapter extends RecyclerView.Adapter<HousesAdapter.HousesViewHolder>{
    Context context;
    List<House> houses;

    public HousesAdapter(Context context, List<House> houses) {
        this.houses = houses;
        this.context = context;
    }

    @NonNull
    @Override
    public HousesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.houses_list_item, null);
        return new HousesViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(HousesViewHolder holder, final int position) {
        // set the data
        holder.name.setText(context.getText(R.string.name) + ": " + houses.get(position).getName());
        // implement setONCLickListener on itemView
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // display a toast with user name
                Toast.makeText(context, houses.get(position).getName(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return houses.size(); // size of the list items
    }

    class HousesViewHolder extends RecyclerView.ViewHolder {
        // init the item view's
        TextView name;

        public HousesViewHolder(View itemView) {
            super(itemView);
            // get the reference of item view's
            name = itemView.findViewById(R.id.name);
        }
    }
}
