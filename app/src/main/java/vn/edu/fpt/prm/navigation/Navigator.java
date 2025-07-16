package vn.edu.fpt.prm.navigation;

import android.content.Context;
import android.content.Intent;

import vn.edu.fpt.prm.features.auth.activities.LoginActivity;
import vn.edu.fpt.prm.features.auth.activities.SignupActivity;
import vn.edu.fpt.prm.features.booking.activities.BookingListActivity;
import vn.edu.fpt.prm.features.tour.activities.TourDetailActivity;
import vn.edu.fpt.prm.features.tour.activities.TourListActivity;
import vn.edu.fpt.prm.features.user.activities.ChangePasswordActivity;
import vn.edu.fpt.prm.features.user.activities.ProfileActivity;

public class Navigator {
    public static void goTo(Context context, Screen screen) {
        Intent intent;
        switch (screen) {
            case LOGIN:
                intent = new Intent(context, LoginActivity.class);
                break;
            case SIGNUP:
                intent = new Intent(context, SignupActivity.class);
                break;
            case HOME:
                intent = new Intent(context, TourListActivity.class);
                break;
            case TOUR_DETAIL:
                intent = new Intent(context, TourDetailActivity.class);
                break;
            case PROFILE:
                intent = new Intent(context, ProfileActivity.class);
                break;
            case MY_BOOKINGS:
                intent = new Intent(context, BookingListActivity.class);
                break;
            case CHANGE_PASSWORD:
                intent = new Intent(context, ChangePasswordActivity.class);
                break;
            default:
                throw new IllegalArgumentException("Unknown screen: " + screen);
        }

        // Chỉ nên dùng flag nếu gọi từ context ngoài activity
        if (!(context instanceof android.app.Activity)) {
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        context.startActivity(intent);
    }

    public static void goToAndFinish(Context context, Screen screen) {
        goTo(context, screen);
        if (context instanceof android.app.Activity) {
            ((android.app.Activity) context).finish();
        }
    }

    public static void goBack(Context context) {
        if (context instanceof android.app.Activity) {
            ((android.app.Activity) context).onBackPressed();
        } else {
            throw new IllegalStateException("Context must be an Activity to go back");
        }
    }
}
