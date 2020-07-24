package ir.ac.kntu.Entity;

import android.content.Context;
import android.util.AttributeSet;

import com.github.captain_miao.optroundcardview.OptRoundCardView;

import ir.ac.kntu.R;

public class G_Chair extends OptRoundCardView {
    private final static int RADIUS = 16;
    private Context context;
    private int chairID;

    public G_Chair(Context context) {
        super(context);
        this.context = context;
        createChair();
    }

    public G_Chair(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        createChair();
    }

    public G_Chair(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        createChair();
    }

    public static int getRADIUS() {
        return RADIUS;
    }

    public int getChairID() {
        return chairID;
    }

    public void setChairID(int id) {
        this.chairID = id;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    /**
     * creates chair with specific RADIUS and COLOR
     */
    private void createChair() {
        setCardBackgroundColor(context.getResources().getColor(R.color.chair_color));
        setRadius(RADIUS);
        setCardElevation(0);
    }
}
