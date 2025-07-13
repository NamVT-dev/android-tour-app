package vn.edu.fpt.prm.features.tour.dto.response;

import java.util.List;

import vn.edu.fpt.prm.features.tour.Tour;

public class TourListResponse {
    private String status;
    private Integer results;
    private Data data;

    public TourListResponse() {}

    public String getStatus() {
        return status;
    }

    public Integer getResults() {
        return results;
    }

    public Data getData() {
        return data;
    }

    public static class Data {
        private List<Tour> data;

        public Data() {
        }

        public List<Tour> getData() {
            return data;
        }
    }
}
