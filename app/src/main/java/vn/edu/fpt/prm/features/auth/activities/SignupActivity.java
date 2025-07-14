package vn.edu.fpt.prm.features.auth.activities;

import android.graphics.Rect;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import retrofit2.Response;
import retrofit2.Call;
import retrofit2.Callback;
import vn.edu.fpt.prm.R;
import vn.edu.fpt.prm.core.prefs.AuthManager;
import vn.edu.fpt.prm.core.utils.Logger;
import vn.edu.fpt.prm.core.widget.LoadingFullScreen;
import vn.edu.fpt.prm.features.auth.AuthService;
import vn.edu.fpt.prm.features.auth.dto.response.AuthResponse;
import vn.edu.fpt.prm.navigation.Navigator;
import vn.edu.fpt.prm.navigation.Screen;

public class SignupActivity extends AppCompatActivity {

    private EditText etName;
    private EditText etEmail;
    private EditText etPassword;
    private EditText etConfirmPassword;
    private Button btnSignup;
    private View loadingFullScreen;

    private AuthService authService;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        authService = new AuthService(this);

        bindingViews();
        bindingAction();
    }

    private void bindingViews() {
        etName = findViewById(R.id.et_name);
        etEmail = findViewById(R.id.et_email);
        etPassword = findViewById(R.id.et_password);
        etConfirmPassword = findViewById(R.id.et_confirm_password);
        btnSignup = findViewById(R.id.btn_signup);
        loadingFullScreen = findViewById(R.id.loading_fullscreen);
    }

    private void bindingAction() {
        btnSignup.setOnClickListener(v -> handleSignup());

        findViewById(R.id.tv_login_link).setOnClickListener(v -> {
            // Navigate to LoginActivity
            Navigator.goTo(this, Screen.LOGIN);
        });
    }

    private void handleSignup() {
        String name = etName.getText().toString().trim();
        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();
        String confirmPassword = etConfirmPassword.getText().toString().trim();

        // Validate input
        if (TextUtils.isEmpty(name) || TextUtils.isEmpty(email) || TextUtils.isEmpty(password) || TextUtils.isEmpty(confirmPassword)) {
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        // Validate email format
        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Toast.makeText(this, "Invalid email format", Toast.LENGTH_SHORT).show();
            return;
        }

        // Validate password must be at least 6 characters including one uppercase letter, one lowercase letter, one number and one digit
        if (password.length() < 6 || !password.matches(".*[A-Z].*") || !password.matches(".*[a-z].*") || !password.matches(".*\\d.*")) {
            Toast.makeText(this, "Password must be at least 6 characters and include uppercase, lowercase letters and a number", Toast.LENGTH_SHORT).show();
            return;
        }

        // Check if passwords match
        if (!password.equals(confirmPassword)) {
            Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show();
            return;
        }

        LoadingFullScreen.show(loadingFullScreen);
        btnSignup.setEnabled(false);

        authService.signup(name, email, password, confirmPassword).enqueue(new Callback<AuthResponse>() {
            @Override
            public void onResponse(@NonNull Call<AuthResponse> call, @NonNull Response<AuthResponse> response) {
                LoadingFullScreen.hide(loadingFullScreen);
                btnSignup.setEnabled(true);

                if (response.isSuccessful() && response.body() != null) {
                    AuthResponse authResponse = response.body();

                    // Save authentication data (token and user info)
                    AuthManager.saveAuth(SignupActivity.this, authResponse.getToken(), authResponse.toUser());

                    Log.d("SignupActivity", "Signup successful: " + authResponse.getToken());

                    // Navigate to home screen
                    Navigator.goTo(SignupActivity.this, Screen.HOME);
                } else {
                    Toast.makeText(SignupActivity.this, "Signup failed: " + response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<AuthResponse> call, @NonNull Throwable t) {
                LoadingFullScreen.hide(loadingFullScreen);
                btnSignup.setEnabled(true);
                Logger.error("SignupActivity", "Signup failed: " + t.getMessage());
                Toast.makeText(SignupActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
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