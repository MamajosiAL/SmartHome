package be.ucll.java.mobile.smarthome_mobile.exception;

import android.util.Log;
import android.widget.Toast;

public class DataNotFoundException extends RuntimeException{
    /**
     * Constructs a new runtime exception with the specified detail message.
     * The cause is not initialized, and may subsequently be initialized by a
     * call to {@link #initCause}.
     *
     */
    public DataNotFoundException(Throwable t) {
        super(t);
        Log.e(this.getClass().getSimpleName(),t.getMessage());
    }
}
