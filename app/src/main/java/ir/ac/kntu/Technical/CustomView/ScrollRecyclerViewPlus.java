package ir.ac.kntu.Technical.CustomView;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ViewConfiguration;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

public class ScrollRecyclerViewPlus extends RecyclerView {
    private int scrollPointerId = -1;
    private int pointTouchX = 0;
    private int pointTouchY = 0;
    private int touchSlopType = 0;
    private Context context;

    public ScrollRecyclerViewPlus(@NonNull Context context) {
        super(context, null);
    }

    public ScrollRecyclerViewPlus(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ScrollRecyclerViewPlus(@NonNull Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.context = context;
        ViewConfiguration vc = ViewConfiguration.get(context);
        touchSlopType = vc.getScaledTouchSlop();
    }


    @Override
    public void setScrollingTouchSlop(int slopConstant) {
        super.setScrollingTouchSlop(slopConstant);

        ViewConfiguration vc = ViewConfiguration.get(context);
        switch (slopConstant) {
            case TOUCH_SLOP_DEFAULT:
                touchSlopType = vc.getScaledTouchSlop();
                break;
            case TOUCH_SLOP_PAGING:
                touchSlopType = vc.getScaledPagingTouchSlop();
                break;
        }
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent e) {
        if (e == null) {
            return false;
        }

        int action = e.getActionMasked();
        int actionIndex = e.getActionIndex();

        switch (action) {
            case MotionEvent.ACTION_DOWN:
                scrollPointerId = e.getPointerId(0);
                pointTouchX = Math.round(e.getX() + 0.5f);
                pointTouchY = Math.round(e.getY() + 0.5f);
                return super.onInterceptTouchEvent(e);

            case MotionEvent.ACTION_POINTER_DOWN:
                scrollPointerId = e.getPointerId(actionIndex);
                pointTouchX = Math.round(e.getX(actionIndex) + 0.5f);
                pointTouchY = Math.round(e.getY(actionIndex) + 0.5f);
                return super.onInterceptTouchEvent(e);

            case MotionEvent.ACTION_MOVE:
                int index = e.findPointerIndex(scrollPointerId);
                if (index < 0) {
                    return false;
                }

                int x = Math.round(e.getX(index) + 0.5f);
                int y = Math.round(e.getY(index) + 0.5f);
                if (getScrollState() != SCROLL_STATE_DRAGGING) {
                    int dx = x - pointTouchX;
                    int dy = y - pointTouchY;
                    boolean startScroll = false;
                    if (getLayoutManager().canScrollHorizontally() && Math.abs(dx) >
                            touchSlopType && (getLayoutManager().canScrollVertically() || Math.abs(dx) > Math.abs(dy))) {
                        startScroll = true;
                    }
                    if (getLayoutManager().canScrollVertically() && Math.abs(dy) >
                            touchSlopType && (getLayoutManager().canScrollHorizontally() || Math.abs(dy) > Math.abs(dx))) {
                        startScroll = true;
                    }
                    return startScroll && super.onInterceptTouchEvent(e);
                }
                return super.onInterceptTouchEvent(e);

            default:
                return super.onInterceptTouchEvent(e);
        }
    }

    @Override
    public void requestDisallowInterceptTouchEvent(boolean disallowIntercept) {
        super.requestDisallowInterceptTouchEvent(disallowIntercept);
    }
}
