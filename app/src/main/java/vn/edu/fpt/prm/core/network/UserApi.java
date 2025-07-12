package vn.edu.fpt.prm.core.network;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.PATCH;
import retrofit2.http.Part;

import vn.edu.fpt.prm.features.profile.model.ChangePasswordRequest;
import vn.edu.fpt.prm.features.profile.model.UpdateUserRequest;
import vn.edu.fpt.prm.features.profile.model.TokenResponse;

public interface UserApi {

    @PATCH("user/updateMe")
    Call<Void> updateProfile(
            @Header("Authorization") String token,
            @Body UpdateUserRequest request
    );

    @PATCH("user/updateMyPassword")
    Call<TokenResponse> changePassword(
            @Header("Authorization") String token,
            @Body ChangePasswordRequest request
    );

    @Multipart
    @PATCH("user/updateMe")
    Call<Void> updatePhoto(
            @Header("Authorization") String token,
            @Part MultipartBody.Part photo
    );
}
