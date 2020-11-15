package com.example.nalp.DATABASE;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class UniqueIDHelper extends SQLiteOpenHelper {

    private SQLiteDatabase mdb = super.getWritableDatabase();
    private static final int INITIAL_DATABASE_SIZE = 1000;
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "UniqueIDs.db";

    public UniqueIDHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //mdb = db;
        db.execSQL(UniqueIDContract.getSqlCreateEntries());
        for(int i=1;i<=INITIAL_DATABASE_SIZE;i++){
            ContentValues values = new ContentValues();
            values.put(UniqueIDContract.UniqueIDEntry._ID,i);
            long ret = db.insert(UniqueIDContract.UniqueIDEntry.TABLE_NAME,null,values);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public Cursor getID(){
        return mdb.query(
                UniqueIDContract.UniqueIDEntry.TABLE_NAME,     // The table to query
                null,                                  // The array of columns to return (pass null to get all)
                null,                                  // The columns for the WHERE clause
                null,                               // The values for the WHERE clause
                null,                                   // don't group the rows
                null,                                    // don't filter by row groups
                null,                                   // The sort order
                "1"
        );
    }

    public void addID(int id){
        ContentValues val = new ContentValues();
        val.put(UniqueIDContract.UniqueIDEntry._ID,id);
        mdb.insert(UniqueIDContract.UniqueIDEntry.TABLE_NAME,null,val);
    }

    public void deleteID(int id){
        String selection = UniqueIDContract.UniqueIDEntry._ID + " = ?";
        String[] selectionArgs = { String.valueOf(id) };
        int ret = mdb.delete(UniqueIDContract.UniqueIDEntry.TABLE_NAME,selection,selectionArgs);
    }
}
