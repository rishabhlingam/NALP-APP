package com.example.nalp.DATABASE;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class TaskHelper extends SQLiteOpenHelper {

    private SQLiteDatabase mdb = super.getWritableDatabase();
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "Tasks.db";

    public TaskHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //mdb = db;
        db.execSQL(TaskContract.getSqlCreateEntries());
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public void deleteTask(int id){
        String selection = TaskContract.TaskEntry._ID + " = ?";
        String[] selectionArgs = { String.valueOf(id) };
        int ret = mdb.delete(TaskContract.TaskEntry.TABLE_NAME,selection,selectionArgs);
    }

    public Cursor getAllEvents(){
        String[] selectionArgs = { "1" };
        return getFromDB(selectionArgs);
    }

    public Cursor getAllRemainders(){
        String[] selectionArgs = { "0" };
        return getFromDB(selectionArgs);
    }

    private Cursor getFromDB(String[] selectionArgs){
        String selection = TaskContract.TaskEntry.TASK_TYPE + " = ?";
        String sortOrder = TaskContract.TaskEntry.TASK_UNIX_TIME + " ASC";
        Cursor cursor = mdb.query(
                TaskContract.TaskEntry.TABLE_NAME,     // The table to query
                null,                         // The array of columns to return (pass null to get all)
                selection,                         // The columns for the WHERE clause
                selectionArgs,                      // The values for the WHERE clause
                null,                         // don't group the rows
                null,                           // don't filter by row groups
                sortOrder                           // The sort order
        );
        return cursor;
    }

}

/*
* initial dataTest code
*  ContentValues v1 = new ContentValues();
        ContentValues v2 = new ContentValues();
        v1.put(TaskContract.TaskEntry._ID,0);
        v1.put(TaskContract.TaskEntry.TASK_TITLE,"Call Joe");
        v1.put(TaskContract.TaskEntry.TASK_DESCRIPTION,"Call and take notes");
        v1.put(TaskContract.TaskEntry.TASK_DATE,"21/12/2020");
        v1.put(TaskContract.TaskEntry.TASK_TIME,"23:30");
        v1.put(TaskContract.TaskEntry.TASK_UNIX_TIME,1234);
        v1.put(TaskContract.TaskEntry.TASK_TYPE,0);
        v2.put(TaskContract.TaskEntry._ID,1);
        v2.put(TaskContract.TaskEntry.TASK_TITLE,"Call Joe");
        v2.put(TaskContract.TaskEntry.TASK_DESCRIPTION,"Call and take notes");
        v2.put(TaskContract.TaskEntry.TASK_DATE,"21/12/2020");
        v2.put(TaskContract.TaskEntry.TASK_TIME,"23:30");
        v2.put(TaskContract.TaskEntry.TASK_UNIX_TIME,1234);
        v2.put(TaskContract.TaskEntry.TASK_TYPE,0);
        long ret = db.insert(TaskContract.TaskEntry.TABLE_NAME,null,v1);
        ret = db.insert(TaskContract.TaskEntry.TABLE_NAME,null,v2);
* */