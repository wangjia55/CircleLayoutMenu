package com.jacob.circlemenu;

import android.content.Context;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;

/**
 * Package : com.jacob.circlemenu
 * Author : jacob
 * Date : 15-3-9
 * Description : 这个类是用来xxx
 */
public class CircleMenuLayoutOne extends ViewGroup {

    private int width = dpToPx(170);

    private int radius = dpToPx(45);

    public CircleMenuLayoutOne(Context context) {
        this(context, null);
    }

    public CircleMenuLayoutOne(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CircleMenuLayoutOne(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
//        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(width, width);


        int count = getChildCount();
        for (int i = 0; i < count; i++) {
            View view = getChildAt(i);
            measureChild(view,widthMeasureSpec,heightMeasureSpec);
        }

    }


    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        if (changed) {
            int count = getChildCount();
            double angle = Math.PI * 2.0 / count;
            for (int i = 0; i < count; i++) {
                View view = getChildAt(i);
                int centerX = (int) (width / 2 + radius * Math.cos(angle * i));
                int centerY = (int) (width / 2 + radius * Math.sin(angle * i));

                int width = view.getMeasuredWidth();
                int height = view.getMeasuredHeight();

                view.layout(centerX - width / 2, centerY - height / 2, centerX + width / 2, centerY + height / 2);
            }

        }
    }


    private int dpToPx(int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, getResources().getDisplayMetrics());
    }
}
