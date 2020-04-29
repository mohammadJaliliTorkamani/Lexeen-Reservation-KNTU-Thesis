package ir.ac.kntu.Entity;

import java.util.List;

public class Food {
    private int id;
    private String name;
    private long calories;
    private String cookTimeMinutes;
    private String description;
    private float price;
    private List<String> pictures;

    public Food(int id, String name, long calories, String cookTimeMinutes, String description, float price, List<String> pictures, int likeNumber) {
        this.id = id;
        this.name = name;
        this.calories = calories;
        this.cookTimeMinutes = cookTimeMinutes;
        this.description = description;
        this.price = price;
        this.pictures = pictures;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getCalories() {
        return calories;
    }

    public void setCalories(long calories) {
        this.calories = calories;
    }

    public String getCookTimeMinutes() {
        return cookTimeMinutes;
    }

    public void setCookTimeMinutes(String cookTimeMinutes) {
        this.cookTimeMinutes = cookTimeMinutes;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public List<String> getPictures() {
        return pictures;
    }

    public void setPictures(List<String> pictures) {
        this.pictures = pictures;
    }
}
