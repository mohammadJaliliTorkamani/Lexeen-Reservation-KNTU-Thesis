package ir.ac.kntu.Entity;

public class WeLove {
    private int id;
    private int foodID;
    private int priority;

    public WeLove(int id, int foodID, int priority) {
        this.id = id;
        this.foodID = foodID;
        this.priority = priority;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getFoodID() {
        return foodID;
    }

    public void setFoodID(int foodID) {
        this.foodID = foodID;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }
}
