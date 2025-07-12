package ui.tourlist;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tour_booking_system.R;

import java.util.List;

import data.model.Tour;
import data.respository.TourRepository;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TourListActivity extends AppCompatActivity {

    private RecyclerView recyclerTour;
    private TourRepository tourRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tour_list);

        recyclerTour = findViewById(R.id.recyclerTour);
        recyclerTour.setLayoutManager(new LinearLayoutManager(this));

        tourRepository = new TourRepository();

        // Lấy token từ SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("MyAppPrefs", MODE_PRIVATE);
        String token = sharedPreferences.getString("token", null);

        if (token == null) {
            Toast.makeText(this, "Chưa đăng nhập!", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        getTourList(token);
    }

    private void getTourList(String token) {
        tourRepository.getTours(token).enqueue(new Callback<List<Tour>>() {
            @Override
            public void onResponse(Call<List<Tour>> call, Response<List<Tour>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Tour> tourList = response.body();
                    TourAdapter adapter = new TourAdapter(tourList);
                    recyclerTour.setAdapter(adapter);
                } else {
                    Toast.makeText(TourListActivity.this, "Không lấy được danh sách tour", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Tour>> call, Throwable t) {
                Toast.makeText(TourListActivity.this, "Lỗi: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
