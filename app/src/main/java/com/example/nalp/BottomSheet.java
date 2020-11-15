package com.example.nalp;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class BottomSheet extends BottomSheetDialogFragment {

    private String Title;
    private String Description;
    private TextView InfoTaskTitle;
    private TextView InfoTaskDesc;

   public BottomSheet(String title,String desc){
       Title = title;
       Description = desc;
   }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.bottom_sheet_info,container,false);
        InfoTaskTitle = v.findViewById(R.id.info_task_title);
        InfoTaskTitle.setText(Title);
        InfoTaskDesc = v.findViewById(R.id.info_task_description);
        InfoTaskDesc.setText(Description);
        return v;
    }


}
