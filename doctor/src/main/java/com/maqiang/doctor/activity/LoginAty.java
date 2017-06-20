package com.maqiang.doctor.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.maqiang.doctor.MyApplication;
import com.maqiang.doctor.R;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;
import com.maqiang.doctor.bean.User;

/**
 * Created by maqiang on 2017/2/26.
 */

public class LoginAty extends BaseActivity{
    private static final String TAG = "LoginAty";
    private TextInputLayout mUsername,mPassword;
    private Button mConfirm;
    private TextView mNewUser,mForgetPwd;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.aty_login);
        MyApplication.getInstance().addActivity(this);
        initView();
        initOnClickListener(mConfirm,mNewUser,mForgetPwd);
    }

    private void initView() {
        mUsername = getViewById(R.id.login_username);
        mPassword = getViewById(R.id.login_password);
        mConfirm = getViewById(R.id.login_button);
        mNewUser = getViewById(R.id.login_new_user);
        mForgetPwd = getViewById(R.id.login_forget);
        mUsername.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override public void afterTextChanged(Editable s) {
                if (mPassword.getEditText().getText().length()>0&&s.length()>0){
                    mConfirm.setEnabled(true);
                    mConfirm.setBackgroundResource(R.color.appbar_bg);
                }else {
                    mConfirm.setEnabled(false);
                    mConfirm.setBackgroundResource(R.color.bt_default);
                }
            }
        });

       mPassword.getEditText().addTextChangedListener(new TextWatcher() {
           @Override
           public void beforeTextChanged(CharSequence s, int start, int count, int after) {

           }

           @Override public void onTextChanged(CharSequence s, int start, int before, int count) {

           }

           @Override public void afterTextChanged(Editable s) {
               if (mUsername.getEditText().getText().length()>0&&s.length()>0){
                   mConfirm.setEnabled(true);
                   mConfirm.setBackgroundResource(R.color.appbar_bg);
               }else {
                   mConfirm.setEnabled(false);
                   mConfirm.setBackgroundResource(R.color.bt_default);
               }
           }
       });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.login_button:
                String username = mUsername.getEditText().getText().toString().trim();
                final String password = mPassword.getEditText().getText().toString().trim();
                User person = new User();
                person.setUsername(username);
                person.setPassword(password);
                person.login(new SaveListener<BmobUser>() {
                    @Override
                    public void done(BmobUser person, BmobException e) {
                        if (e == null) {
                            Toast.makeText(LoginAty.this, "登录成功", Toast.LENGTH_SHORT).show();
                            MyApplication.getInstance().savePassword(password);
                            Intent intent = new Intent(LoginAty.this,HomeActivity.class);
                            startActivity(intent);
                        }else {
                            Toast.makeText(LoginAty.this, "登录失败,请检查账号密码是否正确", Toast.LENGTH_SHORT).show();
                            Log.d(TAG, e.toString());
                        }
                    }
                });
                break;
            case R.id.login_new_user:
                Intent intent = new Intent(LoginAty.this,RegisterAty.class)
                    .putExtra(RegisterAty.USER,new User()).putExtra(RegisterAty.CLICK,false);
                startActivity(intent);
                break;
            case R.id.login_forget:
                Intent intent1 = new Intent(LoginAty.this,FoundPwdAty.class);
                intent1.putExtra("isNext",false);
                startActivity(intent1);
                break;
        }
    }
}
