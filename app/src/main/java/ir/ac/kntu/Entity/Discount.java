package ir.ac.kntu.Entity;

public class Discount {
    private int id;
    private String code;
    private float percentage;
    private float minimumAcceptablePrice;

    public Discount(int id, String code, float percentage, float minimumAcceptablePrice) {
        this.id = id;
        this.code = code;
        this.percentage = percentage;
        this.minimumAcceptablePrice = minimumAcceptablePrice;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public float getPercentage() {
        return percentage;
    }

    public void setPercentage(float percentage) {
        this.percentage = percentage;
    }

    public float getMinimumAcceptablePrice() {
        return minimumAcceptablePrice;
    }

    public void setMinimumAcceptablePrice(float minimumAcceptablePrice) {
        this.minimumAcceptablePrice = minimumAcceptablePrice;
    }
}
