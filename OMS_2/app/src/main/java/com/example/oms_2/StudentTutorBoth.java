package com.example.oms_2;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class StudentTutorBoth extends AppCompatActivity {

        Button studentLogin;
        Button tutorLogin;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.tutor_student_both);
            studentLogin = findViewById(R.id.loginStudent);
            tutorLogin = findViewById(R.id.loginTutor);

            studentLogin.setOnClickListener(v -> {
                Intent intent = new Intent(StudentTutorBoth.this, StudentLoggedIn.class);
                StudentTutorBoth.this.startActivity(intent);
            });

            tutorLogin.setOnClickListener(v -> {
                Intent intent = new Intent(StudentTutorBoth.this, TutorLoggedIn.class);
                StudentTutorBoth.this.startActivity(intent);
            });

        }
}
