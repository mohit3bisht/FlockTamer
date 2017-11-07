package com.flocktamer.customview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

import com.flocktamer.R;
import com.flocktamer.R;
import com.flocktamer.customview.drawable.BorderDrawable;


public class BorderLayout extends RelativeLayout {

    private BorderDrawable borderDrawable;

    public BorderLayout(Context context, AttributeSet attrs) {
        super(context, attrs);

//        if (borderDrawable == null)
        borderDrawable = new BorderDrawable();

        if (attrs != null) {
            TypedArray a = getResources().obtainAttributes(attrs, R.styleable.BorderLayout);

            int width, color;

            width = (int) a.getDimension(R.styleable.BorderLayout_leftBorderWidth, 0);
            color = a.getColor(R.styleable.BorderLayout_leftBorderColor, Color.BLACK);
            borderDrawable.setLeftBorder(width, color);

            width = (int) a.getDimension(R.styleable.BorderLayout_topBorderWidth, 0);
            color = a.getColor(R.styleable.BorderLayout_topBorderColor, Color.BLACK);
            borderDrawable.setTopBorder(width, color);

            width = (int) a.getDimension(R.styleable.BorderLayout_rightBorderWidth, 0);
            color = a.getColor(R.styleable.BorderLayout_rightBorderColor, Color.BLACK);
            borderDrawable.setRightBorder(width, color);

            width = (int) a.getDimension(R.styleable.BorderLayout_bottomBorderWidth, 0);
            color = a.getColor(R.styleable.BorderLayout_bottomBorderColor, Color.BLACK);
            borderDrawable.setBottomBorder(width, color);
        }

        if (getBackground() != null) {
            borderDrawable.setBackground(borderDrawable);
        }

        setBackgroundDrawable(borderDrawable);
    }

    public BorderLayout(Context context) {
        this(context, null);
    }

    @Override
    public void setBackgroundDrawable(Drawable d) {
        if (d == borderDrawable)
            super.setBackgroundDrawable(d);
        else {
            if (borderDrawable == null)
                borderDrawable = new BorderDrawable();
            borderDrawable.setBackground(d);
        }
    }

    public void removeBoottomBorder() {
        borderDrawable.removeBottomBorder();
        invalidate();
    }

    public void setBottomBorder() {
        borderDrawable.reCreateBorder();
        invalidate();
    }
}
