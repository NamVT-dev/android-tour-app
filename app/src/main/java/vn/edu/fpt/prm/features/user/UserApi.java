package vn.edu.fpt.prm.features.user;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.PATCH;
import retrofit2.http.Part;

import vn.edu.fpt.prm.features.user.dto.request.ChangePasswordRequest;
import vn.edu.fpt.prm.features.user.dto.request.UpdateUserRequest;
import vn.edu.fpt.prm.features.user.dto.response.ChangePasswordResponse;
import vn.edu.fpt.prm.features.user.dto.response.UserResponse;

public interface UserApi {

    @PATCH("user/updateMe")
    Call<UserResponse> updateProfile(
            @Header("Authorization") String token,
            @Body UpdateUserRequest request
    );

    @PATCH("user/updateMyPassword")
    Call<ChangePasswordResponse> changePassword(
            @Header("Authorization") String token,
            @Body ChangePasswordRequest request
    );

    @Multipart
    @PATCH("user/updateMe")
    Call<UserResponse> updatePhoto(
            @Header("Authorization") String token,
            @Part MultipartBody.Part photo
    );

    @GET("user/me")
    Call<UserResponse> getProfile();
}
