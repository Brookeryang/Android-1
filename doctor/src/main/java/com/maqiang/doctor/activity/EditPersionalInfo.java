package com.maqiang.doctor.activity;

import android.Manifest;
import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UploadFileListener;
import com.maqiang.doctor.MyApplication;
import com.maqiang.doctor.R;
import com.maqiang.doctor.bean.User;
import com.maqiang.doctor.utils.BmobManager;
import com.maqiang.doctor.utils.ToastUtils;
import com.squareup.picasso.Picasso;
import de.hdodenhof.circleimageview.CircleImageView;
import java.io.File;
import java.util.Calendar;

/**
 * Created by maqiang on 2017/5/5.
 *
 * Function:
 */

public class EditPersionalInfo extends BaseActivity {

  @BindView(R.id.toolbar) Toolbar mToolbar;
  @BindView(R.id.user_pic) CircleImageView mCircleImageView;
  @BindView(R.id.edit_name) TextInputEditText mName;
  @BindView(R.id.edit_age) TextInputEditText mAge;
  @BindView(R.id.edit_city) TextInputEditText mCity;
  @BindView(R.id.edit_pre) TextInputEditText mPre;

  private User mUser;
  /* 头像名称 */
  private static final String PHOTO_FILE_NAME = "temp_photo.jpg";
  private static Uri sUri;
  private File tempFile;
  private static final int PHOTO_REQUEST_CAREMA = 1;// 拍照
  private static final int PHOTO_REQUEST_GALLERY = 2;// 从相册中选择
  private static final int PHOTO_REQUEST_CUT = 3;// 结果
  private Handler mHandler = new Handler() {
    @Override public void handleMessage(Message msg) {
      switch (msg.what) {
        case BmobManager.USER_WHAT:
          mUser = (User) msg.obj;
          if (mUser != null) {
            if (TextUtils.isEmpty(mUser.getNickname())) {
              mName.setText(getResources().getString(R.string.user_none));
            } else {
              mName.setText(mUser.getNickname());
            }

            if (TextUtils.isEmpty(mUser.getCity())) {
              mCity.setText(getResources().getString(R.string.city_none));
            } else {
              mCity.setText(mUser.getCity());
            }

            if (TextUtils.isEmpty(mUser.getAge() + "")) {
              mAge.setText(getResources().getString(R.string.age_none));
            } else {
              mAge.setText(String.valueOf(mUser.getAge()));
            }

            if (TextUtils.isEmpty(mUser.getDuedate())) {
              mPre.setText("--");
            } else {
              mPre.setText(mUser.getDuedate());
            }

            if (mUser.getIcon() == null) {
              mCircleImageView.setImageResource(R.drawable.ic_account_circle_grey_500_48dp);
            } else {
              Picasso.with(EditPersionalInfo.this)
                  .load(mUser.getIcon().getUrl())
                  .into(mCircleImageView);
            }
          } else {
            mName.setText(getResources().getString(R.string.user_none));
            mCity.setText(getResources().getString(R.string.city_none));
            mAge.setText(getResources().getString(R.string.age_none));
            String date = getResources().getString(R.string.duedate);
            mPre.setText(String.format(date, getResources().getString(R.string.none)));
          }
          break;
      }
    }
  };

