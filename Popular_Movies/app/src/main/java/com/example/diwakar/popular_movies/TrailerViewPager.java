package com.example.diwakar.popular_movies;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by diwakar on 7/7/16.
 */

/*
This class sets the viewpager to work correctly when layout_width=wrap_content
 */
public class TrailerViewPager extends ViewPager {
    public TrailerViewPager(Context context) {
        super(context);
    }

    public TrailerViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int height = 0;
        for (int i = 0; i < getChildCount(); i++) {
            View child = getChildAt(i);
            child.measure(widthMeasureSpec, MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));

            int h = child.getMeasuredHeight();
            if (h > height) height = h;
        }

        heightMeasureSpec = MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY);

        //this is important
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }
}
