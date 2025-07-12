package data.respository;

import java.util.List;

import data.api.ApiService;
import data.api.RetrofitInstance;
import data.model.Tour;
import retrofit2.Call;

public class TourRepository {
    private final ApiService apiService;

    public TourRepository() {
        this.apiService = RetrofitInstance.getApiService();
    }

    public Call<List<Tour>> getTours(String token) {
        return apiService.getTours("Bearer " + token);
    }
}
