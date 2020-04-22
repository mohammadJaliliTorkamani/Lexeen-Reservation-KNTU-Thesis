package ir.ac.kntu.Entity;

import java.util.List;

public class Meal {
    private int id;
    private String name;
    private List<String> logos;

    public Meal(int id, String name, List<String> logos) {
        this.id = id;
        this.name = name;
        this.logos = logos;
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
}
