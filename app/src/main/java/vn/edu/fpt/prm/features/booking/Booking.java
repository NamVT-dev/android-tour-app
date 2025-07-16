package vn.edu.fpt.prm.features.booking;

import java.math.BigDecimal;

public class Booking {
    private String tourName;
    private BigDecimal price;
    private boolean paid;
    private String createdAt;

    public Booking(String tourName, BigDecimal price, boolean paid, String createdAt) {
        this.tourName = tourName;
        this.price = price;
        this.paid = paid;
        this.createdAt = createdAt;
    }

    // Getter và Setter
    public String getTourName() { return tourName; }
    public void setTourName(String tourName) { this.tourName = tourName; }

    public BigDecimal getPrice() { return price; }
    public void setPrice(BigDecimal price) { this.price = price; }

    public boolean isPaid() { return paid; }
    public void setPaid(boolean paid) { this.paid = paid; }

    public String getCreatedAt() { return createdAt; }
    public void setCreatedAt(String createdAt) { this.createdAt = createdAt; }
}