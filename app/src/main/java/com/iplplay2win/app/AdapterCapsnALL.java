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
 * Created by Anand on 08-04-2017.
 */

class AdapterCapsnALL extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private LayoutInflater inflater;
    List<CapsNAll> data= Collections.emptyList();

    public AdapterCapsnALL (Context context, List<CapsNAll> data) {

        this.context=context;
        inflater= LayoutInflater.from(context);
        this.data=data;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view=inflater.inflate(R.layout.capsnall, parent,false);
        AdapterCapsnALL.MyHolder holder=new AdapterCapsnALL.MyHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        // Get current position of item in recyclerview to bind data and assign values from list
        AdapterCapsnALL.MyHolder myHolder= (MyHolder) holder;
        final CapsNAll current=data.get(position);
        Log.e("onBindViewHolder", data.size() + " position:" + position + " Heading :" + current.Heading +  " Image Link:" + current.ImageInfo
        );
        myHolder.heading.setText(current.Heading);

        // load image into imageview using glide
        Glide.with(context).load(current.ImageInfo)
                .asBitmap()
                .encoder(new BitmapEncoder(Bitmap.CompressFormat.PNG,100))
                .placeholder(R.color.cardview_light_background)
                .format(PREFER_ARGB_8888)
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .error(R.drawable.ic_img_error)
                .into(myHolder.info_image);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    private class MyHolder extends RecyclerView.ViewHolder {
        ImageView info_image;
        TextView heading;
        //TextView teamID;

        public MyHolder(View itemView) {
            super(itemView);

            info_image=(ImageView)itemView.findViewById(R.id.infoImage);

            heading =(TextView) itemView.findViewById(R.id.Heading);
            //    teamID=(TextView)itemView.findViewById(R.id.teamID);
        }
    }
   
}
