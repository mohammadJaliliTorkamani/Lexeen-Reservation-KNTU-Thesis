package ir.ac.kntu.Entity;

public class TodayMeal {
    private int id;
    private int foodID;
    private int mealID;

    public TodayMeal(int id, int foodID, int mealID) {
        this.id = id;
        this.foodID = foodID;
        this.mealID = mealID;
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

    public int getMealID() {
        return mealID;
    }

    public void setMealID(int mealID) {
        this.mealID = mealID;
    }
}
