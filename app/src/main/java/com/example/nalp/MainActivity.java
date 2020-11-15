package com.example.nalp;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.example.nalp.DATABASE.TaskContract;
import com.example.nalp.DATABASE.TaskHelper;
import com.example.nalp.DATABASE.UniqueIDContract;
import com.example.nalp.DATABASE.UniqueIDHelper;
import com.example.nalp.NotificationUtility.NotificationHelper;
import com.example.nalp.ui.main.SectionsPagerAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;

public class MainActivity extends AppCompatActivity {


    private FloatingActionButton fab;
    public static final String UNNIVERSAL_ID = "PENDINGINTENT_NOTIFICATION_TASH_UNIQUEID_ID";
    public static final int ADD_TASK_REQUEST = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(this, getSupportFragmentManager());
        ViewPager viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(sectionsPagerAdapter);
        TabLayout tabs = findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewPager);

        NotificationHelper notificationHelper = new NotificationHelper(getApplicationContext(),null);

        TaskHelper taskHelper = new TaskHelper(this);
        UniqueIDHelper uniqueIDHelper = new UniqueIDHelper(this);

        fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UniqueIDHelper dbHelper = new UniqueIDHelper(getApplicationContext());
                SQLiteDatabase db = dbHelper.getReadableDatabase();
                Cursor cursor = dbHelper.getID();
                if(cursor.getCount() != 1){
                    Snackbar.make(fab, "Too many remainders!", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }
                else {
                    cursor.moveToNext();
                    int universalID = cursor.getInt(cursor.getColumnIndexOrThrow(UniqueIDContract.UniqueIDEntry._ID));
                    Intent intent = new Intent(MainActivity.this,AddActivity.class);
                    intent.putExtra(UNNIVERSAL_ID,universalID);
                    startActivityForResult(intent,ADD_TASK_REQUEST);
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }
}