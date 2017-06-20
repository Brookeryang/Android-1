package com.maqiang.doctor.fragment;

import android.Manifest;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;

import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.annotation.MainThread;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.maqiang.doctor.MyApplication;
import com.maqiang.doctor.R;
import com.maqiang.doctor.activity.EditPersionalInfo;
import com.maqiang.doctor.bean.User;
import com.maqiang.doctor.utils.BmobManager;
import com.maqiang.doctor.utils.ToastUtils;
import com.squareup.picasso.Picasso;
import de.hdodenhof.circleimageview.CircleImageView;
import java.io.File;
import java.util.Calendar;

import static android.app.Activity.RESULT_OK;
import static android.app.AlarmManager.RTC_WAKEUP;
import static android.content.Context.ALARM_SERVICE;

/**
 * Created by maqiang on 2017/5/4.
 *
 * Function:我的功能页面
 */

public class MyFragment extends Fragment {

  @BindView(R.id.cpb_layout) RelativeLayout mCircleProgressBar;
  @BindView(R.id.user_head) CircleImageView mCircleImageView;
  @BindView(R.id.my_appbar) AppBarLayout mAppBarLayout;
  @BindView(R.id.ctbl) CollapsingToolbarLayout mToolbarLayout;
  @BindView(R.id.toolbar) Toolbar mToolbar;
  @BindView(R.id.user_name) TextView mUserName;
  @BindView(R.id.user_city) TextView mCity;
  @BindView(R.id.user_age) TextView mAge;
  @BindView(R.id.user_pre) TextView mPre;
  @BindView(R.id.ll_record) LinearLayout mRecord;
  @BindView(R.id.tv_help) TextView mHelp;
  @BindView(R.id.tv_my) TextView mMy;
  @BindView(R.id.my_edit) ImageView mEdit;
  @BindView(R.id.imageView) ImageView mLock;

  /* 头像名称 */
  private static final String PHOTO_FILE_NAME = "temp_photo.jpg";
  private static Uri sUri;
  private File tempFile;
  private FragmentManager fm;
  private FragmentTransaction ft;

  private static final int PHOTO_REQUEST_CAREMA = 1;// 拍照
  private static final int PHOTO_REQUEST_GALLERY = 2;// 从相册中选择
  private static final int PHOTO_REQUEST_CUT = 3;// 结果
  public static final int REQUEST_CODE = 4;

  private boolean isCamera = false, isWrite = false;

  private int mRecordIndex = 0;

  private Handler mHandler = new Handler() {
    @Override public void handleMessage(Message msg) {
      switch (msg.what) {
        case BmobManager.USER_WHAT:
          User user = (User) msg.obj;
          mCity.setVisibility(View.VISIBLE);
          mAge.setVisibility(View.VISIBLE);
          if (user != null) {
            if (TextUtils.isEmpty(user.getNickname())) {
              mUserName.setText(getResources().getString(R.string.user_none));
            } else {
              mUserName.setText(user.getNickname());
            }

            if (TextUtils.isEmpty(user.getCity())) {
              mCity.setText(getResources().getString(R.string.city_none));
            } else {
              mCity.setText(user.getCity());
            }

            if (TextUtils.isEmpty(user.getAge() + "")) {
              mAge.setText(getResources().getString(R.string.age_none));
            } else {
              String age = MyApplication.getInstance().getResources().getString(R.string.age);
              mAge.setText(String.format(age, user.getAge()));
            }

            if (TextUtils.isEmpty(user.getDuedate())) {
              String date = getResources().getString(R.string.duedate);
              mPre.setText(String.format(date, getResources().getString(R.string.none)));
            } else {
              String date = getResources().getString(R.string.duedate);
              mPre.setText(String.format(date, user.getDuedate()));
            }

            if (user.getIcon() == null) {
              mCircleImageView.setImageResource(R.drawable.ic_account_circle_grey_500_48dp);
            } else {
              Picasso.with(MyApplication.getInstance()).load(user.getIcon().getUrl()).into(mCircleImageView);
            }
          } else {
            mUserName.setText(getResources().getString(R.string.user_none));
            mCity.setText(getResources().getString(R.string.city_none));
            mAge.setText(getResources().getString(R.string.age_none));
            String date = getResources().getString(R.string.duedate);
            mPre.setText(String.format(date, getResources().getString(R.string.none)));
          }
          break;
      }
    }
  };

