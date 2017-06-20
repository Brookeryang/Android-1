package com.maqiang.doctor.utils;

import android.content.Context;
import android.widget.Toast;
import com.maqiang.doctor.MyApplication;

/**
 * Created by maqiang on 2017/3/30.
 * 显示工具类
 */

public class ToastUtils {

  public static void show(String msg){
    Toast.makeText(MyApplication.getInstance(),msg,Toast.LENGTH_SHORT).show();
  }

  public static void show(int resId){
    Toast.makeText(MyApplication.getInstance(),MyApplication.getInstance().getString(resId),
        Toast.LENGTH_SHORT).show();
  }
}
