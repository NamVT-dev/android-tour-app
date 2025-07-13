package vn.edu.fpt.prm.core.network;

import android.content.Context;

import androidx.annotation.NonNull;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import vn.edu.fpt.prm.core.prefs.AuthManager;
import vn.edu.fpt.prm.core.utils.Logger;

public class AuthInterceptor implements Interceptor {
    private final Context context;

    public AuthInterceptor(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public Response intercept(Chain chain) throws IOException {
        String token = AuthManager.getToken(context); // Lấy token từ SharedPreferences
        Request original = chain.request();

        if (token == null || token.isEmpty()) {
            // If token is null or empty, redirect to login
            return chain.proceed(original);
        }

        Request requestWithToken = original.newBuilder()
                .header("Authorization", "Bearer " + token) // Thêm header Authorization
                .build();
        Logger.debug("AuthInterceptor", "Adding Authorization header with token: " + token);

        return chain.proceed(requestWithToken);
    }
}
