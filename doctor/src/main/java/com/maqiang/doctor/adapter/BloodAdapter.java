package com.maqiang.doctor.adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.maqiang.doctor.R;
import com.maqiang.doctor.bean.Blood;
import com.maqiang.doctor.utils.DensityUtil;
import java.math.BigDecimal;
import java.util.List;

/**
 * Created by maqiang on 2017/5/13.
 *
 * Function:
 */

public class BloodAdapter extends BaseRecyclerAdapter<Blood, BloodAdapter.ItemHolder> {

  private boolean isPre = false;

  public BloodAdapter(Context ctx, List<Blood> list, RecyclerView recyclerView) {
    super(ctx, list, recyclerView);
  }

  @Override public void bindData(ItemHolder holder, int position, Blood item) {
    float value = getBloodValue(item);
    if (isPre) {
      if (pre(value) == 0) {
        holder.mBloodValue.setBackgroundResource(R.color.blood_bg);
        holder.mBloodValue.setTextColor(ContextCompat.getColor(mContext, R.color.text_gray));
        holder.mBloodPic.setImageResource(R.drawable.bloodn);
      } else if (pre(value) == -1) {
        holder.mBloodValue.setBackgroundResource(R.color.l_blood);
        holder.mBloodValue.setTextColor(ContextCompat.getColor(mContext, R.color.white));
        holder.mBloodPic.setImageResource(R.drawable.bloodl);
      } else {
        holder.mBloodValue.setBackgroundResource(R.color.h_blood);
        holder.mBloodValue.setTextColor(ContextCompat.getColor(mContext, R.color.white));
        holder.mBloodPic.setImageResource(R.drawable.bloodh);
      }
    } else {
      if (after(value) == 0) {
        holder.mBloodValue.setBackgroundResource(R.color.blood_bg);
        holder.mBloodValue.setTextColor(ContextCompat.getColor(mContext, R.color.text_gray));
        holder.mBloodPic.setImageResource(R.drawable.bloodn);
      } else if (after(value) == -1) {
        holder.mBloodValue.setBackgroundResource(R.color.l_blood);
        holder.mBloodValue.setTextColor(ContextCompat.getColor(mContext, R.color.white));
        holder.mBloodPic.setImageResource(R.drawable.bloodl);
      } else {
        holder.mBloodValue.setBackgroundResource(R.color.h_blood);
        holder.mBloodValue.setTextColor(ContextCompat.getColor(mContext, R.color.white));
        holder.mBloodPic.setImageResource(R.drawable.bloodh);
      }
    }
    if (mStatus.contains(position)){
      holder.mLayout.setBackgroundResource(R.color.white);
    }else {
      holder.mLayout.setBackgroundResource(R.color.bg);
    }
    holder.mBloodValue.setHeight(DensityUtil.dip2px(mContext, value * 20));
    holder.mBloodValue.setText(String.valueOf(value));
    holder.mDate.setText(getDay(item.getDate()));
  }

  /**
   * 判断餐前的血糖值是否正常
   */
  public static int pre(float value) {
    if (value == 0.0f) {
      return 0;
    }
    Log.d("餐前", "pre: "+value);
    if (value < 3.3) {
      return -1;
    } else if (value > 5.8) {
      return 1;
    } else {
      return 0;
    }
  }

  /**
   * 判断餐后的血糖值是否正常
   */
  public static int after(float value) {
    if (value == 0.0f) {
      return 0;
    }
    if (value < 4.4) {
      return -1;
    } else if (value > 7.8) {
      return 1;
    } else {
      return 0;
    }
  }

  @Override public ItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    View view = mInflater.inflate(R.layout.blood_item, parent, false);
    view.setOnClickListener(this);
    return new ItemHolder(view);
  }

  /**
   * 获取当日日期
   */
  private String getDay(String date) {
    String[] strings = date.split("-");
    if (strings[2].equals("01") || strings[2].equals("1")) {
      return Integer.valueOf(strings[1]) + "月";
    }
    return String.valueOf(Integer.valueOf(strings[2]));
  }

  /**
   * 获得血糖值
   */
  private float getBloodValue(Blood blood) {
    if (pre(blood.getPreBreakfastValue()) == 0) {
      if (after(blood.getAfterBreakfastValue()) == 0) {
        if (pre(blood.getPreLunchValue()) == 0) {
          if (after(blood.getAfterLunchValue()) == 0) {
            if (pre(blood.getPreDinner()) == 0) {
              if (after(blood.getAfterDinner()) == 0) {
                if (after(blood.getSleep()) == 0) {
                  if (blood.getPreBreakfastValue() != 0.0f) {
                    isPre = true;
                    return blood.getPreBreakfastValue();
                  } else if (blood.getAfterBreakfastValue() != 0.0f) {
                    isPre = false;
                    return blood.getAfterBreakfastValue();
                  } else if (blood.getPreLunchValue() != 0.0f) {
                    isPre = true;
                    return blood.getPreLunchValue();
                  } else if (blood.getAfterLunchValue() != 0.0f) {
                    isPre = false;
                    return blood.getAfterLunchValue();
                  } else if (blood.getPreDinner() != 0.0f) {
                    isPre = true;
                    return blood.getPreDinner();
                  } else if (blood.getAfterDinner() != 0.0f) {
                    isPre = false;
                    return blood.getAfterDinner();
                  } else {
                    isPre = false;
                    return blood.getSleep();
                  }
                } else {
                  isPre = false;
                  return blood.getSleep();
                }
              } else {
                isPre = false;
                return blood.getAfterDinner();
              }
            } else {
              isPre = true;
              return blood.getPreDinner();
            }
          } else {
            isPre = false;
            return blood.getAfterLunchValue();
          }
        } else {
          isPre = true;
          return blood.getPreLunchValue();
        }
      } else {
        isPre = false;
        return blood.getAfterBreakfastValue();
      }
    } else {
      isPre = true;
      return blood.getPreBreakfastValue();
    }
  }

  public static class ItemHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.layout) public RelativeLayout mLayout;
    @BindView(R.id.tv_date) public TextView mDate;
    @BindView(R.id.iv_flag) ImageView mBloodPic;
    @BindView(R.id.tv_value) TextView mBloodValue;

    public ItemHolder(View itemView) {
      super(itemView);
      ButterKnife.bind(this, itemView);
    }
  }
}
