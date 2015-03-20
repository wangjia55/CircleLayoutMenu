package com.jacob.circlemenu;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Package : com.jacob.circlemenu
 * Author : jacob
 * Date : 15-3-9
 * Description : 这个类是用来xxx
 */
public class CircleMenu extends ViewGroup {

    //菜单按钮的资源图片
    private int[] imgs = new int[]{
            R.mipmap.ic_item_1,
            R.mipmap.ic_item_2,
            R.mipmap.ic_item_3,
            R.mipmap.ic_item_4,
            R.mipmap.ic_item_5,
            R.mipmap.ic_item_6};

    //菜单按钮对应的文案
    private int[] titles = new int[]{
            R.string.title_one,
            R.string.title_two,
            R.string.title_three,
            R.string.title_four,
            R.string.title_five,
            R.string.title_six
    };

    private int count = titles.length;

    //背景的Bitmap
    private Bitmap mBitmapBg;

    private Bitmap mBitmapCenter;

    private Rect rectCenter;

    //屏幕的尺寸
    private int layoutRadius;

    //菜单和外圆盘的间隔,建议做成自定义属性
    private int padding = dpToPx(25);

    private int mRadius;


    //是否在滚动
    private boolean isFling = false;

    //按下到抬起时的角度
    private float mTmpAngle;

    //按下的时间
    private long mDownTime;


    /**
     * 布局时的开始角度
     */
    private double mStartAngle = 0;


    /**
     * 当每秒移动角度达到该值时，认为是快速移动
     */
    private static final int FLINGABLE_VALUE = 300;

    /**
     * 如果移动角度达到该值，则屏蔽点击
     */
    private static final int NOCLICK_VALUE = 3;

    /**
     * 当每秒移动角度达到该值时，认为是快速移动
     */
    private int mFlingableValue = FLINGABLE_VALUE;


    private OnMenuClickListener menuClickListener;


    public CircleMenu(Context context) {
        this(context, null);
    }

    public CircleMenu(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CircleMenu(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        mBitmapBg = BitmapFactory.decodeResource(getResources(), R.mipmap.circle_bg);
        mBitmapCenter = BitmapFactory.decodeResource(getResources(), R.mipmap.turnplate_center_unlogin);
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        windowManager.getDefaultDisplay().getMetrics(displayMetrics);
        layoutRadius = displayMetrics.widthPixels;
        initLayoutView(context);
        setPadding(0, 0, 0, 0);
    }

    private void initLayoutView(Context context) {
        LayoutInflater inflater = LayoutInflater.from(context);
        for (int i = 0; i < count; i++) {
            final int position = i;
            View view = inflater.inflate(R.layout.layout_menu_item, null, false);
            view.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (menuClickListener != null) {
                        menuClickListener.onMenuClick(v, position);
                    }
                }
            });
            addView(view);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawBitmap(mBitmapCenter, null, rectCenter, null);

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
//        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int modeW = MeasureSpec.getMode(widthMeasureSpec);
        int sizeW = MeasureSpec.getSize(widthMeasureSpec);
        int modeH = MeasureSpec.getMode(heightMeasureSpec);
        int sizeH = MeasureSpec.getSize(heightMeasureSpec);

        if (modeW == MeasureSpec.EXACTLY || modeH == MeasureSpec.EXACTLY) {
            layoutRadius = Math.min(sizeW, sizeH);
        } else {
            int bitmapW = mBitmapBg.getWidth();
            layoutRadius = Math.min(bitmapW, layoutRadius);
        }
        setMeasuredDimension(layoutRadius, layoutRadius);

        mRadius = layoutRadius / 2 - padding;
        int centerX = layoutRadius / 2;
        int centerY = layoutRadius / 2;
        rectCenter = new Rect(centerX - mBitmapCenter.getWidth() / 2, centerY - mBitmapCenter.getHeight() / 2,
                centerX + mBitmapCenter.getWidth() / 2, centerY + mBitmapCenter.getHeight() / 2);

        int count = getChildCount();
        for (int i = 0; i < count; i++) {
            View view = getChildAt(i);
            measureChild(view, widthMeasureSpec, heightMeasureSpec);
        }

    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int count = getChildCount();
        float angle = (float) (Math.PI * 2.0 / (count));
        for (int i = 0; i < count; i++) {
            View view = getChildAt(i);
            int width = view.getMeasuredWidth();
            int height = view.getMeasuredHeight();

            mStartAngle %= Math.PI * 2;
            mRadius = layoutRadius / 2 - padding - (Math.max(width, height) / 2);
            int centerX = (int) (layoutRadius / 2 + mRadius * Math.cos(mStartAngle));
            int centerY = (int) (layoutRadius / 2 + mRadius * Math.sin(mStartAngle));

            view.layout(centerX - width / 2, centerY - height / 2, centerX + width / 2, centerY + height / 2);

            mStartAngle += angle;
        }
    }

    public void updateView() {
        for (int i = 0; i < count; i++) {
            View view = getChildAt(i);
            ImageView imageView = (ImageView) view.findViewById(R.id.image_view_menu);
            TextView textView = (TextView) view.findViewById(R.id.text_view_menu);
            imageView.setImageResource(imgs[i]);
            textView.setText(titles[i]);
        }
    }

    /**
     * 记录上一次的x，y坐标
     */
    private float mLastX;
    private float mLastY;

    private double lastAngle;

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        float x = ev.getX();
        float y = ev.getY();

        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mLastX = x;
                mLastY = y;
                mStartAngle = 0;
                break;
            case MotionEvent.ACTION_MOVE:
                float start = getAngle(mLastX, mLastY);
                float end = getAngle(x, y);
                mStartAngle = (lastAngle + Math.PI * 2 * (end - start) / 360.0f)*1.055;
                // 重新布局
                requestLayout();
                break;
            case MotionEvent.ACTION_UP:
                lastAngle = mStartAngle;
                break;
        }

        return super.dispatchTouchEvent(ev);

    }

    /**
     * 主要为了action_down时，返回true
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return true;
    }

    /**
     * 根据触摸的位置，计算角度
     */
    private float getAngle(float xTouch, float yTouch) {
        double x = xTouch - (mRadius / 2d);
        double y = yTouch - (mRadius / 2d);
        return (float) (Math.asin(y / Math.hypot(x, y)) * 180 / Math.PI);
    }

    /**
     * 根据当前位置计算象限
     *
     * @param x
     * @param y
     *
     * @return
     */
    private int getQuadrant(float x, float y) {
        int tmpX = (int) (x - mRadius / 2);
        int tmpY = (int) (y - mRadius / 2);
        if (tmpX >= 0) {
            return tmpY >= 0 ? 4 : 1;
        } else {
            return tmpY >= 0 ? 3 : 2;
        }

    }


    public interface OnMenuClickListener {
        public void onMenuClick(View view, int position);
    }

    public void setOnMenuClickListener(OnMenuClickListener clickListener) {
        this.menuClickListener = clickListener;
    }

    private int dpToPx(int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, getResources().getDisplayMetrics());
    }
}
