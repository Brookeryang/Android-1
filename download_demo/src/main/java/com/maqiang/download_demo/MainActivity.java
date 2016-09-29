package com.maqiang.download_demo;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.maqiang.download_demo.entities.FileInfo;
import com.maqiang.download_demo.services.DownloadService;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private ProgressBar mBar;
    private Button mStart,mPause;
    private TextView mFileName;
    private TextView mSpeed;
    private FileInfo fileInfo;
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        initEvent();
        IntentFilter filter = new IntentFilter();
        filter.addAction(DownloadService.ACTION_UPDATE);
        registerReceiver(mReceiver,filter);
    }

    private void initEvent() {
        mStart.setOnClickListener(this);
        mPause.setOnClickListener(this);
    }

    private void initView() {
        mBar = (ProgressBar) findViewById(R.id.progressBar);
        mBar.setMax(100);
        mStart = (Button) findViewById(R.id.start);
        mPause = (Button) findViewById(R.id.pause);
        mFileName = (TextView) findViewById(R.id.tvFileName);
        mSpeed = (TextView) findViewById(R.id.downSpeed);
        fileInfo = new FileInfo(0,"http://gdown.baidu.com/data/wisegame/36941122966e3fc1/kuwoyinle_8250.apk",
                    "kuwoyinle.exe",0,0);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.start:
                intent = new Intent(MainActivity.this, DownloadService.class);
                intent.setAction(DownloadService.ACTION_START);
                intent.putExtra("fileInfo",fileInfo);
                startService(intent);
                mFileName.setText(fileInfo.getFileName());
                break;
            case R.id.pause:
                intent = new Intent(MainActivity.this, DownloadService.class);
                intent.setAction(DownloadService.ACTION_STOP);
                intent.putExtra("fileInfo",fileInfo);
                startService(intent);
                break;
        }
    }

    /**
     * 更新UI的广播接收器
     */
    BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if(DownloadService.ACTION_UPDATE.equals(intent.getAction())){
                int index = intent.getIntExtra("finished",0);
                int speed = intent.getIntExtra("speed",0);
                Log.d("test", "onReceive: "+index+"speed"+speed);
                mBar.setProgress(index);
                String format =getResources().getString(R.string.download_speed);
                mSpeed.setText(String.format(format,speed));
                mSpeed.setVisibility(View.VISIBLE);
            }
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mReceiver);
        stopService(intent);
    }
}
