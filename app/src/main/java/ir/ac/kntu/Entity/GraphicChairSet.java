package ir.ac.kntu.Entity;

public class GraphicChairSet {
    private G_Chair GStartChair;
    private G_Chair GEndChair;
    private G_Chair GTopChair;
    private G_Chair GBottomChair;

    public GraphicChairSet(G_Chair GStartChair, G_Chair GEndChair, G_Chair GTopChair, G_Chair GBottomChair) {
        this.GStartChair = GStartChair;
        this.GEndChair = GEndChair;
        this.GTopChair = GTopChair;
        this.GBottomChair = GBottomChair;
    }

    public G_Chair getGStartChair() {
        return GStartChair;
    }

    public void setGStartChair(G_Chair GStartChair) {
        this.GStartChair = GStartChair;
    }

    public G_Chair getGEndChair() {
        return GEndChair;
    }

    public void setGEndChair(G_Chair GEndChair) {
        this.GEndChair = GEndChair;
    }

    public G_Chair getGTopChair() {
        return GTopChair;
    }

    public void setGTopChair(G_Chair GTopChair) {
        this.GTopChair = GTopChair;
    }

    public G_Chair getGBottomChair() {
        return GBottomChair;
    }

    public void setGBottomChair(G_Chair GBottomChair) {
        this.GBottomChair = GBottomChair;
    }
}
