package com.example.oms_2;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

/**
 * This class displays the close biddings.
 */
public class StudViewCloseBids extends AppCompatActivity {

    TextView textView4;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.stud_view_close_bids);

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

        textView4 = findViewById(R.id.textView4);
        textView4.setText(text);
    }
}
