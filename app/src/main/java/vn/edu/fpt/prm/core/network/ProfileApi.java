package vn.edu.fpt.prm.core.network;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;

import vn.edu.fpt.prm.features.booking.Booking;

public interface ProfileApi {
    @GET("user/me")
    Call<List<Booking>> getBookings(@Header("Authorization") String token);
}