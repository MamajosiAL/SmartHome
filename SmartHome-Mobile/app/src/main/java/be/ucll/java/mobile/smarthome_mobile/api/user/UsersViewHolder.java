package be.ucll.java.mobile.smarthome_mobile.api.user;

import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import be.ucll.java.mobile.smarthome_mobile.R;
import be.ucll.java.mobile.smarthome_mobile.api.ViewLinker;

public class UsersViewHolder extends RecyclerView.ViewHolder implements ViewLinker {
        // init the item view's
        TextView name, firstName, email, username, id;

        public UsersViewHolder(View itemView) {
            super(itemView);
            // get the reference of item view's
            linkView(itemView);
        }

    public void linkView(View itemView) {
       /* id = itemView.findViewById(R.id.user_id);
        username = itemView.findViewById(R.id.user_username);
        firstName = itemView.findViewById(R.id.user_firstName);
        name = itemView.findViewById(R.id.user_name);
        email = itemView.findViewById(R.id.user_email);
    */}
}

