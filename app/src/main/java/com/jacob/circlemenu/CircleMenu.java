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
    private int mGroupWidth;

    //菜单和外圆盘的间隔,建议做成自定义属性
    private int padding = dpToPx(25);

    private int radius;


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
        mGroupWidth = displayMetrics.widthPixels;
        initLayoutView(context);
        setPadding(0, 0, 0, 0);
    }

    private void initLayoutView(Context context) {
        LayoutInflater inflater = LayoutInflater.from(context);
        for (int i = 0; i < count; i++) {
            View view = inflater.inflate(R.layout.layout_menu_item, null, false);
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
            mGroupWidth = Math.min(sizeW, sizeH);
        } else {
            int bitmapW = mBitmapBg.getWidth();
            mGroupWidth = Math.min(bitmapW, mGroupWidth);
        }
        setMeasuredDimension(mGroupWidth, mGroupWidth);

        radius = mGroupWidth / 2 - padding;
        int centerX = mGroupWidth / 2;
        int centerY = mGroupWidth / 2;
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
        if (changed) {
            int count = getChildCount();
            float angle = (float) (Math.PI * 2.0 / (count));
            for (int i = 0; i < count; i++) {
                View view = getChildAt(i);
                int width = view.getMeasuredWidth();
                int height = view.getMeasuredHeight();

                radius = mGroupWidth / 2 - padding - (Math.max(width, height) / 2);
                int x = (int) (mGroupWidth / 2 + radius * Math.cos(angle * i));
                int y = (int) (mGroupWidth / 2 + radius * Math.sin(angle * i));

                view.layout(x - width / 2, y - height / 2, x + width / 2, y + height / 2);

                ImageView imageView = (ImageView) view.findViewById(R.id.image_view_menu);
                TextView textView = (TextView) view.findViewById(R.id.text_view_menu);

                imageView.setImageResource(imgs[i]);
                textView.setText(titles[i]);
            }
        }
    }


    private int dpToPx(int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, getResources().getDisplayMetrics());
    }
}
