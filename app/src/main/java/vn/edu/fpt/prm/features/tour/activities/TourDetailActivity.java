package vn.edu.fpt.prm.features.tour.activities;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.maps.MapView;

import java.util.ArrayList;

import vn.edu.fpt.prm.R;
import vn.edu.fpt.prm.core.widget.Toaster;
import vn.edu.fpt.prm.features.tour.Tour;
import vn.edu.fpt.prm.features.tour.adapter.GuideAdapter;
import vn.edu.fpt.prm.features.tour.adapter.LocationAdapter;

public class TourDetailActivity extends AppCompatActivity {
    private ImageView imgBanner;
    private TextView tvTourName, tvDescription, tvStartLocation, tvStartDate;
    private ImageButton btnBack;
    private Button btnPayment;
    private RecyclerView rvGuides, rvLocations;

    private GuideAdapter guideAdapter;
    private LocationAdapter locationAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tour_detail);

        bindingView();
        bindingAction();
    }

    private void bindingView() {
        // Bind views from layout
        imgBanner = findViewById(R.id.img_banner);
        tvTourName = findViewById(R.id.tv_tour_name);
        tvDescription = findViewById(R.id.tv_description);
        tvStartLocation = findViewById(R.id.tv_start_location);
        tvStartDate = findViewById(R.id.tv_start_date);
        btnBack = findViewById(R.id.btn_back);
        btnPayment = findViewById(R.id.btn_payment);
        rvGuides = findViewById(R.id.rv_guides);
        rvLocations = findViewById(R.id.rv_locations);

        // Get tour data from intent
        Tour tour = (Tour) getIntent().getSerializableExtra("tour");
        if (tour == null) {
            Toaster.showToast(this, "Tour data not found");
            finish();
            return;
        }

        // Setup RecyclerViews
        guideAdapter = new GuideAdapter(this, new ArrayList<>());
        rvGuides.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        rvGuides.setAdapter(guideAdapter);
        guideAdapter.setGuideList(tour.getGuides());

        locationAdapter = new LocationAdapter();
        rvLocations.setLayoutManager(new LinearLayoutManager(this));
        rvLocations.setAdapter(locationAdapter);

        // Set data to views
        Glide.with(this)
                .load(tour.getImageCover())
                .placeholder(R.drawable.placeholder)
                .error(R.drawable.placeholder)
                .into(imgBanner);

        tvTourName.setText(tour.getName());
        tvDescription.setText(tour.getDescription());
        tvStartLocation.setText(tour.getStartLocation().getDescription());

        if (tour.getStartDates() != null && !tour.getStartDates().isEmpty()) {
            tvStartDate.setText(tour.getStartDates().get(0).substring(0, 10)); // Format: yyyy-MM-dd
        }

        guideAdapter.setGuideList(tour.getGuides());
        locationAdapter.setLocationList(tour.getLocations());
    }

    private void bindingAction() {
        btnBack.setOnClickListener(v -> onBackPressed());

        btnPayment.setOnClickListener(v ->
            Toaster.showToast(this, "Payment button clicked")
        );
    }
}
