package vn.edu.fpt.prm.features.booking.activities;

import android.os.Bundle;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import vn.edu.fpt.prm.R;
import vn.edu.fpt.prm.core.prefs.AuthManager;
import vn.edu.fpt.prm.core.utils.Logger;
import vn.edu.fpt.prm.core.widget.Toaster;
import vn.edu.fpt.prm.features.booking.Booking;
import vn.edu.fpt.prm.features.booking.BookingService;
import vn.edu.fpt.prm.features.booking.adapter.BookingAdapter;
import vn.edu.fpt.prm.features.booking.dto.response.BookingListResponse;

public class BookingListActivity extends AppCompatActivity {
    private ImageView btnBack;
    private RecyclerView rvBookings;
    private BookingAdapter bookingAdapter;
    private BookingService bookingService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Logger.debug("BookingListActivity", "onCreate called");

        setContentView(R.layout.activity_booking_list);

        bookingService = new BookingService(this);

        bindingView();
        bindingAction();
    }

    private void bindingView() {
        rvBookings = findViewById(R.id.rvBookings);
        btnBack = findViewById(R.id.btnBack);
        rvBookings.setLayoutManager(new LinearLayoutManager(this));
        bookingAdapter = new BookingAdapter(new ArrayList<>());
        rvBookings.setAdapter(bookingAdapter);
    }

    private void bindingAction() {
        btnBack.setOnClickListener(v -> finish());
        handleLoadBookings();
    }

    private void handleLoadBookings() {
        String token = AuthManager.getToken(this);

        if (token == null || token.isEmpty()) {
            Toaster.showToast(this, "You need to log in first");
            finish();
            return;
        }

        bookingService.getBookings(token).enqueue(new Callback<BookingListResponse>() {
            @Override
            public void onResponse(@NonNull Call<BookingListResponse> call, @NonNull Response<BookingListResponse> response) {
                Logger.debug("BookingListActivity", "Response received: " + response.code());
                if (response.isSuccessful() && response.body() != null) {
                    Logger.debug("BookingListActivity", "Bookings loaded successfully" + response.body().getData());
                    BookingListResponse bookingListResponse = response.body();
                    List<Booking> bookings = bookingListResponse.getData();
                    bookingAdapter.setData(bookings);
                    Logger.debug("BookingListActivity", "Bookings loaded: " + bookings.size());
                } else {
                    Logger.error("BookingListActivity", "Failed to load bookings: " + response.code());
                    Toaster.showToast(BookingListActivity.this, "Failed to load bookings");
                }
            }

            @Override
            public void onFailure(@NonNull Call<BookingListResponse> call, @NonNull Throwable t) {
                Logger.error("BookingListActivity", "Error loading bookings: " + t.getMessage());
                Toaster.showToast(BookingListActivity.this, "Error loading bookings");
            }
        });
    }
}
