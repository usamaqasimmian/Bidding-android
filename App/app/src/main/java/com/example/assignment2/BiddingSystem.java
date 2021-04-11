package com.example.assignment2;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

import android.widget.Toast;

public class BiddingSystem extends AppCompatActivity {

    Button openBidding;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bidding); //connects java to xml
        openBidding = findViewById(R.id.openBidding);


        openBidding.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(),"Hello World",Toast.LENGTH_SHORT).show();
            }
        });
    }
}

