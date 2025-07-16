package vn.edu.fpt.prm.features.user.activities;

import android.graphics.Rect;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import vn.edu.fpt.prm.R;
import vn.edu.fpt.prm.core.prefs.AuthManager;
import vn.edu.fpt.prm.core.utils.Logger;
import vn.edu.fpt.prm.core.widget.LoadingFullScreen;
import vn.edu.fpt.prm.core.widget.Toaster;
import vn.edu.fpt.prm.features.user.User;
import vn.edu.fpt.prm.features.user.UserService;
import vn.edu.fpt.prm.features.user.dto.request.ChangePasswordRequest;
import vn.edu.fpt.prm.features.user.dto.response.ChangePasswordResponse;
import vn.edu.fpt.prm.navigation.Navigator;
import vn.edu.fpt.prm.navigation.Screen;

public class ChangePasswordActivity extends AppCompatActivity {

    private ImageView btnBack;
    private EditText etCurrentPassword, etNewPassword, etConfirmPassword;
    private Button btnChangePassword;
    private View loadingFullScreen;
    private UserService userService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Logger.debug("ChangePasswordActivity", "onCreate called");

        setContentView(R.layout.activity_change_password);

        userService = new UserService(this);

        bindingView();
        bindingAction();
    }

    private void bindingView() {
        btnBack = findViewById(R.id.btnBack);
        etCurrentPassword = findViewById(R.id.et_old_password);
        etNewPassword = findViewById(R.id.et_new_password);
        etConfirmPassword = findViewById(R.id.et_confirm_password);
        btnChangePassword = findViewById(R.id.btnChangePassword);
        loadingFullScreen = findViewById(R.id.loading_fullscreen);
    }

    private void bindingAction() {

        btnBack.setOnClickListener(v -> {
            Navigator.goBack(this);
        });

        btnChangePassword.setOnClickListener(v -> {
            String curPass = etCurrentPassword.getText().toString().trim();
            String newPass = etNewPassword.getText().toString().trim();
            String confirmPass = etConfirmPassword.getText().toString().trim();

            if (curPass.isEmpty() || newPass.isEmpty() || confirmPass.isEmpty()) {
                Toaster.showToast(this, "Please fill in all fields");
                return;
            }

            if (!newPass.equals(confirmPass)) {
                Toaster.showToast(this, "New password and confirm password do not match");
                return;
            }

            String token = AuthManager.getToken(this);

            ChangePasswordRequest request = new ChangePasswordRequest(curPass, newPass, confirmPass);

            LoadingFullScreen.show(loadingFullScreen);
            btnChangePassword.setEnabled(false);

            userService.changePassword("Bearer " + token, request).enqueue(new Callback<ChangePasswordResponse>() {
                @Override
                public void onResponse(@NonNull Call<ChangePasswordResponse> call, @NonNull Response<ChangePasswordResponse> response) {
                    LoadingFullScreen.hide(loadingFullScreen);
                    btnChangePassword.setEnabled(true);

                    if (response.isSuccessful() && response.body() != null) {
                        ChangePasswordResponse changePasswordResponse = response.body();
                        String token = changePasswordResponse.getToken();
                        User updatedUser = changePasswordResponse.getData().getUser();

                        AuthManager.saveAuth(ChangePasswordActivity.this, token, updatedUser);

                        Logger.debug("ChangePasswordActivity", "Password changed successfully, new token: " + token);
                        Toaster.showToast(ChangePasswordActivity.this, "Password changed successfully");
                        LoadingFullScreen.hide(loadingFullScreen);
                        Navigator.goToAndFinish(ChangePasswordActivity.this, Screen.PROFILE);
                    } else {
                        Logger.error("ChangePasswordActivity", "Password change failed: " + response.message());
                        Toaster.showToast(ChangePasswordActivity.this, "Password change failed");
                    }
                }

                @Override
                public void onFailure(@NonNull Call<ChangePasswordResponse> call, @NonNull Throwable t) {
                    LoadingFullScreen.hide(loadingFullScreen);
                    btnChangePassword.setEnabled(true);
                    Logger.error("ChangePasswordActivity", "Error changing password: " + t.getMessage());
                    Toaster.showToast(ChangePasswordActivity.this, "Error updating password");
                }

            });
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
