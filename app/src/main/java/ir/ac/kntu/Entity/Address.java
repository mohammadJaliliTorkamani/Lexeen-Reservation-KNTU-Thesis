package ir.ac.kntu.Entity;

import androidx.annotation.NonNull;

import ir.ac.kntu.Technical.Other.Other.Encryption;

public class Address {
    private int id;
    private String country;
    private String state;
    private String city;
    private String street1;
    private String street2;
    private String alley1;
    private String alley2;
    private String block;
    private int floor;
    private String unit;
    private String orientation;
    private double latitude;
    private double longitude;

    public Address(int id, String country, String state, String city, String street1,
                   String street2, String alley1, String alley2, String block, int floor,
                   String unit, String orientation, double latitude, double longitude) {
        this.id = id;
        this.country = country;
        this.state = state;
        this.city = city;
        this.street1 = street1;
        this.street2 = street2;
        this.alley1 = alley1;
        this.alley2 = alley2;
        this.block = block;
        this.floor = floor;
        this.unit = unit;
        this.orientation = orientation;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getStreet1() {
        return street1;
    }

    public void setStreet1(String street1) {
        this.street1 = street1;
    }

    public String getStreet2() {
        return street2;
    }

    public void setStreet2(String street2) {
        this.street2 = street2;
    }

    public String getAlley1() {
        return alley1;
    }

    public void setAlley1(String alley1) {
        this.alley1 = alley1;
    }

    public String getAlley2() {
        return alley2;
    }

    public void setAlley2(String alley2) {
        this.alley2 = alley2;
    }

    public String getBlock() {
        return block;
    }

    public void setBlock(String block) {
        this.block = block;
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

    public String getOrientation() {
        return orientation;
    }

    public void setOrientation(String orientation) {
        this.orientation = orientation;
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

    @NonNull
    @Override
    public String toString() {
        return "کشور " + Encryption.getInstance().decrypt(country) + " , " + "استان " + Encryption.getInstance().decrypt(state) + " , شهر " + Encryption.getInstance().decrypt(city) +
                (street1 != null ? " , " + Encryption.getInstance().decrypt(street1) : "") + " , " + (street2 != null ? " , " + Encryption.getInstance().decrypt(street2) : "") +
                (alley1 != null ? " , کوچه " + Encryption.getInstance().decrypt(alley1) : "") + (alley2 != null ? " , کوچه " + Encryption.getInstance().decrypt(alley2) : "") +
                " پلاک " + Encryption.getInstance().decrypt(block) + " , " + " طبقه " + floor + " , " + " واحد " + Encryption.getInstance().decrypt(unit) +
                (orientation != null ? " , ضلع " + Encryption.getInstance().decrypt(orientation) : "");
    }
}
