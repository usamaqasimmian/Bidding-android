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
 * This class is used to retrieve the references' value and display the (offer) cards to students.
 */
public class StudViewOffersAdapter extends RecyclerView.Adapter<StudViewOffersAdapter.StudViewOffersViewHolder> {

    private ArrayList<BidCardItem> aListY;
    private int whichCardY;

    /**
     * This class is used to retrieve the card's contents.
     */
    public static class StudViewOffersViewHolder extends RecyclerView.ViewHolder{
        public TextView tutorIdHere, mline1_subject;
        public TextView mline2_qualif, mline3_sess, mline4_rate, mline5_time, mline6_days;
        public TextView proceedClick;

        public StudViewOffersViewHolder(@NonNull View itemView) {
            super(itemView);
            tutorIdHere = itemView.findViewById(R.id.whatever_title);
            mline1_subject = itemView.findViewById(R.id.line1_subject);
            mline2_qualif = itemView.findViewById(R.id.line2_qualif);
            mline3_sess = itemView.findViewById(R.id.line3_sess);
            mline4_rate = itemView.findViewById(R.id.line4_rate);
            mline5_time = itemView.findViewById(R.id.line5_time);
            mline6_days = itemView.findViewById(R.id.line6_days);
            proceedClick = itemView.findViewById(R.id.clickable_text);
        }
    }

    //constructor
    public StudViewOffersAdapter(ArrayList<BidCardItem> aly, int wcy){
        aListY = aly;
        this.whichCardY = wcy;
    }

    @NonNull
    @Override
    public StudViewOffersViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(whichCardY, parent, false);
        return new StudViewOffersViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull StudViewOffersViewHolder holder, int position) {
        BidCardItem currentItemY = aListY.get(position);
        holder.tutorIdHere.setText(currentItemY.getmText0());
        holder.mline1_subject.setText(currentItemY.getmText1());
        holder.mline2_qualif.setText(currentItemY.getmText2());
        holder.mline3_sess.setText(currentItemY.getmText3());
        holder.mline4_rate.setText(currentItemY.getmText4());
        holder.mline5_time.setText(currentItemY.getmText5());
        holder.mline6_days.setText(currentItemY.getmText6());

        holder.proceedClick.setText(currentItemY.getmText7());
        holder.proceedClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), Contract.class);
                String tutorID = currentItemY.getmText7().substring(13);
                String subjectID = currentItemY.getmText6().substring(30, 66);
                String bidId = currentItemY.getmText0().substring(6);
                intent.putExtra("tutorid",tutorID);
                intent.putExtra("subjectid",subjectID);
                intent.putExtra("bidId", bidId);
                v.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return aListY.size();
    }
}
