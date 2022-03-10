package be.ucll.java.mobile.smarthome_mobile.util;

import android.widget.EditText;

import be.ucll.java.mobile.smarthome_mobile.R;

public class TxtValidator {
    public static boolean validate(EditText editText) {
        // check the lenght of the enter data in EditText and give error if its empty
        if (editText.getText().toString().trim().length() > 0) {
            return true; // returs true if field is not empty
        }
        editText.setError("Missing values in fields!");
        editText.requestFocus();
        return false;
    }
}
