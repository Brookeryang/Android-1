package com.maqiang.doctor.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import cn.bmob.v3.BmobUser;
import com.maqiang.doctor.R;
import com.maqiang.doctor.bean.Question;
import com.maqiang.doctor.bean.User;
import com.maqiang.doctor.utils.BmobManager;
import com.maqiang.doctor.utils.ConstantUtil;

/**
 * Created by maqiang on 2017/4/17.
 *
 * F
 */

public class FeedBackFragment extends Fragment {

  private View mView;
  private TextView mTips;
  private EditText mEditText;
  private Button mButton;
  private Question mQuestion;
  private User mUser;

  @Nullable @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    mView = inflater.inflate(R.layout.feebback,container,false);
    initView();
    return mView;
  }

  private void initView() {
    mTips = (TextView) mView.findViewById(R.id.tv_number_tips);
    mEditText = (EditText) mView.findViewById(R.id.et_question);
    mButton = (Button) mView.findViewById(R.id.bt_submit);
    mUser = BmobUser.getCurrentUser(User.class);
    String tips = getResources().getString(R.string.record_question);
    mTips.setText(String.format(tips,0));

    mEditText.addTextChangedListener(new TextWatcher() {
      @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {

      }

      @Override public void onTextChanged(CharSequence s, int start, int before, int count) {

      }

      @Override public void afterTextChanged(Editable s) {
        if (s.length()<140) {
          if (s.length() == 0){
            mButton.setBackgroundColor(ContextCompat.getColor(getActivity(),R.color.unclickable));
            mButton.setEnabled(false);
          }else {
            mButton.setEnabled(true);
            mButton.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.help));
            String tips = getResources().getString(R.string.record_question);
            mTips.setText(String.format(tips, s.length()));
            mTips.setTextColor(ContextCompat.getColor(getActivity(), R.color.unclickable));
          }
        }else {
          String tips = getResources().getString(R.string.record_question);
          mTips.setText(String.format(tips, 140));
          mTips.setTextColor(ContextCompat.getColor(getActivity(),R.color.help));
        }
      }
    });

    mButton.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
         String text = mEditText.getText().toString();
        mQuestion = new Question(ConstantUtil.QUESTION);
        if (mUser != null&&!TextUtils.isEmpty(mUser.getMobilePhoneNumber())) {
          mQuestion.setPhoneNumber(mUser.getMobilePhoneNumber());
        }
        mQuestion.setContent(text);
        BmobManager.saveData(mQuestion);
        mEditText.setText("");
      }
    });
  }
}
