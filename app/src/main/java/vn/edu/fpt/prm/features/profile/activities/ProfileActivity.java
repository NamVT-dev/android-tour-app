package vn.edu.fpt.prm.features.profile.activities;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import vn.edu.fpt.prm.R;
import vn.edu.fpt.prm.core.utils.AuthPrefs;
import vn.edu.fpt.prm.core.network.ApiClient;
import vn.edu.fpt.prm.core.network.ProfileApi;
import vn.edu.fpt.prm.features.booking.Booking;

public class ProfileActivity extends AppCompatActivity {

    private TextView txtName, txtEmail;
    private ImageView imgAvatar;
    private TableLayout bookingTable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        txtName = findViewById(R.id.txtName);
        txtEmail = findViewById(R.id.txtEmail);
        imgAvatar = findViewById(R.id.imgAvatar);
        bookingTable = findViewById(R.id.bookingTable);
        Button btnEdit = findViewById(R.id.btnEditProfile);


        String token = AuthPrefs.getToken(this);
        String userName = AuthPrefs.getUserName(this);
        String userEmail = AuthPrefs.getUserEmail(this);

        if (token == null || token.isEmpty()) {
            Toast.makeText(this, "Bạn chưa đăng nhập!", Toast.LENGTH_LONG).show();
            finish();
            return;
        }


        txtName.setText(userName);
        txtEmail.setText(userEmail);


        fetchBookings("Bearer " + token);

        btnEdit.setOnClickListener(v -> {
            // TODO: xử lý chỉnh sửa thông tin cá nhân
        });
    }

    private void fetchBookings(String bearerToken) {
        ProfileApi api = ApiClient.createService(ProfileApi.class);
        api.getBookings(bearerToken).enqueue(new Callback<List<Booking>>() {
            @Override
            public void onResponse(Call<List<Booking>> call, Response<List<Booking>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    for (Booking booking : response.body()) {
                        addBookingRow(
                                booking.getTourName(),
                                String.valueOf(booking.getPrice()),
                                booking.isPaid() ? "Yes" : "No",
                                booking.getCreatedAt()
                        );
                    }
                } else {

                    Toast.makeText(ProfileActivity.this, "Lỗi tải dữ liệu từ server", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Booking>> call, Throwable t) {
                t.printStackTrace();
                Toast.makeText(ProfileActivity.this, "Không thể kết nối API", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void addBookingRow(String name, String price, String paid, String createdAt) {
        TableRow row = new TableRow(this);
        row.addView(makeCell(name));
        row.addView(makeCell(price));
        row.addView(makeCell(paid));
        row.addView(makeCell(createdAt));
        bookingTable.addView(row);
    }

    private TextView makeCell(String text) {
        TextView tv = new TextView(this);
        tv.setText(text);
        tv.setTextColor(getResources().getColor(android.R.color.white));
        tv.setPadding(8, 8, 8, 8);
        return tv;
    }
}
