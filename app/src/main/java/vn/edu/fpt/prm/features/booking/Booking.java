package vn.edu.fpt.prm.features.booking;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.math.BigDecimal;

import vn.edu.fpt.prm.features.tour.Tour;
import vn.edu.fpt.prm.features.user.User;

public class Booking implements Serializable {
    private String id;
    private Tour tour;
    private User user;
    private BigDecimal price;
    private boolean paid;
    private String createdAt;

    public Booking() {
    }

    public Booking(String id, Tour tour, User user, BigDecimal price, boolean paid, String createdAt) {
        this.id = id;
        this.tour = tour;
        this.user = user;
        this.price = price;
        this.paid = paid;
        this.createdAt = createdAt;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Tour getTour() {
        return tour;
    }

    public void setTour(Tour tour) {
        this.tour = tour;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public boolean isPaid() {
        return paid;
    }

    public void setPaid(boolean paid) {
        this.paid = paid;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }
}