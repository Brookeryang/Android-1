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

import com.maqiang.doctor.MyApplication;
import com.maqiang.doctor.R;

import com.maqiang.doctor.bean.User;
import com.maqiang.doctor.utils.BmobManager;
import com.maqiang.doctor.utils.ToastUtils;

/**
 * Created by maqiang on 2017/2/26.
 */

public class RegisterAty extends BaseActivity{

    private static final String TAG = "BaseActivity";
    public static final String CLICK = "click";
    public static final String USER = "user";
    private Toolbar mToolbar;
    private TextInputLayout mPwd,mPhoneNumber;
    private Button mConfirm,mCode;
    private boolean isClicked = false;
    private User mUser;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.aty_register);
        MyApplication.getInstance().addActivity(this);
        init();
        initView();
        initEvent();
    }

    private void init() {
        isClicked = getIntent().getBooleanExtra(CLICK,false);
        mUser = (User) getIntent().getSerializableExtra(USER);
    }

    private void initView() {
        mToolbar = getViewById(R.id.toolbar);
        initToolBar(mToolbar,R.drawable.ic_arrow_back_black_24dp,R.string.register_titile,R.color.black_textcolor);
        mPhoneNumber = getViewById(R.id.phone_number);
        mPwd = getViewById(R.id.password);
        mConfirm = getViewById(R.id.register);
        mCode = getViewById(R.id.bt_getcode);
        if (isClicked){
            mCode.setVisibility(View.GONE);
            mPwd.getEditText().setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD|InputType.TYPE_CLASS_TEXT);
            mPwd.setHint(getResources().getString(R.string.password2));
            mPwd.setPasswordVisibilityToggleEnabled(true);
            mPhoneNumber.getEditText().setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD|InputType.TYPE_CLASS_TEXT);
            mPhoneNumber.setHint(getResources().getString(R.string.password));
            mPhoneNumber.setPasswordVisibilityToggleEnabled(true);
            mPhoneNumber.getEditText().setMaxLines(1);
            mConfirm.setText(R.string.register_bt);
        }
        mPhoneNumber.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override public void afterTextChanged(Editable s) {
                if (mPwd.getEditText().getText().length()>0&&s.length()>0){
                    mConfirm.setEnabled(true);
                    mConfirm.setBackgroundResource(R.color.appbar_bg);
                }else {
                    mConfirm.setEnabled(false);
                    mConfirm.setBackgroundResource(R.color.bt_default);
                }
            }
        });
        mPwd.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override public void afterTextChanged(Editable s) {
                if (mPwd.getEditText().getText().length()>0&&s.length()>0){
                    mConfirm.setEnabled(true);
                    mConfirm.setBackgroundResource(R.color.appbar_bg);
                }else {
                    mConfirm.setEnabled(false);
                    mConfirm.setBackgroundResource(R.color.bt_default);
                }
            }
        });
    }

    private void initEvent() {
        mToolbar.setNavigationOnClickListener(this);
        mCode.setOnClickListener(this);
        initOnClickListener(mConfirm);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.register:
                if (!isClicked) {
                    String phone = mPhoneNumber.getEditText().getText().toString().trim();
                    String code = mPwd.getEditText().getText().toString().trim();
                    BmobManager.verifyCode(RegisterAty.this,phone, code,0);
                }else {
                    String pwd1 = mPhoneNumber.getEditText().getText().toString().trim();
                    String pwd2 = mPwd.getEditText().getText().toString().trim();
                    if (pwd1.equals(pwd2)){
                        mUser.setPassword(pwd1);
                        BmobManager.register(mUser,this);
                    }else {
                        ToastUtils.show("两次密码输入不一样，请重新输入.");
                    }
                }
                break;

            case R.id.bt_getcode:
                if (!isClicked) {
                    mCode.setEnabled(false);
                    CountDownTimer timer = new CountDownTimer(60000, 1000) {
                        @Override public void onTick(final long millisUntilFinished) {
                            runOnUiThread(new Runnable() {
                                @Override public void run() {
                                    String tips = getResources().getString(R.string.second_code);
                                    mCode.setText(
                                        String.format(tips, millisUntilFinished / 1000 + ""));
                                }
                            });
                        }

                        @Override public void onFinish() {
                            mCode.setEnabled(true);
                            mCode.setText(R.string.get_code);
                        }
                    };

                    String phone1 = mPhoneNumber.getEditText().getText().toString().trim();
                    if (phone1.length() == 11) {
                        timer.start();
                        BmobManager.requestCode(phone1);
                    }else {
                        mCode.setEnabled(true);
                        ToastUtils.show("手机号码不对");
                    }
                }
                break;
            default:
                RegisterAty.this.finish();
                break;
        }
    }
}
