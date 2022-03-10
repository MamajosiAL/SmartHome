package be.ucll.java.mobile.smarthome_mobile.api.user;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import be.ucll.java.mobile.smarthome_mobile.R;
import be.ucll.java.mobile.smarthome_mobile.pojo.User;

public class UsersAdapter extends RecyclerView.Adapter<UsersViewHolder>{
    Context context;
    List<User> users;

    public UsersAdapter(Context context, List<User> users) {
        this.users = users;
        this.context = context;
    }

    @Override
    public UsersViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.houses_list_item, null);
        UsersViewHolder usersViewHolder = new UsersViewHolder(view);
        return usersViewHolder;
    }

    @Override
    public void onBindViewHolder(UsersViewHolder holder, final int position) {
        // set the data
        holder.id.setText(context.getText(R.string.userId) + ": " + users.get(position).getId());
        holder.username.setText(context.getText(R.string.username) + ": " + users.get(position).getUsername());
        holder.firstName.setText(context.getText(R.string.firstName) + ": " + users.get(position).getFirstname());
        holder.name.setText(context.getText(R.string.name) + ": " + users.get(position).getName());
        holder.email.setText(context.getText(R.string.email) + ": " + users.get(position).getEmail());

        // implement setONCLickListener on itemView
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // display a toast with user name
                Toast.makeText(context, users.get(position).getName(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return users.size(); // size of the list items
    }


}
