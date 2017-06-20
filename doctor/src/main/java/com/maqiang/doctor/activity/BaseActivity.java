package com.maqiang.doctor.activity;

import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

/**
 * Created by maqiang on 16/8/5.
 * 懒人类
 */
public class BaseActivity extends AppCompatActivity implements View.OnClickListener{

    /**
     * 常用的findViewById方法的二次封装
     * 在onCreat()方法中调用即可 因为使用泛型以后会自动转化为相对于的控件类型,所以一定要写对resId!!!!
     * @param resId
     * @param <T>
     * @return
     */
    protected  <T extends View>T getViewById(int resId){
        return (T) findViewById(resId);
    }

    /**
     * 用于一些动态添加的布局中的控件Id获取
     * @param view
     * @param resId
     * @param <T>
     * @return
     */
    protected  <T extends View>T getViewById(View view,int resId){
        return (T) view.findViewById(resId);
    }

    /**
     * 初始化所有的监听事件 可以一次添加多个控件
     * @param views
     */
    protected void initOnClickListener(View... views){
        for (View view:views) {
            view.setOnClickListener(this);
        }
    }

    /**
     * 初始化Toolbar的图标和主标题 添加了导航图标的点击事件
     * @param bar
     * @param naviIconResId
     * @param title
     */
    protected void initToolBar(Toolbar bar, int naviIconResId, String title, int titleColorResId){
        bar.setTitle(title);
        bar.setTitleTextColor(ContextCompat.getColor(this,titleColorResId));
        bar.setNavigationIcon(naviIconResId);
        setSupportActionBar(bar);
        bar.setNavigationOnClickListener(this);
    }
    protected void initToolBar(Toolbar bar, int naviIconResId, int resId, int titleColorResId){
        bar.setTitle(resId);
        bar.setTitleTextColor(ContextCompat.getColor(this,titleColorResId));
        bar.setNavigationIcon(naviIconResId);
        setSupportActionBar(bar);
        bar.setNavigationOnClickListener(this);
    }

    protected void initToolBar(Toolbar bar, int naviIconResId){
        bar.setTitle("");
        bar.setNavigationIcon(naviIconResId);
        setSupportActionBar(bar);
        bar.setNavigationOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        // TODO: 16/8/5 在相应的Aty重写此方法 
    }
}
