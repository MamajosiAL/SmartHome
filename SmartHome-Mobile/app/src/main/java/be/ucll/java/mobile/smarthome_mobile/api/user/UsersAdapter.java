package be.ucll.java.mobile.smarthome_mobile.api.user;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import be.ucll.java.mobile.smarthome_mobile.R;
import be.ucll.java.mobile.smarthome_mobile.UserInHouseActivity;
import be.ucll.java.mobile.smarthome_mobile.api.house.UserHouseManager;
import be.ucll.java.mobile.smarthome_mobile.pojo.User;
import be.ucll.java.mobile.smarthome_mobile.util.SwipeListener;

public class UsersAdapter extends RecyclerView.Adapter<UsersViewHolder>{
    private final String TAG = this.getClass().getSimpleName();
    Context context;
    List<User> users;

    public UsersAdapter(Context context, List<User> users) {
        this.users = users;
        this.context = context;
    }

    @Override
    public UsersViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.users_list_item, null);
        return new UsersViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(UsersViewHolder holder, final int position) {
        // set the data
        User current = users.get(position);
        holder.username.setText(current.getUsername());
        if (current.isIsadmin()) {
            holder.isAdmin.setText("Admin") ;
        }else{
            holder.isAdmin.setText(R.string.user);
        }

        try{
            //when swiped right, the user gets promoted to admin
            //when swiped left, the user gets removed
            holder.itemView.setOnTouchListener(new SwipeListener(){
                @Override
                public void onLeftToRightSwipe() {
                    new UserHouseManager((UserInHouseActivity) context).promoteUserToAdminForHouse(current.getId(),((UserInHouseActivity) context).getIntent().getIntExtra("houseId",0));
                }

                @Override
                public void onRightToLeftSwipe() {
                    new UserHouseManager((UserInHouseActivity) context).removeUserFromHouse(current.getId(),((UserInHouseActivity) context).getIntent().getIntExtra("houseId",0));
                }
            });
        }catch (Exception e){
            e.printStackTrace();
            Log.e(TAG, e.getMessage());
        }


       // implement setONCLickListener on itemView
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // display a toast with user name
                Toast.makeText(context, context.getText(R.string.swipeInfo), Toast.LENGTH_SHORT).show();
            }
        });
    }



    @Override
    public int getItemCount() {
        return users.size(); // size of the list items
    }


}
