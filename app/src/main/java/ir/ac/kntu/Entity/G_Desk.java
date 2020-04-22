package ir.ac.kntu.Entity;

import android.content.Context;
import android.util.AttributeSet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.github.captain_miao.optroundcardview.OptRoundCardView;

import ir.ac.kntu.R;
import ir.ac.kntu.Technical.Other.Other.Setting;

public class G_Desk extends OptRoundCardView {
    private final static int RADIUS = 9;
    private static final int DEFAULT_DESK_COLOR = R.color.not_reserved_desk;
    private final static int RESERVED_COLOR = R.color.reserved_desk;
    private final static int NOT_RESERVED_COLOR = R.color.not_reserved_desk;
    private final static int TO_RESERVE_COLOR = R.color.to_reserve_desk;
    private int id;
    private boolean full;
    private boolean middle;
    private boolean corner;
    private Context context;
    private DeskType type;
    private CornerType cornerType;
    private MiddleType middleType;
    private Mode reserveMode;

    public G_Desk(@NonNull Context context) {
        super(context);
        type = DeskType.FULL;
        this.context = context;
        createCard();

    }

    public G_Desk(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        type = DeskType.FULL;
        createCard();
    }

    public G_Desk(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        type = DeskType.FULL;
        createCard();
    }

    public static int getRADIUS() {
        return RADIUS;
    }

    public static int getWidthOf(Desk desk, int maxCol) {
        int columnSize = Setting.getInstance().getDeviceWidth() / maxCol;
        if (desk.getDeskTypeDirection().value == DeskType.FULL.value)
            return (int) (columnSize * 0.37);
        else if (desk.getDeskTypeDirection().value == DeskType.MIDDLE.value) {
            if (desk.getMiddleTypeMode().value == MiddleType.HORIZONTAL.value) {
                return LayoutParams.MATCH_PARENT;
            } else {
                return (int) (columnSize * 0.37);
            }
        } else if (desk.getDeskTypeDirection().value == DeskType.CORNER.value) {
            if (desk.getCornerType().value == CornerType.BOTTOM.value) {
                return (int) (columnSize * 0.37);
            } else if (desk.getCornerType().value == CornerType.TOP.value) {
                return (int) (columnSize * 0.37);
            } else if (desk.getCornerType().value == CornerType.START.value) {
                return (int) (columnSize * 0.54);
            } else if (desk.getCornerType().value == CornerType.END.value) {
                return (int) (columnSize * 0.54);
            }
        }
        return 0;
    }

    public static int getHeightOf(Desk desk, int maxCol) {
        int columnSize = Setting.getInstance().getDeviceWidth() / maxCol;
        if (desk.getDeskTypeDirection().value == DeskType.FULL.value)
            return (int) (columnSize * 0.37);
        else if (desk.getDeskTypeDirection().value == DeskType.MIDDLE.value) {
            if (desk.getMiddleTypeMode().value == MiddleType.HORIZONTAL.value) {
                return (int) (columnSize * 0.37);
            } else {
                return LayoutParams.MATCH_PARENT;
            }
        } else if (desk.getDeskTypeDirection().value == DeskType.CORNER.value) {
            if (desk.getCornerType().value == CornerType.BOTTOM.value) {
                return (int) (columnSize * 0.54);
            } else if (desk.getCornerType().value == CornerType.TOP.value) {
                return (int) (columnSize * 0.54);
            } else if (desk.getCornerType().value == CornerType.START.value) {
                return (int) (columnSize * 0.37);
            } else if (desk.getCornerType().value == CornerType.END.value) {
                return (int) (columnSize * 0.37);
            }
        }
        return 0;
    }

    public static int getReservedColor() {
        return RESERVED_COLOR;
    }

    public static int getNo6tReservedColor() {
        return NOT_RESERVED_COLOR;
    }

    public static int getToReserveColor() {
        return TO_RESERVE_COLOR;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public DeskType getType() {
        return type;
    }

    public void setType(DeskType type) {
        this.type = type;
        if (type.value == DeskType.FULL.value) {
            showCorner(true, true, true, true);
        }
    }

    public CornerType getCornerType() {
        return cornerType;
    }

    public void setCornerType(CornerType cornerType) {
        this.cornerType = cornerType;
        if (cornerType != null && cornerType.getValue() == CornerType.START.getValue()) {
            showCorner(false, true, false, true);
        } else if (cornerType != null && cornerType.getValue() == CornerType.END.getValue()) {
            showCorner(true, false, true, false);
        } else if (cornerType != null && cornerType.getValue() == CornerType.TOP.getValue()) {
            showCorner(true, true, false, false);
        } else {//bottom
            showCorner(false, false, true, true);
        }
    }

    public MiddleType getMiddleType() {
        return middleType;
    }

    public void setMiddleType(MiddleType middleType) {
        this.middleType = middleType;
        showCorner(false, false, false, false);
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public boolean isFull() {
        return full;
    }

    public void setFull(boolean full) {
        this.full = full;
    }

    public boolean isMiddle() {
        return middle;
    }

    public void setMiddle(boolean middle) {
        this.middle = middle;
    }

    public boolean isCorner() {
        return corner;
    }

    public void setCorner(boolean corner) {
        this.corner = corner;
    }

    private void createCard() {
        setRadius(RADIUS);
        setElevation(0);
        setCardElevation(0);
        showCorner(false, false, false, false);
        setCardBackgroundColor(getResources().getColor(DEFAULT_DESK_COLOR));
    }

    public Mode getReserveMode() {
        return reserveMode;
    }

    public void setReserveMode(Mode reserveMode) {
        this.reserveMode = reserveMode;
        setCardBackgroundColor(getResources().getColor(reserveMode.value == Mode.RESERVED.value ?
                RESERVED_COLOR : (reserveMode.value == Mode.NOT_RESERVED.value ? NOT_RESERVED_COLOR : TO_RESERVE_COLOR)));
    }

    public static enum DeskType {
        FULL(0), MIDDLE(1), CORNER(2);
        private int value;

        DeskType(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }

    public static enum CornerType {
        START(0), END(1), TOP(2), BOTTOM(3);
        private int value;

        CornerType(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }

        public void setValue(int value) {
            this.value = value;
        }
    }

    public static enum MiddleType {
        HORIZONTAL(0), VERTICAL(1);
        private int value;

        MiddleType(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }

        public void setValue(int value) {
            this.value = value;
        }
    }

    public static enum Mode {
        RESERVED(0), NOT_RESERVED(1), TO_RESERVE(2);
        int value;

        Mode(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }
}
