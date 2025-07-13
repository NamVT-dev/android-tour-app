package vn.edu.fpt.prm.features.tour;

import android.content.Context;

import retrofit2.Call;
import retrofit2.Retrofit;
import vn.edu.fpt.prm.core.network.ApiClient;
import vn.edu.fpt.prm.features.tour.dto.response.TourListResponse;

public class TourService {
    private final TourApi tourApi;

    public TourService(Context context) {
        Retrofit retrofit = ApiClient.getRetrofit(context);
        this.tourApi = retrofit.create(TourApi.class);
    }

    public Call<TourListResponse> getAllTours() {
        return tourApi.getAllTours();
    }

    public Call<TourListResponse> getToursWithinDistance(int distance, double latitude, double longitude) {
        return tourApi.getToursWithinDistance(distance, latitude, longitude);
    }
}
