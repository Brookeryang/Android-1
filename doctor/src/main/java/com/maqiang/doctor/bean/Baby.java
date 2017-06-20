package com.maqiang.doctor.bean;

import cn.bmob.v3.BmobObject;
import com.maqiang.doctor.utils.ConstantUtil;

/**
 * Created by maqiang on 2017/5/14.
 *
 * Function:宝宝说文案
 */

public class Baby extends BmobObject {

  private int week;
  private String content;

  public Baby() {
    super(ConstantUtil.BABY);
  }

  public int getWeek() {
    return week;
  }

  public void setWeek(int week) {
    this.week = week;
  }

  public String getContent() {
    return content;
  }

  public void setContent(String content) {
    this.content = content;
  }
}
