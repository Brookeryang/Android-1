package com.maqiang.doctor.adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.google.android.flexbox.FlexboxLayout;
import com.maqiang.doctor.R;
import com.maqiang.doctor.bean.Avoid;
import java.util.List;

/**
 * Created by maqiang on 2017/5/6.
 *
 * Function:
 */

public class AvoidAdapter extends BaseRecyclerAdapter<Avoid, AvoidAdapter.ItemHolder> {
  public AvoidAdapter(Context ctx, List<Avoid> list, RecyclerView recyclerView) {
    super(ctx, list, recyclerView);
  }

  @Override public void bindData(ItemHolder holder, int position, Avoid item) {
    holder.mAvoid.setText(item.getItem());
    String[] strings = item.getTips().split(",");
    for (int i = 0; i < strings.length; i++) {
      TextView text = new TextView(mContext);
      text.setText(strings[i]);
      text.setTextColor(ContextCompat.getColor(mContext, R.color.check_textcolor));
      text.setTextSize(15);
      text.setPadding(17, 8, 0, 0);
      text.setGravity(Gravity.CENTER);
      holder.mFlexboxLayout.addView(text);
    }
  }

  @Override public ItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    View view = mInflater.inflate(R.layout.avoid_food_item,parent,false);
    view.setOnLongClickListener(this);
    return new ItemHolder(view);
  }

  static class ItemHolder extends RecyclerView.ViewHolder {
    @BindView(R.id.tv_avoid) TextView mAvoid;
    @BindView(R.id.flexbox) FlexboxLayout mFlexboxLayout;

    public ItemHolder(View itemView) {
      super(itemView);
      ButterKnife.bind(this, itemView);
    }
  }
}
