package vn.edu.fpt.prm.features.user.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;

import vn.edu.fpt.prm.R;
import vn.edu.fpt.prm.core.prefs.AuthManager;
import vn.edu.fpt.prm.core.utils.Logger;
import vn.edu.fpt.prm.core.widget.Toaster;
import vn.edu.fpt.prm.features.user.User;
import vn.edu.fpt.prm.features.user.UserService;
import vn.edu.fpt.prm.navigation.Navigator;
import vn.edu.fpt.prm.navigation.Screen;

public class ProfileActivity extends AppCompatActivity {

    private TextView txtName, txtEmail;
    private ImageView imgAvatar;
    private ImageButton btnBack;
    private Button btnEditProfile;
    private LinearLayout btnMyBookings, btnChangePassword, btnLogout;

    private UserService userService;
    private User currentUser;
    private String token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        userService = new UserService(this);

        bindingView();
        bindingAction();
        handleUserData();
    }

    private void bindingView() {
        txtName = findViewById(R.id.txtName);
        txtEmail = findViewById(R.id.txtEmail);
        imgAvatar = findViewById(R.id.imgAvatar);
        btnBack = findViewById(R.id.btnBack);
        btnEditProfile = findViewById(R.id.btnEditProfile);
        btnMyBookings = findViewById(R.id.btnMyBookings);
        btnChangePassword = findViewById(R.id.btnChangePassword);
        btnLogout = findViewById(R.id.btnLogout);
    }

    private void bindingAction() {
        btnEditProfile.setOnClickListener(v -> {
            Intent intent = new Intent(ProfileActivity.this, EditProfileActivity.class);
            intent.putExtra("name", currentUser != null ? currentUser.getName() : "");
            intent.putExtra("email", currentUser != null ? currentUser.getEmail() : "");
            startActivity(intent);
        });

        btnBack.setOnClickListener(v -> {
            Navigator.goBack(this);
        });

        btnMyBookings.setOnClickListener(v -> {
            Logger.debug("ProfileActivity", "Navigating to My Bookings");
            Navigator.goTo(this, Screen.MY_BOOKINGS);
        });

        btnChangePassword.setOnClickListener(v -> {
            Logger.debug("ProfileActivity", "Navigating to Change Password");
            Navigator.goTo(this, Screen.CHANGE_PASSWORD);
        });

        btnLogout.setOnClickListener(v -> {
            AuthManager.clear(this);
            Toaster.showToast(this, "Logged out successfully");
            Navigator.goToAndFinish(this, Screen.LOGIN);
        });
    }

    private void handleUserData() {
        token = AuthManager.getToken(this);
        currentUser = AuthManager.getUser(this);

        if (token == null || token.isEmpty() || currentUser == null) {
            Toaster.showToast(this, "You need to log in first");
            Navigator.goToAndFinish(this, Screen.LOGIN);
            return;
        }

        txtName.setText(currentUser.getName());
        txtEmail.setText(currentUser.getEmail());

        // Load avatar nếu có thể ở đây
        Glide.with(this)
                .load(currentUser.getPhoto())
                .placeholder(R.drawable.ic_profile)
                .error(R.drawable.ic_profile)
                .circleCrop()
                .into(imgAvatar);
    }
}
