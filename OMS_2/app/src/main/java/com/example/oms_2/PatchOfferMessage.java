package com.example.oms_2;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static com.example.oms_2.LoggedInTutorOffersAdapter.getMessageIDHolder;
import static com.example.oms_2.OMSConstants.JSON;
import static com.example.oms_2.OMSConstants.myApiKey;
import static com.example.oms_2.OMSConstants.rootUrl;

/**
 * This class will take all of the tutor's entered offer details from the tutor offer form
 * and patch/update to the message endpoint.
 */
public class PatchOfferMessage extends AppCompatActivity {

    private Button viewAllOffers, gotoTutHome;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.post_or_patch_open_message);

        String contentType = "open";
        patchMsg(contentType);

        viewAllOffers = findViewById(R.id.view_all_offers);
        viewAllOffers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PatchOfferMessage.this, ViewAllOffers.class);
                PatchOfferMessage.this.startActivity(intent);
            }
        });

        gotoTutHome = findViewById(R.id.gotoTutHomeFromOffer);
        gotoTutHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PatchOfferMessage.this, TutorLoggedIn.class);
                PatchOfferMessage.this.startActivity(intent);
            }
        });
    }

    public void patchMsg(String contentType){
        String msgId = getMessageIDHolder();    //retrieve message id from LoggedInTutorOffersAdapter
        String patchMsgUrl = rootUrl + "/message/" + msgId;

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
                        "\"content\":\"" + contentType + "\"," +
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
                .url(patchMsgUrl)
                .header("Authorization", myApiKey)
                .patch(body)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if (response.isSuccessful()){
                    PatchOfferMessage.this.runOnUiThread(() ->{
                        System.out.println("message patched successfully");
                    });
                } else {
                    System.out.println(response);
                    new Handler(Looper.getMainLooper()).post(() -> System.out.println("Error: message not patched"));
                }
            }
        });
    }
}
