package vn.edu.fpt.prm.features.auth.activities;

import android.graphics.Rect;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import vn.edu.fpt.prm.R;
import vn.edu.fpt.prm.core.prefs.AuthManager;
import vn.edu.fpt.prm.core.utils.Logger;
import vn.edu.fpt.prm.core.widget.LoadingFullScreen;
import vn.edu.fpt.prm.core.widget.Toaster;
import vn.edu.fpt.prm.features.auth.AuthService;
import vn.edu.fpt.prm.features.auth.dto.response.AuthResponse;
import vn.edu.fpt.prm.navigation.Navigator;
import vn.edu.fpt.prm.navigation.Screen;


public class LoginActivity extends AppCompatActivity {
    private EditText etEmail;
    private EditText etPassword;
    private Button btnLogin;
    private TextView tvSignupLink;
    private View loadingFullScreen;

    private AuthService authService;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        authService = new AuthService(this);

        bindingViews();
        bindingAction();
    }

    private void bindingViews() {
        etEmail = findViewById(R.id.et_email);
        etPassword = findViewById(R.id.et_password);
        btnLogin = findViewById(R.id.btn_login);
        tvSignupLink = findViewById(R.id.tv_signup_link);
        loadingFullScreen = findViewById(R.id.loading_fullscreen);
    }

    private void bindingAction() {
        btnLogin.setOnClickListener(v -> handleLogin());

        tvSignupLink.setOnClickListener(v -> {
            // Navigate to Signup Activity
            Navigator.goTo(this, Screen.SIGNUP);
        });
    }

    private void handleLogin() {
        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        // Validate input
        if (email.isEmpty() || password.isEmpty()) {
            Toaster.showToast(this, "Email and password cannot be empty");
            return;
        }

        LoadingFullScreen.show(loadingFullScreen);
        btnLogin.setEnabled(false);

        authService.login(email, password).enqueue(new Callback<AuthResponse>() {
            @Override
            public void onResponse(@NonNull Call<AuthResponse> call, @NonNull Response<AuthResponse> response) {
                LoadingFullScreen.hide(loadingFullScreen);
                btnLogin.setEnabled(true);

                if (response.isSuccessful() && response.body() != null) {
                    AuthResponse authResponse = response.body();

                    // Save authentication data (token and user info)
                    AuthManager.saveAuth(LoginActivity.this, authResponse.getToken(), authResponse.toUser());

                    Logger.debug("LoginActivity", "Login successful: " + authResponse.getToken());

                    // Navigate to home screen
                    Navigator.goToAndFinish(LoginActivity.this, Screen.HOME);
                } else {
                    Toaster.showToast(LoginActivity.this, "Login failed. Please try again.");
                }
            }

            @Override
            public void onFailure(@NonNull Call<AuthResponse> call, @NonNull Throwable t) {
                LoadingFullScreen.hide(loadingFullScreen);
                btnLogin.setEnabled(true);
                Logger.error("LoginActivity", "Login failed: " + t.getMessage());
                Toaster.showToast(LoginActivity.this, "Login failed. Please check your network connection and try again.");
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