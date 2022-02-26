package be.ucll.java.mobile.smarthome_mobile.recyclerview.users;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import be.ucll.java.mobile.smarthome_mobile.model.UserPOJO;

public class UsersHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    private static final String TAG = "UsersHolder";
    private TextView txtUsername;
    private TextView txtFirstName;
    private TextView txtName;
    private TextView txtEmail;

    private UserPOJO user;

    public UsersHolder(@NonNull View itemView) {
        super(itemView);
    }

    @Override
    public void onClick(View v) {

    }

    public void setUser(UserPOJO user) {
        this.user = user;
        if (user != null) {
            // Set the username
            txtUsername.setText(user.getUsername());
            // Set the firstName
            txtFirstName.setText(user.getFirstname());
            // Set the name
            txtName.setText(user.getName());
            // Set the email
            txtEmail.setText(user.getEmail());
        }
    }
}
