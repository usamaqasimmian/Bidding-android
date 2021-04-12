package com.example.oms_2;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


public class LoginPage extends AppCompatActivity {

    private static final String myApiKey = "bCBkg7BQ9prgzgkWhbmdwh8tBz8WjH";
    private static final String rootUrl = "https://fit3077.com/api/v1";
    private static final MediaType JSON = MediaType.get("application/json; charset=utf-8");
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
                Error.setText("");
                check(UserName.getText().toString().toLowerCase().trim() , Password.getText().toString());
            }
        });

    }

    @SuppressLint("SetTextI18n")
    private void check(String username, String password){
        String usersUrl = rootUrl + "/user/login";
        String json = "{" +
                "\"userName\":\"" + username + "\"," +
                "\"password\":\"" + password + "\"" +
                "}";
        RequestBody body = RequestBody.create(json, JSON);

        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(usersUrl + "?jwt=true")
                .header("Authorization",myApiKey)
                .post(body)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if(response.isSuccessful()){


                    LoginPage.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                if (response.code() == 200) {
                                    System.out.println(response.code());
                                    JSONObject reader = new JSONObject(response.body().string());
                                    String jwt = reader.getString("jwt");
                                    VerifyToken(jwt);
                                }
                            } catch (IOException | JSONException e) {
                                e.printStackTrace();
                            }

                        }
                    });
                }
            }
        });
        Error.setText("Invalid username or credentials");
    }

    @SuppressLint("SetTextI18n")
    private void VerifyToken(String jwt){
        String jsonString = "{\"jwt\":\"" + jwt + "\"}";
        String usersVerifyTokenUrl = rootUrl + "/user/verify-token";
        RequestBody body = RequestBody.create(jsonString, JSON);
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(usersVerifyTokenUrl)
                .header("Authorization",myApiKey)
                .post(body)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if(response.isSuccessful()){


                    LoginPage.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Intent intent = new Intent(com.example.oms_2.LoginPage.this, StudentLoggedIn.class);
                            LoginPage.this.startActivity(intent);
                        }
                    });
                }
            }
        });
        Error.setText("Invalid Token");
    }
}

