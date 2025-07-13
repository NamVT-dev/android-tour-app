package vn.edu.fpt.prm.features.user;

import retrofit2.Call;
import retrofit2.http.GET;
import vn.edu.fpt.prm.features.user.dto.UserResponse;

public interface UserApi {
    @GET("user/me")
    Call<UserResponse> getProfile();
}
