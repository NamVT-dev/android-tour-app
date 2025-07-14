package vn.edu.fpt.prm.core.widget;

import android.view.View;
import android.widget.ProgressBar;

public class Loading {
    public static void show(ProgressBar progressBar) {
        if (progressBar != null) {
            progressBar.setVisibility(View.VISIBLE);
        }
    }

    public static void hide(ProgressBar progressBar) {
        if (progressBar != null) {
            progressBar.setVisibility(View.GONE);
        }
    }

    public static void toggle(ProgressBar progressBar, boolean show) {
        if (progressBar != null) {
            progressBar.setVisibility(show ? View.VISIBLE : View.GONE);
        }
    }
}
