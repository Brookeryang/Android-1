package com.maqiang.doctor.adapter;

import android.content.Context;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ViewHolder {
  //采用sparsearray键值对模式保存view和对应的id
  private SparseArray<View> mViews;
  @SuppressWarnings("unused") private int position;
  private View mConvertView;// 整体布局管理器

  // 构造方法
  public ViewHolder(Context context, ViewGroup parent, int layoutId, int position) {
    mViews = new SparseArray<View>();
    this.position = position;
    this.mConvertView = LayoutInflater.from(context).inflate(layoutId, parent, false);
    mConvertView.setTag(this);
  }

  //入口方法，旨在避免多次新建Viewholder，第一次调用即可，其余时候直接使用getTag()方法就可以了
  public static ViewHolder get(Context context, ViewGroup parent, View convertview, int layoutId,
      int position) {
    if (convertview == null) {
      return new ViewHolder(context, parent, layoutId, position);
    } else {
      ViewHolder holder = (ViewHolder) convertview.getTag();
      holder.position = position;
      return holder;
    }
  }

  //通过ID获取组件,因为存在不同的组件类型，所以需要使用泛型
  @SuppressWarnings("unchecked") public <T extends View> T getView(int viewId) {
    View view = mViews.get(viewId);//通过viewid这个键找到对应的view值
    //若是第一次使用
    if (view == null) {
      //在布局管理器中findbyId
      view = mConvertView.findViewById(viewId);
      //添加进SparseArray中
      mViews.put(viewId, view);
    }
    return (T) view;
  }

  //获得布局管理器
  public View getConvertView() {
    return mConvertView;
  }

}
