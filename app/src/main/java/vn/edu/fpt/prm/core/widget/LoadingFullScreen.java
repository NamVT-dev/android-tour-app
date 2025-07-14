package vn.edu.fpt.prm.core.widget;

import android.view.View;

public class LoadingFullScreen {
    public static void show(View loadingView) {
        if (loadingView != null) {
            loadingView.setVisibility(View.VISIBLE);
        }
    }

    public static void hide(View loadingView) {
        if (loadingView != null) {
            loadingView.setVisibility(View.GONE);
        }
    }

    public static void toggle(View loadingView, boolean show) {
        if (loadingView != null) {
            loadingView.setVisibility(show ? View.VISIBLE : View.GONE);
        }
    }
}
