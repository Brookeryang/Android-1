package com.maqiang.doctor.bean;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.BmobUser;

/**
 * Created by maqiang on 2017/2/27.
 */

public class Person extends BmobObject {
  private String a,b,c,d,e;

  public Person(String tableName) {
    super(tableName);
  }

  public Person(String tableName, String a, String b, String c, String d, String e) {
    super(tableName);
    this.a = a;
    this.b = b;
    this.c = c;
    this.d = d;
    this.e = e;
  }

  public String getA() {
    return a;
  }

  public void setA(String a) {
    this.a = a;
  }

  public String getB() {
    return b;
  }

  public void setB(String b) {
    this.b = b;
  }

  public String getC() {
    return c;
  }

  public void setC(String c) {
    this.c = c;
  }

  public String getD() {
    return d;
  }

  public void setD(String d) {
    this.d = d;
  }

  public String getE() {
    return e;
  }

  public void setE(String e) {
    this.e = e;
  }
}
