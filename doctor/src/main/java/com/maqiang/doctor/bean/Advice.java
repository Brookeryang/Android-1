package com.maqiang.doctor.bean;

import cn.bmob.v3.BmobObject;
import java.util.Map;

/**
 * Created by maqiang on 2017/4/10.
 */

public class Advice extends BmobObject {

  private String doctorName;
  private String content;
  private String check;
  private String remarks;
  private String date;
  private String phoneNumber;

  public Advice(String tableName) {
    super(tableName);
    doctorName = "";
    content = "";
    check = "";
    remarks = "";
  }

  public String getDoctorName() {
    return doctorName;
  }

  public void setDoctorName(String doctorName) {
    this.doctorName = doctorName;
  }

  public String getContent() {
    return content;
  }

  public void setContent(String content) {
    this.content = content;
  }

  public String getCheck() {
    return check;
  }

  public void setCheck(String check) {
    this.check = check;
  }

  public String getRemarks() {
    return remarks;
  }

  public void setRemarks(String remarks) {
    this.remarks = remarks;
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
