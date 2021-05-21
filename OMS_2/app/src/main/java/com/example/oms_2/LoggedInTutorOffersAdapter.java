package com.example.oms_2;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

/**
 * This class is used to retrieve the references' value and display the (offer) cards to tutors.
 */
public class LoggedInTutorOffersAdapter extends RecyclerView.Adapter<LoggedInTutorOffersAdapter.UpdateOfferViewHolder>{

    private static String messageIDHolder;
    private ArrayList<CardItem> pOfferCardList;
    private int pWhichCard;

    //getter and setter
    public static String getMessageIDHolder(){ return messageIDHolder; }
    public static void setMessageIDHolder(String mIDHolfer){ LoggedInTutorOffersAdapter.messageIDHolder = mIDHolfer; }

    /**
     * This class is used to retrieve the card's contents.
     */
    public static class UpdateOfferViewHolder extends RecyclerView.ViewHolder{
        public TextView litocv_offerid, litocv_content_type, litocv_tutor_given_name, litocv_tutor_last_name;
        public TextView litocv_addinfo_rate, litocv_addinfo_hour, litocv_addinfo_session;
        public TextView litocv_addinfo_moreInfo, litocv_addinfo_qualifs, litocv_addinfo_compLvl;
        public TextView litocv_clickable_update_text;

        public UpdateOfferViewHolder(@NonNull View itemView) {
            super(itemView);
            litocv_offerid = itemView.findViewById(R.id.litocv_offerid);
            litocv_content_type = itemView.findViewById(R.id.litocv_content_type);
            litocv_tutor_given_name = itemView.findViewById(R.id.litocv_tutor_given_name);
            litocv_tutor_last_name = itemView.findViewById(R.id.litocv_tutor_last_name);
            litocv_addinfo_rate = itemView.findViewById(R.id.litocv_addinfo_rate);
            litocv_addinfo_hour = itemView.findViewById(R.id.litocv_addinfo_hour);
            litocv_addinfo_session = itemView.findViewById(R.id.litocv_addinfo_session);
            litocv_addinfo_moreInfo = itemView.findViewById(R.id.litocv_addinfo_moreInfo);
            litocv_addinfo_qualifs = itemView.findViewById(R.id.litocv_addinfo_qualifs);
            litocv_addinfo_compLvl = itemView.findViewById(R.id.litocv_addinfo_compLvl);
            litocv_clickable_update_text = itemView.findViewById(R.id.litocv_clickable_update_text);
        }
    }

    //constructor
    public LoggedInTutorOffersAdapter(ArrayList<CardItem> alist, int pWhichCard){
        pOfferCardList = alist;
        this.pWhichCard = pWhichCard;
    }

    @NonNull
    @Override
    public UpdateOfferViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //pass layout of cards to this adapter
        View w = LayoutInflater.from(parent.getContext()).inflate(pWhichCard, parent, false);
        //create viewholder with this
        UpdateOfferViewHolder evhw = new UpdateOfferViewHolder(w);
        return evhw;
    }

    @Override
    public void onBindViewHolder(@NonNull UpdateOfferViewHolder holder, int position) {
        //pass values to references; the values (info) retrieved from arraylist
        CardItem currentItem = pOfferCardList.get(position);

        //call getter methods on items to get the info out of it to pass it to views in viewholder
        holder.litocv_offerid.setText(currentItem.getmText1());
        holder.litocv_content_type.setText(currentItem.getmText2());
        holder.litocv_tutor_given_name.setText(currentItem.getmText3());
        holder.litocv_tutor_last_name.setText(currentItem.getmText4());
        holder.litocv_addinfo_rate.setText(currentItem.getmText5());
        holder.litocv_addinfo_hour.setText(currentItem.getmText6());
        holder.litocv_addinfo_session.setText(currentItem.getmText7());
        holder.litocv_addinfo_moreInfo.setText(currentItem.getNtext8());
        holder.litocv_addinfo_qualifs.setText(currentItem.getNtext9());
        holder.litocv_addinfo_compLvl.setText(currentItem.getNtext10());

        holder.litocv_clickable_update_text.setText(currentItem.getNtext11());
        holder.litocv_clickable_update_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TutorLoggedIn.buttonCoUOfferForm = "toPatchOffer";
                Intent intent = new Intent(v.getContext(), TutorOfferForm.class);
                setMessageIDHolder(String.valueOf(holder.litocv_clickable_update_text.getText()).substring(13)); //from LoggedInTutorOffers "UPDATE OFFER:"
                v.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        //define how many items there will be in the list
        return pOfferCardList.size();
    }
}
