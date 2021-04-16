package com.example.oms_2;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
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

import static com.example.oms_2.OMSConstants.myApiKey;
import static com.example.oms_2.OMSConstants.rootUrl;


public class LoginPage extends AppCompatActivity {

    public static final MediaType JSON = MediaType.get("application/json; charset=utf-8");
    public static final String myApiKey = "";
    EditText UserName;
    EditText Password;
    Button Login;
    TextView Error;

    private static String studId;
    public static String getStudId(){ return studId; }
    public void setStudId(String sid){ studId = sid; }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_page);
        UserName = findViewById(R.id.userName);
        Password = findViewById(R.id.password);
        Login = findViewById(R.id.login);
        Error = findViewById(R.id.error);

        Login.setOnClickListener(v -> verifyCred(UserName.getText().toString().trim(), Password.getText().toString()));

    }

    @SuppressLint("SetTextI18n")
    private void verifyCred(String username, String password) {
        String usersUrl = rootUrl + "/user/login";
        String json = "{" +
                "\"userName\":\"" + username + "\"," +
                "\"password\":\"" + password + "\"" +
                "}";
        RequestBody body = RequestBody.create(json, JSON);

        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(usersUrl + "?jwt=true")
                .header("Authorization", myApiKey)
                .post(body)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) {

                if (response.isSuccessful()) {

                    LoginPage.this.runOnUiThread(() -> {
                        try {
                            if (response.code() == 200) {
                                JSONObject reader = new JSONObject(response.body().string());
                                String jwt = reader.getString("jwt");
                                VerifyToken(jwt);
                            }

                        } catch (IOException | JSONException e) {
                            e.printStackTrace();
                        }

                    });
                } else {
                    new Handler(Looper.getMainLooper()).post(() -> Error.setText("Invalid Username or Password"));
                }
            }
        });
    }

    @SuppressLint("SetTextI18n")
    private void VerifyToken(String jwt) {
        String jsonString = "{\"jwt\":\"" + jwt + "\"}";
        String usersVerifyTokenUrl = rootUrl + "/user/verify-token";
        RequestBody body = RequestBody.create(jsonString, JSON);
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(usersVerifyTokenUrl)
                .header("Authorization", myApiKey)
                .post(body)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) {
                if (response.isSuccessful()) {

                    LoginPage.this.runOnUiThread(() -> redirectUser());
                }
            }
        });
    }

    private void redirectUser() {
        String getAllUsers = rootUrl + "/user";
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(getAllUsers)
                .header("Authorization", myApiKey)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) {
                if (response.isSuccessful()) {
                    LoginPage.this.runOnUiThread(() -> {
                        String loggedIn = UserName.getText().toString().trim();
                        try {
                            JSONArray array = new JSONArray(response.body().string());
                            for (int i = 0; i < array.length(); i++) {
                                JSONObject row = array.getJSONObject(i);
                                if (row.getString("userName").equals(loggedIn)) {
                                    if (row.getBoolean("isStudent")) {
                                        Intent intent = new Intent(LoginPage.this, biddingAPI.class);
                                        String studId = row.getString("id");
                                        setStudId(studId);
                                        intent.putExtra("UserID",studId);
                                        LoginPage.this.startActivity(intent);
                                    } else {
                                        Intent intent = new Intent(LoginPage.this, TutorLoggedIn.class);
                                        LoginPage.this.startActivity(intent);
                                    }
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                    });
                }
            }
        });


    }
}

