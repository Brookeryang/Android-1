package com.maqiang.doctor.bean;

import cn.bmob.v3.BmobObject;
import com.maqiang.doctor.utils.ConstantUtil;

/**
 * Created by maqiang on 2017/5/13.
 *
 * Function: 血糖数据类
 */

public class Blood extends BmobObject {

  private String phoneNumber = "";
  private String date;
  private float preBreakfastValue = 0.0f;
  private float afterBreakfastValue = 0.0f;
  private float preLunchValue = 0.0f;
  private float afterLunchValue = 0.0f;
  private float preDinner = 0.0f;
  private float afterDinner = 0.0f;
  private float sleep = 0.0f;

  public Blood() {
    super(ConstantUtil.BLOOD_SUAGER);
  }

  public Blood(String tableName) {
    super(tableName);
  }

  public String getPhoneNumber() {
    return phoneNumber;
  }

  public void setPhoneNumber(String phoneNumber) {
    this.phoneNumber = phoneNumber;
  }

  public float getPreBreakfastValue() {
    return preBreakfastValue;
  }

  public void setPreBreakfastValue(float preBreakfastValue) {
    this.preBreakfastValue = preBreakfastValue;
  }

  public float getAfterBreakfastValue() {
    return afterBreakfastValue;
  }

  public void setAfterBreakfastValue(float afterBreakfastValue) {
    this.afterBreakfastValue = afterBreakfastValue;
  }

  public float getPreLunchValue() {
    return preLunchValue;
  }

  public void setPreLunchValue(float preLunchValue) {
    this.preLunchValue = preLunchValue;
  }

  public float getAfterLunchValue() {
    return afterLunchValue;
  }

  public void setAfterLunchValue(float afterLunchValue) {
    this.afterLunchValue = afterLunchValue;
  }

  public float getPreDinner() {
    return preDinner;
  }

  public void setPreDinner(float preDinner) {
    this.preDinner = preDinner;
  }

  public float getAfterDinner() {
    return afterDinner;
  }

  public void setAfterDinner(float afterDinner) {
    this.afterDinner = afterDinner;
  }

  public float getSleep() {
    return sleep;
  }

  public void setSleep(float sleep) {
    this.sleep = sleep;
  }

  public String getDate() {
    return date;
  }

  public void setDate(String date) {
    this.date = date;
  }
}
