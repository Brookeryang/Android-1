package com.maqiang.doctor.utils;

import android.graphics.Bitmap;
import android.util.Base64;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.EOFException;
import okio.Buffer;

/**
 * 创建时间: 2016/08/09 15:14 <br>
 * 作者: dengwei <br>
 * 描述: 工具类
 */
public class Util {

  private static final String TAG = "Util";

  private Util() {
    throw new IllegalStateException("No instances!");
  }

  public static <T> T checkNotNull(T object, String message) {
    if (object == null) throw new NullPointerException(message);
    return object;
  }

  public static void closeQuietly(Closeable closeable) {
    if (closeable != null) {
      try {
        closeable.close();
      } catch (RuntimeException rethrown) {
        throw rethrown;
      } catch (Exception ignored) {
      }
    }
  }

  public static boolean isPlaintext(Buffer buffer) throws EOFException {
    try {
      Buffer prefix = new Buffer();
      long byteCount = buffer.size() < 64 ? buffer.size() : 64;
      buffer.copyTo(prefix, 0, byteCount);
      for (int i = 0; i < 16; i++) {
        if (prefix.exhausted()) {
          break;
        }
        if (Character.isISOControl(prefix.readUtf8CodePoint())) {
          return false;
        }
      }
      return true;
    } catch (EOFException e) {
      return false; // Truncated UTF-8 sequence.
    }
  }

  public static void showLog(String msg){
    Log.d(TAG, msg);
  }

  public static byte[] bitmapToByteArray(Bitmap bitmap){

    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);//质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
    int options = 100;
    while ( baos.toByteArray().length / 1024>100) { //循环判断如果压缩后图片是否大于100kb,大于继续压缩
      baos.reset();//重置baos即清空baos
      bitmap.compress(Bitmap.CompressFormat.JPEG, options, baos);//这里压缩options%，把压缩后的数据存放到baos中
      options -= 10;//每次都减少10
    }

    return baos.toByteArray();
  }
}

