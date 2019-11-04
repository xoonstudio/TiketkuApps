package com.xoonstudio.tiketku;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class TicketAdapter extends RecyclerView.Adapter<TicketAdapter.MyViewHolder>{
    Context context;
    ArrayList<TicketItem> ticketItem;

    public TicketAdapter(Context c, ArrayList<TicketItem> a){
        context = c;
        ticketItem = a;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // create inflate
       return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.ticket_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        // create binding item
       holder.xtravel_name.setText(ticketItem.get(position).getTravel_name());
       holder.xtravel_location.setText(ticketItem.get(position).getTravel_location());
       holder.xtotal_ticket.setText(ticketItem.get(position).getTotal_ticket());

       // using for the intent
        final String travel_name = ticketItem.get(position).getTravel_name();
        final String transaction_number = ticketItem.get(position).getTransaction_number();
        final String total_price = ticketItem.get(position).getTotal_price();

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // moving activity
                Intent gotomyticketdetail = new Intent(context, MyTicketDetail.class);
                gotomyticketdetail.putExtra("travel_name", travel_name);
                gotomyticketdetail.putExtra("total_price", total_price);

                gotomyticketdetail.putExtra("transaction_number", transaction_number);
                context.startActivity(gotomyticketdetail);
            }
        });
    }

    @Override
    public int getItemCount() {
        return ticketItem.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView xtravel_name, xtravel_location, xtotal_ticket;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            // load the element
            xtravel_name = itemView.findViewById(R.id.xtravel_name);
            xtravel_location = itemView.findViewById(R.id.xtravel_location);
            xtotal_ticket = itemView.findViewById(R.id.xtotal_ticket);
        }
    }
}
