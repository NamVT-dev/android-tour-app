package vn.edu.fpt.prm.features.auth;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
import vn.edu.fpt.prm.features.auth.dto.request.LoginRequest;
import vn.edu.fpt.prm.features.auth.dto.request.SignupRequest;
import vn.edu.fpt.prm.features.auth.dto.response.AuthResponse;

public interface AuthApi {
    @POST("user/login")
    Call<AuthResponse> login(@Body LoginRequest request);

    @POST("user/signup")
    Call<AuthResponse> signup(@Body SignupRequest request);
}
