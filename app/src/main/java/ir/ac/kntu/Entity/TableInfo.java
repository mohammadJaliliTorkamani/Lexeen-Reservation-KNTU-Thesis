package ir.ac.kntu.Entity;

import java.util.LinkedList;
import java.util.List;

public class TableInfo {
    private int maxCol;
    private int maxRow;
    private List<List<Desk>> desks;

    public TableInfo() {
        this.desks = new LinkedList<>();
    }

    public int getMaxCol() {
        return maxCol;
    }

    public void setMaxCol(int maxCol) {
        this.maxCol = maxCol;
    }

    public int getMaxRow() {
        return maxRow;
    }

    public void setMaxRow(int maxRow) {
        this.maxRow = maxRow;
    }

    public List<List<Desk>> getDesks() {
        return desks;
    }

    public void setDesks(List<List<Desk>> desks) {
        this.desks = desks;
    }
}
