package com.iplplay2win.app;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.Collections;
import java.util.List;

/**
 * Created by Anand on 24-03-2017.
 */

public class AdapterSchedule extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private LayoutInflater inflater;
    List<ScheduleData> data= Collections.emptyList();
    ScheduleData current;
    int currentPos=0;

    public AdapterSchedule(Context context, List<ScheduleData> data) {
        this.context=context;
        inflater= LayoutInflater.from(context);
        this.data=data;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view=inflater.inflate(R.layout.schedulecard, parent,false);
        MyHolder holder=new MyHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        // Get current position of item in recyclerview to bind data and assign values from list
        MyHolder myHolder= (MyHolder) holder;
        ScheduleData current=data.get(position);
        myHolder.Date.setText(current.date);
        myHolder.Day.setText(current.day);
        myHolder.Time.setText(current.time);
        myHolder.Place.setText(current.place);
        myHolder.TeamAShort_Name.setText(current.teamAShort_name);
        myHolder.TeamBShort_Name.setText(current.teamBShort_name);



        // load image into imageview using glide
        Glide.with(context).load(current.teamAlogo)
                .placeholder(R.drawable.ic_img_placeholder)
                .into(myHolder.teamAlogo);
        Glide.with(context).load(current.teamBlogo)
                .placeholder(R.drawable.ic_img_placeholder)
                .into(myHolder.teamBlogo);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    private class MyHolder extends RecyclerView.ViewHolder {
        ImageView teamAlogo, teamBlogo;
        TextView Day, Time, Place, Date, TeamAShort_Name, TeamBShort_Name;

        public MyHolder(View itemView) {
            super(itemView);

            teamAlogo=(ImageView)itemView.findViewById(R.id.teamA_logo);
            teamBlogo=(ImageView)itemView.findViewById(R.id.teamB_logo);

            TeamAShort_Name = (TextView)itemView.findViewById(R.id.teamA_text);
            TeamBShort_Name = (TextView)itemView.findViewById(R.id.teamB_text);

            Day =(TextView) itemView.findViewById(R.id.day);
            Time =(TextView) itemView.findViewById(R.id.match_time);
            Date =(TextView) itemView.findViewById(R.id.match_date);
            Place =(TextView) itemView.findViewById(R.id.place_schedule);
        }
    }
}
