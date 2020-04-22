package ir.ac.kntu.Technical.CustomView;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.RelativeLayout;

import ir.ac.kntu.R;
import ir.ac.kntu.Technical.Other.Other.ContextHelper;

public class ShadowLayout extends RelativeLayout {

    public static final int ALL = 0x1111;

    public static final int LEFT = 0x0001;

    public static final int TOP = 0x0010;

    public static final int RIGHT = 0x0100;

    public static final int BOTTOM = 0x1000;

    public static final int SHAPE_RECTANGLE = 0x0001;

    public static final int SHAPE_OVAL = 0x0010;

    private Paint mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

    private RectF mRectF = new RectF();

    private int mShadowColor = Color.TRANSPARENT;

    private float mShadowRadius = 0;

    private float mShadowDx = 0;

    private float mShadowDy = 0;

    private int mShadowSide = ALL;

    private int mShadowShape = SHAPE_RECTANGLE;

    public ShadowLayout(Context context) {
        this(context, null);
    }

    public ShadowLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ShadowLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        float effect = mShadowRadius + dip2px(5);
        float rectLeft = 0;
        float rectTop = 0;
        float rectRight = this.getMeasuredWidth();
        float rectBottom = this.getMeasuredHeight();
        int paddingLeft = 0;
        int paddingTop = 0;
        int paddingRight = 0;
        int paddingBottom = 0;
        this.getWidth();
        if ((mShadowSide & LEFT) == LEFT) {
            rectLeft = effect;
            paddingLeft = (int) effect;
        }
        if ((mShadowSide & TOP) == TOP) {
            rectTop = effect;
            paddingTop = (int) effect;
        }
        if ((mShadowSide & RIGHT) == RIGHT) {
            rectRight = this.getMeasuredWidth() - effect;
            paddingRight = (int) effect;
        }
        if ((mShadowSide & BOTTOM) == BOTTOM) {
            rectBottom = this.getMeasuredHeight() - effect;
            paddingBottom = (int) effect;
        }
        if (mShadowDy != 0.0f) {
            rectBottom = rectBottom - mShadowDy;
            paddingBottom = paddingBottom + (int) mShadowDy;
        }
        if (mShadowDx != 0.0f) {
            rectRight = rectRight - mShadowDx;
            paddingRight = paddingRight + (int) mShadowDx;
        }
        mRectF.left = rectLeft;
        mRectF.top = rectTop;
        mRectF.right = rectRight;
        mRectF.bottom = rectBottom;
        this.setPadding(paddingLeft, paddingTop, paddingRight, paddingBottom);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        setUpShadowPaint();
        if (mShadowShape == SHAPE_RECTANGLE) {
            canvas.drawRect(mRectF, mPaint);
        } else if (mShadowShape == SHAPE_OVAL) {
            canvas.drawCircle(mRectF.centerX(), mRectF.centerY(), Math.min(mRectF.width(), mRectF.height()) / 2, mPaint);
        }
    }

    public void setShadowColor(int shadowColor) {
        mShadowColor = shadowColor;
        requestLayout();
        postInvalidate();
    }

    public void setShadowRadius(float shadowRadius) {
        mShadowRadius = shadowRadius;
        requestLayout();
        postInvalidate();
    }

    public void setmShadowDy(float mShadowDy) {
        this.mShadowDy = mShadowDy;
        requestLayout();
        postInvalidate();
    }

    private void init(AttributeSet attrs) {
        setLayerType(View.LAYER_TYPE_SOFTWARE, null);  // Turn off hardware acceleration
        this.setWillNotDraw(false);                    // The onDraw(Canvas) method is executed after this method is called.

        TypedArray typedArray = ContextHelper.retrieveContext().obtainStyledAttributes(attrs, R.styleable.ShadowLayout);
        if (typedArray != null) {
            mShadowColor = typedArray.getColor(R.styleable.ShadowLayout_shadowColor,
                    ContextHelper.retrieveContext().getResources().getColor(android.R.color.black));
            mShadowRadius = typedArray.getDimension(R.styleable.ShadowLayout_shadowRadius, dip2px(0));
            mShadowDx = typedArray.getDimension(R.styleable.ShadowLayout_shadowDx, dip2px(0));
            mShadowDy = typedArray.getDimension(R.styleable.ShadowLayout_shadowDy, dip2px(0));
            mShadowSide = typedArray.getInt(R.styleable.ShadowLayout_shadowSide, ALL);
            mShadowShape = typedArray.getInt(R.styleable.ShadowLayout_shadowShape, SHAPE_RECTANGLE);
            typedArray.recycle();
        }
        setUpShadowPaint();
    }

    private void setUpShadowPaint() {
        mPaint.reset();
        mPaint.setAntiAlias(true);
        mPaint.setColor(Color.TRANSPARENT);
        mPaint.setShadowLayer(mShadowRadius, mShadowDx, mShadowDy, mShadowColor);
    }

    private float dip2px(float dpValue) {
        DisplayMetrics dm = ContextHelper.retrieveContext().getResources().getDisplayMetrics();
        float scale = dm.density;
        return (dpValue * scale + 0.5F);
    }

    public void setmShadowSide(int shadowSide) {
        mShadowSide = shadowSide;
        float effect = mShadowRadius + dip2px(5);
        if ((shadowSide & LEFT) == LEFT) {
            mRectF.left = effect;
            setPadding((int) effect, getPaddingTop(), getPaddingRight(), getPaddingBottom());
        }
        if ((shadowSide & TOP) == TOP) {
            mRectF.top = effect;
            setPadding(getPaddingLeft(), (int) effect, getPaddingRight(), getPaddingBottom());
        }
        if ((shadowSide & RIGHT) == RIGHT) {
            mRectF.right = this.getMeasuredWidth() - effect;
            setPadding(getPaddingLeft(), getPaddingTop(), (int) effect, getPaddingBottom());
        }
        if ((shadowSide & BOTTOM) == BOTTOM) {
            mRectF.bottom = this.getMeasuredHeight() - effect;
            setPadding(getPaddingLeft(), getPaddingTop(), getPaddingRight(), (int) effect);
        }
        requestLayout();
        postInvalidate();
    }
}
