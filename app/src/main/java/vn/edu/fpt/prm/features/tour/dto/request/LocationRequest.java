package vn.edu.fpt.prm.features.tour.dto.request;

public class LocationRequest {
    private double lat;
    private double lng;

    public LocationRequest(double lat, double lng) {
        this.lat = lat;
        this.lng = lng;
    }

    public LocationRequest() {
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }
}
