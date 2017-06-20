package com.maqiang.doctor;

import android.app.Activity;
import android.app.Application;

import android.content.SharedPreferences;
import com.maqiang.doctor.bean.User;
import com.maqiang.doctor.utils.BmobManager;
import com.maqiang.doctor.utils.BmobUtils;

import cn.bmob.v3.Bmob;
import com.squareup.picasso.OkHttpDownloader;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by maqiang on 2017/2/26.
 */

public class MyApplication extends Application {

  public static MyApplication mContext;
  //保存们每一个activity是关键
  private List<Activity> mList = new LinkedList<Activity>();
  private SharedPreferences mSp;


  @Override public void onCreate() {
    mContext = this;
    super.onCreate();
    Bmob.initialize(this, BmobUtils.APP_ID);
  }



  public static MyApplication getInstance() {
    return mContext;
  }

  //添加Activity
  public void addActivity(Activity activity) {
    mList.add(activity);
  }
  //关闭每一个list内的所有activity
  public void exit() {
    try {
      for (Activity activity:mList) {
        if (activity != null)
          activity.finish();
      }
    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      System.exit(0);
    }
  }

  /**
   * 保存用户的登录密码
   * @param password
   */
  public void savePassword(String password){
    mSp = getSharedPreferences("password",MODE_PRIVATE);
    SharedPreferences.Editor editor = mSp.edit();
    editor.putString("password",password);
    editor.commit();
  }

  /**
   * 获取缓存用户的登录密码
   */
  public String getPassword(){
    mSp = getSharedPreferences("password",MODE_PRIVATE);
    return mSp.getString("password",null);
  }

  /**
   * 保存记录血糖的状态值
   * @param state
   */
  public void saveBloodState(int state,User user){
    mSp = getSharedPreferences(user.getObjectId()+"blood"+ BmobManager.getToday(),MODE_PRIVATE);
    SharedPreferences.Editor editor = mSp.edit();
    editor.putInt(user.getObjectId()+"blood"+ BmobManager.getToday(),state);
    editor.commit();
  }

  /**
   * 获取缓存血糖的状态值
   */
  public int getBloodState(User user){
    mSp = getSharedPreferences(user.getObjectId()+"blood"+ BmobManager.getToday(),MODE_PRIVATE);
    return mSp.getInt(user.getObjectId()+"blood"+ BmobManager.getToday(),0);
  }



  public void saveFirstLogin(int state,User user){
    mSp = getSharedPreferences(user.getObjectId()+"first",MODE_PRIVATE);
    SharedPreferences.Editor editor = mSp.edit();
    editor.putInt(user.getObjectId()+"first",state);
    editor.commit();
  }


  public int getFirstLogin(User user){
    mSp = getSharedPreferences(user.getObjectId()+"first",MODE_PRIVATE);
    return mSp.getInt(user.getObjectId()+"first",0);
  }

}
