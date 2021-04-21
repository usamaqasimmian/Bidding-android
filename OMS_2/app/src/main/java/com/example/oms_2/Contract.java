package com.example.oms_2;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

public class Contract extends AppCompatActivity {

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.contract);
        Intent intent = getIntent();
        String tutorId = intent.getStringExtra("tutorid");
        String studentId = LoginPage.getStudId();

    }
    }
