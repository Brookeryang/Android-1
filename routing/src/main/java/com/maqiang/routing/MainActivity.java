package com.maqiang.routing;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.utils.CoordinateConverter;

import java.math.BigDecimal;

public class MainActivity extends AppCompatActivity {

    private LocationUtils mStart,mEnd;

    //百度地图URL跳转到步行路线的参数

    //头部 添加相应地区
    private final static String BAIDU_HEAD = "baidumap://map/direction?region=0";
    //起点的经纬度
    private final static String BAIDU_ORIGIN = "&origin=";
    //终点的经纬度
    private final static String BAIDU_DESTINATION = "&destination=";
    //路线规划方式
    private final static String BAIDU_MODE = "&mode=walking";
    //百度地图的包名
    private final static String BAIDU_PKG = "com.baidu.BaiduMap";

    //高德地图URL跳转到步行路线的参数

    //头部 后面的sourceApplicaiton填自己APP的名字
    private final static String GAODE_HEAD = "androidamap://route?sourceApplication=BaiduNavi";
    //起点经度
    private final static String GAODE_SLON = "&slon=";
    //起点纬度
    private final static String GAODE_SLAT = "&slat=";
    //起点名字
    private final static String GAODE_SNAME = "&sname=";
    //终点经度
    private final static String GAODE_DLON = "&dlon=";
    //终点纬度
    private final static String GAODE_DLAT = "&dlat=";
    //终点名字
    private final static String GAODE_DNAME = "&dname=";
    // dev 起终点是否偏移(0:lat 和 lon 是已经加密后的,不需要国测加密; 1:需要国测加密)
    // t = 1(公交) =2（驾车） =4(步行)
    private final static String GAODE_MODE ="&dev=0&t=4";
    //高德地图包名
    private final static String GAODE_PKG = "com.autonavi.minimap";

    //腾讯地图URL跳转到路线的参数

