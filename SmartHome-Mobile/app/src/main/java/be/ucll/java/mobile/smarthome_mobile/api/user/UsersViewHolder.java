package be.ucll.java.mobile.smarthome_mobile.api.user;

import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import be.ucll.java.mobile.smarthome_mobile.R;
import be.ucll.java.mobile.smarthome_mobile.api.ViewLinker;

public class UsersViewHolder extends RecyclerView.ViewHolder implements ViewLinker {
        // init the item view's
        TextView name, firstName, email, username, id, isAdmin;

        public UsersViewHolder(View itemView) {
            super(itemView);
            // get the reference of item view's
            linkView(itemView);
        }

    public void linkView(View itemView) {
        username = itemView.findViewById(R.id.txtUsername);
        isAdmin = itemView.findViewById(R.id.txtIsAdmin);
        /* id = itemView.findViewById(R.id.user_id);
        firstName = itemView.findViewById(R.id.user_firstName);
        name = itemView.findViewById(R.id.user_name);
        email = itemView.findViewById(R.id.user_email);
    */}

    //when swiped right, the user gets promovated to admin

}

