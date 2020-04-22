package ir.ac.kntu.Entity;

public class Offer {
    private int id;
    private int foodID;
    private float discountPercentage;

    public Offer(int id, int foodID, float discountPercentage) {
        this.id = id;
        this.foodID = foodID;
        this.discountPercentage = discountPercentage;
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

    public float getDiscountPercentage() {
        return discountPercentage;
    }

    public void setDiscountPercentage(float discountPercentage) {
        this.discountPercentage = discountPercentage;
    }
}
