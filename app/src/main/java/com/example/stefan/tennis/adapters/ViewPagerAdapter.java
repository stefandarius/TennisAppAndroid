package com.example.stefan.tennis.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

public class ViewPagerAdapter extends ViewPager {

    private boolean isEnabled;

    public ViewPagerAdapter(@NonNull Context context) {
        super(context);
    }

    public ViewPagerAdapter(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return isEnabled;
    }

    @Override
    public void setEnabled(boolean enabled) {
        isEnabled = enabled;
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        return isEnabled;
    }
}
