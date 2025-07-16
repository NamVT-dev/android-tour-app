package vn.edu.fpt.prm.features.booking.dto.response;

import java.util.List;

import vn.edu.fpt.prm.features.booking.Booking;

public class BookingListResponse {
    private String status;
    private List<Booking> data;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<Booking> getData() {
        return data;
    }

    public void setData(List<Booking> data) {
        this.data = data;
    }
}
