package ir.ac.kntu.Entity;

import java.util.List;

public class RefactoredBills {
    private List<Bill> bills;
    private String orderTime;
    private int n;

    public RefactoredBills(List<Bill> bills, String orderTime, int n) {
        this.bills = bills;
        this.orderTime = orderTime;
        this.n = n;
    }

    public List<Bill> getBills() {
        return bills;
    }

    public void setBills(List<Bill> bills) {
        this.bills = bills;
    }

    public String getOrderTime() {
        return orderTime;
    }

    public void setOrderTime(String orderTime) {
        this.orderTime = orderTime;
    }

    public int getN() {
        return n;
    }

    public void setN(int n) {
        this.n = n;
    }
}
