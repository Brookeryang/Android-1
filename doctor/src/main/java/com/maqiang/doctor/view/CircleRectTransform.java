package com.maqiang.doctor.view;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import com.squareup.picasso.Transformation;

/**
 * 创建时间：2016/12/05  16:12
 * 作者：yangjiao
 * 描述：圆角矩形
 */

public class CircleRectTransform implements Transformation {

  @Override public Bitmap transform(Bitmap source) {
    int widthLight = source.getWidth();
    int heightLight = source.getHeight();

    Bitmap output =
        Bitmap.createBitmap(source.getWidth(), source.getHeight(), Bitmap.Config.ARGB_8888);

    Canvas canvas = new Canvas(output);
    Paint paintColor = new Paint();
    paintColor.setFlags(Paint.ANTI_ALIAS_FLAG);

    RectF rectF = new RectF(new Rect(0, 0, widthLight, heightLight));

    canvas.drawRoundRect(rectF, 8, 8, paintColor);

    Paint paintImage = new Paint();
    paintImage.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_ATOP));
    canvas.drawBitmap(source, 0, 0, paintImage);
    source.recycle();
    return output;
  }

  @Override public String key() {
    return "roundcorner";
  }
}

