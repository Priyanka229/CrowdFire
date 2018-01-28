package com.example.priyanka.crowdfire;

import android.app.AlarmManager;
import android.app.Application;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.StrictMode;

import com.example.priyanka.crowdfire.crowdfirehelper.NotificationAlarm;

import java.util.Calendar;
import java.util.Date;

public class CrowdApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());

        setAlarm();
    }

    private void setAlarm() {
        boolean alarmUp = (PendingIntent.getBroadcast(this, 0,
                new Intent(this, NotificationAlarm.class),
                PendingIntent.FLAG_NO_CREATE) != null);

        if (!alarmUp) {
            AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
            if (alarmManager != null) {
                long timeInMillis = getDateInMillis();
                Calendar.getInstance().setTime(new Date());

                Intent intent = new Intent(this, NotificationAlarm.class);
                PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, intent, 0);

                alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, timeInMillis,
                        AlarmManager.INTERVAL_DAY, pendingIntent);
            }
        }
    }

    public static long getDateInMillis() {
        int year = Calendar.getInstance().get(Calendar.YEAR);
        int month = Calendar.getInstance().get(Calendar.MONTH);
        int day = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
        int hour = 6;
        int minute = 0;
        int second = 0;
        int millis = 0;

        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, year);
        cal.set(Calendar.MONTH, month);
        cal.set(Calendar.DAY_OF_MONTH, day);
        cal.set(Calendar.HOUR_OF_DAY, hour);
        cal.set(Calendar.MINUTE, minute);
        cal.set(Calendar.SECOND, second);
        cal.set(Calendar.MILLISECOND, millis);

        long startUpTime = cal.getTimeInMillis();
        if (System.currentTimeMillis() > startUpTime) {
            startUpTime = startUpTime + 24 * 60 * 60 * 1000;
        }
        return startUpTime;
    }
}
