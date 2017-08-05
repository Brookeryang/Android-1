package com.maqiang.doctor.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import android.view.MenuItem;
import cn.bmob.v3.BmobUser;
import com.maqiang.doctor.MyApplication;
import com.maqiang.doctor.R;
import com.maqiang.doctor.bean.User;
import com.maqiang.doctor.fragment.HomePagerFragment;
import com.maqiang.doctor.fragment.MyFragment;
import com.maqiang.doctor.fragment.SearchFragment;
import com.maqiang.doctor.utils.BmobManager;
import com.maqiang.doctor.utils.ToastUtils;

/**
 * Created by maqiang on 2017/5/1.
 * Function: 首页
 */

public class HomeActivity extends AppCompatActivity {

  BottomNavigationView mNavigationView;
  private FragmentManager fm;
  private FragmentTransaction ft;

  private User mUser;

  private Handler mHandler = new Handler() {
    @Override public void handleMessage(Message msg) {
      super.handleMessage(msg);
      if (msg.arg1 == 0) {
        ToastUtils.show("账号已经失效,请重新登录");
        goToLoginAty();
      }
    }
  };

  @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.main);
    MyApplication.getInstance().addActivity(this);
    mUser = BmobUser.getCurrentUser(User.class);
    if (mUser == null) {
      goToLoginAty();
    }else {
      if (MyApplication.getInstance().getPassword() != null) {
        mUser.setPassword(MyApplication.getInstance().getPassword());
        BmobManager.login(mUser, mHandler);
      }
      init();
    }
  }

  private void goToLoginAty() {
    Intent intent = new Intent(this, LoginAty.class);
    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
    startActivity(intent);
  }

  private void init() {
    mNavigationView = (BottomNavigationView) findViewById(R.id.bnv);
    mNavigationView.setOnNavigationItemSelectedListener(
        new BottomNavigationView.OnNavigationItemSelectedListener() {
          @Override public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
              case R.id.tab_home:
                loadHomePagerFragment();
                return true;
              case R.id.tab_user:
                loadMyFragment();
                return true;
              case R.id.tab_search:
                loadSearchFragment();
                return true;
            }
            return false;
          }
        });
    loadHomePagerFragment();
  }

  public void loadHomePagerFragment() {
    HomePagerFragment fragment = new HomePagerFragment();
    fm = getSupportFragmentManager();
    ft = fm.beginTransaction();
    ft.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out);
    ft.replace(R.id.framelayout, fragment);
    ft.commit();
  }

  public void loadMyFragment() {
    MyFragment fragment = new MyFragment();
    fm = getSupportFragmentManager();
    ft = fm.beginTransaction();
    ft.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out);
    ft.replace(R.id.framelayout, fragment);
    ft.commit();
  }

  public void loadSearchFragment() {
    SearchFragment fragment = new SearchFragment();
    fm = getSupportFragmentManager();
    ft = fm.beginTransaction();
    ft.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out);
    ft.replace(R.id.framelayout, fragment);
    ft.commit();
  }

  long firstTime;

  @Override public void onBackPressed() {
    long secondTime = System.currentTimeMillis();
    if (secondTime - firstTime > 2000) {
      ToastUtils.show("再按一次退出程序");
      firstTime = secondTime;
    } else {
      MyApplication.getInstance().exit();
    }
  }
}
