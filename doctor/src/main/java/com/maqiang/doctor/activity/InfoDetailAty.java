package com.maqiang.doctor.activity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.Toolbar;
import android.transition.Transition;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.WindowManager;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.ImageView;

import butterknife.BindView;
import butterknife.ButterKnife;
import com.maqiang.doctor.MyApplication;
import com.maqiang.doctor.R;
import com.maqiang.doctor.bean.InfoDetail;
import com.maqiang.doctor.bean.InfoItem;
import com.maqiang.doctor.net.APIGenerator;
import com.maqiang.doctor.net.APIService;
import com.maqiang.doctor.rxadapter.rxcompat.SchedulersCompat;
import com.maqiang.doctor.utils.BmobManager;
import com.maqiang.doctor.utils.ConstantUtil;
import com.squareup.picasso.Picasso;

import fr.castorflex.android.smoothprogressbar.SmoothProgressBar;
import java.util.HashMap;
import java.util.Map;

import io.reactivex.observers.DisposableObserver;


/**
 * Created by maqiang on 2017/3/20.
 */

public class InfoDetailAty extends BaseActivity {

  @BindView(R.id.toolbar) Toolbar mToolbar;
  @BindView(R.id.image) ImageView mImageView;
  @BindView(R.id.text) WebView mWebView;
  @BindView(R.id.progress) SmoothProgressBar mProgressBar;
  String flag;

  private Handler mHandler = new Handler(){
    @Override public void handleMessage(Message msg) {
      switch (msg.what){
        case BmobManager.INFO:
          int isCollect = msg.arg1;
          if (isCollect == 1){
            mToolbar.getMenu().findItem(R.id.collect).setIcon(R.drawable.ic_star_black_24dp);
          }else if (isCollect == 0){
            mToolbar.getMenu().findItem(R.id.collect).setIcon(R.drawable.ic_star_border_black_24dp);
          }
          break;
      }
    }
  };

  private InfoItem mInfoItem;
  private String mUrl;

  @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.aty_info_detail);
    MyApplication.getInstance().addActivity(this);
    ButterKnife.bind(this);
    initView();
    fillView();
  }

  @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP) private void initView() {
    getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
    //透明导航栏
    getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
    //内容共享
    mInfoItem = (InfoItem) getIntent().getSerializableExtra("info");
    mUrl = getIntent().getStringExtra("url");
    flag = getIntent().getStringExtra("flag");
    initToolBar(mToolbar,R.drawable.ic_arrow_back_black_24dp);
    setSupportActionBar(mToolbar);
    // 当Android版本低于5.0时,关闭WebView的共享动画
    if(android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
      mWebView.setTransitionGroup(true);
    }
    // 这里指定了被共享的视图元素
    ViewCompat.setTransitionName(mImageView, "image");
    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
      android.transition.Transition transition = getWindow().getSharedElementEnterTransition();
      transition.addListener(new Transition.TransitionListener() {
        @Override public void onTransitionStart(Transition transition) {

        }

        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP) @Override
        public void onTransitionEnd(Transition transition) {
          performCircleReveal();
        }

        @Override public void onTransitionCancel(Transition transition) {

        }

        @Override public void onTransitionPause(Transition transition) {

        }

        @Override public void onTransitionResume(Transition transition) {

        }
      });
    }

    mWebView.setWebChromeClient(new WebChromeClient(){
      @Override public void onProgressChanged(WebView view, int newProgress) {
        if (newProgress == 100){
            mProgressBar.setVisibility(View.INVISIBLE);
            mWebView.setVisibility(View.VISIBLE);
        }
        super.onProgressChanged(view, newProgress);
      }
    });
    mWebView.getSettings().setBlockNetworkImage(true);
    mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        onBackPressed();
      }
    });
    BmobManager.collect(mInfoItem,mHandler);
  }

  @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP) private void performCircleReveal() {
    View view = this.findViewById(android.R.id.content);
    Animator anim = ViewAnimationUtils.createCircularReveal(view,
        (mImageView.getLeft() + mImageView.getRight()) / 2,
        (mImageView.getTop() + mImageView.getBottom()) / 2, (float) mImageView.getWidth() / 2,
        (float) Math.hypot(view.getWidth(), view.getHeight()));
    anim.setDuration(1000);
    anim.setInterpolator(new AccelerateDecelerateInterpolator());
    anim.addListener(new AnimatorListenerAdapter() {
      @Override public void onAnimationStart(Animator animation) {
        super.onAnimationStart(animation);
        Log.e("---", "start anim");
      }

      @Override public void onAnimationEnd(Animator animation) {
        super.onAnimationEnd(animation);
        Log.e("---", "end anim");
        //show views
      }
    });
    anim.start();
  }

  private void fillView() {
    Picasso.with(this).load(mUrl).into(mImageView);
    Map<String, String> option = new HashMap<>();
    option.put("id", mInfoItem.getId()+"");
    option.put("key", ConstantUtil.JUHE_KEY);
    APIGenerator.createService(APIService.class)
        .getInfoDetail(option)
        .compose(SchedulersCompat.<InfoDetail>applyExecutorSchedulers())
        .subscribe(new DisposableObserver<InfoDetail>() {
          @Override public void onNext(final InfoDetail value) {
            final String data = getRemoveImage(value.getResult().getMessage());
            mWebView.loadDataWithBaseURL(null,data,"text/html;charset=UTF-8", null,null);
          }

          @Override public void onError(Throwable e) {

          }

          @Override public void onComplete() {

          }
        });
  }

  private String getRemoveImage(String message) {
    int index = 0;
    while ((index = message.indexOf("<img"))!=-1){
      int end = message.indexOf("<p>",index);
      if (index == 0 ){
        //修复异常数据导致的闪退问题
        break;
      }
      message = message.substring(0,index-3)+message.substring(end,message.length());
    }
    Log.d("getRemoveImage",message);
    return message;
  }

  @Override public void onBackPressed() {
    if (flag.equals("1")){
      setResult(RESULT_OK);
      this.finish();
    }else {
      super.onBackPressed();
    }
  }

  @Override public boolean onCreateOptionsMenu(Menu menu) {
    getMenuInflater().inflate(R.menu.collect,menu);
    return super.onCreateOptionsMenu(menu);
  }

  @Override public boolean onOptionsItemSelected(MenuItem item) {
    int id = item.getItemId();
    BmobManager.updateCollect(mInfoItem,mHandler);
    return super.onOptionsItemSelected(item);
  }
}
