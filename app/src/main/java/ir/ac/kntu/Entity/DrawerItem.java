package ir.ac.kntu.Entity;

public class DrawerItem {
    private String title;
    private int picture;

    public DrawerItem(String title, int picture) {
        this.title = title;
        this.picture = picture;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getPicture() {
        return picture;
    }

    public void setPicture(int picture) {
        this.picture = picture;
    }
}
