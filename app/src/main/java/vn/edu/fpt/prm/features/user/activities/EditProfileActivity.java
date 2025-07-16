package vn.edu.fpt.prm.features.user.activities;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import com.bumptech.glide.Glide;
import com.google.android.material.textfield.TextInputEditText;

import java.io.File;
import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import vn.edu.fpt.prm.R;
import vn.edu.fpt.prm.core.prefs.AuthManager;
import vn.edu.fpt.prm.core.utils.FileUtils;
import vn.edu.fpt.prm.core.utils.Logger;
import vn.edu.fpt.prm.core.widget.LoadingFullScreen;
import vn.edu.fpt.prm.core.widget.Toaster;
import vn.edu.fpt.prm.features.user.dto.request.UpdateUserRequest;
import vn.edu.fpt.prm.features.user.UserService;
import vn.edu.fpt.prm.features.user.User;
import vn.edu.fpt.prm.features.user.dto.response.UserResponse;
import vn.edu.fpt.prm.navigation.Navigator;
import vn.edu.fpt.prm.navigation.Screen;

public class EditProfileActivity extends AppCompatActivity {

    private ImageView btnBack, imgAvatar;
    private ImageButton btnChangeAvatar;
    private TextInputEditText etName, etEmail;
    private Button btnSave;
    private View loadingFullScreen;
    private Uri selectedImageUri;

    private Uri cameraImageUri;
    private UserService userService;
    private static final int REQUEST_CAMERA_PERMISSION = 1003;
    private static final int REQUEST_CAMERA = 1001;
    private static final int REQUEST_GALLERY = 1002;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Logger.debug("EditProfileActivity", "onCreate called");

        setContentView(R.layout.activity_edit_profile);

        userService = new UserService(this);

