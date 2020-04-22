package ir.ac.kntu.Entity;

import java.util.List;

public class Desk {
    private int id;
    private int topChairID;
    private int bottomChairID;
    private int startChairID;
    private int endChairID;
    private int row_index;
    private int lexinTableID;
    private int column_index;
    private float price;
    private int roof;
    private String label;
    private boolean reserved;
    private int previousDeskID; //left(in horz) or top(in vert)
    private int nextDeskID; //right (in horz) or bottom in( vert)

    public Desk(int id, int topChairID, int bottomChairID, int startChairID, int endChairID,
                int row_index, int column_index, float price, int roof, String label,
                Direction direction, int previousDeskID, int nextDeskID, int lexinTableID) {
        this.id = id;
        this.reserved = false;
        this.topChairID = topChairID;
        this.bottomChairID = bottomChairID;
        this.startChairID = startChairID;
        this.endChairID = endChairID;
        this.row_index = row_index;
        this.column_index = column_index;
        this.price = price;
        this.roof = roof;
        this.label = label;
        this.previousDeskID = previousDeskID;
        this.nextDeskID = nextDeskID;
        this.lexinTableID = lexinTableID;
    }

    public Desk() {

    }

    public static Desk getDeskWithRowCol(TableInfo tableInfo, int row, int column) {
        List<List<Desk>> lists = tableInfo.getDesks();
        for (List<Desk> list : lists)
            for (Desk desk : list)
                if (desk != null && desk.getRow_index() == row && desk.getColumn_index() == column) {
                    return desk;
                }
        return null;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getTopChairID() {
        return topChairID;
    }

    public void setTopChairID(int topChairID) {
        this.topChairID = topChairID;
    }

    public int getBottomChairID() {
        return bottomChairID;
    }

    public void setBottomChairID(int bottomChairID) {
        this.bottomChairID = bottomChairID;
    }

    public int getStartChairID() {
        return startChairID;
    }

    public void setStartChairID(int startChairID) {
        this.startChairID = startChairID;
    }

    public int getEndChairID() {
        return endChairID;
    }

    public void setEndChairID(int endChairID) {
        this.endChairID = endChairID;
    }

    public int getRow_index() {
        return row_index;
    }

    public void setRow_index(int row_index) {
        this.row_index = row_index;
    }

    public int getColumn_index() {
        return column_index;
    }

    public void setColumn_index(int column_index) {
        this.column_index = column_index;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public int getRoof() {
        return roof;
    }

    public void setRoof(int roof) {
        this.roof = roof;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public G_Desk.DeskType getDeskTypeDirection() {
        if (startChairID != -1 && endChairID != -1 && topChairID != -1 && bottomChairID != -1)
            return G_Desk.DeskType.FULL;
        if (topChairID != -1 && bottomChairID != -1 && startChairID == -1 && endChairID == -1)
            return G_Desk.DeskType.MIDDLE;
        if (topChairID == -1 && bottomChairID == -1 && startChairID != -1 && endChairID != -1)
            return G_Desk.DeskType.MIDDLE;
        return G_Desk.DeskType.CORNER;
    }

    public G_Desk.MiddleType getMiddleTypeMode() {
        if (topChairID != -1 && bottomChairID != -1 && startChairID == -1 && endChairID == -1)
            return G_Desk.MiddleType.HORIZONTAL;
        return G_Desk.MiddleType.VERTICAL;
    }

    public G_Desk.CornerType getCornerType() {
        if (topChairID == -1)
            return G_Desk.CornerType.BOTTOM;
        else if (bottomChairID == -1)
            return G_Desk.CornerType.TOP;
        else if (startChairID == -1)
            return G_Desk.CornerType.END;
        return G_Desk.CornerType.START;
    }

    public boolean isReserved() {
        return reserved;
    }

    public void setReserved(boolean reserved) {
        this.reserved = reserved;
    }

    public int getPreviousDeskID() {
        return previousDeskID;
    }

    public void setPreviousDeskID(int previousDeskID) {
        this.previousDeskID = previousDeskID;
    }

    public int getNextDeskID() {
        return nextDeskID;
    }

    public void setNextDeskID(int nextDeskID) {
        this.nextDeskID = nextDeskID;
    }

    public int getLexinTableID() {
        return lexinTableID;
    }

    public void setLexinTableID(int lexinTableID) {
        this.lexinTableID = lexinTableID;
    }

    public static enum Direction {
        START, END, TOP, BOTTOM
    }
}