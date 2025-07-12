package com.example.tour_booking_system;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "LoginActivity";
    private static final String API_URL = "https://acf89a9c82d4.ngrok-free.app/user/login";
    private static final String PREFS_NAME = "TourBookingPrefs";
    private static final String KEY_TOKEN = "auth_token";
    private static final String KEY_USER_ROLE = "user_role";
    private static final String KEY_USER_NAME = "user_name";
    private static final String KEY_USER_EMAIL = "user_email";
    
    private EditText etEmail, etPassword;
    private Button btnLogin;
    private ProgressBar progressBar;
    private TextView tvSignupLink;
    
    private ExecutorService executorService;
    private OkHttpClient httpClient;
    private Gson gson;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        
        // Initialize components
        initViews();
        initNetworkComponents();
        initSharedPreferences();
        setupListeners();
        
        // Check if user is already logged in
        checkExistingLogin();
    }

    private void initViews() {
        etEmail = findViewById(R.id.et_email);
        etPassword = findViewById(R.id.et_password);
        btnLogin = findViewById(R.id.btn_login);
        progressBar = findViewById(R.id.progress_bar);
        tvSignupLink = findViewById(R.id.tv_signup_link);
    }

    private void initNetworkComponents() {
        executorService = Executors.newSingleThreadExecutor();
        httpClient = new OkHttpClient();
        gson = new Gson();
    }

    private void initSharedPreferences() {
        sharedPreferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
    }

    private void setupListeners() {
        btnLogin.setOnClickListener(v -> {
            if (validateInputs()) {
                performLogin();
            }
        });

        tvSignupLink.setOnClickListener(v -> {
            Intent intent = new Intent(this, SignupActivity.class);
            startActivity(intent);
            finish();
        });
    }

    private void checkExistingLogin() {
        String token = sharedPreferences.getString(KEY_TOKEN, null);
        if (token != null) {
            // User is already logged in, navigate to appropriate screen
            navigateBasedOnRole();
        }
    }

    private boolean validateInputs() {
        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString();

        if (TextUtils.isEmpty(email)) {
            etEmail.setError(getString(R.string.email_required));
            return false;
        }

        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            etEmail.setError(getString(R.string.valid_email_required));
            return false;
        }

        if (TextUtils.isEmpty(password)) {
            etPassword.setError(getString(R.string.password_required));
            return false;
        }

        return true;
    }

    private void performLogin() {
        showLoading(true);
        
        executorService.execute(() -> {
            try {
                // Create request body
                JsonObject requestBody = new JsonObject();
                requestBody.addProperty("email", etEmail.getText().toString().trim());
                requestBody.addProperty("password", etPassword.getText().toString());

                RequestBody body = RequestBody.create(
                    MediaType.get("application/json; charset=utf-8"),
                    gson.toJson(requestBody)
                );

                Request request = new Request.Builder()
                    .url(API_URL)
                    .post(body)
                    .addHeader("Content-Type", "application/json")
                    .build();

                Response response = httpClient.newCall(request).execute();
                String responseBody = response.body().string();

                runOnUiThread(() -> {
                    showLoading(false);
                    handleLoginResponse(response.code(), responseBody);
                });

            } catch (IOException e) {
                Log.e(TAG, "Network error", e);
                runOnUiThread(() -> {
                    showLoading(false);
                    Toast.makeText(LoginActivity.this, 
                        getString(R.string.network_error) + e.getMessage(), 
                        Toast.LENGTH_LONG).show();
                });
            }
        });
    }

    private void handleLoginResponse(int statusCode, String responseBody) {
        try {
            if (statusCode == 200 || statusCode == 201) {
                // Success response
                JsonObject response = gson.fromJson(responseBody, JsonObject.class);
                if (response.has("status") && "success".equals(response.get("status").getAsString())) {
                    // Save user data
                    if (response.has("token")) {
                        String token = response.get("token").getAsString();
                        saveUserData(token, response.getAsJsonObject("data").getAsJsonObject("user"));
                    }
                    
                    Toast.makeText(this, getString(R.string.login_successful), Toast.LENGTH_SHORT).show();
                    navigateBasedOnRole();
                }
            } else {
                // Error response
                JsonObject errorResponse = gson.fromJson(responseBody, JsonObject.class);
                String errorMessage = getString(R.string.login_failed);
                if (errorResponse.has("message")) {
                    errorMessage = errorResponse.get("message").getAsString();
                }
                Toast.makeText(this, errorMessage, Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            Log.e(TAG, "Error parsing response", e);
            Toast.makeText(this, getString(R.string.error_processing_response), Toast.LENGTH_LONG).show();
        }
    }

    private void saveUserData(String token, JsonObject user) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(KEY_TOKEN, token);
        editor.putString(KEY_USER_ROLE, user.get("role").getAsString());
        editor.putString(KEY_USER_NAME, user.get("name").getAsString());
        editor.putString(KEY_USER_EMAIL, user.get("email").getAsString());
        editor.apply();
    }

    private void navigateBasedOnRole() {
        String userRole = sharedPreferences.getString(KEY_USER_ROLE, "user");
        
        Intent intent;
        switch (userRole) {
//            case "admin":
//                intent = new Intent(this, AdminDashboardActivity.class);
//                break;
//            case "guide":
//                intent = new Intent(this, GuideDashboardActivity.class);
//                break;
            case "user":
            default:
                intent = new Intent(this, UserDashboardActivity.class);
                break;
        }
        
        startActivity(intent);
        finish();
    }

    private void showLoading(boolean show) {
        progressBar.setVisibility(show ? View.VISIBLE : View.GONE);
        btnLogin.setEnabled(!show);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (executorService != null) {
            executorService.shutdown();
        }
    }
} 