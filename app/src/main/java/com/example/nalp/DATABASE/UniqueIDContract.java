package com.example.nalp.DATABASE;

public class UniqueIDContract {
    private UniqueIDContract(){}

    public static class UniqueIDEntry {
        public static final String TABLE_NAME = "UniqueIDs";
        public static final String _ID = "_id";
    }

    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + UniqueIDEntry.TABLE_NAME + " (" +
                    TaskContract.TaskEntry._ID + " INTEGER PRIMARY KEY )" ;

    public static String getSqlCreateEntries() {
        return SQL_CREATE_ENTRIES;
    }
    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + UniqueIDEntry.TABLE_NAME;

}
