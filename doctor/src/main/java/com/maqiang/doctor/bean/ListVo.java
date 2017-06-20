package com.maqiang.doctor.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by maqiang on 2017/4/7.
 *
 * 分页
 */
public class ListVo<T> implements Serializable {

  //总量
  public int total;
  //
  List<T> data;
}
