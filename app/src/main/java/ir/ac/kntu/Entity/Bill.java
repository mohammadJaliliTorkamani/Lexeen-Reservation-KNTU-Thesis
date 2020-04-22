package ir.ac.kntu.Entity;

import androidx.annotation.Nullable;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.util.LinkedList;
import java.util.List;

import ir.ac.kntu.Technical.Other.Other.Constants;
import ir.ac.kntu.Technical.Other.Other.Helper;

@Entity(tableName = Constants._DATABASE_BILL_TABLE)
public class Bill {
    @PrimaryKey(autoGenerate = true)
    private int id;
    @ColumnInfo(name = "foodID")
    private int foodID;
    @ColumnInfo(name = "counter")
    private int counter;  //for foods,its number of food ,  for chairs it's number of connected chairs
    @ColumnInfo(name = "lexinTableID")
    private int lexinTableID;
    @ColumnInfo(name = "totalCost")
    private float totalCost;
    @ColumnInfo(name = "restaurantQRCode")
    private String restaurantQRCode;

    public Bill() {
    }

    @Ignore
    public Bill(int lexinTableID) {
        this.lexinTableID = lexinTableID;
        this.foodID = -1;
        this.restaurantQRCode = Helper.getRestaurantSelectionQRCode();
    }

    @Ignore
    public Bill(int foodID, int lexinTableID, int counter, float totalCost) {
        this.foodID = foodID;
        this.lexinTableID = lexinTableID;
        this.counter = counter;
        this.totalCost = totalCost;
        this.restaurantQRCode = Helper.getRestaurantSelectionQRCode();
    }

    @Ignore
    public Bill(int id, int foodID, int lexinTableID, int counter, float totalCost) {
        this.id = id;
        this.lexinTableID = lexinTableID;
        this.foodID = foodID;
        this.counter = counter;
        this.totalCost = totalCost;
        this.restaurantQRCode = Helper.getRestaurantSelectionQRCode();
    }


    @Ignore
    public Bill(int foodID, int counter) {
        this.foodID = foodID;
        this.counter = counter;
        this.lexinTableID = -1;
        this.restaurantQRCode = Helper.getRestaurantSelectionQRCode();
    }

    @Ignore
    public Bill(int foodID, int lexinTableID, int counter) {
        this.foodID = foodID;
        this.lexinTableID = lexinTableID;
        this.counter = counter;
        this.restaurantQRCode = Helper.getRestaurantSelectionQRCode();
    }

    public static int getNumberOfIDInList(int id, List<Bill> list) {
        if (list == null || list.isEmpty() || id <= 0)
            return 0;
        int counter = 0;
        for (Bill item : list)
            if (item.getFoodID() == id)
                counter += item.getCounter();
        return counter;
    }

    public static float getTotalPrice(List<Bill> foodsBills) {
        float sum = 0;
        for (Bill bill : foodsBills)
            sum += (bill.getTotalCost());
        return sum;
    }

    public static boolean containsFood(List<Bill> bills) {
        for (Bill bill : bills)
            if (bill.getFoodID() != -1)
                return true;
        return false;
    }

    public static int getTotalFoodItems(List<Bill> bills) {
        int counter = 0;
        for (Bill bill : bills) {
            if (bill.foodID != -1)
                counter += bill.getCounter();
        }
        return counter;
    }

    public static List<Bill> removeDeskBillsFrom(List<Bill> bills) {
        List<Bill> list = new LinkedList<>();
        if (bills == null || bills.isEmpty())
            return list;

        for (Bill bill : bills) {
            if (bill.getFoodID() != -1)
                list.add(bill);
        }
        return list;
    }


    public int getFoodID() {
        return foodID;
    }

    public void setFoodID(int foodID) {
        this.foodID = foodID;
    }

    public int getCounter() {
        return counter;
    }

    public void setCounter(int counter) {
        this.counter = counter;
    }

    public float getTotalCost() {
        return totalCost;
    }

    public void setTotalCost(float totalCost) {
        this.totalCost = totalCost;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getLexinTableID() {
        return lexinTableID;
    }

    public void setLexinTableID(int lexinTableID) {
        this.lexinTableID = lexinTableID;
    }

    @Override
    public boolean equals(@Nullable Object object) {
        Bill bill = (Bill) object;
        if (object == null)
            return false;
        if (object == this) {
            return true;
        }
        if (!(object instanceof Bill)) {
            return false;
        }
        return bill.foodID == this.foodID && bill.lexinTableID == this.lexinTableID && bill.counter == this.counter && bill.totalCost == this.totalCost;
    }

    @Override
    public int hashCode() {
        int result = 17;

        result = 31 * result + id;
        result = 31 * result + restaurantQRCode.hashCode();
        result = 31 * result + foodID;
        result = 31 * result + lexinTableID;
        result = 31 * result + counter;
        result = 31 * result + (int) totalCost;
        return result;
    }

    public String getRestaurantQRCode() {
        return restaurantQRCode;
    }

    public void setRestaurantQRCode(String restaurantQRCode) {
        this.restaurantQRCode = restaurantQRCode;
    }
}


