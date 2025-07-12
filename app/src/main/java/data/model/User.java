package data.model;

import java.util.List;

public class User {
    private String id;
    private String name;
    private String email;
    private String avatar;

    private List<Booking> bookings;

    public User(String id, String name, String email, String avatar,
                 List<Booking> bookings) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.avatar = avatar;

        this.bookings = bookings;
    }

    // Getters
    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getAvatar() {
        return avatar;
    }

    public List<Booking> getBookings() {
        return bookings;
    }
}
