package com.example.oms_2;

import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Objects;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static com.example.oms_2.BidCardItemAdapter.getOfferHolder;
import static com.example.oms_2.OMSConstants.myApiKey;
import static com.example.oms_2.OMSConstants.rootUrl;

public class PostOpenMessage extends AppCompatActivity {

    public static final MediaType JSON = MediaType.get("application/json; charset=utf-8");

    Button viewAllOffers;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.post_open_message);

        postMgsOpen();

        viewAllOffers = findViewById(R.id.view_all_offers);
        viewAllOffers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void postMgsOpen(){
        String postMsgUrl = rootUrl + "/message";

        //retrieve required info for json
        String bidId = getOfferHolder();            //from BidCardItemAdapter
        String posterID = LoginPage.getTutorId();   //from the logged in tutor
        LocalDateTime dateCreated = LocalDateTime.now();
        String datePosted = dateCreated + "Z";
        String content = "open";

        String infoTagR = "rate",
                infoTagH = "hour",
                infoTagS = "session",
                infoTagAI = "moreInfo",
                infoTagQ = "qualifs",
                infoTagCL = "compLvl";

        String infoTR = TutorOfferForm.getoRate(),
                infoTH = TutorOfferForm.getoHour(),
                infoTS = TutorOfferForm.getoSess(),
                infoTAI = TutorOfferForm.getoAddiInfo(),
                infoTQ = TutorOfferForm.getdQualif(),
                infoTCL = TutorOfferForm.getdCompL();

        String json =
                "{" +
                        "\"bidId\":\"" + bidId + "\"," +
                        "\"posterId\":\"" + posterID + "\"," +
                        "\"datePosted\":\"" + datePosted + "\"," +
                        "\"content\":\"" + content + "\"," +
                        "\"additionalInfo\":" + "{" + "\"" +
                        infoTagR + "\":" + "\"" + infoTR + "\"," + "\"" +
                        infoTagH + "\":" + "\"" + infoTH + "\"," + "\"" +
                        infoTagS + "\":" + "\"" + infoTS + "\"," + "\"" +
                        infoTagAI + "\":" + "\"" + infoTAI + "\"," + "\"" +
                        infoTagQ + "\":" + "\"" + infoTQ + "\"," + "\"" +
                        infoTagCL + "\":" + "\"" + infoTCL + "\"" +
                        "}" +
                        "}";

        System.out.println(json);

        RequestBody body = RequestBody.create(json, JSON);
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(postMsgUrl)
                .header("Authorization", myApiKey)
                .post(body)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if (response.isSuccessful()){
                    PostOpenMessage.this.runOnUiThread(() -> {
//                        String msgId = "id";
//                        try{
//                            JSONArray array = new JSONArray(Objects.requireNonNull(response.body()).string());
//                            JSONObject row = array.getJSONObject(0);
//                            msgId = row.getString("id");
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        } catch (IOException e) {
//                            e.printStackTrace();
//                        }
                        System.out.println("message posted successfully");
                    });
                } else {
                    System.out.println(response);
                    new Handler(Looper.getMainLooper()).post(() -> System.out.println("Error: message not posted!"));
                }
            }
        });
    }
}
