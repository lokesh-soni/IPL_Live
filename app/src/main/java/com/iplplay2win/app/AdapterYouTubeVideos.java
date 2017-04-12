package com.iplplay2win.app;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
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
 * Created by souravkarmakar on 30/03/17.
 */

public class AdapterYouTubeVideos  extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private LayoutInflater inflater;
    List<YouTubeData> data= Collections.emptyList();
    TeamData current;
    int currentPos=0;

    public AdapterYouTubeVideos(Context context, List<YouTubeData> data) {
        this.context=context;
        inflater= LayoutInflater.from(context);
        this.data=data;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view=inflater.inflate(R.layout.singlevideocard, parent,false);
        MyHolder holder=new MyHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
// Get current position of item in recyclerview to bind data and assign values from list
        MyHolder myHolder= (MyHolder) holder;

        final YouTubeData current =data.get(position);
        Log.e("onBindViewHolder", data.size() + " position:" + position + " title:" + current.title +  " thumbnail:" + current.thumbnail);
        myHolder.VideoTitle.setText(current.title);

        String videothumbnailurl = "http://img.youtube.com/vi/"+current.thumbnail+"/mqdefault.jpg";

        // load image into imageview using glide
        Glide.with(context).load(videothumbnailurl)
                .asBitmap()
                .encoder(new BitmapEncoder(Bitmap.CompressFormat.PNG,100))
                .placeholder(R.color.cardview_light_background)
                .format(PREFER_ARGB_8888)
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .error(R.drawable.ic_img_error)
                .into(myHolder.videothumbnail);

        myHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent lightboxIntent = new Intent(context, single_video.class);
                lightboxIntent.putExtra(single_video.KEY_VIDEO_ID, current.thumbnail);
                context.startActivity(lightboxIntent);
            }

        });
        myHolder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Intent lightboxIntent = new Intent(context, single_video.class);
                lightboxIntent.putExtra(single_video.KEY_VIDEO_ID, current.thumbnail);
                context.startActivity(lightboxIntent);
                return false;
            }
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    private class MyHolder extends RecyclerView.ViewHolder {
        ImageView videothumbnail;
        TextView VideoTitle;
        //TextView teamID;

        public MyHolder(View itemView) {
            super(itemView);

            videothumbnail=(ImageView)itemView.findViewById(R.id.youtubethumbnail);
            VideoTitle =(TextView) itemView.findViewById(R.id.videotitle);
            //    teamID=(TextView)itemView.findViewById(R.id.teamID);
        }
    }
}