package com.iplplay2win.app;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Suraj Singh on 3/28/2017.
 */

public class AdapterTickets extends RecyclerView.Adapter<AdapterTickets.MyViewHolder> {

    private ArrayList<TicketModel> ticketList;

    public class MyViewHolder extends RecyclerView.ViewHolder{
        public TextView name,phone,iwant,ihave;

        public MyViewHolder(View view) {
            super(view);
            name=(TextView)view.findViewById(R.id.tv_name);
            phone=(TextView)view.findViewById(R.id.tv_phone);
            iwant=(TextView)view.findViewById(R.id.tv_iwant);
            ihave=(TextView)view.findViewById(R.id.tv_ihave);

        }
    }
    public AdapterTickets(ArrayList<TicketModel> ticketList){
        this.ticketList=ticketList;
//        ticketList.clear();
//        ticketList.addAll(ticketList);
//        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return ticketList.size();
    }
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView= LayoutInflater.from(parent.getContext()).inflate(R.layout.exchangecard,parent,false);
        return new MyViewHolder(itemView);
    }
    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        TicketModel ticketModel=ticketList.get(position);
        holder.name.setText(ticketModel.getName());
        holder.phone.setText(ticketModel.getPhone());
        holder.iwant.setText(ticketModel.getIwant());
        holder.ihave.setText(ticketModel.getIhave());

    }
}
