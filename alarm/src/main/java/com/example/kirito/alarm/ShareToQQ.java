package com.example.kirito.alarm;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.kirito.bean.Point;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;

import org.json.JSONObject;

import java.util.ArrayList;

public class ShareToQQ extends Activity {

	private Tencent mTencent;
	private static final String APP_ID = "222222";
	private ArrayList<Point> list = new ArrayList<Point>();
	String str="";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.share_main);
		list=getIntent().getParcelableArrayListExtra("list");
		for (Point p :list){
			str+=p.getAddress()+"\n";
		}
		final Context ctxContext = this.getApplicationContext();
		mTencent = Tencent.createInstance(APP_ID, ctxContext);
		mHandler = new Handler();
		TextView textView = (TextView) findViewById(R.id.address);
		textView.setText(textView.getText()+str);
	}

	@Override
	protected void onResume() {
		super.onResume();
		Button btn = (Button) findViewById(R.id.button1);
		btn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				onClickShareToQQ();
			}
		});
		LinearLayout back = (LinearLayout) findViewById(R.id.back);
 		back.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
	}


	
	private void onClickShareToQQ() {
		Bundle b = getShareBundle();
		if(b != null){
			shareParams = b;
			Thread thread = new Thread(shareThread);
			thread.start();
		}
	}

	private Bundle getShareBundle(){
		 Bundle bundle = new Bundle();
         bundle.putString("title", "我想去");
         bundle.putString("imageUrl", "http://img0.imgtn.bdimg.com/it/u=1198970414,2971052980&fm=21&gp=0.jpg");
         bundle.putString("targetUrl", "http://xuptsec.cn/");
         bundle.putString("summary",str);
         bundle.putString("site",  "2222");
         bundle.putString("appName", "来自位置闹铃");
         return bundle;
	}
	Bundle shareParams = null;

	Handler shareHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
		}
	};

	// 线程类，该类使用匿名内部类的方式进行声明
	Runnable shareThread = new Runnable() {

		public void run() {
			doShareToQQ(shareParams);
			Message msg = shareHandler.obtainMessage();

			// 将Message对象加入到消息队列当中
			shareHandler.sendMessage(msg);

		}
	};

	private void doShareToQQ(Bundle params) {
		mTencent.shareToQQ(ShareToQQ.this, params, new BaseUiListener() {
			protected void doComplete(JSONObject values) {
				showResult("shareToQQ:", "onComplete");
			}

			@Override
			public void onError(UiError e) {
				showResult("shareToQQ:", "onError code:" + e.errorCode
						+ ", msg:" + e.errorMessage + ", detail:"
						+ e.errorDetail);
			}

			@Override
			public void onCancel() {
				showResult("shareToQQ", "onCancel");
			}
		});
	}

	private class BaseUiListener implements IUiListener {

		@Override
		public void onComplete(JSONObject response) {
			// mBaseMessageText.setText("onComplete:");
			// mMessageText.setText(response.toString());
			doComplete(response);
		}

		protected void doComplete(JSONObject values) {

		}

		@Override
		public void onError(UiError e) {
			showResult("onError:", "code:" + e.errorCode + ", msg:"
					+ e.errorMessage + ", detail:" + e.errorDetail);
		}

		@Override
		public void onCancel() {
			showResult("onCancel", "");
		}
	}

	private Handler mHandler;

	private void showResult(final String base, final String msg) {
		mHandler.post(new Runnable() {

			@Override
			public void run() {
			}
		});
	}
}
