package com.example.oms_2;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

/**
 * This class is used to retrieve the references' value and display the (bid) cards for tutors.
 */
public class BidCardItemAdapter extends RecyclerView.Adapter<BidCardItemAdapter.BidCardViewHolder>{

    private ArrayList<BidCardItem> mBidCardList;
    private int whichCard;

    public static class BidCardViewHolder extends RecyclerView.ViewHolder{
        public TextView mbid_title, mline1_subject;
        public TextView mline2_qualif, mline3_sess, mline4_rate, mline5_time, mline6_days;
        public Button button_offer, button_message;

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

            button_offer = itemView.findViewById(R.id.button_offer);
            if (button_offer != null){
                button_offer.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(v.getContext(), TutorOfferForm.class);
                        v.getContext().startActivity(intent);
                    }
                });
            }

            button_message = itemView.findViewById(R.id.button_message);
            if (button_message != null){
                button_message.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(v.getContext(), MessagePrivate.class);
                        v.getContext().startActivity(intent);
                    }
                });
            }
        }
    }

    //constructor: pass items of the arraylist into the adapter
    public BidCardItemAdapter(ArrayList<BidCardItem> exList, int whichCardx){
        mBidCardList = exList;
        this.whichCard = whichCardx;
    }

    @NonNull
    @Override
    public BidCardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //pass layout of cards to this adapter
        View v = LayoutInflater.from(parent.getContext()).inflate(whichCard, parent, false);
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
