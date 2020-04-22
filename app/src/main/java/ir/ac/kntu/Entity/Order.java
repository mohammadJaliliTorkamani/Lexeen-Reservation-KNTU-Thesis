package ir.ac.kntu.Entity;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import java.util.LinkedList;
import java.util.List;

import ir.ac.kntu.Technical.Other.Other.BillListTypeConverter;
import ir.ac.kntu.Technical.Other.Other.Constants;

@Entity(tableName = Constants._DATABASE_ORDER_TABLE)
@TypeConverters({BillListTypeConverter.class})
public class Order {
    @PrimaryKey
    private int orderID;
    private List<Bill> specifiedBills;
    private float totalPrice;
    private String date_and_time_start;
    private String date_and_time_end;
    private String restaurant;
    private String qrCodeValue;
    private int discountID;
    private boolean delivery;
    private boolean isFoodBills; //should be set with setters
    private double latitude;//should be set with setters
    private double longitude;//should be set with setters
    private String blockNo;//should be set with setters
    private int floor;//should be set with setters
    private String unit;//should be set with setters

    @Ignore
    public Order(int orderID, List<Bill> specifiedBills, float totalPrice, String date_and_time_start, String date_and_time_end, String restaurant, String qrCodeValue, boolean isFoodBills, int discountID) {
        this.orderID = orderID;
        this.specifiedBills = specifiedBills;
        this.delivery = false;
        this.discountID = discountID;
        this.totalPrice = totalPrice;
        this.date_and_time_start = date_and_time_start;
        this.date_and_time_start = date_and_time_end;
        this.restaurant = restaurant;
        this.qrCodeValue = qrCodeValue;
        this.isFoodBills = isFoodBills;
        this.latitude = -1;
        this.longitude = -1;
        this.blockNo = null;
        this.floor = -1;
        this.unit = null;
    }

    @Ignore
    public Order(List<Bill> foods, float totalPrice) {
        this.specifiedBills = foods;
        this.totalPrice = totalPrice;
        this.delivery = false;
        this.orderID = -1;
        this.latitude = -1;
        this.longitude = -1;
        this.blockNo = null;
        this.floor = -1;
        this.unit = null;
    }

    public Order() {
        this.delivery = false;
        this.latitude = -1;
        this.longitude = -1;
        this.blockNo = null;
        this.orderID = -1;
        this.floor = -1;
        this.unit = null;
        this.specifiedBills = new LinkedList<>();
    }

    public List<Bill> getSpecifiedBills() {
        if (specifiedBills == null)
            specifiedBills = new LinkedList<>();
        return specifiedBills;
    }

    public void setSpecifiedBills(List<Bill> specifiedBills) {
        this.specifiedBills = specifiedBills;
    }

    public float getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(float totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getDate_and_time_start() {
        return date_and_time_start;
    }

    public void setDate_and_time_start(String date_and_time_start) {
        this.date_and_time_start = date_and_time_start;
    }

    public String getRestaurant() {
        return restaurant;
    }

    public void setRestaurant(String restaurant) {
        this.restaurant = restaurant;
    }

    public String getQrCodeValue() {
        return qrCodeValue;
    }

    public void setQrCodeValue(String qrCodeValue) {
        this.qrCodeValue = qrCodeValue;
    }

    public boolean isFoodBills() {
        return isFoodBills;
    }

    public void setFoodBills(boolean foodBills) {
        isFoodBills = foodBills;
    }

    public int getDiscountID() {
        return discountID;
    }

    public void setDiscountID(int discountID) {
        this.discountID = discountID;
    }

    public boolean isDelivery() {
        return delivery;
    }

    public void setDelivery(boolean delivery) {
        this.delivery = delivery;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getDate_and_time_end() {
        return date_and_time_end;
    }

    public void setDate_and_time_end(String date_and_time_end) {
        this.date_and_time_end = date_and_time_end;
    }

    public String getBlockNo() {
        return blockNo;
    }

    public void setBlockNo(String blockNo) {
        this.blockNo = blockNo;
    }

    public int getFloor() {
        return floor;
    }

    public void setFloor(int floor) {
        this.floor = floor;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public void clearSpecifiedBills() {
        getSpecifiedBills().clear();
    }

    public int getOrderID() {
        return orderID;
    }

    public void setOrderID(int orderID) {
        this.orderID = orderID;
    }
}
