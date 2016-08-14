package com.maqiang.socket;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TcpClientActivity extends AppCompatActivity {

    private EditText mEdit;
    private TextView mText;
    private ImageView mSend;

    private final static int MSG_RECEIVE_NEW_MSG = 1;
    private final static int MSG_SOCKET_CONNECTED = 2;

    private PrintWriter mPrintWriter;
    private Socket mClient;
    private boolean isConnected = false;

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case MSG_RECEIVE_NEW_MSG:
                    mText.setText(mText.getText()+(String)msg.obj);
                    break;
                case MSG_SOCKET_CONNECTED:
                    isConnected = true;
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
        initView();
        startService(new Intent(this,TcpServerService.class));
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    connectTcpServer();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    @Override
    protected void onDestroy() {
        if(mClient!=null){
            try {
                mClient.shutdownInput();
                mClient.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        super.onDestroy();
    }

    private void initView() {
        mEdit = (EditText) findViewById(R.id.edit);
        mSend = (ImageView) findViewById(R.id.send);
        mText = (TextView) findViewById(R.id.msg);

        mSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String msg = mEdit.getText().toString();
                if(!TextUtils.isEmpty(msg)&&mPrintWriter!=null&&isConnected){
                    mPrintWriter.println(msg);
                    mEdit.setText("");
                    String time = new SimpleDateFormat("(HH:mm:ss)").
                            format(new Date(System.currentTimeMillis()));
                    String showMsg = "Client"+time+":"+msg+"\n";
                    mText.setText(mText.getText()+showMsg);
                }
            }
        });
    }
    private void connectTcpServer() throws IOException {
        Socket socket = null;
        while (socket == null){
            try {
                socket = new Socket("localhost",8888);
                mClient = socket;
                mPrintWriter = new PrintWriter(new BufferedWriter(
                        new OutputStreamWriter(mClient.getOutputStream())),true);
                handler.sendEmptyMessage(MSG_SOCKET_CONNECTED);
                System.out.println("连接服务器成功");
            } catch (IOException e) {
                SystemClock.sleep(1000);
                System.out.println("连接服务器失败,正在尝试重新连接..");
                e.printStackTrace();
            }
        }
        BufferedReader br = null;
        try {
             br = new BufferedReader(new InputStreamReader(mClient.getInputStream()));
            while (!this.isFinishing()&&isConnected){
                String msg = br.readLine();
                if(msg!=null){
                    System.out.println("Server:"+msg);
                    String time = new SimpleDateFormat("(HH:mm:ss)").
                            format(new Date(System.currentTimeMillis()));

                    String showMsg = "Server"+time+":"+msg+"\n";
                    handler.obtainMessage(MSG_RECEIVE_NEW_MSG,showMsg).sendToTarget();
                }
            }
            System.out.println("Quiting....");
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            mPrintWriter.close();
            br.close();
            socket.close();
        }

    }
}
