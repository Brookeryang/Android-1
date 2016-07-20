package com.example.kirito.alarm;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.LocationManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.amap.api.navi.AMapNavi;
import com.amap.api.navi.AMapNaviView;
import com.amap.api.navi.model.NaviLatLng;
import com.example.kirito.bean.Point;
import com.example.kirito.util.ToastUtil;

import java.util.ArrayList;

/**
 * 实时导航
 */

public class GPSNaviActivity extends BaseActivity {

    private ArrayList<Point> list = new ArrayList<Point>();
    private NaviLatLng mEndLatlng;
    private NaviLatLng mStartLatlng;
    private Button music;
    private RelativeLayout bt;
    private MediaPlayer mp;
    private boolean isOpen=false; //标记背景音乐是否打开
    private static int i=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_basic_navi);
        if(!getIntent().getParcelableArrayListExtra("list").isEmpty()) {
            list = getIntent().getParcelableArrayListExtra("list");
        }else{
            ToastUtil.show(getApplicationContext(), "缺少目的信息");
        }
        mAMapNaviView = (AMapNaviView) findViewById(R.id.navi_view);
        mAMapNaviView.onCreate(savedInstanceState);
        mAMapNaviView.setAMapNaviViewListener(this);

        bt= (RelativeLayout) findViewById(R.id.navi_alarm);
        music = (Button) findViewById(R.id.music);
        music.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if (isOpen) {
                        bt.setVisibility(View.GONE);
                        mp.stop();
                        if (i < list.size() - 1) {
                            onInitNaviSuccess();
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

                if (isOpen) {
                    mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                        @Override
                        public void onCompletion(MediaPlayer mp) {
                            mp.release();
                        }
                    });
                }

            }
        });
    }

    /**
     * 初始化导航
     */
    @Override
    public void onInitNaviSuccess() {
        if(!isOpenGps()){
            dialog();
        }
        if(isOpenGps()) {
            isOpen = false;
            mStartLatlng = new NaviLatLng(list.get(i).getX(), list.get(i).getY());
            mEndLatlng = new NaviLatLng(list.get(i + 1).getX(), list.get(i + 1).getY());
            i++;
            mAMapNavi.calculateWalkRoute(mStartLatlng, mEndLatlng);
        }
    }

    /**
     * 提醒用户开启GPS定位
     */
    protected void dialog() {
         AlertDialog.Builder builder = new AlertDialog.Builder(this);
         builder.setMessage("是否开启GPS?");
         builder.setTitle("提示");
         builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
            @Override
              public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent();
                intent.setAction(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                try {
                    getApplicationContext().startActivity(intent);

                } catch (ActivityNotFoundException ex) {

                    // The Android SDK doc says that the location settings activity
                    // may not be found. In that case show the general settings.

                    // General settings activity
                    intent.setAction(Settings.ACTION_SETTINGS);
                    try {
                        getApplicationContext().startActivity(intent);
                    } catch (Exception e) {

                    }
                }

            }
             });
         builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
              @Override
              public void onClick(DialogInterface dialog, int which) {
                   ToastUtil.show(getApplicationContext(),"请开启GPS定位");
                  }
             });
        builder.create().show();
        }

    /**
     * 开始GPS导航模式
     */
    @Override
    public void onCalculateRouteSuccess() {
        mAMapNavi.startNavi(AMapNavi.GPSNaviMode);
    }

    /**
     * 当到达目的地时
     */
    @Override
    public void onArriveDestination() {
        mTtsManager.stopSpeaking();
        ToastUtil.show(getApplicationContext(), "已经到达目的地" + list.get(i).getAddress());
        if(!isOpen) {
            bt.setVisibility(View.VISIBLE);
            mp = MediaPlayer.create(getApplication(), R.raw.mic);
            mp.start();
            isOpen=true;
        }
    }

    /**
     * 判断GPS定位是否开启
     * @return
     */
    public boolean isOpenGps(){
        LocationManager locationManager = (LocationManager)getApplicationContext().
                getSystemService(Context.LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
    }
}
