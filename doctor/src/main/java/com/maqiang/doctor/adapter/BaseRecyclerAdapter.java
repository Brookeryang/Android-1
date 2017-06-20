package com.maqiang.doctor.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import com.maqiang.doctor.R;
import com.maqiang.doctor.view.ItemViewHolder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by maqiang on 2017/4/21.
 *
 * 对RecycleView的适配器进行封装
 */

public abstract class BaseRecyclerAdapter<T, E extends RecyclerView.ViewHolder>
    extends RecyclerView.Adapter<E> implements View.OnClickListener,View.OnTouchListener,View.OnLongClickListener {

  private static final String TAG = "BaseRecyclerAdapter";
  //数据集
  protected List<T> mData;
  //为了方便子类对资源文件的调取
  protected final Context mContext;
  //视图获取
  protected LayoutInflater mInflater;
  //获取持有改适配器的RecyclerView
  protected RecyclerView mRecyclerView;

  public void setIndex(int index) {
    this.index = index;
  }

  //点击监听事件
  private OnItemClickListener mOnItemClickListener;
  //触摸事件
  private OnItemtTouchListener mOnItemtTouchListener;
  //记录选中状态
  public OnItemtTouchListener getOnItemtTouchListener() {
    return mOnItemtTouchListener;
  }
  //长按事件
  public OnItemLongClickListener mOnItemLongClickListener;
  protected volatile int index = 6;
  private View mLast = null;

  public OnItemLongClickListener getOnItemLongClickListener() {
    return mOnItemLongClickListener;
  }

  public void setOnItemLongClickListener(OnItemLongClickListener onItemLongClickListener) {
    mOnItemLongClickListener = onItemLongClickListener;
  }

  public void setOnItemtTouchListener(OnItemtTouchListener onItemtTouchListener) {
    mOnItemtTouchListener = onItemtTouchListener;
  }

  protected List<Integer> mStatus;

  public List<Integer> getStatus() {
    return mStatus;
  }

  public BaseRecyclerAdapter(Context ctx, List<T> list, RecyclerView recyclerView) {
    mData = (list != null) ? list : new ArrayList<T>();
    mContext = ctx;
    mInflater = LayoutInflater.from(ctx);
    mRecyclerView = recyclerView;
    mStatus = new ArrayList<Integer>();
  }

  public OnItemClickListener getOnItemClickListener() {
    return mOnItemClickListener;
  }


  /**
   * 点击事件
   * @param onItemClickListener
   */
  public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
    mOnItemClickListener = onItemClickListener;
  }

  /**
   * 绑定ViewHolder
   * @param holder
   * @param position
   */
  @Override public void onBindViewHolder(E holder, int position) {
    bindData(holder, position, mData.get(position));
  }

  /**
   * 添加定义的数据到对应的位置
   * @param pos
   * @param item
   */
  public void add(int pos, T item) {
    mData.add(pos, item);
    notifyItemInserted(pos);
    notifyAdapterItemRangChanged(pos);
  }

  /**
   * 删除对应的数据集
   * @param pos
   */
  public void delete(int pos) {
    Log.d(TAG, "delete: "+pos);
    if (pos>=mData.size()){
      mData.remove(mData.size() - 1);
    }else {
      mData.remove(pos);
    }
    if(mStatus.contains(pos)) {
      mStatus.remove(Integer.valueOf(pos));
      int i = 0;
      for (int value : mStatus) {
        if (value>pos){
          mStatus.set(i,value-1);
        }
        i++;
      }
    }
    notifyItemRemoved(pos);
    notifyAdapterItemRangChanged(pos);
  }

  /**
   * 通知数据范围改变
   * @param pos
   */
  public void notifyAdapterItemRangChanged(int pos){
    if (pos != mData.size() - 1) {
      notifyItemRangeChanged(pos, mData.size() - pos);
    }
  }


  @Override public int getItemCount() {
    return mData != null ? mData.size() : 0;
  }

  public void setData(List<T> data) {
    this.mData = data;
    mStatus.clear();
    notifyDataSetChanged();
  }

  @Override public void onClick(View v) {
    E holder = (E) mRecyclerView.findContainingViewHolder(v);
    int position = holder.getAdapterPosition();
    if (mLast != null){
      mLast.setBackgroundResource(R.color.bg);
    }
    mOnItemClickListener.onItemClick(holder.itemView, position, mData.get(position));
    holder.itemView.setBackgroundResource(R.color.white);
    mLast = holder.itemView;
  }

  @Override public boolean onTouch(View v, MotionEvent event) {
    E holder = (E) mRecyclerView.findContainingViewHolder(v);
    int position = holder.getAdapterPosition();
    mOnItemtTouchListener.onItemTouch(holder.itemView, position, mData.get(position),event);
    return true;
  }

  abstract public void bindData(E holder, int position, T item);

  public interface OnItemClickListener<T> {
    public void onItemClick(View itemView, int position, T item);
  }

  public interface OnItemtTouchListener<T>{
    public void onItemTouch(View itemView,int position,T item,MotionEvent event);
  }

  public interface OnItemLongClickListener<T> {
    public void onItemLongClick(View itemView, int position, T item);
  }

  /**
   * 选择项的记录与删除
   * @param position
   */
  public void setInitSelectedItem(int position) {
    if(!mStatus.contains(position)) {
      mStatus.add(position);
    }else {
      mStatus.remove(Integer.valueOf(position));
    }
  }

  @Override public boolean onLongClick(View v) {
    E holder = (E) mRecyclerView.findContainingViewHolder(v);
    int position = holder.getAdapterPosition();
    mOnItemLongClickListener.onItemLongClick(holder.itemView, position, mData.get(position));
    return true;
  }
}
