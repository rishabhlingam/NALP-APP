package com.example.nalp.NotificationUtility;

import android.annotation.TargetApi;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.database.Cursor;
import android.os.Build;

import androidx.core.app.NotificationCompat;

import com.example.nalp.DATABASE.TaskContract;
import com.example.nalp.MainActivity;
import com.example.nalp.R;

public class NotificationHelper extends ContextWrapper {

    public static final String channelID = "com.example.nalp";
    public static final String channelName = "Remainder/Event";
    private NotificationManager mManager;
    private int UNIVERSAL_ID;
    private Cursor mCursor;

    public NotificationHelper(Context base, Cursor cursor) {
        super(base);
        mCursor = cursor;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createChannel();
        }
    }
    @TargetApi(Build.VERSION_CODES.O)
    private void createChannel() {
        NotificationChannel channel = new NotificationChannel(channelID, channelName, NotificationManager.IMPORTANCE_HIGH);
        getManager().createNotificationChannel(channel);
    }
    public NotificationManager getManager() {
        if (mManager == null) {
            mManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        }
        return mManager;
    }
    public NotificationCompat.Builder getChannelNotification() {
        mCursor.moveToFirst();

        int type = mCursor.getInt(mCursor.getColumnIndexOrThrow(TaskContract.TaskEntry.TASK_TYPE));
        String title = mCursor.getString(mCursor.getColumnIndexOrThrow(TaskContract.TaskEntry.TASK_TITLE));
        String time = mCursor.getString(mCursor.getColumnIndexOrThrow(TaskContract.TaskEntry.TASK_TIME));

        String NotificationTitle = "";
        String NotificationText = "";

        if(type == 0){
            NotificationTitle = "Remainder: " + title;
        }
        else if(type == 1){
            NotificationTitle = "Upcoming Event: " + title;
        }

        Intent intent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);
        return new NotificationCompat.Builder(getApplicationContext(), channelID)
                .setContentTitle(NotificationTitle)
                .setContentText(NotificationText)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);
    }
}
