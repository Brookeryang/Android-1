package com.maqiang.doctor.view;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.widget.NestedScrollView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;

/**
 * Created by maqiang on 2017/5/2.
 *
 * Function:因为嵌套了横向滑动的RecycleView
 * 所以需要做事件拦截处理
 */

public class RootNestedScrollView extends NestedScrollView {
  private static final String TAG = "RootNestedScrollView";

  private int downX;
  private int downY;

  public RootNestedScrollView(Context context) {
    this(context, null);
  }

  public RootNestedScrollView(Context context, @Nullable AttributeSet attrs) {
    this(context, attrs, 0);
  }

  public RootNestedScrollView(Context context, @Nullable AttributeSet attrs, int defStyle) {
    super(context, attrs, defStyle);

  }

  @Override public boolean onInterceptTouchEvent(MotionEvent ev) {
    int action = ev.getAction();
    switch (action) {
      case MotionEvent.ACTION_DOWN:
        downX = (int) ev.getRawX();
        downY = (int) ev.getRawY();
        break;
      case MotionEvent.ACTION_MOVE:
        int moveX = (int) ev.getRawX();
        int moveY = (int) ev.getRawY();
        if (Math.abs(moveX - downX) > Math.abs(moveY-downY)) {
          return false;
        }
    }
    return super.onInterceptTouchEvent(ev);
  }

  @Override public boolean onTouchEvent(MotionEvent ev) {
    return super.onTouchEvent(ev);
  }
}
