package com.example.kirito.alarm;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.AMap;
import com.amap.api.maps.LocationSource;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.MyLocationStyle;
import com.example.kirito.bean.Point;
import com.example.kirito.util.Distance;
import com.example.kirito.util.ToastUtil;

import java.util.ArrayList;

public class HomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener
        ,LocationSource, AMapLocationListener{

    private MapView mapView;
    private AMap aMap;
    private Button music;
    private RelativeLayout bt;
    private MediaPlayer mp;
    private Point point = new Point();
    private ArrayList<Point> list = new ArrayList<Point>();
    private ArrayList<Point> listParcelable = new ArrayList<Point>();
    private LocationSource.OnLocationChangedListener mListener;
    private AMapLocationClient mlocationClient;
    private AMapLocationClientOption mLocationOption;
    private MyLocationStyle myLocationStyle;
    private static final int POISEARCH = 1;
    private boolean flag = false;  //表示后台定位是否打开
    private boolean isOpen=false; //标记背景音乐是否打开
    private SharedPreferences pref;
    private static int i=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mapView = (MapView) findViewById(R.id.map);
        mapView.onCreate(savedInstanceState);

        pref = getSharedPreferences("Home",MODE_PRIVATE);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        bt = (RelativeLayout) findViewById(R.id.home_bt);
        music = (Button) findViewById(R.id.music);
        music.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if(isOpen) {
                        bt.setVisibility(View.GONE);
                        isOpen=false;
                        mp.stop();
                        i++;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

                if(isOpen) {
                    mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                        @Override
                        public void onCompletion(MediaPlayer mp) {
                            mp.release();
                        }
                    });
                }

            }
        });
        init();
    }

    /**
     * 初始化AMap对象
     */
    private void init() {
        if (aMap == null) {
            aMap = mapView.getMap();
            setUpMap();
        }


    }
    /**
     * 设置一些amap的属性
     */
    private void setUpMap() {
        //定位样式
        myLocationStyle = new MyLocationStyle();
        myLocationStyle.strokeColor(Color.argb(0, 0, 0, 0));// 设置圆形的边框颜色
        myLocationStyle.radiusFillColor(Color.argb(0, 0, 0, 0));// 设置圆形的填充颜色
        myLocationStyle.myLocationIcon(BitmapDescriptorFactory //定位图标
                .fromResource(R.drawable.location_marker));
        aMap.setMyLocationStyle(myLocationStyle);
        aMap.setLocationSource(this);// 设置定位监听
        aMap.getUiSettings().setMyLocationButtonEnabled(true);// 设置默认定位按钮是否显示
        aMap.setMyLocationEnabled(true);// 设置为true表示显示定位层并可触发定位，false表示隐藏定位层并不可触发定位，默认是false
        // 设置定位的类型为定位模式 ，可以由定位、跟随或地图根据面向方向旋转几种
        aMap.setMyLocationType(AMap.LOCATION_TYPE_LOCATE);
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
        deactivate();
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
        if(null != mlocationClient){
            mlocationClient.onDestroy();
        }
        if(mp != null)
            mp.release();
    }

    /**
     * 返回键的监听事件
     */
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if(id==R.id.search){
            startActivityForResult(new Intent(this, PoiSearchActivity.class), POISEARCH);
        }

        if(id==R.id.share){
            Intent intent = new Intent(this,ShareToQQ.class);
            intent.putParcelableArrayListExtra("list",list);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * 侧栏点击事件的处理
     * @param item
     * @return
     */
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
            startActivityForResult(new Intent(this, PoiSearchActivity.class), POISEARCH);
        } else if (id == R.id.route) {
            listParcelable=(ArrayList<Point>)list.clone();
            listParcelable.add(0,point);
            Intent intent = new Intent(this,BasicNaviActivity.class);
            intent.putParcelableArrayListExtra("list",listParcelable);
            startActivity(intent);
        } else if (id == R.id.route_reality){
            listParcelable=(ArrayList<Point>)list.clone();
            listParcelable.add(0,point);
            Intent intent = new Intent(this,GPSNaviActivity.class);
            intent.putParcelableArrayListExtra("list",listParcelable);
            startActivity(intent);
        } else if(id == R.id.route_back){
            if(!flag)
            {
                flag=true;
                ToastUtil.show(getApplication(), "已开启后台定位！请点击右上角图标刷新自己的位置！" );
            }else {
                flag=false;
                ToastUtil.show(getApplication(), "已关闭后台定位！" );
            }
            SharedPreferences.Editor editor = pref.edit();
            editor.putBoolean("add",flag);
            editor.apply();

        }else if(id == R.id.nav_send){
            startActivity(new Intent(this,About.class));
        }else if(id == R.id.share){
            Intent intent = new Intent(this,ShareToQQ.class);
            intent.putParcelableArrayListExtra("list",list);
            startActivity(intent);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    /**
     * 当距离目的地50m之内时提醒用户已经到达
     */
    private void onArriveDestination(){
        if(new Distance().gps2m(point.getX(),point.getY(),list.get(i).getX(),list.get(i).getY())<50){
            //已经到达目的地
            ToastUtil.show(getApplication(), "已到达目的地附近,请停止闹钟进入下一段路程");
            if(!isOpen) {
                mp = MediaPlayer.create(getApplication(), R.raw.mic);
                mp.start();
                isOpen=true;
                bt.setVisibility(View.VISIBLE);
            }
        }
    }

    /**
     * POI搜索结果返回值
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==RESULT_OK){
            list = data.getParcelableArrayListExtra("list");
            String str="";
            for (Point p :list){
                str+=p.getAddress()+"\n";
            }
            ToastUtil.show(getApplication(), "已添加目的地 : " + str);
        }
    }

    /**
     * 当位置改变时
     * @param amapLocation
     */
    @Override
    public void onLocationChanged(AMapLocation amapLocation) {
        if (mListener != null && amapLocation != null) {
            if (amapLocation != null
                    && amapLocation.getErrorCode() == 0) {
                mListener.onLocationChanged(amapLocation);// 显示系统小蓝点
                point.setX(amapLocation.getLatitude());
                point.setY(amapLocation.getLongitude());
                point.setAddress(amapLocation.getAddress());
                if(flag){
                    onArriveDestination();

                }
                Log.i("Location",amapLocation.getAddress()+amapLocation.getLatitude()+amapLocation.getLongitude());
            } else {
                Log.i("LocationError", amapLocation.getErrorCode() + amapLocation.getErrorInfo());

            }
        }
    }

    /**
     * 定位参数配置
     * @param onLocationChangedListener
     */
    @Override
    public void activate(OnLocationChangedListener onLocationChangedListener) {
        mListener = onLocationChangedListener;
        if (mlocationClient == null) {
            mlocationClient = new AMapLocationClient(this);
            //开始GPS
            mLocationOption = new AMapLocationClientOption();
            //设置定位监听
            mlocationClient.setLocationListener(this);
            mLocationOption.setGpsFirst(true);
            //五秒刷新一次
            mLocationOption.setInterval(5000);
            //设置为高精度定位模式
            mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
            //设置定位参数
            mlocationClient.setLocationOption(mLocationOption);
            // 此方法为每隔固定时间会发起一次定位请求，为了减少电量消耗或网络流量消耗，
            // 注意设置合适的定位时间的间隔（最小间隔支持为2000ms），并且在合适时间调用stopLocation()方法来取消定位请求
            // 在定位结束后，在合适的生命周期调用onDestroy()方法
            // 在单次定位情况下，定位无论成功与否，都无需调用stopLocation()方法移除请求，定位sdk内部会移除
            mlocationClient.startLocation();
        }
    }

    /**
     * 解除定位
     */
    @Override
    public void deactivate() {
        mListener = null;
        if (mlocationClient != null) {
            mlocationClient.stopLocation();
            mlocationClient.onDestroy();
        }
        mlocationClient = null;
    }
}
