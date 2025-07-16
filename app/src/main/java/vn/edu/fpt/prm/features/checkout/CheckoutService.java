package vn.edu.fpt.prm.features.checkout;

import android.content.Context;

import retrofit2.Call;
import retrofit2.Retrofit;
import vn.edu.fpt.prm.core.network.ApiClient;
import vn.edu.fpt.prm.features.checkout.dto.response.CheckoutResponse;

public class CheckoutService {
    private final CheckoutApi checkoutApi;

    public CheckoutService(Context context) {
        Retrofit retrofit = ApiClient.getRetrofit(context);
        this.checkoutApi = retrofit.create(CheckoutApi.class);
    }

    public Call<CheckoutResponse> getCheckoutSession(String token, String id) {
        return checkoutApi.createCheckoutSession(token, id);
    }
}
