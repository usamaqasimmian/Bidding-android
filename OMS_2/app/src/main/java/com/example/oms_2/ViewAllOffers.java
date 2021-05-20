package com.example.oms_2;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.example.oms_2.OMSConstants.myApiKey;
import static com.example.oms_2.OMSConstants.rootUrl;

/**
 * This class displays all the offers made by tutor to the students,
 * based on whether the student chose to view offers for open requests or close requests.
 */
public class ViewAllOffers extends AppCompatActivity {

    private ArrayList<BidCardItem> nListOfOffers;
    private RecyclerView nRecyclerView;
    private OffersCardAdapter nAdapter;
    private RecyclerView.LayoutManager nLayoutManager;
    private boolean interrupted;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.offers_recyclerview);

        fillUpOfferCards();     //call first time, then call again later to refresh page
        setInterrupted(false);

        //updates/refreshes offer list from other tutors for open bids every N second
        Thread t = new Thread(){
            @Override
            public void run() {
                while (!getInterrupted()){
                    try{
                        Thread.sleep(5000);     //5000ms = 5s
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                fillUpOfferCards();
                                System.out.println("Offer page is being updated every 5 second");   //checking
                            }
                        });
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        };
        t.start();
    }

    /**
     * When the user switched to another activity, the offer page stops refreshing
     * until the user returns to this activity.
     */
    @Override
    protected void onPause() {
        super.onPause();
        setInterrupted(true);
    }

    /**
     * Fills up the offer cards with tutors' provided details on the corresponding bid,
     * to display to the student.
     */
    public void fillUpOfferCards(){
        nListOfOffers = new ArrayList<>();

        RequestQueue mQueue = Volley.newRequestQueue(this);
        String getMsgUrl = rootUrl + "/message";

        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, getMsgUrl, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try{
                            for (int i=0 ; i<response.length() ; i++){
                                JSONObject each = response.getJSONObject(i);
                                if (each.getJSONObject("additionalInfo").length() == 6 && each.getString("content").equals("open")){
                                    JSONObject poster = each.getJSONObject("poster");
                                    String gName = poster.getString("givenName");
                                    String fName = poster.getString("familyName");
                                    String gfName = gName + fName;

                                    JSONObject additionalInfo = each.getJSONObject("additionalInfo");
                                    String aiRate = additionalInfo.getString("rate");
                                    String aiHour = additionalInfo.getString("hour");
                                    String aiSession = additionalInfo.getString("session");
                                    String aiMoreInfo = additionalInfo.getString("moreInfo");
                                    String aiQualifs = additionalInfo.getString("qualifs");
                                    String aiCompLvl = additionalInfo.getString("compLvl");

                                    nListOfOffers.add(new BidCardItem("Offer "+String.valueOf(i),
                                            "Tutor's name: "+gfName,
                                            "Rate per week: RM "+aiRate,
                                            "Hours per session: "+aiHour,
                                            "Sessions per week: "+aiSession,
                                            "More info: "+aiMoreInfo,
                                            "Tutor's Qualifications: "+aiQualifs,
                                            "Tutor's Competency: "+aiCompLvl));
                                }
                            }
                            setnListOfOffers(nListOfOffers);
                            buildOfferRecyclerView();

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

    /**
     * Builds up the recycler view for the cards.
     */
    public void buildOfferRecyclerView(){
        nRecyclerView = findViewById(R.id.offerRecyclerView);
        nRecyclerView.setHasFixedSize(true);
        nLayoutManager = new LinearLayoutManager(this);
        nAdapter = new OffersCardAdapter(getnListOfOffers(), R.layout.offers_cardview);

        nRecyclerView.setLayoutManager(nLayoutManager);
        nRecyclerView.setAdapter(nAdapter);
    }

    //getter and setter
    public ArrayList<BidCardItem> getnListOfOffers() { return nListOfOffers; }
    public void setnListOfOffers(ArrayList<BidCardItem> nListOfOffers) { this.nListOfOffers = nListOfOffers; }
    public boolean getInterrupted(){ return interrupted; }
    public void setInterrupted(boolean interruptedY){ this.interrupted = interruptedY;}
}
