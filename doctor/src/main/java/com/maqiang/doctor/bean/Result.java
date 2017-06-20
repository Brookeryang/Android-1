package com.maqiang.doctor.bean;

import java.io.Serializable;

/**
 * Created by maqiang on 2017/4/7.
 *
 * 所有请求数据的基类
 */
public class Result<T> implements Serializable{
  //错误码
  public int error_code;
  //错误原因
  public String reason;
  //返回结果
  public T result;
}
