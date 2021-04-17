package com.example.oms_2;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class TutorLoggedIn extends AppCompatActivity {

    Button bidOnOpenBids, bidOnCloseBids;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tutor_logged_in);

        bidOnOpenBids = findViewById(R.id.bidOnOpenBids);
        bidOnCloseBids = findViewById(R.id.bidOnCloseBids);

        bidOnOpenBids.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TutorLoggedIn.this, ViewBids.class);
                TutorLoggedIn.this.startActivity(intent);
            }
        });

    }
}


