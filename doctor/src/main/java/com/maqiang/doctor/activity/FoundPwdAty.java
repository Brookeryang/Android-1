package com.maqiang.doctor.activity;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;

import butterknife.BindView;
import butterknife.ButterKnife;
import com.maqiang.doctor.MyApplication;
import com.maqiang.doctor.R;
import com.maqiang.doctor.bean.User;
import com.maqiang.doctor.utils.BmobManager;
import com.maqiang.doctor.utils.ToastUtils;

/**
 * Created by maqiang on 2017/2/26.
 */

public class FoundPwdAty extends BaseActivity {

  private static final String TAG = "Found";
  @BindView(R.id.toolbar) Toolbar mToolbar;
  @BindView(R.id.phone_number) TextInputLayout mPhone;
  @BindView(R.id.password) TextInputLayout mVerifyCode;
  @BindView(R.id.password1) TextInputLayout mPwd1;
  @BindView(R.id.password2) TextInputLayout mPwd2;
  @BindView(R.id.found_bt) Button mConfirm;
  @BindView(R.id.bt_getcode) Button mCode;

  private User mUser;
  public static final String NEXT = "isNext";

  @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.aty_found);
    ButterKnife.bind(this);
    MyApplication.getInstance().addActivity(this);
    mUser = (User) getIntent().getSerializableExtra(RegisterAty.USER);
    initView();
  }

  private void initView() {
    initToolBar(mToolbar, R.drawable.ic_arrow_back_black_24dp, R.string.found_title,
        R.color.black_textcolor);
    initOnClickListener(mConfirm);
    mToolbar.setNavigationOnClickListener(this);
    mCode.setOnClickListener(this);
    updateButtonState();
  }

  private void updateButtonState() {
    mPhone.getEditText().addTextChangedListener(new TextWatcher() {
      @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {

      }

      @Override public void onTextChanged(CharSequence s, int start, int before, int count) {

      }

      @Override public void afterTextChanged(Editable s) {
        if (s.length() > 0
            && mVerifyCode.getEditText().getText().length() > 0
            && mPwd1.getEditText().getText().length() > 0
            && mPwd2.getEditText().getText().length() > 0) {
          mConfirm.setEnabled(true);
          mConfirm.setBackgroundResource(R.color.appbar_bg);
        } else {
          mConfirm.setEnabled(false);
          mConfirm.setBackgroundResource(R.color.bt_default);
        }
      }
    });
    mVerifyCode.getEditText().addTextChangedListener(new TextWatcher() {
      @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {

      }

      @Override public void onTextChanged(CharSequence s, int start, int before, int count) {

      }

      @Override public void afterTextChanged(Editable s) {
        if (s.length() > 0
            && mPhone.getEditText().getText().length() > 0
            && mPwd1.getEditText().getText().length() > 0
            && mPwd2.getEditText().getText().length() > 0) {
          mConfirm.setEnabled(true);
          mConfirm.setBackgroundResource(R.color.appbar_bg);
        } else {
          mConfirm.setEnabled(false);
          mConfirm.setBackgroundResource(R.color.bt_default);
        }
      }
    });
    mPwd1.getEditText().addTextChangedListener(new TextWatcher() {
      @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {

      }

      @Override public void onTextChanged(CharSequence s, int start, int before, int count) {

      }

      @Override public void afterTextChanged(Editable s) {
        if (s.length() > 0
            && mVerifyCode.getEditText().getText().length() > 0
            && mPhone.getEditText().getText().length() > 0
            && mPwd2.getEditText().getText().length() > 0) {
          mConfirm.setEnabled(true);
          mConfirm.setBackgroundResource(R.color.appbar_bg);
        } else {
          mConfirm.setEnabled(false);
          mConfirm.setBackgroundResource(R.color.bt_default);
        }
      }
    });

    mPwd2.getEditText().addTextChangedListener(new TextWatcher() {
      @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {

      }

      @Override public void onTextChanged(CharSequence s, int start, int before, int count) {

      }

      @Override public void afterTextChanged(Editable s) {
        if (s.length() > 0
            && mVerifyCode.getEditText().getText().length() > 0
            && mPwd1.getEditText().getText().length() > 0
            && mPhone.getEditText().getText().length() > 0) {
          mConfirm.setEnabled(true);
          mConfirm.setBackgroundResource(R.color.appbar_bg);
        } else {
          mConfirm.setEnabled(false);
          mConfirm.setBackgroundResource(R.color.bt_default);
        }
      }
    });
  }

  @Override public void onClick(View v) {
    switch (v.getId()) {
      case R.id.found_bt:
        if (mPhone.getEditText().getText().length() != 11) {
          ToastUtils.show("手机号码不对");
        } else if (mVerifyCode.getEditText().getText().length() != 6) {
          ToastUtils.show("验证码位数不对");
        } else if (!mPwd1.getEditText().getText().toString()
            .equals(mPwd2.getEditText().getText().toString())) {
          ToastUtils.show("两次密码输入不一样");
        } else {
          BmobManager.updatePwd(mVerifyCode.getEditText().getText().toString(),
              mPwd1.getEditText().getText().toString(), this);
        }
        break;
      case R.id.bt_getcode:
        mCode.setEnabled(false);
        CountDownTimer timer = new CountDownTimer(60000, 1000) {
          @Override public void onTick(final long millisUntilFinished) {
            runOnUiThread(new Runnable() {
              @Override public void run() {
                String tips = getResources().getString(R.string.second_code);
                mCode.setText(String.format(tips, millisUntilFinished / 1000 + ""));
              }
            });
          }

          @Override public void onFinish() {
            mCode.setEnabled(true);
            mCode.setText(R.string.get_code);
          }
        };
        String phone1 = mPhone.getEditText().getText().toString().trim();
        if (phone1.length() == 11) {
          timer.start();
          BmobManager.requestCode(phone1);
        } else {
          ToastUtils.show("手机号码不对");
        }
        break;
      default:
        FoundPwdAty.this.finish();
        break;
    }
  }
}
