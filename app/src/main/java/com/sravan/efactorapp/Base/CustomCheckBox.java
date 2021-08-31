package com.sravan.efactorapp.Base;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;

import android.util.AttributeSet;
import android.util.Log;

import androidx.appcompat.widget.AppCompatCheckBox;
import com.sravan.efactorapp.R;

/**
 * Created by ubuntu on 7/12/17.
 */

public class CustomCheckBox extends AppCompatCheckBox {
    private static final String TAG = "CustomCheckBox";
    public CustomCheckBox(Context context) {
        super(context);
    }

    public CustomCheckBox(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomCheckBox(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setCustomFont(context, defStyleAttr);
    }

    private void setCustomFont(Context context, int defStyleAttr){
        TypedArray typedArray=context.obtainStyledAttributes(defStyleAttr, R.styleable.CustomTextView);
        String customFont=typedArray.getString(R.styleable.CustomTextView_customFont);
        setCustomFont(context, customFont);
    }

    private boolean setCustomFont(Context context, String asset) {
        Typeface typeface=null;
        try {
            typeface=Typeface.createFromAsset(context.getAssets(), asset);
        }
        catch (Exception e){
            Log.e(TAG, "setCustomFont: "+e.getMessage() );
            return false;
        }

        setTypeface(typeface);
        return true;
    }

    }
