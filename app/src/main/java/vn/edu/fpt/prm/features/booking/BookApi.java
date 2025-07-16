package vn.edu.fpt.prm.features.booking;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import vn.edu.fpt.prm.features.booking.dto.response.BookingListResponse;

public interface BookApi {
    @GET("bookings/my-booking")
    Call<BookingListResponse> getBookings(@Header("Authorization") String token);
}
