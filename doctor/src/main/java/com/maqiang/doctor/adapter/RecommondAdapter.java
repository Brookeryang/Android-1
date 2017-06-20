package com.maqiang.doctor.adapter;

import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.maqiang.doctor.R;
import com.maqiang.doctor.bean.InfoItem;
import com.maqiang.doctor.view.ItemViewHolder;
import com.squareup.picasso.Picasso;
import java.util.List;
import retrofit2.http.POST;

/**
 * Created by maqiang on 2017/5/2.
 *
 * Function: 为你推荐的适配器
 */

public class RecommondAdapter extends BaseRecyclerAdapter<InfoItem, RecommondAdapter.ViewHolder> {

  private static final String TAG = "RecommondAdapter";

  public RecommondAdapter(Context ctx, List<InfoItem> list, RecyclerView recyclerView) {
    super(ctx, list, recyclerView);
  }

  @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN) @Override
  public void bindData(final ViewHolder holder, final int position, InfoItem item) {
    Picasso.with(mContext).load(item.getImg()).into(holder.mPic);
    holder.mSubject.setText(mContext.getResources().getStringArray(R.array.info)[index-1]);
    holder.mTitle.setText(item.getTitle());

    //设置TAG
    //holder.mPlus.setTag(position);
    //final int pos = (int) holder.mPlus.getTag();
    //updateButton(holder, pos);
    //holder.mPlus.setOnClickListener(new View.OnClickListener() {
    //  @Override public void onClick(View v) {
    //    setInitSelectedItem(pos);
    //    updateButton(holder,pos);
    //  }
    //});
    holder.mDele.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        delete(position);
      }
    });
  }

  //@RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN) public void updateButton(ViewHolder holder, int pos) {
  //  if (!mStatus.contains(pos)){
  //    holder.mPlus.setBackground(ContextCompat.getDrawable(mContext, R.drawable.recycle_item_default));
  //    holder.mPlus.setImageResource(R.drawable.addfood1);
  //  } else {
  //    holder.mPlus.setBackground(ContextCompat.getDrawable(mContext, R.drawable.recycle_item_clicked));
  //    holder.mPlus.setImageResource(R.drawable.follow);
  //  }
  //}

  @Override public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    View view = mInflater.inflate(R.layout.recycle_item, parent, false);
    view.setOnClickListener(this);
    return new ViewHolder(view);
  }

  public static class ViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.pic) ImageView mPic;
    @BindView(R.id.dele) ImageView mDele;
    @BindView(R.id.plus) ImageView mPlus;
    @BindView(R.id.title) TextView mTitle;
    @BindView(R.id.subject) TextView mSubject;

    public ViewHolder(View itemView) {
      super(itemView);
      ButterKnife.bind(this, itemView);
    }
  }
}