        bindingView();
        bindingAction();
        loadCurrentUser();
    }

    private void bindingView() {
        btnBack = findViewById(R.id.btnBack);
        imgAvatar = findViewById(R.id.imgAvatar);
        btnChangeAvatar = findViewById(R.id.btnChangeAvatar);
        etName = findViewById(R.id.et_name);
        etEmail = findViewById(R.id.et_email);
        btnSave = findViewById(R.id.btnSave);
        loadingFullScreen = findViewById(R.id.loading_fullscreen);
    }

    private void bindingAction() {
        btnBack.setOnClickListener(v -> finish());

        btnChangeAvatar.setOnClickListener(v -> showImagePickerDialog());

        btnSave.setOnClickListener(v -> {
            String name = etName.getText().toString().trim();
            String email = etEmail.getText().toString().trim();

            if (name.isEmpty()) {
                Toaster.showToast(this, "Full name is required");
                return;
            }

            if (email.isEmpty()) {
                Toaster.showToast(this, "Email is required");
                return;
            }

            updateProfile(name, email);
        });
    }
    private void loadCurrentUser() {
        User user = AuthManager.getUser(this);

        if (user == null) {
            Toaster.showToast(this, "User not found");
            return;
        }

        etName.setText(user.getName());
        etEmail.setText(user.getEmail());

        Glide.with(this)
                .load(user.getPhoto())
                .placeholder(R.drawable.ic_profile)
                .error(R.drawable.ic_profile)
                .circleCrop()
                .into(imgAvatar);
    }

    private void updateProfile(String name, String email) {
        String token = AuthManager.getToken(this);
        UpdateUserRequest request = new UpdateUserRequest(name, email);

        LoadingFullScreen.show(loadingFullScreen);
        btnSave.setEnabled(false);

        userService.updateProfile("Bearer " + token, request).enqueue(new Callback<UserResponse>() {
            @Override
            public void onResponse(@NonNull Call<UserResponse> call, @NonNull Response<UserResponse> response) {
                LoadingFullScreen.hide(loadingFullScreen);
                btnSave.setEnabled(true);

                if (response.isSuccessful() && response.body() != null) {
                    UserResponse userResponse = response.body();
                    User updatedUser = userResponse.getData().getUser();

                    AuthManager.saveUser(EditProfileActivity.this, updatedUser);

                    Logger.debug("EditProfileActivity", "Profile updated successfully: " + updatedUser);
                    Toaster.showToast(EditProfileActivity.this, "Update user successfully");
                    LoadingFullScreen.hide(loadingFullScreen);
                    Navigator.goToAndFinish(EditProfileActivity.this, Screen.PROFILE);
                } else {
                    Logger.error("EditProfileActivity", "Profile update failed: " + response.message());
                    Toaster.showToast(EditProfileActivity.this, "Update user failed: " + response.message());
                }
            }

            @Override
            public void onFailure(@NonNull Call<UserResponse> call, @NonNull Throwable t) {
                LoadingFullScreen.hide(loadingFullScreen);
                btnSave.setEnabled(true);
                Logger.error("EditProfileActivity", "Profile update failed: " + t.getMessage());
                Toaster.showToast(EditProfileActivity.this, "Error: " + t.getMessage());
            }
        });
    }

    private void uploadPhoto(Uri uri) {
        String token = AuthManager.getToken(this);

        String filePath = FileUtils.getPathFromUri(this, uri);
        if (filePath == null) {
            Toaster.showToast(this, "Cannot get image path");
            return;
        }

        File file = new File(filePath);
        RequestBody requestFile = RequestBody.create(MediaType.parse("image/*"), file);
        MultipartBody.Part body = MultipartBody.Part.createFormData("photo", file.getName(), requestFile);

        LoadingFullScreen.show(loadingFullScreen);
        btnChangeAvatar.setEnabled(false);

        userService.updatePhoto("Bearer " + token, body).enqueue(new Callback<UserResponse>() {
            @Override
            public void onResponse(@NonNull Call<UserResponse> call, @NonNull Response<UserResponse> response) {
                LoadingFullScreen.hide(loadingFullScreen);
                btnChangeAvatar.setEnabled(true);

                if (response.isSuccessful() && response.body() != null) {
                    UserResponse userResponse = response.body();
                    User updatedUser = userResponse.getData().getUser();
                    AuthManager.saveUser(EditProfileActivity.this, updatedUser);
                    Glide.with(EditProfileActivity.this)
                            .load(updatedUser.getPhoto())
                            .placeholder(R.drawable.ic_profile)
                            .error(R.drawable.ic_profile)
                            .circleCrop()
                            .into(imgAvatar);
                    Logger.debug("EditProfileActivity", "Avatar updated successfully: " + updatedUser.getPhoto());
                    Toaster.showToast(EditProfileActivity.this, "Avatar updated");
                    LoadingFullScreen.hide(loadingFullScreen);
                    Navigator.goToAndFinish(EditProfileActivity.this, Screen.PROFILE);
                } else {
                    Toaster.showToast(EditProfileActivity.this, "Avatar update failed");
                }
            }

            @Override
            public void onFailure(@NonNull Call<UserResponse> call, @NonNull Throwable t) {
                LoadingFullScreen.hide(loadingFullScreen);
                btnChangeAvatar.setEnabled(true);
                Logger.error("EditProfileActivity", "Error uploading image: " + t.getMessage());
                Toaster.showToast(EditProfileActivity.this, "Error uploading image");
            }
        });
    }

    private void showImagePickerDialog() {
        String[] options = {"Camera", "Gallery"};
        new android.app.AlertDialog.Builder(this)
                .setTitle("Choose Image Source")
                .setItems(options, (dialog, which) -> {
                    if (which == 0) {
                        openCamera();
                    } else if (which == 1) {
                        openGallery();
                    }
                })
                .show();
    }

    private void openCamera() {
        if (checkSelfPermission(android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{android.Manifest.permission.CAMERA}, REQUEST_CAMERA_PERMISSION);
            return;
        }

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        File photoFile;
        try {
            photoFile = File.createTempFile("camera_photo", ".jpg", getCacheDir());
            cameraImageUri = FileProvider.getUriForFile(this, getPackageName() + ".provider", photoFile);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, cameraImageUri);
            intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
            startActivityForResult(intent, REQUEST_CAMERA);
        } catch (IOException e) {
            e.printStackTrace();
            Toaster.showToast(this, "Không thể tạo file ảnh");
        }
    }

    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, REQUEST_GALLERY);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CAMERA_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openCamera();
            } else {
                Toaster.showToast(this, "Bạn cần cấp quyền camera để sử dụng tính năng này");
            }
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == REQUEST_CAMERA && cameraImageUri != null) {
                selectedImageUri = cameraImageUri;
                imgAvatar.setImageURI(selectedImageUri);
                uploadPhoto(selectedImageUri);
            } else if (requestCode == REQUEST_GALLERY && data != null) {
                selectedImageUri = data.getData();
                imgAvatar.setImageURI(selectedImageUri);
                uploadPhoto(selectedImageUri);
            }
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            View focusedView = getCurrentFocus();
            if (focusedView instanceof EditText) {
                Rect outRect = new Rect();
                focusedView.getGlobalVisibleRect(outRect);
                if (!outRect.contains((int) ev.getRawX(), (int) ev.getRawY())) {
                    focusedView.clearFocus();
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
