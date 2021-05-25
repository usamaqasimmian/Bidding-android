package com.example.oms_2;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Objects;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static com.example.oms_2.OMSConstants.JSON;
import static com.example.oms_2.OMSConstants.myApiKey;
import static com.example.oms_2.OMSConstants.rootUrl;

/**
 * This class sets the login page for students and tutors.
 * Users can enter their username and password to gain access to the system.
 */
public class LoginPage extends AppCompatActivity {

    private EditText UserName;
    private EditText Password;
    private Button Login;
    private TextView Error;
    private static String studId;
    private static String tutorId;

    //getters and setters
    public static String getStudId(){ return studId; }
    public void setStudId(String sid){ studId = sid; }
    public static String getTutorId(){ return tutorId; }
    public void setTutorId(String tid){ tutorId = tid; }

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

    /**
     * Verifies the username and password entered.
     * @param username
     * @param password
     */
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

                    new Thread(() -> {


                        try {
                            JSONObject reader = new JSONObject(Objects.requireNonNull(response.body()).string());
                            String jwt = reader.getString("jwt");
                            VerifyToken(jwt);


                        } catch (IOException | JSONException e) {
                            e.printStackTrace();
                        }
                    }).start();
                } else {
                    new Handler(Looper.getMainLooper()).post(() -> Error.setText("Invalid Username or Password"));
                }
            }
        });
    }

    /**
     * Verifies the jwt token.
     * @param jwt
     */
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
                    new Thread(() -> {
                        redirectUser();
                    }).start();
                }
            }
        });
    }

    /**
     * If all provided info is valid, the user is directed to the next corresponding page.
     */
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
                    new Thread(() -> {
                        String loggedIn = UserName.getText().toString().trim();
                        try {
                            JSONArray array = new JSONArray(Objects.requireNonNull(response.body()).string());
                            for (int i = 0; i < array.length(); i++) {
                                JSONObject row = array.getJSONObject(i);
                                if (row.getString("userName").equals(loggedIn)) {
                                    if (row.getBoolean("isStudent") && row.getBoolean("isTutor")) {
                                        Intent intent = new Intent(LoginPage.this, StudentTutorBoth.class);
                                        String studId = row.getString("id");
                                        setStudId(studId);
                                        LoginPage.this.startActivity(intent);
                                    } else if (row.getBoolean("isStudent")){
                                        Intent intent = new Intent(LoginPage.this, StudentLoggedIn.class);
                                        String studId = row.getString("id");
                                        setStudId(studId);
                                        LoginPage.this.startActivity(intent);
                                    }
                                    else{
                                        Intent intent = new Intent(LoginPage.this, TutorLoggedIn.class);
                                        String tutId = row.getString("id");
                                        setTutorId(tutId);
                                        LoginPage.this.startActivity(intent);
                                    }
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                    }).start();
                }
            }
        });


    }
}

