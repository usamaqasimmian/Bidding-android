package com.example.oms_2;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

/**
 * This class is used to retrieve the references' value and display the (tutor's personal details) cards to students.
 */
public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder> {

    private ArrayList<TutorViewItems> data;

    //constructor
    public RecyclerAdapter(ArrayList<TutorViewItems> _data) {
        super();
        data = _data;
    }

    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.view_all_tutors_card, viewGroup, false); //CardView inflated as RecyclerView list item
        ViewHolder viewHolder = new ViewHolder(v);

        return viewHolder;

    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {
        viewHolder.tutorName.setText(data.get(position).getTutorName());
        viewHolder.tutorQualification.setText(data.get(position).getTutorQualification());
        viewHolder.level.setText(data.get(position).getTutorLevel());

        viewHolder.itemView.setOnClickListener(new View.OnClickListener() { //set back to itemView for students
            @Override
            public void onClick(View v) {
                Intent intent = new Intent (v.getContext(), Contract.class);
                String tutorID = data.get(position).getTutorId();
                String subjectID = data.get(position).getSubjectId();
                intent.putExtra("tutorid",tutorID);
                intent.putExtra("subjectid",subjectID);
                v.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    /**
     * This class is used to retrieve the card's contents.
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {
        public View itemView;
        public TextView tutorName;
        public TextView tutorQualification;
        public TextView level;

        public ViewHolder(View itemView) {
            super(itemView);
            this.itemView = itemView;
            tutorName = itemView.findViewById(R.id.tutor_name);
            tutorQualification = itemView.findViewById(R.id.tutor_qualification);
            level = itemView.findViewById(R.id.level);
        }
    }

}





