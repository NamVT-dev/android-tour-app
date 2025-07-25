package vn.edu.fpt.prm.features.tour.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import vn.edu.fpt.prm.R;
import vn.edu.fpt.prm.core.prefs.AuthManager;
import vn.edu.fpt.prm.core.widget.Toaster;
import vn.edu.fpt.prm.features.checkout.CheckoutService;
import vn.edu.fpt.prm.features.checkout.activities.CheckoutActivity;
import vn.edu.fpt.prm.features.checkout.dto.response.CheckoutResponse;
import vn.edu.fpt.prm.features.tour.Location;
import vn.edu.fpt.prm.features.tour.Tour;
import vn.edu.fpt.prm.features.tour.adapter.GuideAdapter;
import vn.edu.fpt.prm.features.tour.adapter.LocationAdapter;

public class TourDetailActivity extends AppCompatActivity implements OnMapReadyCallback {
    private ImageView imgBanner;
    private TextView tvTourName, tvDescription, tvStartLocation, tvStartDate, tv_rating, tv_duration;
    private ImageButton btnBack;
    private Button btnPayment;
    private RecyclerView rvGuides, rvLocations;
    private MapView mapView;
    private GoogleMap googleMap;
    private GuideAdapter guideAdapter;
    private LocationAdapter locationAdapter;
    private CheckoutService checkoutService = new CheckoutService(this);
    private Tour tour;

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
        tvStartLocation = findViewById(R.id.tv_start_location);
        tv_rating = findViewById(R.id.tv_rating);
        tv_duration = findViewById(R.id.tv_duration);
        tvDescription = findViewById(R.id.tv_description);
        tvStartDate = findViewById(R.id.tv_start_date);
        btnBack = findViewById(R.id.btn_back);
        btnPayment = findViewById(R.id.btn_payment);
        rvGuides = findViewById(R.id.rv_guides);
        rvLocations = findViewById(R.id.rv_locations);

        // Initialize MapView
        mapView = findViewById(R.id.tour_map);
        mapView.onCreate(null);
        mapView.getMapAsync(this);

        // Get tour data from intent
        tour = (Tour) getIntent().getSerializableExtra("tour");
        if (tour == null) {
            Toaster.showToast(this, "Tour data not found");
            finish();
            return;
        }

        // Setup RecyclerViews
        guideAdapter = new GuideAdapter(this, new ArrayList<>());
        rvGuides.setLayoutManager(new LinearLayoutManager(this));
        rvGuides.setAdapter(guideAdapter);

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
        tv_rating.setText(String.valueOf(tour.getRatingsAverage()));
        tv_duration.setText(tour.getDuration() + " days");

        if (tour.getStartDates() != null && !tour.getStartDates().isEmpty()) {
            tvStartDate.setText(tour.getStartDates().get(0).substring(0, 10)); // Format: yyyy-MM-dd
        }

        guideAdapter.setGuideList(tour.getGuides());
        locationAdapter.setLocationList(tour.getLocations());

        String priceText = vn.edu.fpt.prm.core.utils.Formatter.formatCurrency(tour.getPrice().intValue());
        btnPayment.setText("Only " + priceText);
    }

    private void bindingAction() {
        btnBack.setOnClickListener(v -> onBackPressed());

        btnPayment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String token = AuthManager.getToken(TourDetailActivity.this);
                String tourId = tour.getId();
                getCheckoutUrl(token, tourId);
            }
        });
    }

    private void getCheckoutUrl(String token, String id){
        checkoutService.getCheckoutSession(token, id).enqueue(new Callback<CheckoutResponse>() {
            @Override
            public void onResponse(Call<CheckoutResponse> call, Response<CheckoutResponse> response) {
                if (response.isSuccessful() && response.body() != null){
                    String url = response.body().getSession().getUrl();
                    Intent intent = new Intent(TourDetailActivity.this, CheckoutActivity.class);
                    intent.putExtra("checkout_url", url);
                    startActivity(intent);
                }
            }

            @Override
            public void onFailure(Call<CheckoutResponse> call, Throwable t) {
                t.printStackTrace();
                Toast.makeText(TourDetailActivity.this, "Không thể kết nối API", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onMapReady(@NonNull GoogleMap map) {
        googleMap = map;

        if (tour == null) return;

        // 1. Start Location (Red marker)
        LatLng startLatLng = new LatLng(
                tour.getStartLocation().getCoordinates().get(1),
                tour.getStartLocation().getCoordinates().get(0)
        );
        MarkerOptions startMarker = new MarkerOptions()
                .position(startLatLng)
                .title(tour.getStartLocation().getAddress())
                .snippet(tour.getStartLocation().getDescription())
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));

        googleMap.addMarker(startMarker);

        // 2. Other Locations (Blue marker)
        for (Location location : tour.getLocations()) {
            LatLng latLng = new LatLng(
                    location.getCoordinates().get(1),
                    location.getCoordinates().get(0)
            );

            MarkerOptions marker = new MarkerOptions()
                    .position(latLng)
                    .title(location.getAddress())
                    .snippet(location.getDescription())
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));

            googleMap.addMarker(marker);
        }

        // 3. Move camera to start location
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(startLatLng, 13f));

        // 4. Enable info window click (optional)
        googleMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {
            @Override
            public View getInfoWindow(@NonNull Marker marker) {
                return null;
            }

            @Override
            public View getInfoContents(@NonNull Marker marker) {
                View view = getLayoutInflater().inflate(R.layout.item_marker_info, null);
                TextView tvTitle = view.findViewById(R.id.tv_marker_title);
                TextView tvSnippet = view.findViewById(R.id.tv_marker_snippet);
                tvTitle.setText(marker.getTitle());
                tvSnippet.setText(marker.getSnippet());
                return view;
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }
}
