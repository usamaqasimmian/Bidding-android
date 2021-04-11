package com.example.oms_2;

import android.annotation.SuppressLint;
        import android.content.Intent;
        import android.os.Bundle;
        import android.view.View;
        import android.widget.Button;
        import android.widget.EditText;
        import android.widget.TextView;

        import androidx.appcompat.app.AppCompatActivity;

public class LoginPage extends AppCompatActivity {

    EditText UserName;
    EditText Password;
    Button Login;
    TextView Error;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_page);
        UserName = findViewById(R.id.userName);
        Password = findViewById(R.id.password);
        Login = findViewById(R.id.login);
        Error = findViewById(R.id.error);


        Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                check(UserName.getText().toString().toLowerCase().trim() , Password.getText().toString());
            }
        });

    }

    @SuppressLint("SetTextI18n")
    private void check(String Username, String Password){
        if(Username.equals("admin") && (Password.equals("admin"))){
            Intent intent = new Intent(com.example.oms_2.LoginPage.this, StudentLoggedIn.class);
            LoginPage.this.startActivity(intent);

        }

        else if (Username.equals("ted") && (Password.equals("1234"))) {
            Intent intent = new Intent(com.example.oms_2.LoginPage.this, StudentLoggedIn.class);
            LoginPage.this.startActivity(intent);

        }

        else if (Username.equals("tom") && (Password.equals("warehouse"))) {
            Intent intent = new Intent(com.example.oms_2.LoginPage.this, StudentLoggedIn.class);
            LoginPage.this.startActivity(intent);

        }


        else{
            Error.setText("Incorrect Password or Username");
        }
    }
}
