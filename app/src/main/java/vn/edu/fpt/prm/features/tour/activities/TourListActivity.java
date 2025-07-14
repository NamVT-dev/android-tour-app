package vn.edu.fpt.prm.features.tour.activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Rect;
import android.location.Location;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import vn.edu.fpt.prm.R;
import vn.edu.fpt.prm.core.utils.Logger;
import vn.edu.fpt.prm.core.widget.Toaster;
import vn.edu.fpt.prm.features.game.activities.GameHomeActivity;
import vn.edu.fpt.prm.features.tour.Tour;
import vn.edu.fpt.prm.features.tour.TourService;
import vn.edu.fpt.prm.features.tour.adapter.TourAdapter;
import vn.edu.fpt.prm.features.tour.dto.response.TourListResponse;

public class TourListActivity extends AppCompatActivity {
    private EditText etSearch;
    private ImageButton filterButton;
    private RecyclerView recyclerView;
    private ImageView gameIcon;

    private TourAdapter tourAdapter;
    private List<Tour> allTours = new ArrayList<>();
    private List<Tour> filteredTours = new ArrayList<>();

    private FusedLocationProviderClient fusedLocationClient;
    private LocationRequest locationRequest;
    private double currentLat = 0.0;
    private double currentLng = 0.0;

    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1001;
    private double pendingDistance = -1;

    private TourService tourService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Logger.debug("TourListActivity", "onCreate called");

        setContentView(R.layout.activity_tour_list);

        Logger.debug("TourListActivity", "Tour List Activity Started");

        tourService = new TourService(this);

