package vn.edu.fpt.prm.features.profile.activities;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.io.File;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import vn.edu.fpt.prm.R;
import vn.edu.fpt.prm.core.network.ApiClient;
import vn.edu.fpt.prm.core.network.UserApi;
import vn.edu.fpt.prm.core.utils.AuthPrefs;
import vn.edu.fpt.prm.core.utils.FileUtils;
import vn.edu.fpt.prm.core.widget.Toaster;
import vn.edu.fpt.prm.features.profile.model.ChangePasswordRequest;
import vn.edu.fpt.prm.features.profile.model.TokenResponse;
import vn.edu.fpt.prm.features.profile.model.UpdateUserRequest;

public class EditProfileActivity extends AppCompatActivity {
    private static final int REQUEST_CAMERA = 1001;
    private static final int REQUEST_GALLERY = 1002;

    private EditText etName, etEmail, etCurrentPassword, etNewPassword, etConfirmPassword;
    private ImageView imgAvatar;
    private LinearLayout passwordContainer;
    private Uri selectedImageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        etName = findViewById(R.id.etName);
        etEmail = findViewById(R.id.etEmail);
        etCurrentPassword = findViewById(R.id.etCurrentPassword);
        etNewPassword = findViewById(R.id.etNewPassword);
        etConfirmPassword = findViewById(R.id.etConfirmPassword);
        imgAvatar = findViewById(R.id.imgAvatar);
        passwordContainer = findViewById(R.id.passwordContainer);

        // Nhận dữ liệu từ Intent (nếu có)
        Intent intent = getIntent();
        if (intent != null) {
            String name = intent.getStringExtra("name");
            String email = intent.getStringExtra("email");

            if (name != null) etName.setText(name);
            if (email != null) etEmail.setText(email);
        }
        Button btnSave = findViewById(R.id.btnSave);
        Button btnCancel = findViewById(R.id.btnCancel);
        Button btnChangePassword = findViewById(R.id.btnChangePassword);
        ImageButton btnCamera = findViewById(R.id.btnCamera);
        ImageButton btnGallery = findViewById(R.id.btnGallery);

        // Khởi tạo dữ liệu cũ
        etName.setText(AuthPrefs.getUserName(this));
        etEmail.setText(AuthPrefs.getUserEmail(this));

        btnCamera.setOnClickListener(v -> openCamera());
        btnGallery.setOnClickListener(v -> openGallery());
        btnCancel.setOnClickListener(v -> finish());

        btnChangePassword.setOnClickListener(v -> {
            if (passwordContainer.getVisibility() == View.GONE) {
                passwordContainer.setVisibility(View.VISIBLE);
            } else {
                updatePassword();
            }
        });

        btnSave.setOnClickListener(v -> updateProfile());
    }

    private void updateProfile() {
        String token = AuthPrefs.getToken(this);
        String name = etName.getText().toString();
        String email = etEmail.getText().toString();

        UpdateUserRequest request = new UpdateUserRequest(name, email);

        UserApi api = ApiClient.createService(UserApi.class);
        api.updateProfile("Bearer " + token, request).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    AuthPrefs.saveUserInfo(EditProfileActivity.this, name, email, "");
                    Toaster.showToast(EditProfileActivity.this, "Updated successfully");
                    finish();
                } else {
                    Toaster.showToast(EditProfileActivity.this, "Update failed");
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                t.printStackTrace();
                Toaster.showToast(EditProfileActivity.this, "Update error");
            }
        });
    }

    private void updatePassword() {
        String token = AuthPrefs.getToken(this);
        String currentPass = etCurrentPassword.getText().toString();
        String newPass = etNewPassword.getText().toString();
        String confirmPass = etConfirmPassword.getText().toString();

        if (!newPass.equals(confirmPass)) {
            Toaster.showToast(this, "Passwords do not match");
            return;
        }

        ChangePasswordRequest request = new ChangePasswordRequest(currentPass, newPass, confirmPass);

        UserApi api = ApiClient.createService(UserApi.class);
        api.changePassword("Bearer " + token, request).enqueue(new Callback<TokenResponse>() {
            @Override
            public void onResponse(Call<TokenResponse> call, Response<TokenResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    AuthPrefs.saveToken(EditProfileActivity.this, response.body().getToken());
                    Toaster.showToast(EditProfileActivity.this, "Password updated");
                    passwordContainer.setVisibility(View.GONE);
                } else {
                    Toaster.showToast(EditProfileActivity.this, "Password change failed");
                }
            }

            @Override
            public void onFailure(Call<TokenResponse> call, Throwable t) {
                t.printStackTrace();
                Toaster.showToast(EditProfileActivity.this, "Error updating password");
            }
        });
    }

    private void openCamera() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, REQUEST_CAMERA);
    }

    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, REQUEST_GALLERY);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK && data != null) {
            if (requestCode == REQUEST_CAMERA) {
                Bitmap photo = (Bitmap) data.getExtras().get("data");
                Uri uri = FileUtils.getImageUri(this, photo);
                selectedImageUri = uri;
                imgAvatar.setImageURI(uri);
                uploadPhoto(uri);
            } else if (requestCode == REQUEST_GALLERY) {
                selectedImageUri = data.getData();
                imgAvatar.setImageURI(selectedImageUri);
                uploadPhoto(selectedImageUri);
            }
        }
    }

    private void uploadPhoto(Uri uri) {
        String token = AuthPrefs.getToken(this);
        String filePath = FileUtils.getPathFromUri(this, uri);
        if (filePath == null) {
            Toaster.showToast(this, "Cannot get image path");
            return;
        }

        File file = new File(filePath);
        RequestBody requestFile = RequestBody.create(MediaType.parse("image/*"), file);
        MultipartBody.Part body = MultipartBody.Part.createFormData("photo", file.getName(), requestFile);

        UserApi api = ApiClient.createService(UserApi.class);
        api.updatePhoto("Bearer " + token, body).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Toaster.showToast(EditProfileActivity.this, "Avatar updated");
                } else {
                    Toaster.showToast(EditProfileActivity.this, "Avatar update failed");
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                t.printStackTrace();
                Toaster.showToast(EditProfileActivity.this, "Error uploading image");
            }
        });
    }
}
