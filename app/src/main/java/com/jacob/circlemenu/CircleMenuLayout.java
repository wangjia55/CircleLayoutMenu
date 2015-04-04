package com.jacob.circlemenu;

import android.content.Context;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by jacob-wj on 2015/4/4.
 */
public class CircleMenuLayout extends ViewGroup {


    /**
     * 中心点的坐标
     */
    private float mCenterXY;

    /**
     * 半径
     */
    private float mRadius;

    /**
     * 所有子菜单的图片和文案
     */
    private int[] mDrawable;
    private int[] mTitles;

    /**
     * 每个菜单项目所分得的角度
     */
    private  float sPerAngle ;

    private float mAngle;

    public CircleMenuLayout(Context context) {
        this(context, null);
    }

    public CircleMenuLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CircleMenuLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);


    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
//        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width, height;
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);

        if (widthMode != MeasureSpec.EXACTLY) {
            width = getSuggestedMinimumWidth();
            // 如果未设置背景图片，则设置为屏幕宽高的默认值
            width = width == 0 ? getDefaultWidth() : width;
        } else {
            width = widthSize;
        }

        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);

        if (heightMode != MeasureSpec.EXACTLY) {
            height = getSuggestedMinimumHeight();
            height = height == 0 ? getDefaultWidth() : height;
        } else {
            height = heightSize;
        }

        int layoutSize = Math.min(width,height);
        mCenterXY = layoutSize/2f;
        mRadius = layoutSize/3.3f;

        setMeasuredDimension(layoutSize,layoutSize);

        int count = getChildCount();
        for (int i = 0; i < count; i++) {
            View child = getChildAt(i);
            measureChild(child,widthMeasureSpec,heightMeasureSpec);
        }
    }



    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        if (changed){
            int count = getChildCount();
            mAngle = 0;
            for (int i = 0; i < count; i++) {
                View child = getChildAt(i);
                int childW = child.getMeasuredWidth();
                int childH = child.getMeasuredHeight();
                int left = (int) (mCenterXY+ mRadius*Math.sin(mAngle*Math.PI/180f)-childW/2);
                int top = (int) (mCenterXY-mRadius*Math.cos(mAngle*Math.PI/180f)-childH/2);
                child.layout(left,top,left+childW,top+childH);
                mAngle += sPerAngle;
            }
        }
    }

    /**
     * 外部调用，用来传入菜单的资源文件
     */
    public void setMenuResource(int[] drawables,int[] titles){
        if (drawables.length != titles.length){
            throw new IllegalArgumentException("资源文件和文案没有对应");
        }
        sPerAngle = 360.0f/drawables.length;
        mDrawable = drawables;
        mTitles = titles;
        setMenuChildView();
    }

    private void setMenuChildView() {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        int count = mDrawable.length;
        for (int i = 0; i < count; i++) {
          View child = inflater.inflate(R.layout.layout_menu_item,this,false);
            ImageView imageView = (ImageView) child.findViewById(R.id.image_view_menu);
            TextView textView = (TextView) child.findViewById(R.id.text_view_menu);
            imageView.setImageResource(mDrawable[i]);
            textView.setText(mTitles[i]);
            addView(child);
        }
    }


    /**
     * 获得默认该layout的尺寸
     */
    private int getDefaultWidth() {
        WindowManager windowManager = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        windowManager.getDefaultDisplay().getMetrics(displayMetrics);
        return Math.min(displayMetrics.widthPixels, displayMetrics.heightPixels);
    }

}
