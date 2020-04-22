package ir.ac.kntu.Entity;

public class FullDesk {
    private Desk desk;
    private G_Desk g_desk;

    public FullDesk(Desk desk, G_Desk g_desk) {
        this.desk = desk;
        this.g_desk = g_desk;
    }

    public Desk getDesk() {
        return desk;
    }

    public void setDesk(Desk desk) {
        this.desk = desk;
    }

    public G_Desk getG_desk() {
        return g_desk;
    }

    public void setG_desk(G_Desk g_desk) {
        this.g_desk = g_desk;
    }

    public boolean isStarter() {
        return desk.getPreviousDeskID() == -1;
    }
}