    //头部 type出行方式
    private final static String TX_HEAD = "qqmap://map/routeplan?type=walk";
    //起点名称
    private final static String TX_FROM = "&from=";
    //起点的经纬度
    private final static String TX_FROMCOORD = "&fromcoord=";
    //终点名称
    private final static String TX_TO = "&to=";
    //终点的经纬度
    private final static String TX_TOCOORD = "&tocoord=";
    /**
         本参数取决于type参数的取值
         公交：type=bus，policy有以下取值
         0：较快捷
         1：少换乘
         2：少步行
         3：不坐地铁
         驾车：type=drive，policy有以下取值
         0：较快捷
         1：无高速
         2：距离
         policy的取值缺省为0
     */
    private final static String TX_END = "&policy=1&referer=myapp";
    //腾讯地图包名
    private final static String TX_PKG = "com.tencent.map";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SDKInitializer.initialize(getApplicationContext());
        setContentView(R.layout.activity_main);
        mStart = new LocationUtils(116.312268,40.046293,"我的位置");
        mEnd = new LocationUtils(116.397491, 39.908749, "北京天安门");
    }

    public void click(View v){
        Intent intent = new Intent();
        switch (v.getId()){
            case R.id.baidu:
                if (checkMapAppsIsExist(MainActivity.this,BAIDU_PKG)){
                    //Toast.makeText(MainActivity.this,"百度地图已经安装",Toast.LENGTH_SHORT).show();
                    intent.setData(Uri.parse(BAIDU_HEAD+BAIDU_ORIGIN+mStart.getLatitude()
                            +","+mStart.getLongitude()+BAIDU_DESTINATION+mEnd.getLatitude()+","+mEnd.getLongitude()
                            +BAIDU_MODE));
                    startActivity(intent);
                }else {
                   Toast.makeText(MainActivity.this,"百度地图未安装",Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.gaode:
                if (checkMapAppsIsExist(MainActivity.this,GAODE_PKG)){
                    //Toast.makeText(MainActivity.this,"高德地图已经安装",Toast.LENGTH_SHORT).show();
                    BD09ToGCJ02(mStart,mEnd);
                    intent.setAction("android.intent.action.VIEW");
                    intent.setPackage("com.autonavi.minimap");
                    intent.addCategory("android.intent.category.DEFAULT");
                    intent.setData(Uri.parse(GAODE_HEAD+GAODE_SLAT+mStart.getLatitude()+GAODE_SLON+mStart.getLongitude()+
                            GAODE_SNAME+mStart.getName()+GAODE_DLAT+mEnd.getLatitude()+GAODE_DLON+mEnd.getLongitude()+
                            GAODE_DNAME+mEnd.getName()+GAODE_MODE));
                    startActivity(intent);
                }else {
                    Toast.makeText(MainActivity.this,"高德地图未安装",Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.tengxun:
                if (checkMapAppsIsExist(MainActivity.this,TX_PKG)){
                   // Toast.makeText(MainActivity.this,"腾讯地图已经安装",Toast.LENGTH_SHORT).show();
                    BD09ToGCJ02(mStart,mEnd);
                    intent.setData(Uri.parse(TX_HEAD + TX_FROM+mStart.getName()+TX_FROMCOORD+mStart.getLatitude()+
                            ","+mStart.getLongitude()+TX_TO+mEnd.getName()+TX_TOCOORD+mEnd.getLatitude()+
                            ","+mEnd.getLongitude()+TX_END));
                    startActivity(intent);
                }else {
                    Toast.makeText(MainActivity.this,"腾讯地图未安装",Toast.LENGTH_SHORT).show();
                }
                break;
        }

    }

    /**
     * 检测网络状况
     * @param context
     * @param packagename
     * @return
     */
    public boolean checkMapAppsIsExist(Context context,String packagename){
        PackageInfo packageInfo;
        try{
            packageInfo = context.getPackageManager().getPackageInfo(packagename,0);
        }catch (Exception e){
            packageInfo = null;
            e.printStackTrace();
        }
        if (packageInfo == null){
            return false;
        }else{
            return true;
        }
    }
    /**
     * 坐标转换
     * @param mStart
     * @param mEnd
     */
    public void BD09ToGCJ02(LocationUtils mStart,LocationUtils mEnd){
        LatLng newStart = convertBaiduToGPS(new LatLng(mStart.getLatitude(),mStart.getLongitude()));
        LatLng newEnd = convertBaiduToGPS(new LatLng(mEnd.getLatitude(),mEnd.getLongitude()));
        mStart.setLatitude(newStart.latitude);
        mStart.setLongitude(newStart.longitude);

        mEnd.setLatitude(newEnd.latitude);
        mEnd.setLongitude(newEnd.longitude);

    }

    /**
     * 将百度地图坐标转换成GPS坐标
     * @param sourceLatLng
     * @return
     */
    public static LatLng convertBaiduToGPS(LatLng sourceLatLng) {
        // 将GPS设备采集的原始GPS坐标转换成百度坐标
        CoordinateConverter converter = new CoordinateConverter();
        converter.from(CoordinateConverter.CoordType.GPS);
        // sourceLatLng待转换坐标
        converter.coord(sourceLatLng);
        LatLng desLatLng = converter.convert();
        double latitude = 2 * sourceLatLng.latitude - desLatLng.latitude;
        double longitude = 2 * sourceLatLng.longitude - desLatLng.longitude;
        BigDecimal bdLatitude = new BigDecimal(latitude);
        bdLatitude = bdLatitude.setScale(6, BigDecimal.ROUND_HALF_UP);
        BigDecimal bdLongitude = new BigDecimal(longitude);
        bdLongitude = bdLongitude.setScale(6, BigDecimal.ROUND_HALF_UP);
        return new LatLng(bdLatitude.doubleValue(), bdLongitude.doubleValue());
    }
}
