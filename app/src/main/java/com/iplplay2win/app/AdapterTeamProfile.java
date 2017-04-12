package com.iplplay2win.app;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
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
 * Created by Anand on 25-03-2017.
 */

class AdapterTeamProfile extends RecyclerView.Adapter<RecyclerView.ViewHolder>  {


    private Context context;
    private LayoutInflater inflater;
    List<Team_ProfileData> data= Collections.emptyList();
    TeamData current;
    int currentPos=0;

    public AdapterTeamProfile(Context context, List<Team_ProfileData> data) {

        this.context=context;
        inflater= LayoutInflater.from(context);
        this.data=data;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view=inflater.inflate(R.layout.playercard, parent,false);
        MyHolder holder=new MyHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        // Get current position of item in recyclerview to bind data and assign values from list
        MyHolder myHolder= (MyHolder) holder;
        final Team_ProfileData current=data.get(position);
        Log.e("onBindViewHolder", data.size() + " position:" + position + " id:" + current.playerid +  " name:" + current.player_name);
        myHolder.player_Name.setText(current.player_name);

        // load image into imageview using glide
        Glide.with(context).load(current.player_image)
                .asBitmap()
                .encoder(new BitmapEncoder(Bitmap.CompressFormat.PNG,100))
                .placeholder(R.color.cardview_light_background)
                .format(PREFER_ARGB_8888)
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .error(R.drawable.ic_img_error)
                .into(myHolder.player_image);

        myHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent teamdetails = new Intent(context,player_profile.class);

                try {
                    Log.e("TeamID", "onClick " + current.playerid);
                    teamdetails.putExtra("PlayerID", current.playerid + "");
                    context.startActivity(teamdetails);
                }
                catch(NullPointerException e){
                    e.printStackTrace();
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    private class MyHolder extends RecyclerView.ViewHolder {
        ImageView player_image;
        TextView player_Name;
        //TextView teamID;

        public MyHolder(View itemView) {
            super(itemView);

            player_image=(ImageView)itemView.findViewById(R.id.player_image);

            player_Name =(TextView) itemView.findViewById(R.id.player_name);
            //    teamID=(TextView)itemView.findViewById(R.id.teamID);
        }
    }
}
