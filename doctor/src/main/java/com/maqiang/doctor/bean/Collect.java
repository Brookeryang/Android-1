package com.maqiang.doctor.bean;


import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobFile;

/**
 * Created by maqiang on 2017/4/17.
 */

public class Collect extends BmobObject{

  private String phoneNumber;
  private String dish;
  private String description;
  private BmobFile pic;
  private BmobFile image1;
  private BmobFile image2;
  private BmobFile image3;
  private BmobFile image4;

  public Collect(String tableName) {
    super(tableName);
  }

  public String getPhoneNumber() {
    return phoneNumber;
  }

  public void setPhoneNumber(String phoneNumber) {
    this.phoneNumber = phoneNumber;
  }

  public String getDish() {
    return dish;
  }

  public void setDish(String dish) {
    this.dish = dish;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public BmobFile getPic() {
    return pic;
  }

  public void setPic(BmobFile pic) {
    this.pic = pic;
  }

  public BmobFile getImage1() {
    return image1;
  }

  public void setImage1(BmobFile image1) {
    this.image1 = image1;
  }

  public BmobFile getImage2() {
    return image2;
  }

  public void setImage2(BmobFile image2) {
    this.image2 = image2;
  }

  public BmobFile getImage3() {
    return image3;
  }

  public void setImage3(BmobFile image3) {
    this.image3 = image3;
  }

  public BmobFile getImage4() {
    return image4;
  }

  public void setImage4(BmobFile image4) {
    this.image4 = image4;
  }
}
