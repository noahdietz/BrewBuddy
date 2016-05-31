package edu.calpoly.ndietz.brewbuddy;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.IBinder;
import android.os.Vibrator;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.util.Log;

/**
 * Created by ndietz on 4/18/16.
 */
public class BrewTimer extends Service {
    private final static String TIMER_TAG = "BrewTimer";
    public final static int ONGOING_NOTIFICATION_ID = 2;
    private final static long TIMER_INTERVAL = 1000;
    private final static long MILLI_IN_SECOND = 1000;

    public static final String COUNTDOWN_BR = "BrewTimer.brew_timer";
    Intent bi = new Intent(COUNTDOWN_BR);

    private Intent m_intent;
    private CountDownTimer m_timer;

    private static BrewTimer sInstance;
    private boolean isDone;

    @Override
    public void onCreate() {
        super.onCreate();

        sInstance = this;
        this.isDone = false;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        this.m_intent = intent;
        long numSeconds = 0;

        Log.d(TIMER_TAG, "Starting service");

        Bundle extras = this.m_intent.getExtras();
        if (extras != null) {
            numSeconds =  (long)extras.getInt("extra_brew_time");
            Log.d(TIMER_TAG, "Starting timer with total: "+numSeconds);

            this.m_timer = new CountDownTimer(numSeconds*MILLI_IN_SECOND, TIMER_INTERVAL) {
                @Override
                public void onTick(long millisUntilFinished) {
                    Log.d(TIMER_TAG, "Seconds remaining: "+ millisUntilFinished/MILLI_IN_SECOND);
                    bi.putExtra("extra_time_left", millisUntilFinished/MILLI_IN_SECOND);
                    sendBroadcast(bi);
                }

                @Override
                public void onFinish() {
                    Log.d(TIMER_TAG, "Timer done");
                    bi.putExtra("extra_time_left", (long)0);
                    sendBroadcast(bi);

                    isDone = true;

                    Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                    v.vibrate(MILLI_IN_SECOND);
                }
            }.start();
        } else {
            Log.e(TIMER_TAG, "EMPTY INTENT EXTRAS");
        }

        return super.onStartCommand(intent, flags, startId);
    }

    public static BrewTimer getInstance() {
        return sInstance;
    }

    public boolean checkIfDone() {
        return this.isDone;
    }

    public void promoteToForeground(PendingIntent pending) {

        startForeground(ONGOING_NOTIFICATION_ID, new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.ic_coffee_notification)
                .setContentTitle(getText(R.string.notification_title))
                .setContentText(getText(R.string.notification_text))
                .setContentIntent(pending)
                .build());
    }

    @Override
    public void onDestroy() {

        sInstance = null;
        this.m_timer.cancel();
        Log.i(TIMER_TAG, "Timer cancelled");
        super.onDestroy();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
