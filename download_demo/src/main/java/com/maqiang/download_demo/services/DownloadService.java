package com.maqiang.download_demo.services;

import android.app.Service;
import android.content.Intent;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;

import com.maqiang.download_demo.entities.FileInfo;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by maqiang on 16/9/12.
 */
public class DownloadService extends Service {

    public static final String ACTION_START = "ACTION_START";
    public static final String ACTION_STOP = "ACTION_STOP";
    public static final String ACTION_UPDATE = "ACTION_UPDATE";
    public static final String DOWNLOAD_PATH = Environment.getExternalStorageDirectory().getPath();
    public static final int MSG_INIT = 0;
    private DownloadTask mTask = null;
    private FileInfo fileInfo = null;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        //获得aty传过来的参数
        if(intent.getAction().equals(ACTION_START)){
            fileInfo  = (FileInfo) intent.getSerializableExtra("fileInfo");
            Log.d("test", "start "+fileInfo.toString());
            //启动初始化线程
            new InitThread(fileInfo).start();
        }else if(intent.getAction().equals(ACTION_STOP)) {
            fileInfo = (FileInfo) intent.getSerializableExtra("fileInfo");
            Log.d("test", "stop" + fileInfo.toString());
            if(mTask != null){
                mTask.isPause = true;
            }
        }
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case MSG_INIT:
                    FileInfo info = (FileInfo) msg.obj;
                    Log.d("test", "handleMessage:obj"+info.toString());
                    //启动下载任务
                    mTask = new DownloadTask(DownloadService.this,fileInfo);
                    mTask.download();
                    break;
            }
        }
    };


    /**
     * 初始化子线程
     */
    class InitThread extends Thread{
        private FileInfo mFileInfo = null;

        public InitThread(FileInfo mFileInfo) {
            this.mFileInfo = mFileInfo;
        }

        @Override
        public void run() {
            HttpURLConnection conn = null;
            RandomAccessFile raf = null;

            try {
                //连接网络文件
                URL url = new URL(mFileInfo.getUrl());
                conn = (HttpURLConnection) url.openConnection();
                conn.setConnectTimeout(3000);
                conn.setRequestMethod("GET");
                int length = -1;
                if(conn.getResponseCode() == HttpURLConnection.HTTP_OK){
                    length = conn.getContentLength();
                }else {
                    Log.d("test", "请求失败"+conn.getResponseCode());
                }

                //如果请求文件长度不对
                if(length<=0){
                    return;
                }
                File dir = new File(DOWNLOAD_PATH);
                //如果文件目录不存在 创 建此目录
                if (!dir.exists()){
                    dir.mkdir();
                }
                //在本地创建文件
                File file = new File(dir,mFileInfo.getFileName());
                raf = new RandomAccessFile(file,"rwd");
                //设置文件长度
                raf.setLength(length);
                mFileInfo.setLength(length);
                handler.obtainMessage(MSG_INIT,mFileInfo).sendToTarget();
            }catch (Exception e){
                Log.d("test", "error"+e.toString());
            }finally {
                if(conn!=null){
                    conn.disconnect();
                }
                if(raf!=null){
                    try {
                        raf.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
}
