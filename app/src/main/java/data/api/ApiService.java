package data.api;

import data.model.Tour;
import data.model.User;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;

public interface ApiService {

    @GET("user/me")
    Call<User> getProfile(@Header("Authorization") String token);

    @GET("tours")
    Call<List<Tour>> getTours(@Header("Authorization") String token);
}
