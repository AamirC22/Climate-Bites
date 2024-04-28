package com.app.weathernews.notifications;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.app.weathernews.R;
import com.app.weathernews.activity.MainActivity;
import com.app.weathernews.activity.SplashActivity;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import io.paperdb.Paper;

// FirebaseMessageReceiver class from https://origin.geeksforgeeks.org/how-to-push-notification-in-android-using-firebase-cloud-messaging/
// Date of article 12 Dec 2022, Author: aayushitated2000

// Main class for Integration of Firebase into the Application
public class FirebaseMessageReceiver extends FirebaseMessagingService {

    // This override makes the application receive a new token
    @Override
    public void onNewToken(@NonNull String token) {
        Log.d("onNotificationReceive", "Refreshed token: " + token);
    }
    // This overrides the onMessageReceived method in order the get the title and the body
    // of the message sent via Firebase Cloud notifications system
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        Log.d("TAGdfs", "onMessageReceived: ");
        Paper.init(this);
        Paper.book().write("isFromNotification",true);

        if (remoteMessage.getNotification() != null) {
        // If the notifications are allowed, then get the title and the body of the notification
            boolean isNotificationAllowed = Paper.book().read("isNotificationEnabled", false);
            if (isNotificationAllowed) {
                showNotification(
                        remoteMessage.getNotification().getTitle(),
                        remoteMessage.getNotification().getBody());
            } else {
                // Nothing occurs as the notifications are not allowed to be sent
            }
        }
    }
    // showNotification method from https://origin.geeksforgeeks.org/how-to-push-notification-in-android-using-firebase-cloud-messaging/
    // Date of article 12 Dec 2022, Author: aayushitated2000
    // Method used to show the notifications on the application
    public void showNotification(String title, String message) {
        // The intent is used to check if the user wants to switch to the Main Activity Class
        Intent intent = new Intent(this, MainActivity.class);
        String channel_id = "notification_channel";
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        // This passes the intent from the other intent in order for the application to know to start the next activity or not
        PendingIntent pendingIntent = PendingIntent.getActivity(
                this, 0, intent,
                PendingIntent.FLAG_ONE_SHOT | PendingIntent.FLAG_IMMUTABLE);
        // This creates an object using NotificationCompat by using a builder tha has control over the flags
        NotificationCompat.Builder builder = new NotificationCompat
                .Builder(getApplicationContext(), channel_id)
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setAutoCancel(true)
                .setVibrate(new long[]{1000, 1000, 1000, 1000, 1000})
                .setOnlyAlertOnce(true)
                .setContentIntent(pendingIntent);

        builder = builder.setContentTitle(title)
                .setContentText(message)
                .setSmallIcon(R.drawable.ic_launcher_foreground);
        // This creates a new class of notificationManager and then gives it a system service to notify of different events
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        // This checks what the android version is and if it is a newer version of Oreo
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel = new NotificationChannel(
                    channel_id, "web_app",
                    NotificationManager.IMPORTANCE_HIGH);
            notificationManager.createNotificationChannel(notificationChannel);
        }
        // This builds the object then provides the notification
        notificationManager.notify(0, builder.build());
    }
}
