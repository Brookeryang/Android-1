package com.maqiang.aidl;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "ATY";
    private static final int MESSAGE_NEW_BOOK_ARRIVED = 1;

    private IBookManager mBinder;

    private ServiceConnection conn = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            mBinder = IBookManager.Stub.asInterface(iBinder);
            try {
                mBinder.registerListener(listener);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        @Override
        public void onServiceDisconnected(ComponentName componentName) {

        }
    };

    private IOnNewBookArrivedListener listener = new IOnNewBookArrivedListener.Stub() {
        @Override
        public void onNewBookArrived(Book newBook) throws RemoteException {
            handler.obtainMessage(MESSAGE_NEW_BOOK_ARRIVED,newBook).sendToTarget();
        }
    };

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case MESSAGE_NEW_BOOK_ARRIVED:
                    Log.d(TAG, "新书:"+msg.obj);
                    break;
                default:
                    super.handleMessage(msg);
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        onBindService();
    }

    public void onBindService(){
        Intent intent = new Intent(MainActivity.this,BookManagerService.class);
        bindService(intent,conn, Context.BIND_AUTO_CREATE);
    }

    public void onclick(View view){
        switch (view.getId()){
            case R.id.search:
                //查询放在子线程中 防止ANR
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            List<Book> books = mBinder.getBookList();
                            Log.d(TAG, "查询书籍列表,书存在:"+books.getClass().getCanonicalName());
                            Log.d(TAG, "书籍有:"+books.toString());
                        } catch (RemoteException e) {
                            e.printStackTrace();
                        }
                    }
                }).start();

                break;
            case R.id.add:
                try {
                    mBinder.addBook(new Book(3,"Android开发艺术探索"));
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
                break;
        }
    }

    @Override
    protected void onDestroy() {
        if(mBinder!=null&&mBinder.asBinder().isBinderAlive()){
            try {
                mBinder.unregisterListener(listener);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
            unbindService(conn);
            super.onDestroy();
        }
    }
}
