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
/**
 * This class is directs the tutor to a page where he/she has the option
 * to bid on open requests, or bid on close requests.
 */
public class TutorLoggedIn extends AppCompatActivity {

    private Button bidOnOpenBids, bidOnCloseBids, viewMyOffersToUpdate;
    public static String bidOnBids;
    public static String buttonCoUOfferForm;    //used in adapters for offer form to confirm or update

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tutor_logged_in);

        bidOnOpenBids = findViewById(R.id.bidOnOpenBids);
        bidOnCloseBids = findViewById(R.id.bidOnCloseBids);
        viewMyOffersToUpdate = findViewById(R.id.viewMyOffersToUpdate);

        bidOnOpenBids.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bidOnBids = "bidOnOpenBids";
                Intent intent = new Intent(TutorLoggedIn.this, TutorViewRequests.class);
                TutorLoggedIn.this.startActivity(intent);
            }
        });

        bidOnCloseBids.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bidOnBids = "bidOnCloseBids";
                Intent intent = new Intent(TutorLoggedIn.this, TutorViewRequests.class);
                TutorLoggedIn.this.startActivity(intent);
            }
        });

        viewMyOffersToUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TutorLoggedIn.this, LoggedInTutorOffers.class);
                TutorLoggedIn.this.startActivity(intent);
            }
        });
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.optionsmenu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id=item.getItemId();
        if (id == R.id.action_Logout) {
            Intent intent = new Intent(com.example.oms_2.TutorLoggedIn.this, LoginPage.class);
            TutorLoggedIn.this.startActivity(intent);
        } else if (id == R.id.view_all_bids) {
            Intent intent = new Intent(com.example.oms_2.TutorLoggedIn.this, TutorViewRequests.class);
            TutorLoggedIn.this.startActivity(intent);
        }
        return true;
    }
}


