package vn.edu.fpt.prm.core.utils;

import android.util.Log;

public class Logger {
    // Enable or disable log for app
    private static final boolean ENABLE_LOG = true;

    public static void debug(String tag, String message) {
        if (ENABLE_LOG) {
            Log.d(tag, message);
        }
    }

    public static void error(String tag, String message) {
        if (ENABLE_LOG) {
            Log.e(tag, message);
        }
    }

    public static void info(String tag, String message) {
        if (ENABLE_LOG) {
            Log.i(tag, message);
        }
    }

    public static void warning(String tag, String message) {
        if (ENABLE_LOG) {
            Log.w(tag, message);
        }
    }
}
