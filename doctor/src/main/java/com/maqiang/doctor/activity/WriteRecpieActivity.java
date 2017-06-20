package com.maqiang.doctor.activity;

import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import butterknife.BindArray;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.bmob.v3.BmobUser;
import com.google.android.flexbox.FlexboxLayout;
import com.maqiang.doctor.MyApplication;
import com.maqiang.doctor.R;
import com.maqiang.doctor.bean.Advice;
import com.maqiang.doctor.bean.Recipe;
import com.maqiang.doctor.bean.User;
import com.maqiang.doctor.utils.BmobManager;
import com.maqiang.doctor.utils.ConstantUtil;
import com.maqiang.doctor.utils.ToastUtils;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by maqiang on 2017/5/3.
 *
 * Function: 记录食谱和医嘱、备注
 */

public class WriteRecpieActivity extends BaseActivity {

  @BindView(R.id.toolbar) Toolbar mToolbar;
  @BindView(R.id.tv_breakfast) TextView mBreakfast;
  @BindView(R.id.tv_lunch) TextView mLunch;
  @BindView(R.id.tv_dinner) TextView mDinner;
  @BindView(R.id.tv_add) TextView mAdd;
  @BindView(R.id.flexbox) FlexboxLayout mFlex;
  @BindView(R.id.et_recipe) EditText mEtRecipe;
  @BindView(R.id.et_advice) EditText mEtRemark;

  @BindArray(R.array.check_item) String[] res;

  int mRecordIndex = 0;
  List<Integer> mIntegers = new ArrayList<Integer>();
  Recipe mRecipe = new Recipe(ConstantUtil.RECIPE);
  Advice mAdvice = new Advice(ConstantUtil.DOCTOR_ADVICE);
  User mUser;

  private Handler mHandler = new Handler() {
    @Override public void handleMessage(Message msg) {
      switch (msg.what) {
        case BmobManager.RECIPE_WHAT:
          List<Recipe> list = (List<Recipe>) msg.obj;
          if (list != null && list.size() > 0) {
            mRecipe = list.get(0);
            if (!TextUtils.isEmpty(mRecipe.getBreakfast())){
              mEtRecipe.setText(mRecipe.getBreakfast());
            }
          }
          break;
        case BmobManager.ADVICE_WHAT:
          List<Advice> list1 = (List<Advice>) msg.obj;
          if (list1 != null && list1.size() > 0) {
            mAdvice = list1.get(0);
            if (!TextUtils.isEmpty(mAdvice.getCheck())) {
              boolean[] mRecord = new boolean[res.length];
              String[] strs = mAdvice.getCheck().split(",");
              for (int i = 0; i < strs.length; i++) {
                for (int j = i; j < mRecord.length; j++) {
                  if (strs[i].equals(res[j])){
                    mRecord[j] = true;
                  }
                }
              }
              updateCheckItem(mRecord);
            }
            if (!TextUtils.isEmpty(mAdvice.getContent())) {
              mEtRemark.setText(mAdvice.getContent());
            }
          }
          break;
      }
    }
  };

