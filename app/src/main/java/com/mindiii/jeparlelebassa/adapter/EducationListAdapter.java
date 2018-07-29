package com.mindiii.jeparlelebassa.adapter;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mindiii.jeparlelebassa.EducationActivity;
import com.mindiii.jeparlelebassa.LessonActivity;
import com.mindiii.jeparlelebassa.R;
import com.mindiii.jeparlelebassa.model.EducationInfo;

import java.util.ArrayList;

/**
 * Created by mindiii on 17/4/17.
 */

public class EducationListAdapter extends RecyclerView.Adapter<EducationListAdapter.ViewHolder> {


    ArrayList<EducationInfo> eduactionList;
    Activity activity;

    public EducationListAdapter(ArrayList<EducationInfo> eduactionList, Activity activity) {
        this.eduactionList = eduactionList;
        this.activity = activity;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.adpter_view_lesson,parent,false);
        EducationListAdapter.ViewHolder view = new EducationListAdapter.ViewHolder(v);
        return view;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {

        holder.tvLessonName.setText(eduactionList.get(position).LessonName);
        holder.tvSerialnum.setText(position+1+"");
    }

    @Override
    public int getItemCount() {
        return eduactionList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

            private TextView tvSerialnum,tvLessonName;
        private LinearLayout mainLayout;

        public ViewHolder(View itemView) {
            super(itemView);
            this.tvSerialnum = (TextView) itemView.findViewById(R.id.tv_serial_number);
            this.tvLessonName = (TextView) itemView.findViewById(R.id.tv_lesson);
            mainLayout = (LinearLayout) itemView.findViewById(R.id.mainLayout);
            mainLayout.setOnClickListener(this);

        }

        @Override
        public void onClick(View view) {

            EducationInfo info = eduactionList.get(getAdapterPosition());
            Bundle bundle = new Bundle();
         //   bundle.putString("lessonId", String.valueOf(eduactionList.get(getAdapterPosition()).LessonId));
            bundle.putSerializable("lesson",info);
            Intent intent = new Intent(activity, LessonActivity.class);
            intent.putExtras(bundle);
            activity.startActivity(intent);
        }
    }
}
