package com.example.oms_2;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import java.time.LocalDateTime;

public class Contract extends AppCompatActivity {

    DatePicker expiryDate;
    Button SignContract;
    String tutorId;
    String subjectId;
    String studentId;
    LocalDateTime dateCreated;

    @RequiresApi(api = Build.VERSION_CODES.O)
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.contract);
        Intent intent = getIntent();
        expiryDate = findViewById(R.id.expiry_date);
        expiryDate.setMinDate(System.currentTimeMillis() - 1000); //no expiry date in the past
        tutorId = intent.getStringExtra("tutorid");
        subjectId = intent.getStringExtra("subjectid");
        studentId = LoginPage.getStudId();
        dateCreated = LocalDateTime.now(); //time 2021-04-17T07:20:17.918
        SignContract = findViewById(R.id.signContract);
        SignContract.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                contractSign();
            }
        });
    }

    private void contractSign() {
        String date = dateCreated + "Z";

    }


}
