package vn.edu.fpt.prm.features.tour;

import java.io.Serializable;
import java.util.List;

public class Location implements Serializable {
    private String id;
    private String type;
    private List<Double> coordinates;
    private String address;
    private String description;
    private Integer day;

    public Location() {
    }

    public Location(String id, String type, List<Double> coordinates, String address, String description, Integer day) {
        this.id = id;
        this.type = type;
        this.coordinates = coordinates;
        this.address = address;
        this.description = description;
        this.day = day;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<Double> getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(List<Double> coordinates) {
        this.coordinates = coordinates;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getDay() {
        return day;
    }

    public void setDay(Integer day) {
        this.day = day;
    }
}
