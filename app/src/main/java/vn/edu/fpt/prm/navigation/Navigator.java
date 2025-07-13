package vn.edu.fpt.prm.navigation;

import android.content.Context;
import android.content.Intent;

import vn.edu.fpt.prm.features.auth.activities.LoginActivity;
import vn.edu.fpt.prm.features.auth.activities.SignupActivity;
import vn.edu.fpt.prm.features.tour.activities.TourDetailActivity;
import vn.edu.fpt.prm.features.tour.activities.TourListActivity;

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
//            case TOUR_DETAIL:
//                intent = new Intent(context, TourDetailActivity.class);
//                break;
            default:
                throw new IllegalArgumentException("Unknown screen: " + screen);
        }

        context.startActivity(intent);
    }

    public static void goToAndFinish(Context context, Screen screen) {
        goTo(context, screen);
        if (context instanceof android.app.Activity) {
            ((android.app.Activity) context).finish();
        }
    }
}
