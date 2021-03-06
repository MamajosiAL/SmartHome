package be.ucll.java.mobile.smarthome_mobile.api.bigelectro;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import be.ucll.java.mobile.smarthome_mobile.R;
import be.ucll.java.mobile.smarthome_mobile.pojo.BigElectro;

public class BigElektroAdapter extends RecyclerView.Adapter<BigElektroViewHolder> {
    Context context;
    List<BigElectro> bigElectros;

    public BigElektroAdapter(Context context, List<BigElectro> bigElectros){
        this.context = context;
        this.bigElectros = bigElectros;
    }

    @Override
    public BigElektroViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType){
        View view = LayoutInflater.from(context).inflate(R.layout.bigelectro_list_item, null);
        return new BigElektroViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(BigElektroViewHolder holder, final int position){
        holder.name.setText(bigElectros.get(position).getName());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return bigElectros!=null?bigElectros.size():0;
    }
}
