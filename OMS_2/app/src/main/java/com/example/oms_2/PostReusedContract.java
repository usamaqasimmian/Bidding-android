package com.example.oms_2;

import android.annotation.SuppressLint;
import android.app.DownloadManager;
import android.content.Intent;
import android.icu.text.SimpleDateFormat;
import android.icu.util.Calendar;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static com.example.oms_2.OMSConstants.JSON;
import static com.example.oms_2.OMSConstants.myApiKey;
import static com.example.oms_2.OMSConstants.rootUrl;

/**
 * This class is used to perform the POST /contract endpoint when the student decides
 * to reuse and sign a contract with a different tutor.
 */
public class PostReusedContract extends AppCompatActivity {

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        postReusedContract();
    }

    /**
     * This method is used to post a contract, the one for
     * Assignment 3 requirement 3b - reuse a contract with different tutor.
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    public void postReusedContract(){
        String postContrUrl = rootUrl + "/contract";

        String studentId = LoginPage.getStudId();
        String tutorId = ContractDiffTutor.getTheSelectedTut();
        String subjectID = ContractDiffTutor.getSubjectIDNow();

        LocalDateTime dateCreated = LocalDateTime.now();
        String dateCreatedx = dateCreated + "Z";

        Calendar calendar = Calendar.getInstance();
        int defaultMonth = 3;   //set a default duration of contract, student can extend later
        calendar.add(Calendar.MONTH, defaultMonth);
        @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String date = sdf.format(calendar.getTime());
        String expiryDate = date + "T23:59:59.999Z";

        String paymentInfoTag = "Payment Method";
        String paymentInfo = "";
        String lessonInfoTag = "Course Books";
        String lessonInfo = "";
        String additionalInfoTag = "Special Request";
        String additionalInfoRequest = "";

        String json =
                "{" +
                        "\"firstPartyId\":\"" + studentId + "\"," +
                        "\"secondPartyId\":\"" + tutorId + "\"," +
                        "\"subjectId\":\"" + subjectID + "\"," +
                        "\"dateCreated\":\"" + dateCreatedx + "\"," +
                        "\"expiryDate\":\"" + expiryDate + "\"," +
                        "\"paymentInfo\":"   +   "{" + "\"" + paymentInfoTag + "\":" + "\""+ paymentInfo + "\"" + "}," +
                        "\"lessonInfo\":"  +   "{" + "\"" + lessonInfoTag + "\":" + "\""+ lessonInfo + "\"" + "}," +
                        "\"additionalInfo\":"   +   "{" + "\"" + additionalInfoTag + "\":" + "\""+ additionalInfoRequest + "\"" + "}" +
                        "}";

        System.out.println(json);

        RequestBody body = RequestBody.create(json, JSON);
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(postContrUrl)
                .header("Authorization", myApiKey)
                .post(body)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if (response.isSuccessful()){
                    PostReusedContract.this.runOnUiThread(() -> {
                        System.out.println("contract posted successfully");
                        autoSignContract(dateCreatedx, expiryDate);
                    });
                } else {
                    System.out.println(response);
                    new Handler(Looper.getMainLooper()).post(() -> System.out.println("Error: contract not posted"));
                }
            }
        });
    }

    /**
     * This method automatically signs the contract for the student when he/she selects the different tutor.
     * @param dateCreatedy
     * @param expiryDateY
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    public void autoSignContract(String dateCreatedy, String expiryDateY){
        //find the contract id by looping through GET /contract endpoint
        RequestQueue cQueue = Volley.newRequestQueue(this);
        String contrUrl = rootUrl + "/contract";
        JsonArrayRequest crequest = new JsonArrayRequest(com.android.volley.Request.Method.GET, contrUrl, null,
                new com.android.volley.Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            for (int p=0 ; p<response.length() ; p++){
                                JSONObject eachContr = response.getJSONObject(p);
                                String dc = eachContr.getString("dateCreated");
                                String exd = eachContr.getString("expiryDate");

                                if (dc.equals(dateCreatedy) && exd.equals(expiryDateY)){
                                    String contrId = eachContr.getString("id");

                                    LocalDateTime dateCreated = LocalDateTime.now();
                                    LocalDateTime startDate = dateCreated.plusDays(1);
                                    String date = startDate + "Z";
                                    String usersUrl = rootUrl + "/contract/" + contrId + "/sign";
                                    String json = "{\"dateSigned\":\"" + date + "\"}";
                                    RequestBody body = RequestBody.create(json, JSON);
                                    OkHttpClient client = new OkHttpClient();
                                    Request request = new Request.Builder()
                                            .url(usersUrl)
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
                                                PostReusedContract.this.runOnUiThread(() -> {
                                                    Intent inntt = new Intent(PostReusedContract.this, StudentLoggedIn.class);  //return to home page
                                                    PostReusedContract.this.startActivity(inntt);
                                                });
                                            }
                                        }
                                    });
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("Authorization", myApiKey);
                return headers;
            }
        };
        cQueue.add(crequest);



    }

}
