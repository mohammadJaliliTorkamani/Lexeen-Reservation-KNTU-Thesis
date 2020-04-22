package ir.ac.kntu.Entity;

public class Chair {
    private int id;
    private int deskID;

    public Chair(int id, int deskID) {
        this.id = id;
        this.deskID = deskID;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getDeskID() {
        return deskID;
    }

    public void setDeskID(int deskID) {
        this.deskID = deskID;
    }
}
