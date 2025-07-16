package vn.edu.fpt.prm.features.checkout;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Path;
import vn.edu.fpt.prm.features.checkout.dto.response.CheckoutResponse;

public interface CheckoutApi {
    @GET("bookings/checkout-session/{id}")
    Call<CheckoutResponse> createCheckoutSession(@Header("Authorization") String token, @Path("id") String id);
}
