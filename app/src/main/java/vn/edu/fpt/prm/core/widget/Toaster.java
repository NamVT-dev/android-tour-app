package vn.edu.fpt.prm.core.widget;

import android.content.Context;
import android.widget.Toast;

public class Toaster {
    public static void showToast(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }
}
