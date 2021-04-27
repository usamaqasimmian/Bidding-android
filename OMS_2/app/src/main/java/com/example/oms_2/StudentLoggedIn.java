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
 * This class is directs the student to a page where he/she can open a bid form.
 */
public class StudentLoggedIn extends AppCompatActivity {

    Button createNewRequest;
    Button viewAllTutors;
    Button viewOffersOpen, viewOffersClose;
    public static String viewWhichOffers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.student_logged_in);

        viewAllTutors = findViewById(R.id.view_all_tutors);
        createNewRequest = findViewById(R.id.createNewRequest);

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




