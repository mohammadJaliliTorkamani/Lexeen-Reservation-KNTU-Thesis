package ir.ac.kntu.Entity;

public class FoodOrder {
    private double totalPrice;
    private int id;
    private int user_id;
    private int issueTrackingNo;
    private String orderTime;
    private String serveMode;
    private String status;
    private String payMode;
    private String categoryLogo;

    public FoodOrder(int id, int user_id, String orderTime, String serveMode, double totalPrice,
                     String status, String payMode, int issueTrackingNo, String categoryLogo) {
        this.id = id;
        this.user_id = user_id;
        this.orderTime = orderTime;
        this.serveMode = serveMode;
        this.totalPrice = totalPrice;
        this.status = status;
        this.payMode = payMode;
        this.issueTrackingNo = issueTrackingNo;
        this.categoryLogo = categoryLogo;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public String getOrderTime() {
        return orderTime;
    }

    public void setOrderTime(String orderTime) {
        this.orderTime = orderTime;
    }

    public String getServeMode() {
        return serveMode;
    }

    public void setServeMode(String serveMode) {
        this.serveMode = serveMode;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPayMode() {
        return payMode;
    }

    public void setPayMode(String payMode) {
        this.payMode = payMode;
    }

    public int getIssueTrackingNo() {
        return issueTrackingNo;
    }

    public void setIssueTrackingNo(int issueTrackingNo) {
        this.issueTrackingNo = issueTrackingNo;
    }

    public String getCategoryLogo() {
        return categoryLogo;
    }

    public void setCategoryLogo(String categoryLogo) {
        this.categoryLogo = categoryLogo;
    }
}
