package vn.edu.fpt.prm.features.auth;

import android.content.Context;

import retrofit2.Call;
import retrofit2.Retrofit;
import vn.edu.fpt.prm.core.network.ApiClient;
import vn.edu.fpt.prm.features.auth.dto.request.LoginRequest;
import vn.edu.fpt.prm.features.auth.dto.request.SignupRequest;
import vn.edu.fpt.prm.features.auth.dto.response.AuthResponse;

public class AuthService {
    private final AuthApi authApi;

    public AuthService(Context context) {
        Retrofit retrofit = ApiClient.getRetrofit(context);
        this.authApi = retrofit.create(AuthApi.class);
    }

    public Call<AuthResponse> login(String email, String password) {
        LoginRequest request = new LoginRequest(email, password);
        return authApi.login(request);
    }

    public Call<AuthResponse> signup(String name, String email, String password, String passwordConfirm) {
        SignupRequest request = new SignupRequest(name, email, password, passwordConfirm);
        return authApi.signup(request);
    }
}
