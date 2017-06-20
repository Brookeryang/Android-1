package com.maqiang.doctor.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import java.util.ArrayList;
import java.util.List;

//通用adapter类，属抽象类，需要使用时继承使用
public abstract class CommonAdapter<T> extends BaseAdapter {
	protected Context mContext;
	protected List<T> datas;// 用户数据
//	 protected LayoutInflater mInflater;
	protected int mLayoutId;
	//标记控件的点击或选择效果
	protected List<Integer> mStatus;

	// 构造方法
	public CommonAdapter(Context context, List<T> data, int layoutid) {
		this.mContext = context;
		this.datas = data;
		this.mLayoutId = layoutid;
		mStatus = new ArrayList<Integer>();
		// mInflater =LayoutInflater.from(mContext);
	}
	// 获取数据量
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return datas.size();
	}

	// 获取对应ID项对应的Item
	@Override
	public T getItem(int position) {
		// TODO Auto-generated method stub
		return datas.get(position);
	}

	// 获取对应项Item的ID
	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public boolean hasStableIds() {
		return true;
	}

	// 将getView写为抽象方法公布出去，继承后由子类实现
	@Override
	public abstract View getView(int position, View convertView,
			ViewGroup parent);

	public void setSelectItem(int postion) {
		if (mStatus.contains(postion)){
			mStatus.remove(Integer.valueOf(postion));
		}else {
			mStatus.add(postion);
		}
	}

	public List<Integer> getStatus() {
		return mStatus;
	}
}
