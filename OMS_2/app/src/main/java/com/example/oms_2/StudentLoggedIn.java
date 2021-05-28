package com.example.oms_2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

/**
 * This class is directs the student to a page where he/she has options to
 * open a request form, to view all open bid offers, to view all close bid offers,
 * to view all tutors.
 */
public class StudentLoggedIn extends AppCompatActivity {

    private Button createNewRequest;
    private Button viewAllTutors;
    private Button viewOffersOpen, viewOffersClose;
    private Button contracts, reuse_contract;
    public static String viewWhichOffers;
    public static String doWhatContract;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.student_logged_in);

        viewAllTutors = findViewById(R.id.view_all_tutors);
        createNewRequest = findViewById(R.id.createNewRequest);
        contracts = findViewById(R.id.contracts);

        createNewRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(StudentLoggedIn.this, StudRequestForm.class);
                StudentLoggedIn.this.startActivity(intent);
            }
        });

        viewAllTutors.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(StudentLoggedIn.this, ViewAllTutors.class);
                StudentLoggedIn.this.startActivity(intent);
            }
        });


        contracts.setOnClickListener(v -> {
            doWhatContract = "same tutor contract";
            Intent intent = new Intent(StudentLoggedIn.this, ViewContracts.class);
            StudentLoggedIn.this.startActivity(intent);
        });

        reuse_contract = findViewById(R.id.reuse_contract);
        reuse_contract.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doWhatContract = "different tutor contract";
                Intent intent = new Intent(StudentLoggedIn.this, ViewContracts.class);
                StudentLoggedIn.this.startActivity(intent);
            }
        });

        viewOffersOpen = findViewById(R.id.view_offers_for_open_requests);
        viewOffersOpen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewWhichOffers = "viewOffersOpen";
                Intent intent = new Intent(StudentLoggedIn.this, StudViewOffers.class);
                StudentLoggedIn.this.startActivity(intent);
            }
        });

        viewOffersClose = findViewById(R.id.view_offers_for_close_requests);
        viewOffersClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewWhichOffers = "viewOffersClose";
                Intent intent = new Intent(StudentLoggedIn.this, StudViewOffers.class);
                StudentLoggedIn.this.startActivity(intent);
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
            Intent intent = new Intent(com.example.oms_2.StudentLoggedIn.this, LoginPage.class);
            StudentLoggedIn.this.startActivity(intent);
        } else if (id == R.id.student_bidding_form) {
            Intent intent = new Intent(com.example.oms_2.StudentLoggedIn.this, StudRequestForm.class);
            StudentLoggedIn.this.startActivity(intent);
        }
        else if (id == R.id.view_all_tutors) {
            Intent intent = new Intent(com.example.oms_2.StudentLoggedIn.this, ViewAllTutors.class);
            StudentLoggedIn.this.startActivity(intent);
        }
        return true;
    }
}




