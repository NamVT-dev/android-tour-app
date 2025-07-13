package vn.edu.fpt.prm.features.user;

import android.content.Context;

import retrofit2.Call;
import retrofit2.Retrofit;
import vn.edu.fpt.prm.core.network.ApiClient;
import vn.edu.fpt.prm.features.user.dto.UserResponse;

public class UserService {
    private final UserApi userApi;

    public UserService(Context context) {
        Retrofit retrofit = ApiClient.getRetrofit(context);
        this.userApi = retrofit.create(UserApi.class);
    }

    public Call<UserResponse> getProfile() {
        return userApi.getProfile();
    }


}
