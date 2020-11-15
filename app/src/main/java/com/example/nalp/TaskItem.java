package com.example.nalp;

public class TaskItem {
    private String Title;
    private String  Description;
    private String Date;
    private String Time;
    private int _ID;
    private long UnixTime;
    private int Type;

    public TaskItem(String title, String description, String date, String time, int _ID, long unixTime, int type) {
        Title = title;
        Description = description;
        Date = date;
        Time = time;
        this._ID = _ID;
        UnixTime = unixTime;
        Type = type;
    }

    public String getTitle() {
        return Title;
    }

    public String getDescription() {
        return Description;
    }

    public String getDate() {
        return Date;
    }

    public String getTime() {
        return Time;
    }

    public int get_ID() {
        return _ID;
    }

    public long getUnixTime() {
        return UnixTime;
    }

    public int isType() {
        return Type;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public void setDate(String date) {
        Date = date;
    }

    public void setTime(String time) {
        Time = time;
    }

    public void set_ID(int _ID) {
        this._ID = _ID;
    }

    public void setUnixTime(long unixTime) {
        UnixTime = unixTime;
    }

    public void setType(int type) {
        Type = type;
    }
}
