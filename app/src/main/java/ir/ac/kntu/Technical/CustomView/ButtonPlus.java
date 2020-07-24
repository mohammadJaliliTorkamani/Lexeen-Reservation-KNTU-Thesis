package ir.ac.kntu.Technical.CustomView;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatButton;

import ir.ac.kntu.R;
import ir.ac.kntu.Technical.Other.Other.Constants;

public class ButtonPlus extends AppCompatButton {
    public ButtonPlus(Context context) {
        super(context);
    }

    public ButtonPlus(Context context, AttributeSet attrs) {
        super(context, attrs);
        setCustomFont(context, attrs);
    }

    public ButtonPlus(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setCustomFont(context, attrs);
    }


    public void setCustomFont(Context context, AttributeSet attrs) {
        try {
            String customFont = getCustomFont(context, attrs);
            Typeface face = Typeface.createFromAsset(context.getAssets(), customFont == null ? Constants.DEFAULT_FARSI_FONT_ASSET_ADDRESS :
                    ("fonts/" + customFont));
            setTypeface(face);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private String getCustomFont(Context context, AttributeSet attrs) {
        TypedArray ta = null;

        try {
            ta = context.obtainStyledAttributes(attrs, R.styleable.ButtonPlus, 0, 0);
            String fontName = ta.getString(R.styleable.ButtonPlus_customFont_btn);
            return fontName;
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            ta.recycle();
        }
        return null;
    }
}
