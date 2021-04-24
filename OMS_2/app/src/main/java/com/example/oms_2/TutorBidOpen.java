package com.example.oms_2;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class TutorBidOpen extends AppCompatActivity implements OpenDialogForm.TutorOpenDialogListener {

    TextView textView14, textView17;
    Button tutorClickOpenBid;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tutor_bid_open);

        //TEMPORARY DUMMY TEXT CODE

        Intent intent = getIntent();
        String text = "", nextln = "\n\n";

        text += "Student ID: " + LoginPage.getStudId() + nextln;

        text += "Type of bid: " + intent.getStringExtra(StudBiddingForm.EXTRA_TOGGLE) + nextln;
        text += "Lesson needed: " + intent.getStringExtra(StudBiddingForm.EXTRA_SUBJECT) + nextln;

        text += "Lesson ID: " + StudBiddingForm.getTheSubjId() + nextln;

        text += "Tutor's qualification: " + intent.getStringExtra(StudBiddingForm.EXTRA_QUALIF) + nextln;
        text += "No. of sessions per week: " + intent.getStringExtra(StudBiddingForm.EXTRA_SESSION) + nextln;
        text += "Preferred rate per session: RM " + intent.getStringExtra(StudBiddingForm.EXTRA_RATE) + nextln;
        text += "Preferred time: " + intent.getStringExtra(StudBiddingForm.EXTRA_TIME) + nextln;
        text += "Preferred day(s): " + intent.getStringExtra(StudBiddingForm.EXTRA_DAYS) + nextln;

        textView14 = findViewById(R.id.textView14);
        textView14.setText(text);

        textView17 = findViewById(R.id.textView17);
        tutorClickOpenBid = findViewById(R.id.tutorClickOpenBid);

        tutorClickOpenBid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDialog();
            }
        });
    }

    public void openDialog(){
        OpenDialogForm obDialog = new OpenDialogForm();
        obDialog.show(getSupportFragmentManager(), "obDialog");
    }

    @Override
    public void applyTexts(String rate) {
        textView17.setText("Rate per session (RM): " + rate);
    }
}
