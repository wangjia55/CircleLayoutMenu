package com.jacob.circlemenu;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;


public class MainActivity extends ActionBarActivity {
    //菜单按钮的资源图片
    private int[] imgs = new int[]{
            R.drawable.ic_item_1,
            R.drawable.ic_item_2,
            R.drawable.ic_item_3,
            R.drawable.ic_item_4,
            R.drawable.ic_item_5,
            R.drawable.ic_item_6};

    //菜单按钮对应的文案
    private int[] titles = new int[]{
            R.string.title_one,
            R.string.title_two,
            R.string.title_three,
            R.string.title_four,
            R.string.title_five,
            R.string.title_six
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        CircleMenuLayout circleMenu = (CircleMenuLayout) findViewById(R.id.circle_menu);
        circleMenu.setMenuResource(imgs,titles);
    }

}
