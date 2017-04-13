package com.iplplay2win.app;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Anand on 11-04-2017.
 */

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MyViewHolder> {

    private ArrayList<MessageModel> List;
Context context;
    public class MyViewHolder extends RecyclerView.ViewHolder{
        public TextView name,message,timing;

        public MyViewHolder(View view) {
            super(view);
//            name=(TextView)view.findViewById(R.id.chattersname);
//            message=(TextView)view.findViewById(R.id.chatemessage);
//            timing=(TextView)view.findViewById(R.id.timeago);
        }
    }
    public void newMessage (ArrayList<MessageModel> List){
        this.List=new ArrayList<>(List);
        List.clear();
//        List.notifyAll();
        notifyDataSetChanged();

    }

    public MessageAdapter(ArrayList<MessageModel> List, Context context){
        this.List=List;
        this.context= context;

    }

    @Override
    public int getItemCount() {
        return List.size();
    }
    @Override
    public MessageAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView= LayoutInflater.from(parent.getContext()).inflate(R.layout.sendmessage,parent,false);
        return new MessageAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        MessageModel messageModel=List.get(position);
        holder.name.setText(messageModel.getName());
        holder.message.setText(messageModel.getMessage());
        holder.timing.setText(messageModel.getTiming());
    }

}