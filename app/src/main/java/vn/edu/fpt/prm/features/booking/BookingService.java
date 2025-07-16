package vn.edu.fpt.prm.features.booking;

import android.content.Context;

import retrofit2.Call;
import retrofit2.Retrofit;
import vn.edu.fpt.prm.core.network.ApiClient;
import vn.edu.fpt.prm.features.booking.dto.response.BookingListResponse;

public class BookingService {
    private final BookApi bookApi;

    public BookingService(Context context) {
        Retrofit retrofit = ApiClient.getRetrofit(context);
        this.bookApi = retrofit.create(BookApi.class);
    }

    public Call<BookingListResponse> getBookings(String token) {
        return bookApi.getBookings(token);
    }
}
