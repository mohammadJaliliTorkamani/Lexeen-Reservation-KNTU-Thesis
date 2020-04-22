package ir.ac.kntu.Entity;

import java.util.List;

public class RefactoredBills {
    private List<Bill> bills;
    private int n;

    public RefactoredBills(List<Bill> bills, int n) {
        this.bills = bills;
        this.n = n;
    }

    public List<Bill> getBills() {
        return bills;
    }

    public void setBills(List<Bill> bills) {
        this.bills = bills;
    }

    public int getN() {
        return n;
    }

    public void setN(int n) {
        this.n = n;
    }
}
