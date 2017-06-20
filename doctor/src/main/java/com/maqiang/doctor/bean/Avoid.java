package com.maqiang.doctor.bean;

import cn.bmob.v3.BmobObject;

/**
 * Created by maqiang on 2017/5/6.
 *
 * Function: 忌口类
 */

public class Avoid extends BmobObject {

  private String phoneNumber;
  private String date;
  private String item;
  private String tips;

  public Avoid(String tableName) {
    super(tableName);
  }

  public String getPhoneNumber() {
    return phoneNumber;
  }

  public void setPhoneNumber(String phoneNumber) {
    this.phoneNumber = phoneNumber;
  }

  public String getDate() {
    return date;
  }

  public void setDate(String date) {
    this.date = date;
  }

  public String getItem() {
    return item;
  }

  public void setItem(String item) {
    this.item = item;
  }

  public String getTips() {
    return tips;
  }

  public void setTips(String tips) {
    this.tips = tips;
  }
}
