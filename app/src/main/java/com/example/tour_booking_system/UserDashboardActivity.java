package com.example.tour_booking_system;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class UserDashboardActivity extends AppCompatActivity {

    private static final String PREFS_NAME = "TourBookingPrefs";
    private static final String KEY_USER_NAME = "user_name";
    private static final String KEY_USER_EMAIL = "user_email";
    
    private TextView tvWelcome, tvUserEmail;
    private Button btnBrowseTours, btnMyBookings, btnProfile, btnLogout;
    private RecyclerView rvRecentTours;
    private BottomNavigationView bottomNav;
    
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_dashboard);
        
        initViews();
        initSharedPreferences();
        setupUserInfo();
        setupListeners();
        setupBottomNavigation();
    }

    private void initViews() {
        tvWelcome = findViewById(R.id.tv_welcome);
        tvUserEmail = findViewById(R.id.tv_user_email);
        btnBrowseTours = findViewById(R.id.btn_browse_tours);
        btnMyBookings = findViewById(R.id.btn_my_bookings);
        btnProfile = findViewById(R.id.btn_profile);
        btnLogout = findViewById(R.id.btn_logout);
        rvRecentTours = findViewById(R.id.rv_recent_tours);
        bottomNav = findViewById(R.id.bottom_navigation);
    }

    private void initSharedPreferences() {
        sharedPreferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
    }

    private void setupUserInfo() {
        String userName = sharedPreferences.getString(KEY_USER_NAME, "User");
        String userEmail = sharedPreferences.getString(KEY_USER_EMAIL, "");
        
        tvWelcome.setText("Welcome, " + userName + "!");
        tvUserEmail.setText(userEmail);
    }

    private void setupListeners() {
        btnBrowseTours.setOnClickListener(v -> {
            // Navigate to tour browsing screen
            Toast.makeText(this, "Browse Tours - Coming Soon!", Toast.LENGTH_SHORT).show();
        });

        btnMyBookings.setOnClickListener(v -> {
            // Navigate to my bookings screen
            Toast.makeText(this, "My Bookings - Coming Soon!", Toast.LENGTH_SHORT).show();
        });

        btnProfile.setOnClickListener(v -> {
            // Navigate to profile screen
            Toast.makeText(this, "Profile - Coming Soon!", Toast.LENGTH_SHORT).show();
        });

        btnLogout.setOnClickListener(v -> {
            logout();
        });
    }

    private void setupBottomNavigation() {
        bottomNav.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.nav_home) {
                // Already on home
                return true;
            } else if (itemId == R.id.nav_tours) {
                Toast.makeText(this, "Tours - Coming Soon!", Toast.LENGTH_SHORT).show();
                return true;
            } else if (itemId == R.id.nav_bookings) {
                Toast.makeText(this, "Bookings - Coming Soon!", Toast.LENGTH_SHORT).show();
                return true;
            } else if (itemId == R.id.nav_profile) {
                Toast.makeText(this, "Profile - Coming Soon!", Toast.LENGTH_SHORT).show();
                return true;
            }
            return false;
        });
    }

    private void logout() {
        // Clear user data
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
        
        // Navigate to login
        Intent intent = new Intent(this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
} 