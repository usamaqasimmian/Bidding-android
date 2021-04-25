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

public class ViewAllOffers extends AppCompatActivity {

    private ArrayList<BidCardItem> nListOfOffers;

    private RecyclerView nRecyclerView;
    private OffersCardAdapter nAdapter;
    private RecyclerView.LayoutManager nLayoutManager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.offers_recyclerview);

        fillUpOfferCards();
    }

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

    public void buildOfferRecyclerView(){
        nRecyclerView = findViewById(R.id.offerRecyclerView);
        nRecyclerView.setHasFixedSize(true);
        nLayoutManager = new LinearLayoutManager(this);
        nAdapter = new OffersCardAdapter(getnListOfOffers(), R.layout.offers_cardview);

        nRecyclerView.setLayoutManager(nLayoutManager);
        nRecyclerView.setAdapter(nAdapter);
    }

    public ArrayList<BidCardItem> getnListOfOffers() { return nListOfOffers; }
    public void setnListOfOffers(ArrayList<BidCardItem> nListOfOffers) { this.nListOfOffers = nListOfOffers; }
}
