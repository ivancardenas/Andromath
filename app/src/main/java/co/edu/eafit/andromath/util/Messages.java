package co.edu.eafit.andromath.util;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

public class Messages {

    public static void invalidEquation(final String tag, Context context) {
        Log.e(tag, "Invalid equation");
        Toast.makeText(context, "Invalid equation", Toast.LENGTH_SHORT).show();
    }
}