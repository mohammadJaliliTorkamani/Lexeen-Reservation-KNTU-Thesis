package ir.ac.kntu.Entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import java.util.List;

import ir.ac.kntu.Technical.Other.Other.AddressTypeConverter;
import ir.ac.kntu.Technical.Other.Other.Constants;
import ir.ac.kntu.Technical.Other.Other.StringListTypeConverter;

@Entity(tableName = Constants._DATABASE_RESTAURANT_TABLE)
@TypeConverters({StringListTypeConverter.class, AddressTypeConverter.class})
public class Restaurant {
    @PrimaryKey(autoGenerate = false)
    private int id;
    private String encryptedCode;
    private String name;
    private String type;
    private String phone;
    private Address address;
    private int ownerID;
    private boolean active;
    private String restaurantClass;
    private String site;
    private List<String> pictures;


    public Restaurant(int id, boolean active, String encryptedCode, String name, String type, String phone, Address address, int ownerID, String site, String restaurantClass, List<String> pictures) {
        this.id = id;
        this.restaurantClass = restaurantClass;
        this.encryptedCode = encryptedCode;
        this.name = name;
        this.active = active;
        this.type = type;
        this.phone = phone;
        this.address = address;
        this.ownerID = ownerID;
        this.site = site;
        this.pictures = pictures;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEncryptedCode() {
        return encryptedCode;
    }

    public void setEncryptedCode(String encryptedCode) {
        this.encryptedCode = encryptedCode;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public int getOwnerID() {
        return ownerID;
    }

    public void setOwnerID(int ownerID) {
        this.ownerID = ownerID;
    }

    public String getSite() {
        return site;
    }

    public void setSite(String site) {
        this.site = site;
    }

    public List<String> getPictures() {
        return pictures;
    }

    public void setPictures(List<String> pictures) {
        this.pictures = pictures;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public String getRestaurantClass() {
        return restaurantClass;
    }

    public void setRestaurantClass(String restaurantClass) {
        this.restaurantClass = restaurantClass;
    }
}