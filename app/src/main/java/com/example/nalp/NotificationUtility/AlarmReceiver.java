package com.example.nalp.NotificationUtility;

import android.annotation.TargetApi;
import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.job.JobScheduler;
import android.app.job.JobService;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;

import com.example.nalp.AddActivity;
import com.example.nalp.DATABASE.TaskContract;
import com.example.nalp.DATABASE.TaskHelper;
import com.example.nalp.MainActivity;

public class AlarmReceiver extends BroadcastReceiver {

    private Context mContext;
    private int UNIVERSAL_ID ;
    private Intent mIntent;
    public static final String KEY_1 = "vfvdfjvdifvndjfnvjdnfjnv";

    @Override
    public void onReceive(Context context, Intent intent) {
        mContext  =context;
        UNIVERSAL_ID = intent.getIntExtra(KEY_1,-1);

        TaskHelper dbHelper = new TaskHelper(context);
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String selection = TaskContract.TaskEntry._ID + " = ?";
        String[] selectionArgs = { String.valueOf(UNIVERSAL_ID) };

        Cursor cursor = db.query(
                TaskContract.TaskEntry.TABLE_NAME,     // The table to query
                null,                         // The array of columns to return (pass null to get all)
                selection,                         // The columns for the WHERE clause
                selectionArgs,                      // The values for the WHERE clause
                null,                         // don't group the rows
                null,                           // don't filter by row groups
                null                           // The sort order
        );

        NotificationHelper notificationHelper = new NotificationHelper(context,cursor);
        NotificationCompat.Builder nb = notificationHelper.getChannelNotification();
       // Log.i("ALARM-RECEIVER", "onReceive: "+String.valueOf(UNIVERSAL_ID));
        notificationHelper.getManager().notify(UNIVERSAL_ID, nb.build());

    }

}
