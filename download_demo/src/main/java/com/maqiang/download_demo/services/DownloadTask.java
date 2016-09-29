package com.maqiang.download_demo.services;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.maqiang.download_demo.db.ThreadDAO;
import com.maqiang.download_demo.db.ThreadDAOImp;
import com.maqiang.download_demo.entities.FileInfo;
import com.maqiang.download_demo.entities.ThreadInfo;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

/**
 * 下载任务
 * Created by maqiang on 16/9/12.
 */
public class DownloadTask {

    private Context mContext = null;
    private FileInfo mInfo = null;
    private ThreadDAO mDao = null;
    private int mFinished = 0;
    public boolean isPause = false;

    public DownloadTask(Context mContext, FileInfo mInfo) {
        this.mContext = mContext;
        this.mInfo = mInfo;
        this.mDao = new ThreadDAOImp(mContext);
    }

    public void download(){
        //读取数据库的线程信息
        List<ThreadInfo> lists = mDao.getThreads(mInfo.getUrl());
        ThreadInfo info = null;
        if(lists.size() == 0){
            info = new ThreadInfo(0,mInfo.getUrl(),0,mInfo.getLength(),0);
        }else {
            info = lists.get(0);
        }
        //创建子线程开始下载
        new DownloadThread(info).start();
    }

    /**
     * 下载线程
     */
    class DownloadThread extends Thread{
        private ThreadInfo mThreadInfo = null;

        public DownloadThread(ThreadInfo mThreadInfo) {
            this.mThreadInfo = mThreadInfo;
        }

        @Override
        public void run() {
            //向数据库中插入一条线程信息
            if(!mDao.isExists(mThreadInfo.getUrl(),mThreadInfo.getId())){
                mDao.insertThread(mThreadInfo);
            }
            //找到线程的下载位置
            HttpURLConnection conn = null;
            RandomAccessFile raf = null;
            InputStream inputStream = null;
            try {
                URL url = new URL(mThreadInfo.getUrl());
                conn = (HttpURLConnection) url.openConnection();
                conn.setConnectTimeout(3000);
                conn.setRequestMethod("GET");
                //设置下载位置
                int start = mThreadInfo.getStart()+mThreadInfo.getFinished();
                int end = mThreadInfo.getEnd();
                conn.setRequestProperty("Range","bytes="+start+"-"+end);
                //设置文件写入位置
                File file = new File(DownloadService.DOWNLOAD_PATH,mInfo.getFileName());
                raf = new RandomAccessFile(file,"rwd");
                raf.seek(start);
                Intent intent = new Intent(DownloadService.ACTION_UPDATE);
                mFinished+=mThreadInfo.getFinished();
                //开始下载
                if (conn.getResponseCode() == HttpURLConnection.HTTP_PARTIAL){
                    //读取数据
                    inputStream = conn.getInputStream();
                    byte[] buffer = new byte[1024*4];
                    int len = -1;
                    int downloadSize = 0;
                    long time = System.currentTimeMillis();
                    while ((len = inputStream.read(buffer))!=-1){
                        //写入文件
                        raf.write(buffer,0,len);
                        //把下载进度发送广播给Activity
                        mFinished+=len;
                        downloadSize+=len;
                        int intervalTime = (int) (System.currentTimeMillis() - time);
                        if(intervalTime >= 1000) {
                            time = System.currentTimeMillis();
                            float progress = Float.valueOf(mFinished+"")/Float.valueOf(mInfo.getLength()+"");
                            int progressIndex = (int) (progress*100);
                            intent.putExtra("finished",progressIndex);
                            intent.putExtra("speed",downloadSize*1000/intervalTime/1024);
                            Log.d("test", "run: "+mFinished+"/"+mInfo.getLength());
                            mContext.sendBroadcast(intent);
                            downloadSize = 0;
                       }
                        //有可能存在下载完毕,进度只有99%
                        if(mFinished==mInfo.getLength()){
                            intent.putExtra("finished",100);
                            mContext.sendBroadcast(intent);
                        }
                        //下载暂停时,要把进度保存到数据库
                        if(isPause){
                            mDao.updateTHread(mThreadInfo.getUrl(),mThreadInfo.getId(),mFinished);
                            return;
                        }

                    }
                    //下载完成删除线程信息
                    mDao.deleteThread(mThreadInfo.getUrl(),mThreadInfo.getId());
                }

            } catch (Exception e) {
                Log.d("test", "DownloadTask_error: "+e.toString());
            }finally {
                try {
                    if(conn!=null)
                    conn.disconnect();
                    if(raf!=null)
                    raf.close();
                    if(inputStream!=null)
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
