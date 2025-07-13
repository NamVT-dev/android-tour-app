package vn.edu.fpt.prm.core.network;

import android.content.Context;
import android.content.Intent;

import androidx.annotation.NonNull;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import vn.edu.fpt.prm.core.config.AppConfig;
import vn.edu.fpt.prm.core.prefs.AuthManager;
import vn.edu.fpt.prm.core.utils.Logger;
import vn.edu.fpt.prm.core.widget.Toaster;
import vn.edu.fpt.prm.features.auth.activities.LoginActivity;
import vn.edu.fpt.prm.navigation.Navigator;
import vn.edu.fpt.prm.navigation.Screen;

public class ApiClient {
    private static Retrofit retrofit;

    public static Retrofit getRetrofit(Context context) {
        if (retrofit == null) {
            OkHttpClient client = new OkHttpClient.Builder()
                    .addInterceptor(new AuthInterceptor(context))
                    .addInterceptor(new UnauthorizedInterceptor(context))
                    .connectTimeout(30, TimeUnit.SECONDS)
                    .readTimeout(30, TimeUnit.SECONDS)
                    .build();

            retrofit = new Retrofit.Builder()
                    .baseUrl(AppConfig.BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(client)
                    .build();
        }

        return retrofit;
    }

    static class UnauthorizedInterceptor implements Interceptor {
        private final Context context;

        public UnauthorizedInterceptor(Context context) {
            this.context = context;
        }

        @NonNull
        @Override
        public Response intercept(Chain chain) throws IOException {
            Response response = chain.proceed(chain.request());

            if (response.code() == 401) {
                AuthManager.clear(context); // Clear auth data on 401 Unauthorized
                Logger.warning("ApiClient", "Unauthorized access detected. Clearing auth data.");

                // Notify
                new android.os.Handler(context.getMainLooper()).post(() -> {
                    Toaster.showToast(context, "Unauthorized access. Please log in again.");
                });

                // Redirect to login activity
                Navigator.goToAndFinish(context, Screen.LOGIN);
            }

            return response;
        }
    }
}
