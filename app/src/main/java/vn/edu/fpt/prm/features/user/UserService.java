package vn.edu.fpt.prm.features.user;

import android.content.Context;

import java.util.List;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.Retrofit;
import vn.edu.fpt.prm.core.network.ApiClient;
import vn.edu.fpt.prm.features.booking.Booking;
import vn.edu.fpt.prm.features.user.dto.request.ChangePasswordRequest;
import vn.edu.fpt.prm.features.user.dto.request.UpdateUserRequest;
import vn.edu.fpt.prm.features.user.dto.response.TokenResponse;
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

    public Call<Void> updateProfile(String token, UpdateUserRequest request) {
        return userApi.updateProfile(token, request);
    }

    public Call<TokenResponse> changePassword(String token, ChangePasswordRequest request) {
        return userApi.changePassword(token, request);
    }

    public Call<Void> updatePhoto(String token, MultipartBody.Part photo) {
        return userApi.updatePhoto(token, photo);
    }

    public Call<List<Booking>> getBookings(String token) {
        return userApi.getBookings(token);
    }
}
