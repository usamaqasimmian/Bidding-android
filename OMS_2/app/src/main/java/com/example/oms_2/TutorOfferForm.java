package com.example.oms_2;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputFilter;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static com.example.oms_2.BidCardItemAdapter.getOfferHolder;
import static com.example.oms_2.OMSConstants.myApiKey;
import static com.example.oms_2.OMSConstants.rootUrl;

/**
 * This class represents the offer form for a tutor to provide details on bids.
 * Upon confirmation, all provided details are sent to PostOfferMessage class,
 * to be posted to the message endpoint.
 */
public class TutorOfferForm extends AppCompatActivity {

    private EditText offer_rate;
    private EditText offer_hour;
    private EditText offer_sess;
    private EditText offer_addi_info;
    private TextView dummy_qualif, dummy_comp_lvl;
    private TextView reqBidId;
    private Button button_confirm_offer;
    private static String oRate, oHour, oSess, oAddiInfo, dQualif, dCompL;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tutor_offer_form_page);

        displayReqBidId();      //display the requester's bid id
        retrieveTutor();        //display tutor's qualification and competency in the offer
        callToSetFilters();     //set range of values allowed for certain entries

        button_confirm_offer = findViewById(R.id.button_confirm_offer);
        button_confirm_offer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TutorLoggedIn.buttonCoUOfferForm.equals("toPostOffer")) {
                    callToSetDetails();     //store the entered details so we can retrieve them later
                    Intent intent = new Intent(TutorOfferForm.this, PostOfferMessage.class);
                    TutorOfferForm.this.startActivity(intent);
                }
                else if (TutorLoggedIn.buttonCoUOfferForm.equals("toPatchOffer")){
                    callToSetDetails();
                    Intent intt = new Intent(TutorOfferForm.this, PatchOfferMessage.class);
                    TutorOfferForm.this.startActivity(intt);
                }
            }
        });
    }

    /**
     * Displays the selected request id.
     */
    public void displayReqBidId(){
        reqBidId = findViewById(R.id.reqBidId);
        reqBidId.setText(getOfferHolder());
    }

    /**
     * Sets a range of values to a certain fields in the offer form.
     */
    public void callToSetFilters(){
        offer_rate = findViewById(R.id.offer_rate);
        offer_hour = findViewById(R.id.offer_hour);
        offer_sess = findViewById(R.id.offer_sess);
        offer_addi_info = findViewById(R.id.offer_addi_info);

        offer_rate.setFilters(new InputFilter[]{new InputFilterMinMax("0", "100")});
        offer_hour.setFilters(new InputFilter[]{new InputFilterMinMax("1", "3")});
        offer_sess.setFilters(new InputFilter[]{new InputFilterMinMax("1", "5")});
    }

    /**
     * Sets the entered values using the setters.
     */
    public void callToSetDetails(){
        setoRate(offer_rate.getText().toString());
        setoHour(offer_hour.getText().toString());
        setoSess(offer_sess.getText().toString());
        setoAddiInfo(offer_addi_info.getText().toString());
    }

    /**
     * Retrieves the tutor's details such as competency level and qualifications
     * and display it on the offer form.
     */
    public void retrieveTutor() {
        String tutorID = LoginPage.getTutorId();
        StringBuilder strComp = new StringBuilder("");
        StringBuilder strQual = new StringBuilder("");

        dummy_comp_lvl = findViewById(R.id.dummy_comp_lvl);
        dummy_qualif = findViewById(R.id.dummy_qualif);

        RequestQueue mQueue = Volley.newRequestQueue(this);
        String theUrl = rootUrl + "/user/"
                + tutorID
                + "?fields=competencies&fields=competencies.subject&fields=qualifications";

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, theUrl, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray tutComp = response.getJSONArray("competencies");
                            JSONArray tutQual = response.getJSONArray("qualifications");
                            if (tutComp.length() != 0){
                                JSONObject tutCompSubjObj = tutComp.getJSONObject(0);
                                JSONObject tutCompSubj = tutCompSubjObj.getJSONObject("subject");
                                String tutSubjID = tutCompSubj.getString("id");
                                String tutSubj = tutCompSubj.getString("name") +" - "+ tutCompSubj.getString("description");
                                String lvl = tutCompSubjObj.getString("level");
                                strComp.append("SubjectID:")
                                        .append(tutSubjID)
                                        .append("Subject: ")
                                        .append(tutSubj)
                                        .append(", ")
                                        .append("Level: ")
                                        .append(lvl);
                                dummy_comp_lvl.setText(strComp);
                                setdCompL(dummy_comp_lvl.getText().toString());
                            }
                            if (tutQual.length() != 0){
                                JSONObject tutQualObj = tutQual.getJSONObject(0);
                                boolean vf = tutQualObj.getBoolean("verified");
                                if (vf){
                                    String tutCert = tutQualObj.getString("title") + " - " + tutQualObj.getString("description");
                                    strQual.append(tutCert);
                                    dummy_qualif.setText(strQual);
                                    setdQualif(dummy_qualif.getText().toString());
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
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

        mQueue.add(request);
    }

    //Setters
    public void setoRate(String offerR){ this.oRate = offerR; }
    public void setoHour(String offerH){ this.oHour = offerH; }
    public void setoSess(String offerS){ this.oSess = offerS; }
    public void setoAddiInfo(String offerAI){ this.oAddiInfo = offerAI; }
    public void setdQualif(String dummyQ){ this.dQualif = dummyQ; }
    public void setdCompL(String dummyCL){ this.dCompL = dummyCL; }

    //Getters
    public static String getoRate(){ return oRate; }
    public static String getoHour(){ return oHour; }
    public static String getoSess(){ return oSess; }
    public static String getoAddiInfo(){return oAddiInfo; }
    public static String getdQualif(){ return dQualif; }
    public static String getdCompL(){ return dCompL; }

    //menu
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.options_menu2, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id=item.getItemId();
        if (id == R.id.action_Logout) {
            Intent intent = new Intent(com.example.oms_2.TutorOfferForm.this, LoginPage.class);
            TutorOfferForm.this.startActivity(intent);
        } else if (id == R.id.homepage) {
            Intent intent = new Intent(com.example.oms_2.TutorOfferForm.this, TutorLoggedIn.class);
            TutorOfferForm.this.startActivity(intent);
        }
        return true;
    }
}
