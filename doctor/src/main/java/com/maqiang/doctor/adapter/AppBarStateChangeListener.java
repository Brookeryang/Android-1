package com.maqiang.doctor.adapter;

import android.support.design.widget.AppBarLayout;

/**
 * Created by maqiang on 2017/5/3.
 *
 * Function:自定义监听AppBarLayout的展开状态
 */

public abstract class AppBarStateChangeListener implements AppBarLayout.OnOffsetChangedListener {
  /**
   *  展开状态，折叠状态，中间状态
   */
  public enum State{
    EXPANDED,
    COLLAPSED,
    IDIE
  }
  private State mCurrentState = State.IDIE;

  @Override public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
    if (verticalOffset == 0) {
      if (mCurrentState != State.EXPANDED) {
          onStateChanged(appBarLayout,State.EXPANDED);
      }
      mCurrentState = State.EXPANDED;
    } else if (Math.abs(verticalOffset) >= appBarLayout.getTotalScrollRange()){
      if (mCurrentState != State.COLLAPSED) {
        onStateChanged(appBarLayout,State.COLLAPSED);
      }
      mCurrentState = State.COLLAPSED;
    }else {
      if (mCurrentState != State.IDIE) {
        onStateChanged(appBarLayout,State.IDIE);
      }
      mCurrentState = State.IDIE;
    }
  }

  public abstract void onStateChanged(AppBarLayout appBarLayout, State state);
}
