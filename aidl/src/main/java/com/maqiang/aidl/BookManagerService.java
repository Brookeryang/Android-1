package com.maqiang.aidl;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.os.RemoteCallbackList;
import android.os.RemoteException;
import android.os.SystemClock;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.util.Log;

import java.util.List;
import java.util.Stack;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Created by maqiang on 16/8/14.
 */
public class BookManagerService extends Service {
    private static final String TAG = "BMS";

    //用来标记服务是否被销毁
    private AtomicBoolean mIsServiceDestory = new AtomicBoolean(false);
    //书籍列表
    private CopyOnWriteArrayList<Book> mBookList = new CopyOnWriteArrayList<>();
    private RemoteCallbackList<IOnNewBookArrivedListener> mListener = new
            RemoteCallbackList<IOnNewBookArrivedListener>();


    private Binder mBinder = new IBookManager.Stub() {
        @Override
        public List<Book> getBookList() throws RemoteException {
            return mBookList;
        }

        @Override
        public void addBook(Book book) throws RemoteException {
            Log.d(TAG, "addBook: "+book.toString());
            mBookList.add(book);
        }

        @Override
        public void registerListener(IOnNewBookArrivedListener listener) throws RemoteException {
            mListener.register(listener);
            Log.d(TAG, "registerListener is succeed"+mListener.getRegisteredCallbackCount());
        }

        @Override
        public void unregisterListener(IOnNewBookArrivedListener listener) throws RemoteException {
            mListener.unregister(listener);
            Log.d(TAG, "unregisterListener is succeed"+mListener.getRegisteredCallbackCount());
        }
    };



    @Override
    public void onCreate() {
        super.onCreate();
        mBookList.add(new Book(1,"Android"));
        mBookList.add(new Book(2,"JAVA"));
        new Thread(new ServiceWorker()).start();
    }
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        Log.d(TAG, "onBind: "+"连接远程服务器成功.");
        return mBinder;
    }

    @Override
    public void onDestroy() {
        mIsServiceDestory.set(true);
        super.onDestroy();
    }

    public void onNewBookArrived(Book book) throws RemoteException {
        mBookList.add(book);
        final int N = mListener.beginBroadcast();
        Log.d(TAG, "新增书籍: "+book.toString());
        Log.d(TAG, "现有书籍: "+mBookList.toString());
        for (int i = 0; i < N; i++) {
            IOnNewBookArrivedListener listener = mListener.getBroadcastItem(i);
            Log.d(TAG, "onNewBookArrived: "+listener);
            if(listener!=null) {
                listener.onNewBookArrived(book);
            }
        }
        mListener.finishBroadcast();
    }

    private class ServiceWorker implements Runnable {
        @Override
        public void run() {
            while(!mIsServiceDestory.get()){
                try {
                    Thread.sleep(5000);
                    int bookId = mBookList.size()+1;
                    Book book = new Book(bookId,"new Book#"+bookId);
                    onNewBookArrived(book);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
