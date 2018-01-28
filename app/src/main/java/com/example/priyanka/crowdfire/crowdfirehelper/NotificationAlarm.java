package com.example.priyanka.crowdfire.crowdfirehelper;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;

import com.example.priyanka.crowdfire.MainActivity;
import com.example.priyanka.crowdfire.R;
import com.example.priyanka.crowdfire.db.CrowdFireDBHelper;

public class NotificationAlarm extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        showNotification(context);
    }

    public void showNotification(Context context) {
        String text1 = "Top to check this out...";
        String text2 = "New combination is waiting for you...";

        int smallIcon = R.drawable.ring;
        Bitmap largeIcon = BitmapFactory.decodeResource(context.getResources(), R.drawable.ring);

        NotificationCompat.BigTextStyle notificationBig = new NotificationCompat.BigTextStyle();
        notificationBig.bigText(text1);
        notificationBig.setBigContentTitle(text2);

        Intent myIntent = new Intent(context, MainActivity.class);

        int shirtsCount = CrowdFireDBHelper.getInstance(context).getShirtImagePathListFromDB().size();
        int trousersCount = CrowdFireDBHelper.getInstance(context).getTrouserImagePathListFromDB().size();
        int shirtRandomPosition = (int) (Math.random() * shirtsCount);
        int trouserRandomPosition = (int) (Math.random() * trousersCount);

        myIntent.putExtra("shirt_section_pos", shirtRandomPosition);
        myIntent.putExtra("trouser_section_pos", trouserRandomPosition);

        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, myIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context.getApplicationContext())
                .setContentText("Item is on it's way to you...").setContentTitle(text2)
                .setSmallIcon(smallIcon).setAutoCancel(true)
                .setTicker(text2)
                .setLargeIcon(largeIcon).
                        setDefaults(Notification.DEFAULT_LIGHTS | Notification.DEFAULT_SOUND)
                .setContentIntent(pendingIntent).setStyle(notificationBig);

        Notification notification = notificationBuilder.build();

        //Notify User
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(0, notification);
    }
}
