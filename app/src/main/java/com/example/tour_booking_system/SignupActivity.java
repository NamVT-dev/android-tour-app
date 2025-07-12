package com.example.tour_booking_system;

import android.content.Intent;
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

public class SignupActivity extends AppCompatActivity {

    private static final String TAG = "SignupActivity";
    private static final String API_URL = "https://acf89a9c82d4.ngrok-free.app/user/signup";
    
    private EditText etName, etEmail, etPassword, etPasswordConfirm;
    private Button btnSignup;
    private ProgressBar progressBar;
    private TextView tvLoginLink;
    
    private ExecutorService executorService;
    private OkHttpClient httpClient;
    private Gson gson;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        
        // Initialize components
        initViews();
        initNetworkComponents();
        setupListeners();
    }

    private void initViews() {
        etName = findViewById(R.id.et_name);
        etEmail = findViewById(R.id.et_email);
        etPassword = findViewById(R.id.et_password);
        etPasswordConfirm = findViewById(R.id.et_password_confirm);
        btnSignup = findViewById(R.id.btn_signup);
        progressBar = findViewById(R.id.progress_bar);
        tvLoginLink = findViewById(R.id.tv_login_link);
    }

    private void initNetworkComponents() {
        executorService = Executors.newSingleThreadExecutor();
        httpClient = new OkHttpClient();
        gson = new Gson();
    }

    private void setupListeners() {
        btnSignup.setOnClickListener(v -> {
            if (validateInputs()) {
                performSignup();
            }
        });

        tvLoginLink.setOnClickListener(v -> {
            // Navigate to login activity (you can create this later)
            Toast.makeText(this, getString(R.string.login_coming_soon), Toast.LENGTH_SHORT).show();
        });
    }

    private boolean validateInputs() {
        String name = etName.getText().toString().trim();
        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString();
        String passwordConfirm = etPasswordConfirm.getText().toString();

        if (TextUtils.isEmpty(name)) {
            etName.setError(getString(R.string.name_required));
            return false;
        }

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

        if (password.length() < 6) {
            etPassword.setError(getString(R.string.password_min_length));
            return false;
        }

        if (!password.equals(passwordConfirm)) {
            etPasswordConfirm.setError(getString(R.string.passwords_not_match));
            return false;
        }

        return true;
    }

    private void performSignup() {
        showLoading(true);
        
        executorService.execute(() -> {
            try {
                // Create request body
                JsonObject requestBody = new JsonObject();
                requestBody.addProperty("name", etName.getText().toString().trim());
                requestBody.addProperty("email", etEmail.getText().toString().trim());
                requestBody.addProperty("password", etPassword.getText().toString());
                requestBody.addProperty("passwordConfirm", etPasswordConfirm.getText().toString());

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
                    handleSignupResponse(response.code(), responseBody);
                });

            } catch (IOException e) {
                Log.e(TAG, "Network error", e);
                runOnUiThread(() -> {
                    showLoading(false);
                    Toast.makeText(SignupActivity.this, 
                        getString(R.string.network_error) + e.getMessage(), 
                        Toast.LENGTH_LONG).show();
                });
            }
        });
    }

    private void handleSignupResponse(int statusCode, String responseBody) {
        try {
            if (statusCode == 200 || statusCode == 201) {
                // Success response
                JsonObject response = gson.fromJson(responseBody, JsonObject.class);
                if (response.has("status") && "success".equals(response.get("status").getAsString())) {
                    // Save token if needed
                    if (response.has("token")) {
                        String token = response.get("token").getAsString();
                        // You can save this token to SharedPreferences for future API calls
                        Log.d(TAG, "Token received: " + token);
                    }
                    
                    Toast.makeText(this, getString(R.string.signup_successful), Toast.LENGTH_SHORT).show();
                    // Navigate to login activity
                    Intent intent = new Intent(this, LoginActivity.class);
                    startActivity(intent);
                    finish();
                }
            } else {
                // Error response
                JsonObject errorResponse = gson.fromJson(responseBody, JsonObject.class);
                String errorMessage = getString(R.string.signup_failed);
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

    private void showLoading(boolean show) {
        progressBar.setVisibility(show ? View.VISIBLE : View.GONE);
        btnSignup.setEnabled(!show);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (executorService != null) {
            executorService.shutdown();
        }
    }
} 