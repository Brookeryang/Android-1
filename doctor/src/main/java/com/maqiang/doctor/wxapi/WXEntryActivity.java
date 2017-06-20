package com.maqiang.doctor.wxapi;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import com.maqiang.doctor.R;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

/**
 * Created by maqiang on 2017/5/31.
 *
 * Function:
 */

public class WXEntryActivity extends Activity implements IWXAPIEventHandler {
  // IWXAPI 是第三方app和微信通信的openapi接口

  public static final String TAG = "WXEntryActivity";
  private static final String WXAPP_ID = "wx988019db68e5fbd3";
  public static IWXAPI api;

  @Override protected void onCreate(Bundle savedInstanceState) {
    api = WXAPIFactory.createWXAPI(this,WXAPP_ID,true);
    api.handleIntent(getIntent(), this);
    super.onCreate(savedInstanceState);
  }

  @Override public void onReq(BaseReq baseReq) {

  }

  @Override public void onResp(BaseResp resp) {
    switch (resp.errCode) {
      case BaseResp.ErrCode.ERR_OK:
        //分享成功
        Log.d(TAG, "onResp: ok ");
        break;
      case BaseResp.ErrCode.ERR_USER_CANCEL:
        //分享取消
        Log.d(TAG, "onResp: cancel");
        break;
      case BaseResp.ErrCode.ERR_AUTH_DENIED:
        //分享拒绝
        Log.d(TAG, "onResp: fail");
        break;
      case BaseResp.ErrCode.ERR_BAN:
        Log.d(TAG, "onResp: ban");
        break;
    }
  }
}

