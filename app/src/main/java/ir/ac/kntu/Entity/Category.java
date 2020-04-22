package ir.ac.kntu.Entity;

import java.util.List;

public class Category {
    private int id;
    private String name;
    private String color;
    private List<String> logos;  //first is white png, second is black png, others in later

    public Category(int id, String name, String color, List<String> logos) {
        this.id = id;
        this.name = name;
        this.logos = logos;
        this.color = color;
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

    public List<String> getLogos() {
        return logos;
    }

    public void setLogos(List<String> logos) {
        this.logos = logos;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }
}
