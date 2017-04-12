package com.iplplay2win.app;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.BitmapEncoder;

import java.util.Collections;
import java.util.List;

import static com.bumptech.glide.load.DecodeFormat.PREFER_ARGB_8888;

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
        final ScheduleData current=data.get(position);
        myHolder.Date.setText(current.date);
        myHolder.Day.setText(current.day);
        myHolder.Time.setText(current.time);
        myHolder.Place.setText(current.place);
        myHolder.TeamAShort_Name.setText(current.teamAShort_name);
        myHolder.TeamBShort_Name.setText(current.teamBShort_name);

        // load image into imageview using glide
        Glide.with(context)
                .load(current.teamAlogo)
                .asBitmap()
                .encoder(new BitmapEncoder(Bitmap.CompressFormat.PNG,100))
                .placeholder(R.color.cardview_light_background)
                .format(PREFER_ARGB_8888)
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .into(myHolder.teamAlogo);

        Glide.with(context).load(current.teamBlogo)
                .asBitmap()
                .encoder(new BitmapEncoder(Bitmap.CompressFormat.PNG,100))
                .placeholder(R.color.cardview_light_background)
                .format(PREFER_ARGB_8888)
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .into(myHolder.teamBlogo);

       myHolder.predictcta.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               Intent predictcall = new Intent(context,login.class);
               predictcall.putExtra("Activity","p2w");
               predictcall.putExtra("SCHEDULEID",current.schedule_id);
               context.startActivity(predictcall);
           }
       });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    private class MyHolder extends RecyclerView.ViewHolder {
        ImageView teamAlogo, teamBlogo;
        TextView Day, Time, Place, Date, TeamAShort_Name, TeamBShort_Name;
        CardView predictcta;

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
            predictcta = (CardView) itemView.findViewById(R.id.predictcta);
        }
    }
}