  @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.personal_edit);
    MyApplication.getInstance().addActivity(this);
    ButterKnife.bind(this);
    initToolBar(mToolbar, R.drawable.ic_arrow_back_black_24dp, R.string.edit,
        R.color.black_textcolor);
    setSupportActionBar(mToolbar);
    BmobManager.getUserInfo(mHandler);
    initView();
  }

  @OnClick(R.id.edit_exit) public void goToLoginAty() {
    Intent intent = new Intent(this, LoginAty.class);
    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
    startActivity(intent);
  }

  private void initView() {
    mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        EditPersionalInfo.super.onBackPressed();
      }
    });

    mPre.setOnFocusChangeListener(new View.OnFocusChangeListener() {
      @Override public void onFocusChange(View v, boolean hasFocus) {
        Calendar calendar = Calendar.getInstance();
        DatePickerDialog datePickerDialog =
            new DatePickerDialog(EditPersionalInfo.this, new DatePickerDialog.OnDateSetListener() {
              @Override
              public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                monthOfYear++;
                mPre.setText(year + "-" + monthOfYear + "-" + dayOfMonth);
              }
            }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH));

        if (hasFocus){
          datePickerDialog.show();
        }else {
          datePickerDialog.cancel();
        }
      }
    });
  }

  @Override public boolean onCreateOptionsMenu(Menu menu) {
    getMenuInflater().inflate(R.menu.recepie_menu, menu);
    return super.onCreateOptionsMenu(menu);
  }

  @Override public boolean onOptionsItemSelected(MenuItem item) {
    if (item.getItemId() == R.id.done) {
      String username = mName.getText().toString();
      String age = mAge.getText().toString();
      String city = mCity.getText().toString();
      String pre = mPre.getText().toString();
      if (!TextUtils.isEmpty(username)
          && !TextUtils.isEmpty(age)
          && !TextUtils.isEmpty(city)
          && !TextUtils.isEmpty(pre)) {
          mUser.setNickname(username);
          mUser.setCity(city);
          mUser.setAge(Integer.valueOf(age));
          mUser.setDuedate(pre);
          if (sUri!=null) {
            if (getImageURL(sUri) != null) {
              final BmobFile icon = new BmobFile(new File(getImageURL(sUri)));
              icon.upload(new UploadFileListener() {
                @Override public void done(BmobException e) {
                  if (e == null) {
                    mUser.setIcon(icon);
                    BmobManager.updata(mUser);
                    setResult(RESULT_OK);
                    EditPersionalInfo.this.finish();
                  } else {
                    ToastUtils.show("头像上传失败");
                  }
                }
              });
            }
          }else {
            BmobManager.updata(mUser);
            setResult(RESULT_OK);
            EditPersionalInfo.this.finish();
          }
        }
      } else {
        ToastUtils.show("必填项数据不能为空.");
      }
    return super.onOptionsItemSelected(item);
  }

  @OnClick(R.id.edit_pic) public void openDialog(final View view) {
    AlertDialog dialog = new AlertDialog.Builder(this).setTitle("请选择头像")
        .setItems(new String[] { "拍照", "相册" }, new DialogInterface.OnClickListener() {
          @Override public void onClick(DialogInterface dialog, int which) {
            if (which == 1) {
              goAlbums();
            } else {
              //当Android版本处于Android6.0及其以上时进行动态权限申请
              if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (ContextCompat.checkSelfPermission(EditPersionalInfo.this,
                    Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                  Log.d("TAG", "开始申请相机权限");
                  ActivityCompat.requestPermissions(EditPersionalInfo.this,
                      new String[] { android.Manifest.permission.CAMERA }, 101);
                } else {
                  //动态权限被授权时
                  camera(view);
                }
              } else {
                //当Android版本处于Android6.0以下时可以直接使用
                camera(view);
              }
            }
          }
        })
        .create();
    dialog.show();
  }

  /**
   * 动态权限申请结果回调
   */
  @Override public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
      @NonNull int[] grantResults) {
    super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    if (requestCode == 101) {
      if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
        camera(mCircleImageView);
      } else {
        ToastUtils.show("相机权限被禁止,请在手机权限管理处给予授权.");
      }
    }
  }

  /**
   * 从相册获取
   */
  private void goAlbums() {
    Intent intent = new Intent(Intent.ACTION_PICK);
    //intent.addCategory(Intent.CATEGORY_OPENABLE);
    intent.setType("image/*");
    startActivityForResult(intent, PHOTO_REQUEST_GALLERY);
  }

  /**
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
      sUri = this.getContentResolver()
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
    this.startActivityForResult(intent, PHOTO_REQUEST_CUT);
  }

  /**
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
    Cursor cursor = this.managedQuery(oldUri, images, null, null, null);
    int index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
    cursor.moveToFirst();
    return cursor.getString(index);
  }

  @Override public void onActivityResult(int requestCode, int resultCode, Intent data) {
    if (requestCode == PHOTO_REQUEST_GALLERY) {
      // 从相册返回的数据
      if (data != null) {
        // 得到图片的全路径
        sUri = data.getData();
        crop(sUri);
        //if (getImageURL(sUri) != null) {
        //  BmobManager.uploadIcon(getImageURL(uri));
        //  BmobManager.getUserInfo(mHandler);
        //}
      }
    } else if (requestCode == PHOTO_REQUEST_CAREMA) {
      // 从相机返回的数据
      if (hasSdcard()) {
        crop(sUri);
        //if (getImageURL(sUri) != null) {
        //  BmobManager.uploadIcon(getImageURL(sUri));
        //  BmobManager.getUserInfo(mHandler);
        //}
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
    }
    super.onActivityResult(requestCode, resultCode, data);
  }
}
