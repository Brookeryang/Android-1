package com.maqiang.doctor.bean;

import cn.bmob.v3.BmobObject;

/**
 * Created by maqiang on 2017/4/10.
 */

public class Recipe extends BmobObject{

  private String breakfast;
  private String afternoon;
  private String dinner;
  private String add;
  private String date;
  private String phoneNumber;

  public Recipe(String tableName) {
    super(tableName);
    breakfast = "";
    afternoon = "";
    dinner = "";
    add = "";
  }


  public String getBreakfast() {
    return breakfast;
  }

  public void setBreakfast(String breakfast) {
    this.breakfast = breakfast;
  }

  public String getAfternoon() {
    return afternoon;
  }

  public void setAfternoon(String afternoon) {
    this.afternoon = afternoon;
  }

  public String getDinner() {
    return dinner;
  }

  public void setDinner(String dinner) {
    this.dinner = dinner;
  }

  public String getAdd() {
    return add;
  }

  public void setAdd(String add) {
    this.add = add;
  }

  public String getDate() {
    return date;
  }

  public void setDate(String date) {
    this.date = date;
  }

  public String getPhoneNumber() {
    return phoneNumber;
  }

  public void setPhoneNumber(String phoneNumber) {
    this.phoneNumber = phoneNumber;
  }
}
