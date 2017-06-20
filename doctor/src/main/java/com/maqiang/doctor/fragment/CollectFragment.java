package com.maqiang.doctor.fragment;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import cn.bmob.v3.BmobUser;
import com.maqiang.doctor.MyApplication;
import com.maqiang.doctor.R;
import com.maqiang.doctor.adapter.BaseRecyclerAdapter;
import com.maqiang.doctor.adapter.CollectAdapter;
import com.maqiang.doctor.activity.InfoDetailAty;
import com.maqiang.doctor.bean.InfoItem;
import com.maqiang.doctor.bean.User;
import com.maqiang.doctor.utils.BmobManager;

import java.util.ArrayList;
import java.util.List;

import static android.app.Activity.RESULT_OK;

/**
 * Created by maqiang on 2017/4/17.
 *
 * Function:收藏
 */

public class CollectFragment extends Fragment implements BaseRecyclerAdapter.OnItemClickListener{

  private View mView;
  private RecyclerView mRecyclerView;
  private List<InfoItem> mDatas = new ArrayList<InfoItem>();
  private CollectAdapter mAdapter;
  public static final int RQUEST_REFRESH = 1;
  private User mUser;
  @BindView(R.id.rl_none) RelativeLayout mNone;
  @BindView(R.id.tv_none) TextView mTextView;

  private android.os.Handler handler = new android.os.Handler() {
    @Override public void handleMessage(Message msg) {
      switch (msg.what) {
        case BmobManager.COLLECT:
          mDatas.clear();
          if ( (List<InfoItem>) msg.obj == null ||  ((List<InfoItem>) msg.obj).size()==0){
            mNone.setVisibility(View.VISIBLE);
            mTextView.setText(MyApplication.getInstance().getResources().getString(R.string.collect_none));
          }else {
            mNone.setVisibility(View.GONE);
            for (InfoItem photo : (List<InfoItem>) msg.obj) {
              mDatas.add(photo);
            }
          }

          mAdapter.notifyDataSetChanged();
          break;
      }
    }
  };

  @Nullable @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    mView = inflater.inflate(R.layout.pictures, container, false);
    ButterKnife.bind(this,mView);
    initView();
    mUser = BmobUser.getCurrentUser(User.class);
    if (mUser != null&&!TextUtils.isEmpty(mUser.getMobilePhoneNumber())) {
      BmobManager.getCollect(mUser.getMobilePhoneNumber(),handler);
    }
    return mView;
  }

  private void initView() {
    mRecyclerView = (RecyclerView) mView.findViewById(R.id.recycler);
    mAdapter = new CollectAdapter(getActivity(), mDatas, mRecyclerView);
    mAdapter.setOnItemClickListener(this);
    LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
    layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
    mRecyclerView.setLayoutManager(layoutManager);
    mRecyclerView.setAdapter(mAdapter);
    mRecyclerView.setNestedScrollingEnabled(false);
    mRecyclerView.addItemDecoration(new DividerItemDecoration(getActivity(),DividerItemDecoration.VERTICAL));
  }

  @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP) @Override public void onItemClick(View itemView, int position, Object item) {
    Intent intent = new Intent(getActivity(), InfoDetailAty.class);
    intent.putExtra("info", mDatas.get(position));
    intent.putExtra("url", mDatas.get(position).getImg());
    intent.putExtra("flag","1");

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
      ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(getActivity(),
          new Pair<View, String>(itemView.findViewById(R.id.iv_collect), "image"));
      this.startActivityForResult(intent,RQUEST_REFRESH, options.toBundle());
    }else {
      // TODO: 2017/5/14 兼容5.0以下
      this.startActivityForResult(intent,RQUEST_REFRESH);
    }
  }

  @Override public void onActivityResult(int requestCode, int resultCode, Intent data) {
    if (resultCode == RESULT_OK){
      if (mUser != null&&!TextUtils.isEmpty(mUser.getMobilePhoneNumber())) {
        BmobManager.getCollect(mUser.getMobilePhoneNumber(),handler);
      }
    }
    super.onActivityResult(requestCode, resultCode, data);
  }

}
