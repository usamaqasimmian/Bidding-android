package com.example.oms_2;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.icu.text.DateFormat;
import android.icu.text.SimpleDateFormat;
import android.icu.util.Calendar;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Objects;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static com.example.oms_2.OMSConstants.myApiKey;
import static com.example.oms_2.OMSConstants.rootUrl;

public class Contract extends AppCompatActivity {
    public static final MediaType JSON = MediaType.get("application/json; charset=utf-8");

    DatePicker expiryDate;
    Button SignContract;
    String tutorId;
    String subjectId;
    String studentId;
    LocalDateTime dateCreated;
    TextView heading;

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
        heading = findViewById(R.id.heading);
        SignContract.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                contractSign();
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void contractSign() {
        int   day  = expiryDate.getDayOfMonth();
        int   month= expiryDate.getMonth();
        int   year = expiryDate.getYear();
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, day);

        @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String date = sdf.format(calendar.getTime());
        String expiry = date + "T23:59:59.999Z";
        String startDate = dateCreated + "Z";
        String usersUrl = rootUrl + "/contract";
        String additionalInfoTag = "Special Request";
        String additionalInfoRequest = "";
        String paymentInfoTag = "Payment Method";
        String paymentInfo = "";
        String lessonInfoTag = "Course Books";
        String lessonInfo = "";
        String json =
                "{" +
                        "\"firstPartyId\":\"" + studentId + "\"," +
                        "\"secondPartyId\":\"" + tutorId + "\"," +
                        "\"subjectId\":\"" + subjectId + "\"," +
                        "\"dateCreated\":\"" + startDate + "\"," +
                        "\"expiryDate\":\"" + expiry + "\"," +
                        "\"paymentInfo\":"   +   "{" + "\"" + paymentInfoTag + "\":" + "\""+ paymentInfo + "\"" + "}," +
                        "\"lessonInfo\":"  +   "{" + "\"" + lessonInfoTag + "\":" + "\""+ lessonInfo + "\"" + "}," +
                        "\"additionalInfo\":"   +   "{" + "\"" + additionalInfoTag + "\":" + "\""+ additionalInfoRequest + "\"" + "}" +
                        "}";
        System.out.println(json);
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

            @SuppressLint("SetTextI18n")
            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) {

                if (response.isSuccessful()) {

                    Contract.this.runOnUiThread(() -> {
                       SignContract.setVisibility(View.GONE);
                       expiryDate.setVisibility(View.GONE);
                       heading.setText("Contract Signed Successfully!");

                    });
                }
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
            Intent intent = new Intent(com.example.oms_2.Contract.this, LoginPage.class);
            Contract.this.startActivity(intent);
        }
        else if (id == R.id.view_all_tutors) {
            Intent intent = new Intent(com.example.oms_2.Contract.this, ViewAllTutors.class);
            Contract.this.startActivity(intent);
        }
        return true;
    }

}
