package com.iplplay2win.app;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.Collections;
import java.util.List;

/**
 * Created by Anand on 08-04-2017.
 */

class AdapterPoints extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context context;
    private LayoutInflater inflater;
    List<PointsData> data= Collections.emptyList();
    PointsData current;
    public AdapterPoints (Context context, List<PointsData> data) {

        this.context=context;
        inflater= LayoutInflater.from(context);
        this.data=data;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view=inflater.inflate(R.layout.tablepoint, parent,false);
        AdapterPoints.MyHolder holder=new MyHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        // Get current position of item in recyclerview to bind data and assign values from list
        AdapterPoints.MyHolder myHolder= (MyHolder) holder;
        final PointsData current=data.get(position);
       // Log.e("onBindViewHolder", data.size() + " position:" + position + " Heading :" + current.Heading +  " Image Link:" + current.ImageInfo
//        );
        // load image into imageview using glide

        myHolder.totalmatch_list.setText(current.totalmatch_list);
        myHolder.totalwin_list.setText(current.totalwin_list);
        myHolder.totallost_list.setText(current.totallost_list);
        myHolder.teamnamelist.setText(current.teamnamelist);
        myHolder.totaldraw_list.setText(current.totaldraw_list);
        myHolder.totalnrr_list.setText(current.totalnrr_list);
        myHolder.totalpoints_list.setText(current.totalpoints_list);

    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    private class MyHolder extends RecyclerView.ViewHolder {
        TextView teamnamelist, totalmatch_list, totalwin_list, totallost_list,totaldraw_list,totalnrr_list,totalpoints_list;
        //TextView teamID;

        public MyHolder(View itemView) {
            super(itemView);

            teamnamelist =(TextView) itemView.findViewById(R.id.teamname_list);
            totalmatch_list =(TextView) itemView.findViewById(R.id.totalmatch_list);
            totalwin_list =(TextView) itemView.findViewById(R.id.totalwin_list);
            totallost_list =(TextView) itemView.findViewById(R.id.totallost_list);
            totaldraw_list =(TextView) itemView.findViewById(R.id.totaldraw_list);
            totalnrr_list =(TextView) itemView.findViewById(R.id.totalnrr_list);
            totalpoints_list =(TextView) itemView.findViewById(R.id.totalpoints_list);
        }
    }
}
