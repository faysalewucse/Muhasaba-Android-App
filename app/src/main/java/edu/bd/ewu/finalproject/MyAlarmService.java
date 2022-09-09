package edu.bd.ewu.finalproject;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.RequiresApi;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

public class MyAlarmService extends Service {

    private static final String TAG = "TAG";
    public static final String SERVICE_MESSAGE = "ServiceMessage";
    Intent ui = new Intent(SERVICE_MESSAGE);
    final Handler handler = new Handler();
    Runnable myRunnable;

    public MyAlarmService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "onStartCommand: ");
        //For Showing Notification
        final String CHANNELID = "Foreground Service Id";
        NotificationChannel channel = new NotificationChannel(
                CHANNELID, CHANNELID, NotificationManager.IMPORTANCE_LOW
        );
        getSystemService(NotificationManager.class).createNotificationChannel(channel);
        myRunnable = new Runnable() {
            @RequiresApi(api = Build.VERSION_CODES.R)
            public void run() {
                Notification.Builder notification = new Notification.Builder(getApplicationContext(), CHANNELID)
                        .setContentText("আপনার আজকের কিছু জিকির বাকি আছে। দ্রুত শেষ করে ফেলুন")
                        .setContentTitle("এলার্ট !!")
                        .setActions()
                        .setSmallIcon(R.drawable.ic_launcher_background);
                notification.setFlag(Notification.FLAG_AUTO_CANCEL, true);
                startForeground(1001, notification.build());
                handler.postDelayed(this, 5000);
            }
        };
        handler.post(myRunnable);
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        handler.removeCallbacks(myRunnable);
        Log.d("second", "onDestroy: Service Destroyed");
    }
}