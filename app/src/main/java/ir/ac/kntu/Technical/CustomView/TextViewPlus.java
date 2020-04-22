package ir.ac.kntu.Technical.CustomView;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.util.AttributeSet;
import android.util.Log;

import com.daasuu.cat.CountAnimationTextView;

import ir.ac.kntu.R;

public class TextViewPlus extends CountAnimationTextView {
    private static final String TAG = "TextView";
    private static final String DEFAULT_FARSI_FONT_ASSET_ADDRESS = "fonts/farsi/syekan.otf";

    public TextViewPlus(Context context) {
        super(context);
    }

    public TextViewPlus(Context context, AttributeSet attrs) {
        super(context, attrs);
        setCustomFont(context, attrs);
    }

    public TextViewPlus(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setCustomFont(context, attrs);
    }

    private void setCustomFont(Context ctx, AttributeSet attrs) {
        TypedArray a = ctx.obtainStyledAttributes(attrs, R.styleable.TextViewPlus);
        String customFont = a.getString(R.styleable.TextViewPlus_customFont);
        boolean underline = a.getBoolean(R.styleable.TextViewPlus_underline, false);
        String text = getText().toString();
        if (underline) {
            SpannableString content = new SpannableString(text);
            content.setSpan(new UnderlineSpan(), 0, content.length(), 0);
            setText(content);
        }
        setCustomFont(ctx, attrs, customFont);
        a.recycle();
    }

    public boolean setCustomFont(Context ctx, AttributeSet attrs, String asset) {
        Typeface typeface;
        try {
            typeface = Typeface.createFromAsset(ctx.getAssets(), asset == null ? DEFAULT_FARSI_FONT_ASSET_ADDRESS : ("fonts/" + asset));
        } catch (Exception e) {
            Log.e(TAG, "Unable to load typeface: " + e.getMessage());
            return false;
        }
        setTypeface(typeface, ctx.obtainStyledAttributes(attrs, R.styleable.TextViewPlus).getBoolean(R.styleable.TextViewPlus_bold, false) ? Typeface.BOLD : Typeface.NORMAL);
        return true;
    }
}