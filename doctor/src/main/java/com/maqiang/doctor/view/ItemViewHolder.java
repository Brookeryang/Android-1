package com.maqiang.doctor.view;

import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by Nirui on 17/3/23.
 */

public abstract class ItemViewHolder extends RecyclerView.ViewHolder {

  public ItemViewHolder(View itemView) {
    super(itemView);
  }

  public abstract void setSelected(boolean selected);
}
