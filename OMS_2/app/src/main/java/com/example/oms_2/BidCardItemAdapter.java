package com.example.oms_2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

/**
 * This class is used to retrieve the references' value and display the (bid) cards for tutors.
 */
public class BidCardItemAdapter extends RecyclerView.Adapter<BidCardItemAdapter.BidCardViewHolder>{

    private ArrayList<BidCardItem> mBidCardList;

    public static class BidCardViewHolder extends RecyclerView.ViewHolder{
        public TextView mbid_title, mline1_subject;
        public TextView mline2_qualif, mline3_sess, mline4_rate, mline5_time, mline6_days;

        public BidCardViewHolder(@NonNull View itemView) {
            super(itemView);
            //references
            mbid_title = itemView.findViewById(R.id.bid_title);
            mline1_subject = itemView.findViewById(R.id.line1_subject);
            mline2_qualif = itemView.findViewById(R.id.line2_qualif);
            mline3_sess = itemView.findViewById(R.id.line3_sess);
            mline4_rate = itemView.findViewById(R.id.line4_rate);
            mline5_time = itemView.findViewById(R.id.line5_time);
            mline6_days = itemView.findViewById(R.id.line6_days);
        }
    }

    //constructor: pass items of the arraylist into the adapter
    public BidCardItemAdapter(ArrayList<BidCardItem> exList){
        mBidCardList = exList;
    }

    @NonNull
    @Override
    public BidCardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //pass layout of cards to this adapter
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.bid_card_item_card, parent, false);
        //create viewholder with this
        BidCardViewHolder evh = new BidCardViewHolder(v);
        return evh;     //now we have the viewholder
    }

    @Override
    public void onBindViewHolder(@NonNull BidCardViewHolder holder, int position) {
        //pass values to references; the values (info) retrieved from arraylist
        BidCardItem currentItem = mBidCardList.get(position);

        //call getter methods on items to get the info out of it to pass it to views in viewholder
        holder.mbid_title.setText(currentItem.getmText0());
        holder.mline1_subject.setText(currentItem.getmText1());
        holder.mline2_qualif.setText(currentItem.getmText2());
        holder.mline3_sess.setText(currentItem.getmText3());
        holder.mline4_rate.setText(currentItem.getmText4());
        holder.mline5_time.setText(currentItem.getmText5());
        holder.mline6_days.setText(currentItem.getmText6());
    }


    @Override
    public int getItemCount() {
        //define how many items there will be in the list
        return mBidCardList.size();
    }


}