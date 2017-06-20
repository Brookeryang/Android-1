package com.maqiang.doctor.bean;

import cn.bmob.v3.BmobObject;

/**
 * Created by maqiang on 2017/5/11.
 *
 * Function: 意见反馈
 */

public class Question extends BmobObject {

  private String phoneNumber;
  private String content;

  public Question(String tableName) {
    super(tableName);
  }

  public String getPhoneNumber() {
    return phoneNumber;
  }

  public void setPhoneNumber(String phoneNumber) {
    this.phoneNumber = phoneNumber;
  }

  public String getContent() {
    return content;
  }

  public void setContent(String content) {
    this.content = content;
  }
}
