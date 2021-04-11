package com.example.oms_2;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

/**
 * This class displays the open biddings.
 */
public class StudViewOpenBids extends AppCompatActivity {

    TextView textView5;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.stud_view_open_bids);

        Intent intent = getIntent();
        String text = "";
        text += intent.getStringExtra(StudBiddingForm.EXTRA_SUBJECT) + "\n";
        text += intent.getStringExtra(StudBiddingForm.EXTRA_QUALIF) + "\n";
        text += intent.getStringExtra(StudBiddingForm.EXTRA_SESSION) + "\n";
        text += intent.getStringExtra(StudBiddingForm.EXTRA_RATE) + "\n";
        text += intent.getStringExtra(StudBiddingForm.EXTRA_TIME) + "\n";
        text += intent.getStringExtra(StudBiddingForm.EXTRA_DAYS) + "\n";

        textView5 = findViewById(R.id.textView5);
        textView5.setText(text);
    }
}
