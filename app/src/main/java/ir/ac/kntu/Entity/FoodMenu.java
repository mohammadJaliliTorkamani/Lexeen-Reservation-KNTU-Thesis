package ir.ac.kntu.Entity;

import java.util.List;

public class FoodMenu {
    private Category category;
    private List<Food> foodList;

    public FoodMenu(Category category, List<Food> foodList) {
        this.category = category;
        this.foodList = foodList;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public List<Food> getFoodList() {
        return foodList;
    }

    public void setFoodList(List<Food> foodList) {
        this.foodList = foodList;
    }
}
