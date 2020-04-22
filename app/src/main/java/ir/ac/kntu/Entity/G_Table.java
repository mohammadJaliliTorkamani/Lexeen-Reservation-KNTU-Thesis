package ir.ac.kntu.Entity;

import java.util.List;

public class G_Table {
    private int id;
    private GravityDirection gravityDirection;
    private G_Desk desk;
    private List<G_Chair> chairs;

    public G_Table(int id, GravityDirection gravityDirection, G_Desk desk, List<G_Chair> chairs) {
        this.id = id;
        this.gravityDirection = gravityDirection;
        this.desk = desk;
        this.chairs = chairs;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public GravityDirection getGravityDirection() {
        return gravityDirection;
    }

    public void setGravityDirection(GravityDirection gravityDirection) {
        this.gravityDirection = gravityDirection;
    }

    public G_Desk getDesk() {
        return desk;
    }

    public void setDesk(G_Desk desk) {
        this.desk = desk;
    }

    public List<G_Chair> getChairs() {
        return chairs;
    }

    public void setChairs(List<G_Chair> chairs) {
        this.chairs = chairs;
    }

    public static enum GravityDirection {
        LEFT, RIGHT, TOP, BOTTOM
    }
}
