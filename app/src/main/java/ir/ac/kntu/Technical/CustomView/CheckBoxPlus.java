package ir.ac.kntu.Technical.CustomView;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.Typeface;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatCheckBox;

import ir.ac.kntu.R;
import ir.ac.kntu.Technical.Other.Other.Constants;


public class CheckBoxPlus extends AppCompatCheckBox {

    public CheckBoxPlus(Context context) {
        super(context);
        setButtonTintList(ColorStateList.valueOf(Color.BLACK));
    }

    public CheckBoxPlus(Context context, AttributeSet attrs) {
        super(context, attrs);
        setButtonTintList(ColorStateList.valueOf(Color.BLACK));
        setCustomFont(context, attrs);
    }

    public CheckBoxPlus(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setButtonTintList(ColorStateList.valueOf(Color.BLACK));
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
            String fontName = ta.getString(R.styleable.CheckBoxPlus_customFont_cb);
            return fontName;
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            ta.recycle();
        }
        return null;
    }
}