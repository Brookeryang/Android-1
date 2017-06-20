package com.maqiang.doctor.fragment;

import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import butterknife.BindArray;
import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import cn.bmob.v3.BmobUser;
import com.maqiang.doctor.R;
import com.maqiang.doctor.adapter.AvoidAdapter;
import com.maqiang.doctor.adapter.BaseRecyclerAdapter;
import com.maqiang.doctor.bean.Avoid;
import com.maqiang.doctor.bean.User;
import com.maqiang.doctor.utils.BmobManager;
import com.maqiang.doctor.utils.ConstantUtil;
import com.maqiang.doctor.utils.ToastUtils;
import com.miguelcatalan.materialsearchview.MaterialSearchView;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by maqiang on 2017/5/5.
 *
 * Function: 搜索页面
 */

public class SearchFragment extends Fragment implements
    BaseRecyclerAdapter.OnItemLongClickListener{

  @BindView(R.id.search_view) MaterialSearchView mSearchView;
  @BindView(R.id.appbar) AppBarLayout mAppBarLayout;
  @BindView(R.id.collapsing) CollapsingToolbarLayout mToolbarLayout;
  @BindView(R.id.toolbar) Toolbar mToolbar;
  @BindView(R.id.select_layout) LinearLayout mLayout;
  @BindView(R.id.recycler) RecyclerView mRecyclerView;
  //@BindView(R.id.iv_arrow) ImageView mArrow;
  @BindView(R.id.number) TextView mNumber;
  @BindView(R.id.rl_none) RelativeLayout mNone;
  @BindView(R.id.tv_none) TextView mTextView;
  @BindString(R.string.all) String mRecordSelect;
  @BindArray(R.array.advoid) String[] strs;

  Map<String,Integer> map = ConstantUtil.build();
  AvoidAdapter mAdapter;
  List<Avoid> mAvoids = new ArrayList<Avoid>();
  Avoid mAvoid = new Avoid(ConstantUtil.AVOID);
  User mUser;

  private Handler mHandler = new Handler() {
    @Override public void handleMessage(Message msg) {
      switch (msg.what) {
        case BmobManager.AVOID:
          if ((List<Avoid>) msg.obj == null ||  ((List<Avoid>) msg.obj).size() ==0){
            mNone.setVisibility(View.VISIBLE);
            mTextView.setText(getResources().getString(R.string.avoid_none));
          }else {
            mNumber.setText(((List<Avoid>) msg.obj).size()+"");
            mNone.setVisibility(View.GONE);
            mAvoids.clear();
            for (Avoid avoid:(List<Avoid>) msg.obj) {
              mAvoids.add(avoid);
            }
          }
          mAdapter.setData(mAvoids);
          break;
        case BmobManager.AVOID_DELETE:
          if (msg.arg1 == 1){
            mAvoids.remove(msg.arg2);
            mAdapter.notifyItemRemoved(msg.arg2);
          }
          break;
        case BmobManager.AVOID_ADD:
          if (msg.arg1 == 1) {
            if (mNone.getVisibility() == View.VISIBLE){
              mNone.setVisibility(View.GONE);
            }
            mAvoids.add((Avoid) msg.obj);
            mAdapter.notifyItemChanged(mAvoids.size());
          }
          break;
      }
      super.handleMessage(msg);
    }
  };

  @Nullable @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_search, container, false);
    ButterKnife.bind(this, view);
    initViewAndListener();
    mUser = BmobUser.getCurrentUser(User.class);
    if (mUser != null&&!TextUtils.isEmpty(mUser.getMobilePhoneNumber())) {
      BmobManager.getAvoid(mUser.getMobilePhoneNumber(), mHandler);
    }
    return view;
  }

  private void initViewAndListener() {
    setHasOptionsMenu(true);
    mToolbar.setTitle("");
    ((AppCompatActivity) getActivity()).setSupportActionBar(mToolbar);
    mAppBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
      @Override public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
        if (Math.abs(verticalOffset) >= appBarLayout.getTotalScrollRange() * 2 / 3) {
          //mToolbar.setNavigationIcon(R.drawable.ic_filter_list_black_24dp);
          //mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
          //  @Override public void onClick(View v) {
          //    click(v);
          //  }
          //});
          mToolbarLayout.setTitle("忌口");
          Menu menu = mToolbar.getMenu();
          mSearchView.closeSearch();
          menu.findItem(R.id.menu_add).setIcon(R.drawable.ic_add_black_24dp);
          menu.findItem(R.id.menu_search).setVisible(false);
        } else {
          if (!TextUtils.isEmpty(mToolbarLayout.getTitle())) {
            mToolbar.setNavigationIcon(null);
            mToolbarLayout.setTitle("");
            Menu menu = mToolbar.getMenu();
            menu.findItem(R.id.menu_add).setIcon(R.drawable.ic_add_white_24dp);
            menu.findItem(R.id.menu_search).setVisible(true);
            menu.findItem(R.id.menu_search).setIcon(R.drawable.ic_search_white_24dp);
          }
        }
      }
    });

    mSearchView.setOnSearchViewListener(new MaterialSearchView.SearchViewListener() {
      @Override public void onSearchViewShown() {
        mToolbarLayout.setTitle("");
      }

      @Override public void onSearchViewClosed() {
        mToolbarLayout.setTitle("test");
      }
    });

    mSearchView.setOnQueryTextListener(new MaterialSearchView.OnQueryTextListener() {
      @Override public boolean onQueryTextSubmit(String query) {
        if (map.containsKey(query)){
          AlertDialog dialog = new AlertDialog.Builder(getActivity())
              .setMessage(query+"每100g的含糖量为:"+String.valueOf(map.get(query))+"%")
              .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                @Override public void onClick(DialogInterface dialog, int which) {
                  dialog.cancel();
                }
              })
              .create();
          dialog.show();
        }else {
          AlertDialog dialog = new AlertDialog.Builder(getActivity())
              .setMessage("暂无数据")
              .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                @Override public void onClick(DialogInterface dialog, int which) {
                  dialog.cancel();
                }
              })
              .create();
          dialog.show();
        }
        mSearchView.closeSearch();
        return false;
      }

      @Override public boolean onQueryTextChange(String newText) {
        return false;
      }
    });

    mToolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
      @Override public boolean onMenuItemClick(MenuItem item) {
        if (item.getItemId() == R.id.menu_search) {
          mToolbarLayout.setTitle(" ");
          mToolbar.setTitle(" ");
          mSearchView.showSearch();
          return true;
        }else {
          if (item.getItemId() == R.id.menu_add) {
            final View view = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_edit,null);
            final AlertDialog ad =
                new AlertDialog.Builder(getActivity()).setView(view)
                    .setNegativeButton("取消", null)
                    .setPositiveButton("下一步", new DialogInterface.OnClickListener() {
                      @Override public void onClick(DialogInterface dialog, int which) {
                        TextInputEditText et = (TextInputEditText) view.findViewById(R.id.edit_avoid);
                        String temp = et.getText().toString();
                        if (!TextUtils.isEmpty(temp)) {
                          mAvoid.setItem(temp);
                          final boolean[] mRecord = new boolean[8];
                          AlertDialog alertDialog = new AlertDialog.Builder(getActivity()).setTitle("请选择相应的标签")
                              .setMultiChoiceItems(strs, mRecord, new DialogInterface.OnMultiChoiceClickListener() {
                                @Override public void onClick(DialogInterface dialog, int which,
                                    boolean isChecked) {
                                  mRecord[which] = isChecked;
                                }
                              })
                              .setNegativeButton("取消", null)
                              .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                @Override public void onClick(DialogInterface dialog, int which) {
                                  saveAvoidFood(mRecord);
                                }
                              })
                              .create();
                          alertDialog.show();
                        }else {
                          ToastUtils.show("食物名称不能为空");
                        }
                      }
                    })
                    .create();
            ad.show();
          }
        }
        return false;
      }
    });

    mAdapter = new AvoidAdapter(getActivity(), mAvoids, mRecyclerView);
    mRecyclerView.setAdapter(mAdapter);
    mAdapter.setOnItemLongClickListener(this);
    LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
    layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
    mRecyclerView.setLayoutManager(layoutManager);
    mRecyclerView.setItemAnimator(new DefaultItemAnimator());
  }

  /**
   * 保存忌口
   * @param mRecord
   */
  private void saveAvoidFood(boolean[] mRecord) {
    StringBuilder sb = new StringBuilder();
    for (int i = 0; i < mRecord.length; i++) {
      if (mRecord[i]){
        if (i == 0){
          sb.append(strs[i]);
        }else {
          sb.append(","+strs[i]);
        }
      }
    }
    mAvoid.setTips(sb.toString());
    if (mUser != null&&!TextUtils.isEmpty(mUser.getMobilePhoneNumber())) {
      mAvoid.setPhoneNumber(mUser.getMobilePhoneNumber());
    }
    mAvoid.setDate(BmobManager.getToday());
    BmobManager.saveAvoid(mAvoid,mHandler);

  }

  @Override public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
    menu.clear();
    inflater.inflate(R.menu.select_white, menu);
    super.onCreateOptionsMenu(menu, inflater);
  }

  @Override public boolean onOptionsItemSelected(MenuItem item) {
    return super.onOptionsItemSelected(item);
  }

  @Override public void onItemLongClick(View itemView, final int position, Object item) {
    AlertDialog dialog = new AlertDialog.Builder(getActivity())
        .setMessage("删除忌口")
        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
          @Override public void onClick(DialogInterface dialog, int which) {
            BmobManager.removeAvoid(mAvoids.get(position),position,mHandler);
          }
        })
        .setNegativeButton("取消",null)
        .create();
    dialog.show();
  }
}
