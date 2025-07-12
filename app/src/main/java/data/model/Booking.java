package data.model;

public class Booking {
    private String tourName;
    private double price;
    private boolean paid;
    private String createdAt;

    public Booking(String tourName, double price, boolean paid, String createdAt) {
        this.tourName = tourName;
        this.price = price;
        this.paid = paid;
        this.createdAt = createdAt;
    }

    public String getTourName() {
        return tourName;
    }

    public double getPrice() {
        return price;
    }

    public boolean isPaid() {
        return paid;
    }

    public String getCreatedAt() {
        return createdAt;
    }
}
