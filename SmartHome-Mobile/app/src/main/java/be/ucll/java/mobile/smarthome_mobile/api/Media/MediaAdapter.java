package be.ucll.java.mobile.smarthome_mobile.api.Media;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import be.ucll.java.mobile.smarthome_mobile.R;
import be.ucll.java.mobile.smarthome_mobile.api.BigElectro.BigElektroViewHolder;
import be.ucll.java.mobile.smarthome_mobile.pojo.BigElectro;
import be.ucll.java.mobile.smarthome_mobile.pojo.Media;

public class MediaAdapter extends RecyclerView.Adapter<MediaViewHolder>  {
    Context context;
    List<Media> mediaList;

    public MediaAdapter(Context context, List<Media> mediaList){
        this.context = context;
        this.mediaList = mediaList;
    }

    @Override
    public MediaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType){
        View view = LayoutInflater.from(context).inflate(R.layout.media_list_item, null);
        return new MediaViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(MediaViewHolder holder, final int position){
        holder.name.setText(mediaList.get(position).getName());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return mediaList!=null?mediaList.size():0;
    }
}
