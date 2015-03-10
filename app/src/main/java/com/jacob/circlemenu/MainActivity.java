package com.jacob.circlemenu;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;


public class MainActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        CircleMenu circleMenu = (CircleMenu) findViewById(R.id.circle_menu);
        circleMenu.setOnMenuClickListener(new CircleMenu.OnMenuClickListener() {
            @Override
            public void onMenuClick(View view, int position) {
                Log.e("TAG", "position:" + position);
            }
        });

    }

}
