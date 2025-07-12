package vn.edu.fpt.prm.core.network;

import android.content.Context;

import androidx.annotation.NonNull;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import vn.edu.fpt.prm.core.utils.AuthPrefs;

public class ApiInterceptor implements Interceptor {
    private final Context context;

    public ApiInterceptor(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public Response intercept(Chain chain) throws IOException {
        String token = AuthPrefs.getToken(context); // Lấy token từ SharedPreferences
        Request original = chain.request();

        if (token != null) {
            Request requestWithToken = original.newBuilder()
                    .header("Authorization", "Bearer " + token)
                    .build();
            return chain.proceed(requestWithToken);
        }

        return chain.proceed(original);
    }
}
