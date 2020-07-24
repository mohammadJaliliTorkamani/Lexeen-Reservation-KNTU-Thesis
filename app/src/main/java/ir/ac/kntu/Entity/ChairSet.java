package ir.ac.kntu.Entity;

import android.content.Context;
import android.view.View;
import android.widget.FrameLayout;

import ir.ac.kntu.Technical.Other.Other.ContextHelper;
import ir.ac.kntu.Technical.Other.Other.Setting;

public class ChairSet {
    private Chair startChair;
    private Chair endChair;
    private Chair topChair;
    private Chair bottomChair;

    public ChairSet(Chair startChair, Chair endChair, Chair topChair, Chair bottomChair) {
        this.startChair = startChair;
        this.endChair = endChair;
        this.topChair = topChair;
        this.bottomChair = bottomChair;
    }

    /**
     * creates new GraphicChairSet for the passed logical object (chaiset)
     *
     * @param chairSet to create corresponding graphic chair set from/
     * @param maxCol   maximum columns of view (used for widths determining)
     * @return GraphicChairSet
     */
    public static GraphicChairSet populateGraphics(ChairSet chairSet, int maxCol) {

        int columnSize = Setting.getInstance().getDeviceWidth() / maxCol;

        Context context = ContextHelper.retrieveContext();
        G_Chair g_chair_TOP = new G_Chair(context);
        G_Chair g_chair_BOTTOM = new G_Chair(context);
        G_Chair g_chair_START = new G_Chair(context);
        G_Chair g_chair_END = new G_Chair(context);
        g_chair_TOP.setId(View.generateViewId());
        g_chair_BOTTOM.setId(View.generateViewId());
        g_chair_START.setId(View.generateViewId());
        g_chair_END.setId(View.generateViewId());
        g_chair_TOP.setChairID(chairSet.getTopChair().getId());
        g_chair_BOTTOM.setChairID(chairSet.getBottomChair().getId());
        g_chair_START.setChairID(chairSet.getStartChair().getId());
        g_chair_END.setChairID(chairSet.getEndChair().getId());

        int minVal = (int) (columnSize * 0.10);
        int maxVal = (int) (columnSize * 0.31);

        FrameLayout.LayoutParams H_TOP_layoutParams = new FrameLayout.LayoutParams(maxVal, minVal);
        FrameLayout.LayoutParams H_BOTTOM_layoutParams = new FrameLayout.LayoutParams(maxVal, minVal);
        FrameLayout.LayoutParams V_START_layoutParams = new FrameLayout.LayoutParams(minVal, maxVal);
        FrameLayout.LayoutParams V_END_layoutParams = new FrameLayout.LayoutParams(minVal, maxVal);

        g_chair_TOP.setLayoutParams(H_TOP_layoutParams);
        g_chair_BOTTOM.setLayoutParams(H_BOTTOM_layoutParams);
        g_chair_END.setLayoutParams(V_START_layoutParams);
        g_chair_START.setLayoutParams(V_END_layoutParams);

        return new GraphicChairSet(g_chair_START, g_chair_END, g_chair_TOP, g_chair_BOTTOM);
    }

    public Chair getStartChair() {
        return startChair;
    }

    public void setStartChair(Chair startChair) {
        this.startChair = startChair;
    }

    public Chair getEndChair() {
        return endChair;
    }

    public void setEndChair(Chair endChair) {
        this.endChair = endChair;
    }

    public Chair getTopChair() {
        return topChair;
    }

    public void setTopChair(Chair topChair) {
        this.topChair = topChair;
    }

    public Chair getBottomChair() {
        return bottomChair;
    }

    public void setBottomChair(Chair bottomChair) {
        this.bottomChair = bottomChair;
    }
}
