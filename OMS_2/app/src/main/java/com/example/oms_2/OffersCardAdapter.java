package com.example.oms_2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

/**
 * This class is used to retrieve the references' value and display the (offer) cards to tutors for open bids.
 */
public class OffersCardAdapter extends RecyclerView.Adapter<OffersCardAdapter.OffersCardViewHolder> {

    private ArrayList<BidCardItem> nOffersList;
    private int whichCardX;

    /**
     * This class is used to retrieve the card's contents.
     */
    public static class OffersCardViewHolder extends RecyclerView.ViewHolder {
        public TextView view_offer_title, view_offer_givenName, view_offer_familyName;
        public TextView view_offer_rateX, view_offer_session, view_offer_moreInfo, view_offer_qualifsX, view_offer_complvl;

        public OffersCardViewHolder(@NonNull View itemView) {
            super(itemView);
            view_offer_title = itemView.findViewById(R.id.view_offer_title);
            view_offer_givenName = itemView.findViewById(R.id.view_offer_givenName);
            view_offer_familyName = itemView.findViewById(R.id.view_offer_familyName);
            view_offer_rateX = itemView.findViewById(R.id.view_offer_rateX);
            view_offer_session = itemView.findViewById(R.id.view_offer_session);
            view_offer_moreInfo = itemView.findViewById(R.id.view_offer_moreInfo);
            view_offer_qualifsX = itemView.findViewById(R.id.view_offer_qualifsX);
            view_offer_complvl = itemView.findViewById(R.id.view_offer_complvl);
        }
    }

    //constructor
    public OffersCardAdapter(ArrayList<BidCardItem> offerClist, int whichOffCard){
        nOffersList = offerClist;
        this.whichCardX = whichOffCard;
    }

    @NonNull
    @Override
    public OffersCardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(whichCardX, parent, false);
        return new OffersCardViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull OffersCardViewHolder holder, int position) {
        BidCardItem currentItemX = nOffersList.get(position);

        holder.view_offer_title.setText(currentItemX.getmText0());
        holder.view_offer_givenName.setText(currentItemX.getmText1());
        holder.view_offer_familyName.setText(currentItemX.getmText2());
        holder.view_offer_rateX.setText(currentItemX.getmText3());
        holder.view_offer_session.setText(currentItemX.getmText4());
        holder.view_offer_moreInfo.setText(currentItemX.getmText5());
        holder.view_offer_qualifsX.setText(currentItemX.getmText6());
        holder.view_offer_complvl.setText(currentItemX.getmText7());
    }

    @Override
    public int getItemCount() {
        return nOffersList.size();
    }
}
