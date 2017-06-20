package com.maqiang.doctor.receiver;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import com.maqiang.doctor.R;
import com.maqiang.doctor.activity.HomeActivity;

/**
 * Created by maqiang on 2017/4/20.
 */

public class AlarmReceiver extends BroadcastReceiver {

  @Override public void onReceive(Context context, Intent intent) {
    if (intent.getAction().equals("android.intent.action.test")) {
      NotificationManager manager =
          (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
      Intent intent1 = new Intent(context, HomeActivity.class);
      intent1.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
      PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent1, 0);
      NotificationCompat.Builder builder = new NotificationCompat.Builder(context).setSmallIcon(
          R.drawable.logo0)
          .setContentTitle("你有一条新提醒")
          .setContentIntent(pendingIntent)
          .setContentText("记录血糖的时间到了,点击开始记录");
      manager.notify(1, builder.build());
    }
  }
}
