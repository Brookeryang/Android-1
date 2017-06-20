package com.maqiang.doctor.utils;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by maqiang on 2017/3/9.
 */

public class DateUtils {

  // strTime要转换的String类型的时间
  // formatType时间格式
  // strTime的时间格式和formatType的时间格式必须相同
  public static long stringToLong(String strTime, String formatType) throws ParseException {
    Date date = stringToDate(strTime, formatType); // String类型转成date类型
    if (date == null) {
      return 0;
    } else {
      long currentTime = dateToLong(date); // date类型转成long类型
      return currentTime;
    }
  }

  // formatType格式为yyyy-MM-dd HH:mm:ss//yyyy年MM月dd日 HH时mm分ss秒
  // data Date类型的时间
  public static String dateToString(Date data, String formatType) {
    return new SimpleDateFormat(formatType).format(data);
  }

  // currentTime要转换的long类型的时间
  // formatType要转换的string类型的时间格式
  public static String longToString(long currentTime, String formatType) throws ParseException {
    Date date = longToDate(currentTime, formatType); // long类型转成Date类型
    String strTime = dateToString(date, formatType); // date类型转成String
    return strTime;
  }

  // strTime要转换的string类型的时间，formatType要转换的格式yyyy-MM-dd HH:mm:ss//yyyy年MM月dd日
  // HH时mm分ss秒，
  // strTime的时间格式必须要与formatType的时间格式相同
  public static Date stringToDate(String strTime, String formatType) throws ParseException {
    SimpleDateFormat formatter = new SimpleDateFormat(formatType);
    Date date = null;
    date = formatter.parse(strTime);
    return date;
  }

  // currentTime要转换的long类型的时间
  // formatType要转换的时间格式yyyy-MM-dd HH:mm:ss//yyyy年MM月dd日 HH时mm分ss秒
  public static Date longToDate(long currentTime, String formatType) throws ParseException {
    Date dateOld = new Date(currentTime); // 根据long类型的毫秒数生命一个date类型的时间
    String sDateTime = dateToString(dateOld, formatType); // 把date类型的时间转换为string
    Date date = stringToDate(sDateTime, formatType); // 把String类型转换为Date类型
    return date;
  }

  // date要转换的date类型的时间
  public static long dateToLong(Date date) {
    return date.getTime();
  }

  /**
   * @param data 初始时间，毫秒
   * @param delay 往后推迟的天数，天
   * @return 推迟后的日期，string
   */
  public static String getDateDelay(Date date) {
    Calendar calendar = Calendar.getInstance();
    calendar.setTime(date);
    calendar.set(Calendar.DAY_OF_MONTH,calendar.get(Calendar.DAY_OF_MONTH)+280);
    return dateToString(calendar.getTime(),"yyyy-MM-dd");
  }


}
