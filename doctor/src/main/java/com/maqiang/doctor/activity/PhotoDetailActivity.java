package com.maqiang.doctor.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.maqiang.doctor.MyApplication;
import com.maqiang.doctor.R;
import com.squareup.picasso.Picasso;

/**
 * Created by maqiang on 2017/4/17.
 */

public class PhotoDetailActivity extends BaseActivity {

  @BindView(R.id.toolbar) Toolbar mToolbar;
  @BindView(R.id.imageView) ImageView mImageView;
  private String mDate;

  @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.photo_detail);
    MyApplication.getInstance().addActivity(this);
    ButterKnife.bind(this);
    mDate = getIntent().getStringExtra("date");
    initToolBar(mToolbar, R.drawable.ic_arrow_back_black_24dp,
        mDate.split(" ")[0].replaceAll("-", "/"), R.color.toolbar_textcolor);
    String url = getIntent().getStringExtra("url");
    // 这里指定了被共享的视图元素
    ViewCompat.setTransitionName(mImageView, "image");
    Picasso.with(this).load(url).into(mImageView);
  }

  @Override public boolean onCreateOptionsMenu(Menu menu) {
    getMenuInflater().inflate(R.menu.toolbar, menu);
    return super.onCreateOptionsMenu(menu);
  }

  @Override public boolean onOptionsItemSelected(MenuItem item) {
    switch (item.getItemId()) {
      case R.id.add_blood:
        break;
    }
    return super.onOptionsItemSelected(item);
  }
}
