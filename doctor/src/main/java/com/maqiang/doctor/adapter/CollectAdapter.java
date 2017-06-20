package com.maqiang.doctor.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.maqiang.doctor.R;
import com.maqiang.doctor.bean.Collect;
import com.maqiang.doctor.bean.InfoItem;
import com.squareup.picasso.Picasso;
import java.util.List;

/**
 * Created by maqiang on 2017/5/4.
 *
 * Function:
 */

public class CollectAdapter extends BaseRecyclerAdapter<InfoItem,CollectAdapter.HodlerItem> {

  public CollectAdapter(Context ctx, List<InfoItem> list, RecyclerView recyclerView) {
    super(ctx, list, recyclerView);
  }

  @Override public void bindData(HodlerItem holder, int position, InfoItem item) {
    Picasso.with(mContext).load(item.getImg()).into(holder.mImageView);
    holder.mTitle.setText(item.getTitle());
    holder.mContent.setText(item.getDescription());
  }

  @Override public HodlerItem onCreateViewHolder(ViewGroup parent, int viewType) {
    View view = mInflater.inflate(R.layout.collect_artical_item,parent,false);
    view.setOnClickListener(this);
    return new HodlerItem(view);
  }

  public static class HodlerItem extends RecyclerView.ViewHolder{

    @BindView(R.id.iv_collect) ImageView mImageView;
    @BindView(R.id.tv_collect_title) TextView mTitle;
    @BindView(R.id.tv_collect_content) TextView mContent;

    public HodlerItem(View itemView) {
      super(itemView);
      ButterKnife.bind(this, itemView);
    }
  }
}
