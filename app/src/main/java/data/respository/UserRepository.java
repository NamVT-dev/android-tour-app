package data.respository;

import data.api.ApiService;
import data.api.RetrofitInstance;
import data.model.User;

import retrofit2.Call;

public class UserRepository {

    private final ApiService apiService;

    public UserRepository() {
        this.apiService = RetrofitInstance.getApiService();
    }

    public Call<User> getUserProfile(String token) {
        return apiService.getProfile("Bearer " + token);
    }
}
