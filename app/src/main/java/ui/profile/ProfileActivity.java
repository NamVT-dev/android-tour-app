package ui.profile;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tour_booking_system.R;
import com.squareup.picasso.Picasso;

import java.util.List;

import data.model.Booking;
import data.model.User;
import data.respository.UserRepository;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileActivity extends AppCompatActivity {

    private TextView nameTextView, emailTextView;
    private ImageView avatarImageView;
    private RecyclerView recyclerBooking;
    private UserRepository userRepository;
    private String token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        nameTextView = findViewById(R.id.textName);
        emailTextView = findViewById(R.id.textEmail);
        avatarImageView = findViewById(R.id.imageAvatar);
        recyclerBooking = findViewById(R.id.recyclerBooking);
        recyclerBooking.setLayoutManager(new LinearLayoutManager(this));

        SharedPreferences sharedPreferences = getSharedPreferences("MyAppPrefs", MODE_PRIVATE);
        token = sharedPreferences.getString("token", null);

        if (token == null) {
            Toast.makeText(this, "Chưa đăng nhập!", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        token = "Bearer " + token;
        userRepository = new UserRepository();
        getUserProfile();
    }

    private void getUserProfile() {
        userRepository.getUserProfile(token).enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful() && response.body() != null) {
                    User user = response.body();
                    nameTextView.setText(user.getName());
                    emailTextView.setText(user.getEmail());

                    if (user.getAvatar() != null && !user.getAvatar().isEmpty()) {
                        Picasso.get()
                                .load(user.getAvatar())
                                .placeholder(R.drawable.ic_launcher_foreground)
                                .error(R.drawable.ic_launcher_foreground)
                                .into(avatarImageView);
                    }

                    List<Booking> bookings = user.getBookings();
                    if (bookings != null) {
                        BookingAdapter adapter = new BookingAdapter(bookings);
                        recyclerBooking.setAdapter(adapter);
                    }

                } else {
                    Toast.makeText(ProfileActivity.this, "Lỗi lấy thông tin người dùng", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Toast.makeText(ProfileActivity.this, "Lỗi mạng: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
