package org.apache.cordova.firebase;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import java.util.Random;

public class NotificationService extends Service {
    private static NotificationService _instance;
    private Context _context = null;
    private String _channelId = "push";
    static final String defaultSmallIconName = "notification_icon";
    private int _defaultSmallIconResID = 0;

    private NotificationService(Context context) {
        _context = context;

        try {
            _defaultSmallIconResID = getApplicationInfo().icon;
        } catch (Exception e) {
            //TODO: handle exception
        }

        showToast( "ID иконки: " + _defaultSmallIconResID, Toast.LENGTH_LONG);
        
        createNotificationChannel();
    }

    public static NotificationService getInstance(Context context) {
        if (_instance == null) {
            _instance = new NotificationService(context);
        }

        return _instance;
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    public void showToast(String text, int duration) {
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(_context, text, duration).show();
            }
        });
    }

    public void showNotification(String text) {
               NotificationCompat.Builder builder = new NotificationCompat.Builder(_context, _channelId)
                .setSmallIcon(_defaultSmallIconResID)
                .setContentTitle("Ку-ку")
                .setContentText(text)
                .setStyle(new NotificationCompat.BigTextStyle()
                        .bigText(text))
                .setDefaults(NotificationCompat.DEFAULT_ALL)
                .setPriority(NotificationCompat.PRIORITY_MAX)
                .setCategory(NotificationCompat.CATEGORY_MESSAGE);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(_context);

// notificationId is a unique int for each notification that you must define
        notificationManager.notify(new Random().nextInt(),
                builder.build());


    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "test";
            String description = "test";
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel channel = new NotificationChannel(_channelId, name, importance);
            channel.setDescription(description);

            NotificationManager notificationManager = _context.getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

}