        bindingView();
        bindingAction();
    }

    private void bindingView() {
        etSearch = findViewById(R.id.et_search);
        filterButton = findViewById(R.id.btn_filter);
        recyclerView = findViewById(R.id.recycler_view_tours);
        gameIcon = findViewById(R.id.img_game_icon);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        tourAdapter = new TourAdapter(this, new ArrayList<>(), tour -> {
            // Handle tour item click
            Logger.debug("TourListActivity", "Tour clicked: " + tour.getName());

            Intent intent = new Intent(this, TourDetailActivity.class);
            intent.putExtra("tour", tour);
            this.startActivity(intent);

            Logger.debug("TourListActivity", "Navigating to TourDetailActivity with tour: " + tour.getName());
        });
        recyclerView.setAdapter(tourAdapter);

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
    }

    private void bindingAction() {
        handleGetAllTours(); // Fetch all tours from the server
        setupSearchTextListener(); // Set up search functionality
        filterButton.setOnClickListener(v -> showDistanceFilterDialog()); // Set up filter button click listener
        gameIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(TourListActivity.this, GameHomeActivity.class);
                startActivity(intent);
            }
        });
    }

    private void setupSearchTextListener() {
        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filterTours(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });
    }

    // Handle getting all tours from the server
    private void handleGetAllTours() {
        Logger.debug("TourListActivity", "Loading tours...");

        tourService.getAllTours().enqueue(new Callback<TourListResponse>() {
            @Override
            public void onResponse(@NonNull Call<TourListResponse> call, @NonNull Response<TourListResponse> response) {
                Logger.debug("TourListActivity", "HTTP Status Code: " + response.code());
                if (response.isSuccessful() && response.body() != null) {
                    TourListResponse tourListResponse = response.body();
                    allTours = tourListResponse.getData().getData();
                    filteredTours = new ArrayList<>(allTours);
                    tourAdapter.setTourList(filteredTours);
                    Logger.debug("TourListActivity", "Tours loaded: " + allTours.size());
                } else {
                    String errorMsg = "Failed to load tours: " + (response.body() != null ? response.body().getStatus() : "Unknown error");
                    Logger.error("TourListActivity", errorMsg);
                    Toaster.showToast(TourListActivity.this, errorMsg);
                }
            }

            @Override
            public void onFailure(@NonNull Call<TourListResponse> call, @NonNull Throwable t) {
                Logger.error("TourListActivity", "Error loading tours: " + t.getMessage());
                Toaster.showToast(TourListActivity.this, "Error loading tours: " + t.getMessage());
            }
        });
    }

    // Handle search functionality
    private void filterTours(String query) {
        filteredTours.clear();
        for (Tour tour : allTours) {
            if (
                tour.getName().toLowerCase().contains(query.toLowerCase()) ||
                tour.getStartLocation().getAddress().toLowerCase().contains(query.toLowerCase())
            ) {
                filteredTours.add(tour);
            }
        }
        tourAdapter.setTourList(filteredTours);
    }

    // Show dialog to filter tours by distance
    private void showDistanceFilterDialog() {
        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(this);
        builder.setTitle("Find tour near you");
        builder.setMessage("Enter distance you want to find (km):");

        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        input.setHint("Enter distance (km)");
        input.setPadding(16, 16, 16, 16);
        builder.setView(input);

        builder.setPositiveButton("Find", (dialog, which) -> {
            String distanceStr = input.getText().toString();
            Logger.debug("TourListActivity", "Distance entered: " + distanceStr);
            if (!distanceStr.isEmpty()) {
                double maxKm = Double.parseDouble(distanceStr);
                Logger.debug("TourListActivity", "Max distance to filter: " + maxKm);
                requestLocationPermissionAndFilter(maxKm);
            } else {
                Logger.error("TourListActivity", "Invalid distance entered");
                Toaster.showToast(TourListActivity.this, "Please enter a valid distance");
            }
        });

        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());
        builder.show();
    }

    private void requestLocationPermissionAndFilter(double maxKm) {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            Logger.debug("TourListActivity", "Location permission not granted, requesting permission");

            // If permission is not granted, request it
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    LOCATION_PERMISSION_REQUEST_CODE);

            // Store the pending distance to filter later
            this.pendingDistance = maxKm;
        } else {
            Logger.debug("TourListActivity", "Location permission already granted");
            // If permission is already granted, get current location and filter tours
            getCurrentLocationAndFilter(maxKm);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        Logger.debug("TourListActivity", "onRequestPermissionsResult called with requestCode: " + requestCode);

        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            Logger.debug("TourListActivity", "Location permission request result received");
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (pendingDistance > 0) {
                    getCurrentLocationAndFilter(pendingDistance);
                    pendingDistance = -1;
                }
            } else {
                Toaster.showToast(this, "Location permission is required to filter tours by distance");
            }
        }
    }

    private void getCurrentLocationAndFilter(double maxKm) {
        Logger.debug("TourListActivity", "getCurrentLocationAndFilter called with maxKm: " + maxKm);
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                    this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    1001
            );
            return;
        }

        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        if (location != null) {
                            double latitude = location.getLatitude();
                            double longitude = location.getLongitude();
                            Logger.debug("TourListActivity", "Current location: Lat = " + latitude + ", Lng = " + longitude);

                            // Gọi API lọc tour gần
                            tourService.getToursWithinDistance((int) maxKm, currentLat, currentLng)
                                    .enqueue(new Callback<TourListResponse>() {
                                        @Override
                                        public void onResponse(@NonNull Call<TourListResponse> call, @NonNull Response<TourListResponse> response) {
                                            if (response.isSuccessful() && response.body() != null) {
                                                List<Tour> tours = response.body().getData().getData();

                                                Logger.debug("TourListActivity", "Tours within " + maxKm + " km: " + tours.size());

                                                tourAdapter.setTourList(tours);
                                            } else {
                                                Toaster.showToast(TourListActivity.this, "Don't have any tour within " + maxKm + " km");
                                            }
                                        }

                                        @Override
                                        public void onFailure(Call<TourListResponse> call, Throwable t) {
                                            Toaster.showToast(TourListActivity.this, "Error: " + t.getMessage());
                                        }
                                    });
                        } else {
                            Toaster.showToast(TourListActivity.this, "Unable to get current location. Please try again.");
                            Logger.error("TourListActivity", "Current location is null");
                        }
                    }
                });
    }
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        // Hide keyboard when touching outside EditText
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            View focusedView = getCurrentFocus();
            if (focusedView instanceof EditText) {
                Rect outRect = new Rect();
                focusedView.getGlobalVisibleRect(outRect);
                if(!outRect.contains((int) ev.getRawX(), (int) ev.getRawY())) {
                    focusedView.clearFocus(); // Clear focus from EditText
                    // Hide keyboard
                    InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                    if (imm != null) {
                        imm.hideSoftInputFromWindow(focusedView.getWindowToken(), 0);
                    }
                }
            }
        }
        return super.dispatchTouchEvent(ev);
    }
}
