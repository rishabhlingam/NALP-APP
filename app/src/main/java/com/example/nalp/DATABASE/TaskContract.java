package com.example.nalp.DATABASE;

public class TaskContract {
    private TaskContract(){}

    public static class TaskEntry{
        public static final String TABLE_NAME = "Tasks";
        public static final String _ID = "_id";
        public static final String TASK_TITLE = "TaskTitle";
        public static final String TASK_DESCRIPTION = "TaskDescription";
        public static final String TASK_DATE = "TaskDate";
        public static final String TASK_TIME = "TaskTime";
        public static final String TASK_UNIX_TIME = "TaskUnixTime";
        public static final String TASK_TYPE = "TaskType";
    }

    public static String getSqlCreateEntries() {
        return SQL_CREATE_ENTRIES;
    }

    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + TaskEntry.TABLE_NAME + " (" +
                    TaskEntry._ID + " INTEGER PRIMARY KEY," +
                    TaskEntry.TASK_TITLE + " TEXT," +
                    TaskEntry.TASK_DESCRIPTION + " TEXT," +
                    TaskEntry.TASK_DATE + " TEXT," +
                    TaskEntry.TASK_TIME + " TEXT," +
                    TaskEntry.TASK_UNIX_TIME + " INTEGER," +
                    TaskEntry.TASK_TYPE + " INTEGER)";

    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + TaskEntry.TABLE_NAME;
}


