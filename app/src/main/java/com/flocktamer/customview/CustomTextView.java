package com.flocktamer.customview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.text.Html;
import android.text.Spanned;
import android.util.AttributeSet;
import android.widget.TextView;

import com.flocktamer.R;


/**
 * Created by amandeep on 14/10/15.
 */
public class CustomTextView extends TextView {

    private String mFont;
    private String htmlText;


    public CustomTextView(Context context) {
        super(context, null);
        init(context);
    }


    public CustomTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray a = context.getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.ViewShofur,
                0, 0);
        try {
            mFont = a.getString(R.styleable.ViewShofur_font);
            htmlText = a.getString(R.styleable.ViewShofur_html);
        } finally {
            a.recycle();
        }
        init(context);
    }

    public CustomTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray a = context.getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.ViewShofur,
                0, 0);

        try {
            mFont = a.getString(R.styleable.ViewShofur_font);
            htmlText = a.getString(R.styleable.ViewShofur_html);
        } finally {
            a.recycle();
        }
        init(context);
    }

    private void init(Context context) {
        if (mFont != null) {
            Typeface tf = Typeface.createFromAsset(context.getAssets(), "fonts/" + mFont);
            setTypeface(tf);
        }

        if (htmlText != null && !htmlText.isEmpty()) {

            Spanned result;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                result = Html.fromHtml(htmlText, Html.FROM_HTML_MODE_LEGACY);
            } else {
                result = Html.fromHtml(htmlText);
            }

            setText(result);
        }
    }


}
