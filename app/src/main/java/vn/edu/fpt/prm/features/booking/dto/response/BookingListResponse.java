package vn.edu.fpt.prm.features.booking.dto.response;

import java.util.List;

import vn.edu.fpt.prm.features.booking.Booking;

public class BookingListResponse {
    private String status;
    private Data data;

    public BookingListResponse() {}

    public Data getData() {
        return data;
    }

    public static class Data {
        private List<Booking> data;

        public Data() {}

        public List<Booking> getData() {
            return data;
        }
    }
}
