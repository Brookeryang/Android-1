package com.maqiang.socket;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.util.Log;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Random;

/**
 * Created by maqiang on 16/8/14.
 */
public class TcpServerService extends Service {

    private static final String TAG = "TcpSerevr";
    private boolean mIsServiceDead = false;
    private String[] mInitWords = new String[]{"你好啊,哈哈","请问你叫什么名字","你喜欢什么食物","今天天气不错a( ⊙ o ⊙ )啊！"
    ,"你知道吗?我可是同时在和多个人聊天","给你讲个笑话,据说爱笑的人运气不会太差,运气差的人怎么会笑得出来!",
            "给你讲个笑话,朝鲜：大哥，我要做核试验了。　中国：好的，什么时候？　朝：10. 　中：10？10什么？10天还是10小时？　朝：9，8,7，6。。。　中：你大爷的",
    "给你讲个笑话,中午，看到一女孩牵着条大狗在路边等车来接，那女孩冲狗喊了声：“坐下！”\n" +
            "狗一屁股就坐地上，然后被烫得“汪”的一声跳了起来！","给你讲个笑话,早上捡到个钱包，里面有几百块钱和证件，最重要的是有一张纸，上面写着：本人经常掉东西，钱包里的钱给你了，把证件还给我就行了。手机：XXXXX。。。\n" +
            "然后我就打过去了，只听对方传来一句话：这手机我也是刚捡到的"};

    @Override
    public void onCreate() {
        new Thread(new TcpServer()).start();
        super.onCreate();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        mIsServiceDead = true;
        super.onDestroy();
    }
    private class TcpServer implements Runnable{
        ServerSocket serverSocket = null;
        @Override
        public void run() {
            try {
                //监听本地接口8888
                 serverSocket = new ServerSocket(8888);
                Log.d(TAG, "服务器端口8888开启成功。");
            } catch (IOException e) {
                System.err.println("服务器端口8888开启失败。");
                e.printStackTrace();
                return;
            }
            while (!mIsServiceDead){
                try {
                    final Socket socket = serverSocket.accept();
                    Log.d(TAG, "客户端:"+socket.getLocalAddress()+"连接服务器成功。");
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                responseClient(socket);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }).start();

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    private void responseClient(Socket socket) throws IOException {
        //用于接收客户端消息
        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        //用于向客户端发送消息
        PrintWriter out = new PrintWriter(new BufferedWriter(new OutputStreamWriter
                (socket.getOutputStream())), true);
        out.println("欢迎来到聊天室");
        while (!mIsServiceDead){
            String str = in.readLine();
            Log.d(TAG, "Client:"+str);
            if(str==null){
                //断开连接
                break;
            }
            int i = new Random().nextInt(mInitWords.length);
            String msg = mInitWords[i];
            out.println(msg);
            out.flush();
            Log.d(TAG, "Server:"+msg);
        }
        Log.d(TAG, "Client quit");
        //关闭各种流
        in.close();
        out.close();
        socket.close();
    }
}