  @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.aty_recipe);
    MyApplication.getInstance().addActivity(this);
    ButterKnife.bind(this);
    initView();
    initRecipeAndRemark();
  }

  private void initView() {
    initToolBar(mToolbar, R.drawable.ic_arrow_back_black_24dp);
    mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        WriteRecpieActivity.this.finish();
      }
    });

  }

  /**
   * 从服务器抓取信息
   */
  private void initRecipeAndRemark() {
    mUser = BmobUser.getCurrentUser(User.class);
    if (mUser != null && !TextUtils.isEmpty(mUser.getMobilePhoneNumber())) {
      BmobManager.getRecipe(mUser.getMobilePhoneNumber(), mHandler);
      BmobManager.getAdvice(mUser.getMobilePhoneNumber(), mHandler);
    }
  }

  @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
  @OnClick({ R.id.tv_breakfast, R.id.tv_lunch, R.id.tv_dinner, R.id.tv_add, R.id.add_check })
  public void selectRecipe(View view) {
    switch (view.getId()) {
      case R.id.tv_breakfast:
        mRecordIndex = 0;
        resetSelectMenu();
        mBreakfast.setBackgroundResource(R.drawable.oval);
        mBreakfast.setTextColor(ContextCompat.getColor(this, R.color.white));
        if (!TextUtils.isEmpty(mRecipe.getBreakfast())){
          mEtRecipe.setText(mRecipe.getBreakfast());
        }else {
          mEtRecipe.setText(null);
        }
        break;
      case R.id.tv_lunch:
        mRecordIndex = 1;
        resetSelectMenu();
        mLunch.setBackgroundResource(R.drawable.oval);
        mLunch.setTextColor(ContextCompat.getColor(this, R.color.white));
        if (!TextUtils.isEmpty(mRecipe.getAfternoon())){
          mEtRecipe.setText(mRecipe.getAfternoon());
        }else {
          mEtRecipe.setText(null);
        }
        break;
      case R.id.tv_dinner:
        mRecordIndex = 2;
        resetSelectMenu();
        mDinner.setBackgroundResource(R.drawable.oval);
        mDinner.setTextColor(ContextCompat.getColor(this, R.color.white));
        if (!TextUtils.isEmpty(mRecipe.getDinner())){
          mEtRecipe.setText(mRecipe.getDinner());
        }else {
          mEtRecipe.setText(null);
        }
        break;
      case R.id.tv_add:
        mRecordIndex = 3;
        resetSelectMenu();
        mAdd.setBackgroundResource(R.drawable.oval);
        mAdd.setTextColor(ContextCompat.getColor(this, R.color.white));
        if (!TextUtils.isEmpty(mRecipe.getAdd())){
          mEtRecipe.setText(mRecipe.getAdd());
        }else {
          mEtRecipe.setText(null);
        }
        break;
      case R.id.add_check:
        final boolean[] mRecord = new boolean[res.length];
        AlertDialog dialog =
            new AlertDialog.Builder(this).setMultiChoiceItems(R.array.check_item, mRecord,
                new DialogInterface.OnMultiChoiceClickListener() {
                  @Override
                  public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                    mRecord[which] = isChecked;
                  }
                }).setTitle("检查项目").setPositiveButton("确定", new DialogInterface.OnClickListener() {
              @Override public void onClick(DialogInterface dialog, int which) {
                updateCheckItem(mRecord);
              }
            }).setNegativeButton("取消", null).create();
        dialog.show();
        break;
    }
  }

  public void updateCheckItem(boolean[] record) {
    for (int i = 0; i < record.length; i++) {
      if (record[i] && !mIntegers.contains(i)) {
        mIntegers.add(i);
        TextView text = new TextView(this);
        text.setText(res[i]);
        text.setTextColor(ContextCompat.getColor(this, R.color.check_textcolor));
        text.setTextSize(15);
        text.setPadding(17, 8, 0, 0);
        text.setGravity(Gravity.CENTER);
        mFlex.addView(text);
      }
    }
  }

  private void resetSelectMenu() {
    mBreakfast.setBackgroundResource(R.color.white);
    mBreakfast.setTextColor(ContextCompat.getColor(this, R.color.black_textcolor));
    mLunch.setBackgroundResource(R.color.white);
    mLunch.setTextColor(ContextCompat.getColor(this, R.color.black_textcolor));
    mDinner.setBackgroundResource(R.color.white);
    mDinner.setTextColor(ContextCompat.getColor(this, R.color.black_textcolor));
    mAdd.setBackgroundResource(R.color.white);
    mAdd.setTextColor(ContextCompat.getColor(this, R.color.black_textcolor));
  }

  @Override public boolean onCreateOptionsMenu(Menu menu) {
    getMenuInflater().inflate(R.menu.recepie_menu, menu);
    return super.onCreateOptionsMenu(menu);
  }

  @Override public boolean onOptionsItemSelected(MenuItem item) {
    int id = item.getItemId();
    if (id == R.id.done) {
      String recpie = mEtRecipe.getText().toString();
      if (TextUtils.isEmpty(recpie)) {
       // ToastUtils.show(R.string.recipe_none);
      } else {
        mRecipe.setDate(BmobManager.getToday());
        if (mUser != null && mUser.getMobilePhoneNumber() != null) {
          mRecipe.setPhoneNumber(mUser.getMobilePhoneNumber());
        }
        if (mRecordIndex == 0) {
          mRecipe.setBreakfast(recpie);
        } else if (mRecordIndex == 1) {
          mRecipe.setAfternoon(recpie);
        } else if (mRecordIndex == 2) {
          mRecipe.setDinner(recpie);
        } else if (mRecordIndex == 3) {
          mRecipe.setAdd(recpie);
        }
        BmobManager.updateRecipeData(mRecipe, mRecordIndex);
      }
      String advice = mEtRemark.getText().toString();
      if (TextUtils.isEmpty(advice)) {
       // ToastUtils.show(R.string.advice_none);
      } else {
        mAdvice.setDate(BmobManager.getToday());
        mAdvice.setContent(advice);
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < mIntegers.size(); i++)
          if (i == 0) {
            builder.append(res[mIntegers.get(i)]);
          } else {
            builder.append("," + res[mIntegers.get(i)]);
          }
        mAdvice.setCheck(builder.toString());
        if (mUser != null && mUser.getMobilePhoneNumber() != null) {
          mAdvice.setPhoneNumber(mUser.getMobilePhoneNumber());
        }
        BmobManager.updateRemark(mAdvice);
      }
      setResult(RESULT_OK);
      this.finish();
    }
    return super.onOptionsItemSelected(item);
  }
}
