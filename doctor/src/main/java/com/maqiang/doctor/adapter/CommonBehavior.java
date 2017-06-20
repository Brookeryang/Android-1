package com.maqiang.doctor.adapter;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import com.maqiang.doctor.utils.ToastUtils;

/**
 * Created by maqiang on 2017/5/1.
 *
 * Function:
 */

public class CommonBehavior extends CoordinatorLayout.Behavior {

  private boolean isAnimatingOut = false;
  private Context mContext;


  public CommonBehavior(Context context, AttributeSet attrs) {
    super(context, attrs);
    mContext = context;
  }

  @Override public boolean onStartNestedScroll(CoordinatorLayout coordinatorLayout, View child,
      View directTargetChild, View target, int nestedScrollAxes) {
    return nestedScrollAxes == ViewCompat.SCROLL_AXIS_VERTICAL;
  }

  @RequiresApi(api = Build.VERSION_CODES.M) @Override public void onNestedScroll(CoordinatorLayout coordinatorLayout, View child, View target,
      int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed) {
    AppBarLayout appbar = (AppBarLayout) coordinatorLayout.getChildAt(0);
    appbar.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
      @Override public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
        if (verticalOffset == 700){
          ToastUtils.show("700");
        }
      }
    });




    if ((dyConsumed > 0 || dyUnconsumed > 0) && !isAnimatingOut
        && child.getVisibility() == View.VISIBLE) {// 手指上滑，隐藏FAB
      Log.d("手指上滑", "onNestedScroll: "+dxConsumed+"-"+dyConsumed+"-"+dxConsumed+"-"+dyUnconsumed);
      ObjectAnimator.ofFloat(child,"translationY",0,-child.getMeasuredHeight()).setDuration(500).start();
      isAnimatingOut = true;
      //child.setVisibility(View.INVISIBLE);
    } else if ((dyConsumed < 0 || dyUnconsumed < 0) && isAnimatingOut) {
       // 手指下滑，显示FAB
      Log.d("手指下滑", "onNestedScroll: "+dxConsumed+"-"+dyConsumed+"-"+dxConsumed+"-"+dyUnconsumed);
      ObjectAnimator.ofFloat(child,"translationY",-child.getMeasuredHeight(),0).setDuration(500).start();
      isAnimatingOut = false;
    }
  }

  @Override
  public boolean onNestedFling(CoordinatorLayout coordinatorLayout, View child, View target,
      float velocityX, float velocityY, boolean consumed) {
    return super.onNestedFling(coordinatorLayout, child, target, velocityX, velocityY, consumed);
  }
}

