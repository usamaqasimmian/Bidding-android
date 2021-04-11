package com.example.oms_2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

/**
 * This class is directs the student to a page where he/she can open a bid form.
 */
public class StudentLoggedIn extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button createNewBid = findViewById(R.id.createNewBid);
        createNewBid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(StudentLoggedIn.this, StudBiddingForm.class);
                StudentLoggedIn.this.startActivity(intent);
            }
        });

    }
}