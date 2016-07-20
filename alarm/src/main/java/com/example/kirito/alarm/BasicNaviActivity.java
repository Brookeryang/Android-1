package com.example.kirito.alarm;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.amap.api.navi.AMapNaviView;
import com.amap.api.navi.AMapNaviViewOptions;
import com.amap.api.navi.model.NaviLatLng;
import com.example.kirito.bean.Point;
import com.example.kirito.util.ToastUtil;

import java.util.ArrayList;

/**
 *
 * 最普通的导航页面，如果你想处理一些诸如菜单点击，停止导航按钮点击的事件处理
 * 请implement AMapNaviViewListener
 * 基本导航页
 *
 */

public class BasicNaviActivity extends BaseActivity {

    private NaviLatLng mEndLatlng;
    private NaviLatLng mStartLatlng;
    private ArrayList<Point> list = new ArrayList<Point>();
    private Button music;
    private RelativeLayout bt;
    private MediaPlayer mp;
    private boolean isOpen; //标记背景音乐是否打开
    private int i=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_basic_navi);
        mAMapNaviView = (AMapNaviView) findViewById(R.id.navi_view);
        mAMapNaviView.onCreate(savedInstanceState);
        mAMapNaviView.setAMapNaviViewListener(this);
        if(!getIntent().getParcelableArrayListExtra("list").isEmpty()) {
            list = getIntent().getParcelableArrayListExtra("list");
        }else{
            ToastUtil.show(getApplicationContext(),"缺少目的信息");
        }

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


        AMapNaviViewOptions viewOptions = mAMapNaviView.getViewOptions();
        viewOptions.setSettingMenuEnabled(true);//设置菜单按钮是否在导航界面显示
        viewOptions.setNaviNight(true);//设置导航界面是否显示黑夜模式
        viewOptions.setReCalculateRouteForYaw(true);//设置偏航时是否重新计算路径
        viewOptions.setReCalculateRouteForTrafficJam(true);//前方拥堵时是否重新计算路径
        viewOptions.setTrafficInfoUpdateEnabled(true);//设置交通播报是否打开
        viewOptions.setCameraInfoUpdateEnabled(true);//设置摄像头播报是否打开
        viewOptions.setScreenAlwaysBright(true);//设置导航状态下屏幕是否一直开启。
        viewOptions.setNaviViewTopic(0);//设置导航界面的主题
        mAMapNaviView.setViewOptions(viewOptions);

    }

    @Override
    public void onInitNaviSuccess() {
            isOpen=false;
            mStartLatlng = new NaviLatLng(list.get(i).getX(), list.get(i).getY());
            mEndLatlng = new NaviLatLng(list.get(i+1).getX(), list.get(i+1).getY());
            i++;
            mAMapNavi.calculateWalkRoute(mStartLatlng, mEndLatlng);
    }

    @Override
    public void onArriveDestination() {
        mTtsManager.stopSpeaking();
        ToastUtil.show(getApplicationContext(), "已经到达目的地" + list.get(i).getAddress() + "请停止闹钟进入下一段路程");
        if(!isOpen) {
            bt.setVisibility(View.VISIBLE);
            mp = MediaPlayer.create(getApplication(), R.raw.mic);
            mp.start();
            isOpen=true;
        }
    }

    @Override
    public void onEndEmulatorNavi() {
        onArriveDestination();
    }

    /**
     * 左下角退出导航
     * @return
     */
    @Override
    public boolean onNaviBackClick() {
        return super.onNaviBackClick();
    }

    /**
     * 返回键取消到导航
     */
    @Override
    public void onNaviCancel() {
        super.onNaviCancel();
    }
}
