package vn.edu.fpt.prm.features.tour;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

public class Tour implements Serializable {
    private String id;
    private String name;
    private Integer duration;
    private Integer maxGroupSize;
    private String difficulty;
    private Double ratingsAverage;
    private Integer ratingsQuantity;
    private BigDecimal price;
    private BigDecimal priceDiscount;
    private String summary;
    private String description;
    private List<String> images;
    private List<String> startDates;
    private Boolean secretTour;
    private List<Guide> guides;
    private Location startLocation;
    private List<Location> locations;
    private String slug;
    private String imageCover;
    private Double durationWeeks;

    public Tour() {
    }

    public Tour(String id, String name, Integer duration, Integer maxGroupSize, String difficulty, Double ratingsAverage, Integer ratingsQuantity, BigDecimal price, BigDecimal priceDiscount, String summary, String description, List<String> images, List<String> startDates, Boolean secretTour, List<Guide> guides, Location startLocation, List<Location> locations, String slug, String imageCover, Double durationWeeks) {
        this.id = id;
        this.name = name;
        this.duration = duration;
        this.maxGroupSize = maxGroupSize;
        this.difficulty = difficulty;
        this.ratingsAverage = ratingsAverage;
        this.ratingsQuantity = ratingsQuantity;
        this.price = price;
        this.priceDiscount = priceDiscount;
        this.summary = summary;
        this.description = description;
        this.images = images;
        this.startDates = startDates;
        this.secretTour = secretTour;
        this.guides = guides;
        this.startLocation = startLocation;
        this.locations = locations;
        this.slug = slug;
        this.imageCover = imageCover;
        this.durationWeeks = durationWeeks;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getDuration() {
        return duration;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    public Integer getMaxGroupSize() {
        return maxGroupSize;
    }

    public void setMaxGroupSize(Integer maxGroupSize) {
        this.maxGroupSize = maxGroupSize;
    }

    public String getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(String difficulty) {
        this.difficulty = difficulty;
    }

    public Double getRatingsAverage() {
        return ratingsAverage;
    }

    public void setRatingsAverage(Double ratingsAverage) {
        this.ratingsAverage = ratingsAverage;
    }

    public Integer getRatingsQuantity() {
        return ratingsQuantity;
    }

    public void setRatingsQuantity(Integer ratingsQuantity) {
        this.ratingsQuantity = ratingsQuantity;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public BigDecimal getPriceDiscount() {
        return priceDiscount;
    }

    public void setPriceDiscount(BigDecimal priceDiscount) {
        this.priceDiscount = priceDiscount;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<String> getImages() {
        return images;
    }

    public void setImages(List<String> images) {
        this.images = images;
    }

    public List<String> getStartDates() {
        return startDates;
    }

    public void setStartDates(List<String> startDates) {
        this.startDates = startDates;
    }

    public Boolean getSecretTour() {
        return secretTour;
    }

    public void setSecretTour(Boolean secretTour) {
        this.secretTour = secretTour;
    }

    public List<Guide> getGuides() {
        return guides;
    }

    public void setGuides(List<Guide> guides) {
        this.guides = guides;
    }

    public Location getStartLocation() {
        return startLocation;
    }

    public List<Location> getLocations() {
        return locations;
    }

    public void setLocations(List<Location> locations) {
        this.locations = locations;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public String getImageCover() {
        return imageCover;
    }

    public void setImageCover(String imageCover) {
        this.imageCover = imageCover;
    }

    public Double getDurationWeeks() {
        return durationWeeks;
    }

    public void setDurationWeeks(Double durationWeeks) {
        this.durationWeeks = durationWeeks;
    }
}
