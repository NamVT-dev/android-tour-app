package vn.edu.fpt.prm.features.user;

import android.content.Context;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.Retrofit;
import vn.edu.fpt.prm.core.network.ApiClient;
import vn.edu.fpt.prm.features.user.dto.request.ChangePasswordRequest;
import vn.edu.fpt.prm.features.user.dto.request.UpdateUserRequest;
import vn.edu.fpt.prm.features.user.dto.response.ChangePasswordResponse;
import vn.edu.fpt.prm.features.user.dto.response.UserResponse;

public class UserService {
    private final UserApi userApi;

    public UserService(Context context) {
        Retrofit retrofit = ApiClient.getRetrofit(context);
        this.userApi = retrofit.create(UserApi.class);
    }

    public Call<UserResponse> getProfile() {
        return userApi.getProfile();
    }

    public Call<UserResponse> updateProfile(String token, UpdateUserRequest request) {
        return userApi.updateProfile(token, request);
    }

    public Call<ChangePasswordResponse> changePassword(String token, ChangePasswordRequest request) {
        return userApi.changePassword(token, request);
    }

    public Call<UserResponse> updatePhoto(String token, MultipartBody.Part photo) {
        return userApi.updatePhoto(token, photo);
    }
}