  @Nullable @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_personal, container, false);
    ButterKnife.bind(this, view);
    setHasOptionsMenu(true);
    mToolbar.setTitle("");
    ((AppCompatActivity) getActivity()).setSupportActionBar(mToolbar);
    initUserInfo();
    loadCollectFragment();
    initEvent();
    return view;
  }

  @MainThread private void initEvent() {

    mAppBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
      @Override public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
        if (Math.abs(verticalOffset) >= appBarLayout.getTotalScrollRange()) {
          mToolbarLayout.setTitle(
              mRecordIndex == 0 ? getString(R.string.record) : getString(R.string.help));
        } else {
          mToolbarLayout.setTitle("");
        }
        if (Math.abs(verticalOffset) > appBarLayout.getTotalScrollRange() / 2) {
          if (mToolbar.getVisibility() == View.INVISIBLE) mToolbar.setVisibility(View.VISIBLE);
        } else {
          if (mToolbar.getVisibility() == View.VISIBLE) mToolbar.setVisibility(View.INVISIBLE);
        }
      }
    });
  }

  private void initUserInfo() {
    BmobManager.getUserInfo(mHandler);
  }

  @OnClick({ R.id.ll_record, R.id.tv_help, R.id.cpb_layout, R.id.user_head })
  public void click(View view) {
    switch (view.getId()) {
      case R.id.ll_record:
        mRecordIndex = 0;
        updateBar(mRecordIndex);
        loadCollectFragment();
        break;
      case R.id.tv_help:
        mRecordIndex = 1;
        updateBar(mRecordIndex);
        loadFeedBackFragment();
        break;
      case R.id.cpb_layout:
        final AlertDialog dialog =
            new AlertDialog.Builder(getActivity()).setView(R.layout.time).create();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable());
        dialog.show();
        if (dialog.isShowing()) {
          dialog.findViewById(R.id.tv_timetstart).setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
              ToastUtils.show("已经开始计时,2小时后将会提醒记录血糖,请不要关闭应用.");
              Intent intent = new Intent("android.intent.action.test");
              PendingIntent sender =
                  PendingIntent.getBroadcast(MyApplication.getInstance(), 0, intent, 0);

              //设置一个十秒后的时间
              Calendar calendar = Calendar.getInstance();
              calendar.setTimeInMillis(System.currentTimeMillis());
              calendar.add(Calendar.SECOND, 5);

              AlarmManager alarmManager =
                  (AlarmManager) MyApplication.getInstance().getSystemService(ALARM_SERVICE);
              alarmManager.set(RTC_WAKEUP, calendar.getTimeInMillis(), sender);
              dialog.dismiss();
            }
          });
        }
        break;
      case R.id.user_head:
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
          if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA)
              != PackageManager.PERMISSION_GRANTED) {
            Log.d("TAG", "开始申请相机权限");
            this.requestPermissions(new String[] { android.Manifest.permission.CAMERA }, PHOTO_REQUEST_CAREMA);
          } else {
            camera(view);
          }
        } else {
          camera(view);
        }
        break;
    }
  }

  @Override public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
      @NonNull int[] grantResults) {
    super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    if (requestCode == PHOTO_REQUEST_CAREMA) {
      if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
        camera(mCircleImageView);
      } else {
        ToastUtils.show("相机权限被禁止,请在手机权限管理处给予授权.");
      }
    }
  }

  private void updateBar(int i) {
    if (i == 0) {
      mRecord.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.oval));
      mMy.setTextColor(Color.WHITE);
      mHelp.setBackgroundColor(0);
      mHelp.setTextColor(ContextCompat.getColor(getActivity(), R.color.album_bar));
      mLock.setImageResource(R.drawable.lock);
    } else if (i == 1) {
      mRecord.setBackgroundResource(0);
      mMy.setTextColor(ContextCompat.getColor(getActivity(), R.color.album_bar));
      mHelp.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.oval));
      mHelp.setTextColor(Color.WHITE);
      mLock.setImageResource(R.drawable.lock2);
    }
  }

  /*
  * 从相机获取
  */
  public void camera(View view) {
    // 激活相机
    Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
    // 判断存储卡是否可以用，可用进行存储
    if (hasSdcard()) {
      ContentValues contentValues = new ContentValues(2);
      //如果想拍完存在系统相机的默认目录,改为
      contentValues.put(MediaStore.Images.Media.DISPLAY_NAME, PHOTO_FILE_NAME);
      contentValues.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");
      sUri = getActivity().getContentResolver()
          .insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);
      intent.putExtra(MediaStore.EXTRA_OUTPUT, sUri);
    }
    // 开启一个带有返回值的Activity，请求码为PHOTO_REQUEST_CAREMA
    startActivityForResult(intent, PHOTO_REQUEST_CAREMA);
  }

  /*
   * 剪切图片
   */
  private void crop(Uri uri) {
    // 裁剪图片意图
    Intent intent = new Intent("com.android.camera.action.CROP");
    intent.setDataAndType(uri, "image/*");
    intent.putExtra("crop", "true");
    // 裁剪框的比例，1：1
    intent.putExtra("aspectX", 1);
    intent.putExtra("aspectY", 1);
    // 裁剪后输出图片的尺寸大小
    intent.putExtra("outputX", 250);
    intent.putExtra("outputY", 250);

    intent.putExtra("outputFormat", "JPEG");// 图片格式
    intent.putExtra("noFaceDetection", true);// 取消人脸识别
    intent.putExtra("return-data", true);
    // 开启一个带有返回值的Activity，请求码为PHOTO_REQUEST_CUT
    getActivity().startActivityFromFragment(this, intent, PHOTO_REQUEST_CUT);
  }

  /*
    * 判断sdcard是否被挂载
    */
  private boolean hasSdcard() {
    if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
      return true;
    } else {
      return false;
    }
  }

  private String getImageURL(Uri oldUri) {
    String[] images = { MediaStore.Images.Media.DATA };
    Cursor cursor = getActivity().managedQuery(oldUri, images, null, null, null);
    int index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
    cursor.moveToFirst();
    return cursor.getString(index);
  }

  @Override public void onActivityResult(int requestCode, int resultCode, Intent data) {
    if (requestCode == PHOTO_REQUEST_GALLERY) {
      // 从相册返回的数据
      if (data != null) {
        // 得到图片的全路径
        Uri uri = data.getData();
        crop(uri);
        BmobManager.uploadIcon(getImageURL(uri));
      }
    } else if (requestCode == PHOTO_REQUEST_CAREMA) {
      // 从相机返回的数据
      if (hasSdcard()) {
        crop(sUri);
        BmobManager.uploadIcon(getImageURL(sUri));
      } else {
        ToastUtils.show("未找到存储卡，无法存储照片！");
      }
    } else if (requestCode == PHOTO_REQUEST_CUT) {
      // 从剪切图片返回的数据
      if (data != null) {
        Bitmap bitmap = data.getParcelableExtra("data");
        mCircleImageView.setImageBitmap(bitmap);
      }
      try {
        // 将临时文件删除
        if (tempFile != null) {
          tempFile.delete();
        }
      } catch (Exception e) {
        e.printStackTrace();
      }
    } else if (requestCode == REQUEST_CODE&&resultCode == RESULT_OK) {
      initUserInfo();
    }
    super.onActivityResult(requestCode, resultCode, data);
  }

  private void loadFeedBackFragment() {
    FeedBackFragment fragment = new FeedBackFragment();
    fm = getActivity().getSupportFragmentManager();
    ft = fm.beginTransaction();
    ft.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out);
    ft.replace(R.id.my_framelayout, fragment);
    ft.commit();
  }

  private void loadCollectFragment() {
    CollectFragment fragment = new CollectFragment();
    fm = getActivity().getSupportFragmentManager();
    ft = fm.beginTransaction();
    ft.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out);
    ft.replace(R.id.my_framelayout, fragment);
    ft.commit();
  }

  //@Override public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
  //  menu.clear();
  //  inflater.inflate(R.menu.edit, menu);
  //  super.onCreateOptionsMenu(menu, inflater);
  //}
  //
  //@Override public boolean onOptionsItemSelected(MenuItem item) {
  //  if (item.getItemId() == R.id.edit) {
  //    startEditPersionalActivity();
  //  }
  //  return super.onOptionsItemSelected(item);
  //}

  @OnClick(R.id.my_edit) public void startEditPersionalActivity() {
    Intent intent = new Intent(getActivity(), EditPersionalInfo.class);
    this.startActivityForResult(intent, REQUEST_CODE, null);
  }
}
