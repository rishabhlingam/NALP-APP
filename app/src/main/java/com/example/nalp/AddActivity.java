package com.example.nalp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TimePicker;

import com.example.nalp.DATABASE.TaskContract;
import com.example.nalp.DATABASE.TaskHelper;
import com.example.nalp.DATABASE.UniqueIDContract;
import com.example.nalp.DATABASE.UniqueIDHelper;
import com.example.nalp.NotificationUtility.AlarmReceiver;
import com.example.nalp.Pickers.DatePickerFragment;
import com.example.nalp.Pickers.TimePickerFragment;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;

import java.text.DateFormat;
import java.util.Calendar;

public class AddActivity extends AppCompatActivity
        implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {

    private Calendar c = Calendar.getInstance();

    private RadioGroup radioGroup;
    private RadioButton radioButton;
    private TextInputLayout text_task_title;
    private Button button_datepick;
    private Button button_timepick;
    private Button button_clearall;
    private Button button_done;
    private TextInputLayout text_task_description;
    private TextInputLayout inputLayout_datetime;

    private int UNIVERSAL_ID_INT;
    public static final String UNNIVERSAL_ID = "PENDINGINTENT_NOTIFICATION_TASH_UNIQUEID_ID";
    public static final String KEY_1 = "vfvdfjvdifvndjfnvjdnfjnv";
    private final String[] dropdown_items = new String[]{"Remainder","Event"};
    private final int TITLE_MAX_LENGTH = 20;

    private Intent intent;

    private String mTitle;
    private String  mDescription;
    private String mDate;
    private String mTime;
    private int m_ID;
    private long mUnixTime;
    private int mType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        intent = getIntent();
        UNIVERSAL_ID_INT = intent.getIntExtra(UNNIVERSAL_ID,-1);
        m_ID = UNIVERSAL_ID_INT;

        InstantiateRadiogroup();
        InstantiateTaskTitle();
        InstantiateButtonDateTimePick();
        InstantiateTaskDescription();
        InstantiateButtonClearAll();
        InstantiateButtonDone();
    }

    private void InstantiateRadiogroup(){
        radioGroup = (RadioGroup) findViewById(R.id.text_task_type);
    }

    private void InstantiateTaskTitle(){
        text_task_title = findViewById(R.id.text_task_title);
        text_task_title.setCounterMaxLength(TITLE_MAX_LENGTH);
        text_task_title.setCounterEnabled(true);
        text_task_title.setErrorEnabled(true);
    }

    private void InstantiateButtonDateTimePick(){
        inputLayout_datetime = findViewById(R.id.inputlayout_datetime);
        button_datepick = (Button) findViewById(R.id.button_date_pick);
        button_datepick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 DialogFragment datePicker = new DatePickerFragment();
                 datePicker.show(getSupportFragmentManager(),"Date Picker Tag");
            }
        });
        button_timepick = (Button) findViewById(R.id.button_time_pick);
        button_timepick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment timePicker = new TimePickerFragment();
                timePicker.show(getSupportFragmentManager(),"Time pick Tag");
            }
        });
    }

    private void InstantiateTaskDescription(){
        text_task_description = findViewById(R.id.text_task_description);
    }

    private void InstantiateButtonClearAll(){
        button_clearall = (Button) findViewById(R.id.button_clearall);
        button_clearall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                text_task_title.getEditText().setText(null);
                text_task_description.getEditText().setText(null);
            }
        });
    }

    private void InstantiateButtonDone(){
        button_done = (Button) findViewById(R.id.button_done);
        button_done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!validateType() | !validateTitle() | !validateDateTime()){return;}
                mDescription = text_task_description.getEditText().getText().toString().trim();
                new AddtoTasksAsync().execute();
                StartAlarm(c);
                setResult(RESULT_OK);
                finish();
            }
        });
    }

    private boolean validateType(){
        int radioId = radioGroup.getCheckedRadioButtonId();
        switch (radioId){
            case R.id.radio_task_remainder:{
                mType = 0;
                break;
            }
            case R.id.radio_task_event:{
                mType = 1;
                break;
            }
        }
        return true;
    }

    private boolean validateTitle(){
        String title = text_task_title.getEditText().getText().toString().trim();
        if (title.isEmpty()) {
            text_task_title.setError("Field can't be empty");
            return false;
        }
        else if(title.length() > TITLE_MAX_LENGTH){
            text_task_title.setError("Too long!");
            return false;

        }
        else {
            mTitle = title;
            text_task_title.setError(null);
            return true;
        }
    }

    private boolean validateDateTime(){
        if(c.before(Calendar.getInstance())){
            inputLayout_datetime.setError("Select valid Date-Time.");
            return false;
        }
        else {
            inputLayout_datetime.setError(null);
            mUnixTime = c.getTimeInMillis();
            return true;
        }
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        c.set(Calendar.YEAR,year);
        c.set(Calendar.MONTH,month);
        c.set(Calendar.DAY_OF_MONTH,dayOfMonth);
        String currentDate = DateFormat.getDateInstance(DateFormat.FULL).format(c.getTime());

        mDate = currentDate;
        button_datepick.setText(currentDate);
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        c.set(Calendar.HOUR_OF_DAY,hourOfDay);
        c.set(Calendar.MINUTE,minute);
        c.set(Calendar.SECOND,0);
        String  currentTime = "";
        if(hourOfDay < 10){currentTime += "0";}
        currentTime += String.valueOf(hourOfDay);
        currentTime += ":";
        if(minute < 10){currentTime += "0";}
        currentTime += String.valueOf(minute);

        mTime = currentTime;
        button_timepick.setText(currentTime);
    }

    private class AddtoTasksAsync extends AsyncTask<Void,Void,Long>{
        @Override
        protected Long doInBackground(Void... voids) {
            TaskHelper dbHelper = new TaskHelper(getBaseContext());
            SQLiteDatabase db = dbHelper.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(TaskContract.TaskEntry._ID,m_ID);
            values.put(TaskContract.TaskEntry.TASK_TYPE,mType);
            values.put(TaskContract.TaskEntry.TASK_TITLE,mTitle);
            values.put(TaskContract.TaskEntry.TASK_DESCRIPTION,mDescription);
            values.put(TaskContract.TaskEntry.TASK_DATE,mDate);
            values.put(TaskContract.TaskEntry.TASK_TIME,mTime);
            values.put(TaskContract.TaskEntry.TASK_UNIX_TIME,mUnixTime);
            values.put(TaskContract.TaskEntry.TASK_TYPE,mType);
            long ret = db.insert(TaskContract.TaskEntry.TABLE_NAME,null,values);
            return ret;
        }

        @Override
        protected void onPostExecute(Long aLong) {
            if(aLong != -1){
                Snackbar.make(button_done, "Task added", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                new removeFromUniqueIDasync().execute();
            }
            else {
                Snackbar.make(button_done, "Error! Task not added", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        }
    }

    private class removeFromUniqueIDasync extends AsyncTask<Void,Void,Void>{
        @Override
        protected Void doInBackground(Void... voids) {
            UniqueIDHelper dbHelper = new UniqueIDHelper(getBaseContext());
            dbHelper.deleteID(UNIVERSAL_ID_INT);
            return null;
        }
    }

    private void StartAlarm(Calendar c){
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this,AlarmReceiver.class);
        intent.putExtra(KEY_1,UNIVERSAL_ID_INT);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this,UNIVERSAL_ID_INT,intent,PendingIntent.FLAG_UPDATE_CURRENT);

        alarmManager.setExact(AlarmManager.RTC_WAKEUP,c.getTimeInMillis(),pendingIntent);
    }
}