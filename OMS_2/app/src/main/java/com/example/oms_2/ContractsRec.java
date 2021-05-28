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
 * The adapter class for ViewContracts class.
 */
public class ContractsRec extends  RecyclerView.Adapter<ContractsRec.ViewHolder> {

    private final ArrayList<ContractViewItems> data;

    public ContractsRec(ArrayList<ContractViewItems> _data) {
        super();
        data = _data;
    }


    @NonNull
    @Override
    public ContractsRec.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.view_all_contracts_card, viewGroup, false); //CardView inflated as RecyclerView list item

        return new ViewHolder(v);

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        holder.studentID.setText(data.get(position).getStudentID());
        holder.tutorID.setText(data.get(position).getTutorID());
        holder.dateCreated.setText(data.get(position).getDateCreated());
        holder.dateExpired.setText(data.get(position).getDateExpired());
        holder.payment.setText(data.get(position).getPayment());
        holder.lesson.setText( data.get(position).getLesson());
        holder.subjid.setText(data.get(position).getSubjectIdH());

        if (StudentLoggedIn.doWhatContract.equals("same tutor contract")) {
            holder.itemView.setOnClickListener(new View.OnClickListener() { //set back to itemView for students
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(v.getContext(), Contract.class);
                    v.getContext().startActivity(intent);
                }
            });
        } else if (StudentLoggedIn.doWhatContract.equals("different tutor contract")){
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(v.getContext(), ContractDiffTutor.class);
                    String subjIDHere = data.get(position).getSubjectIdH().substring(12);   //from ViewContracts "Subject ID: " (id is after 12 chars)
                    intent.putExtra("subjIDFromHere", subjIDHere);
                    v.getContext().startActivity(intent);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public View itemView;
        public TextView studentID;
        public TextView tutorID;
        public TextView dateCreated;
        public TextView dateExpired;
        public TextView payment;
        public TextView lesson;
        public TextView subjid;


        public ViewHolder (View itemView) {
            super(itemView);
            this.itemView = itemView;
            tutorID = itemView.findViewById(R.id.tutor_id);
            studentID = itemView.findViewById(R.id.student_id);
            dateCreated = itemView.findViewById(R.id.date_created);
            dateExpired = itemView.findViewById(R.id.date_expired);
            payment = itemView.findViewById(R.id.rate);
            lesson = itemView.findViewById(R.id.lesson);
            subjid = itemView.findViewById(R.id.subjid);

        }
    }
}

