package vn.edu.fpt.prm.features.tour;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import vn.edu.fpt.prm.features.tour.dto.response.TourListResponse;

public interface TourApi {
    @GET("tours")
    Call<TourListResponse> getAllTours();

    @GET("tours/tours-within/{distance}/center/{lat},{lng}/unit/km")
    Call<TourListResponse> getToursWithinDistance(
            @Path("distance") int distance,
            @Path("lat") double latitude,
            @Path("lng") double longitude
    );
}
