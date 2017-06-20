package com.maqiang.doctor.bean;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;

/**
 * Created by maqiang on 2017/4/10.
 */

public class User extends BmobUser{

  private BmobFile icon;
  private String duedate;
  private String nickname;
  private String city;
  private int age;

  public String getNickname() {
    return nickname;
  }

  public void setNickname(String nickname) {
    this.nickname = nickname;
  }

  public BmobFile getIcon() {
    return icon;
  }

  public void setIcon(BmobFile icon) {
    this.icon = icon;
  }

  public String getDuedate() {
    return duedate;
  }

  public void setDuedate(String duedate) {
    this.duedate = duedate;
  }

  public String getCity() {
    return city;
  }

  public void setCity(String city) {
    this.city = city;
  }

  public int getAge() {
    return age;
  }

  public void setAge(int age) {
    this.age = age;
  }

  @Override public String toString() {
    return "User{"
        + "icon="
        + icon
        + ", duedate='"
        + duedate
        + '\''
        + ", city='"
        + city
        + '\''
        + ", age="
        + age
        + '}';
  }
}
