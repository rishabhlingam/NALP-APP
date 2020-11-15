package com.example.nalp;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.PopupMenu;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.nalp.DATABASE.TaskContract;
import com.example.nalp.DATABASE.TaskHelper;
import com.example.nalp.DATABASE.UniqueIDHelper;
import com.example.nalp.NotificationUtility.AlarmReceiver;

import java.util.ArrayList;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.TaskViewHolder> {

    private final int Viewof;
    private Cursor mCursor;
    private Context mContext;

    public static class TaskViewHolder extends RecyclerView.ViewHolder{
        public TextView mTaskTitle;
        public TextView mTaskDescription;
        public TextView mTaskDate;
        public TextView mTaskTime;
        public ImageButton mImageButton;
        public TaskViewHolder(@NonNull View itemView) {
            super(itemView);
            mTaskTitle = itemView.findViewById(R.id.task_title);
            mTaskDate = itemView.findViewById(R.id.task_date);
            mTaskTime = itemView.findViewById(R.id.task_time);
            mImageButton = itemView.findViewById(R.id.menu_button);
        }

    }

    public TaskAdapter(Cursor cursor, Context context,int val){
        mContext = context;
        mCursor = cursor;
        Viewof = val;
    }

    @NonNull
    @Override
    public TaskViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.task_item,parent,false);
        TaskViewHolder tvh = new TaskViewHolder(v);
        return tvh;
    }

    @Override
    public void onBindViewHolder(@NonNull final TaskViewHolder holder, int position) {
        if(!mCursor.moveToPosition(position)){return;}
        String title = mCursor.getString(mCursor.getColumnIndexOrThrow(TaskContract.TaskEntry.TASK_TITLE));
        String date = mCursor.getString(mCursor.getColumnIndexOrThrow(TaskContract.TaskEntry.TASK_DATE));
        String time = mCursor.getString(mCursor.getColumnIndexOrThrow(TaskContract.TaskEntry.TASK_TIME));
        Integer id = mCursor.getInt(mCursor.getColumnIndexOrThrow(TaskContract.TaskEntry._ID));
        String description = mCursor.getString(mCursor.getColumnIndexOrThrow(TaskContract.TaskEntry.TASK_DESCRIPTION));

        holder.mTaskTitle.setText(title);
        holder.mTaskDate.setText(date);
        holder.mTaskTime.setText(time);
        holder.itemView.setTag(R.string.KEY_ID,id);
        holder.itemView.setTag(R.string.KEY_DESC,description);

        holder.mImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popup = new PopupMenu(mContext, v);
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.delete_task:
                                deleteTask(holder);
                                return true;
                            default:
                                return false;
                        }
                    }
                });
                popup.inflate(R.menu.taskitem_dropdown);
                popup.show();
            }
        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BottomSheet sheet = new BottomSheet(holder.mTaskTitle.getText().toString(),holder.itemView.getTag(R.string.KEY_DESC).toString());
                sheet.show(((FragmentActivity)mContext).getSupportFragmentManager(),"InfoBottomSheet");
            }
        });

    }

    @Override
    public int getItemCount() {
        return mCursor.getCount();
    }

    private void deleteTask(TaskViewHolder holder){
        Integer id =  Integer.parseInt(holder.itemView.getTag(R.string.KEY_ID).toString());
        TaskHelper taskHelper = new TaskHelper(mContext);
        UniqueIDHelper uniqueIDHelper = new UniqueIDHelper(mContext);
        taskHelper.deleteTask(id);
        uniqueIDHelper.addID(id);
        if(Viewof == 0){
            swapCursor(taskHelper.getAllRemainders());
        }
        else if(Viewof == 1){
            swapCursor(taskHelper.getAllEvents());
        }
        AlarmManager alarmManager = (AlarmManager) mContext.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(mContext, AlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(mContext,id,intent,PendingIntent.FLAG_UPDATE_CURRENT);
        alarmManager.cancel(pendingIntent);
    }

    public void swapCursor(Cursor newCursor) {
        if (mCursor != null) {
            mCursor.close();
        }
        mCursor = newCursor;
        if (newCursor != null) {
            notifyDataSetChanged();
        }
    }
}
