package com.example.oms_2;

import android.app.TimePickerDialog;
import android.content.Intent;

import android.os.Build;
import android.os.Bundle;
//import android.util.Log;
import android.util.Log;
import android.view.View;
import android.view.textclassifier.TextLinks;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

//import java.net.http.HttpClient;
//import java.net.http.HttpRequest;
//import java.net.http.HttpResponse;

import java.io.IOException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;

import static com.example.oms_2.LoginPage.JSON;
import static com.example.oms_2.OMSConstants.myApiKey;
import static com.example.oms_2.OMSConstants.rootUrl;

/**
 * This class implements all the operations required for all the selected components in the bidding form.
 * Upon completion of the form, the student is directed to either the Open or Close biddings page.
 */
public class StudBiddingForm extends AppCompatActivity implements TimePickerDialog.OnTimeSetListener{

    //constants used to pass data to another activity
    public static final String EXTRA_SUBJECT = "EXTRA_SUBJECT", EXTRA_QUALIF = "EXTRA_QUALIF",
            EXTRA_SESSION = "EXTRA_SESSION", EXTRA_RATE = "EXTRA_RATE", EXTRA_TIME = "EXTRA_TIME",
            EXTRA_DAYS = "EXTRA_DAYS", EXTRA_TOGGLE = "EXTRA_TOGGLE";

    //attributes for the xml layout components
    Spinner bidSubject, qualif_dropdown, num_session, rate_per_session;
    ToggleButton toggleBid;
    Button time_picker, confirmCreateBid;
    TextView textView11;
    CheckBox checkMonday, checkTuesday, checkWednesday, checkThursday, checkFriday;

    private boolean toggle;                                     //attribute used for open-close toggle button
    private String bidType;
    public String getBidType(){ return bidType; }
    public void setBidType(String bt){ this.bidType = bt; }

    private List<String> subjects = new ArrayList<>();          //attribute for dropdown list of subjects
    private List<String> subjectsId = new ArrayList<>();        //attribute for the list of subjects ids
    private static String theSubjId;                            //attribute for the selected subject's id
    private List<String> qualifications = new ArrayList<>();    //attribute for dropdown list of qualifications
    private List<Integer> sessions = new ArrayList<>();         //attribute for dropdown list of number os sessions per week
    private List<Integer> rates = new ArrayList<>();            //attribute for dropdown list of rates per session
    private List<String> checkBoxList = new ArrayList<>();      //attribute used to add checked box values

    //attribute for upon clicking on toggle
    private final CompoundButton.OnCheckedChangeListener toggleBidOnCheckChangeListener = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            toggleBidChecked(isChecked);
        }
    };

    //attribute for upon clicking on dropdown subject list
    private final AdapterView.OnItemSelectedListener spinnerSelect = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            String item = parent.getItemAtPosition(position).toString();
            Toast.makeText(parent.getContext(), item, Toast.LENGTH_SHORT).show();
        }
        @Override
        public void onNothingSelected(AdapterView<?> parent) {}
    };

    //attribute for upon clicking on confirm button
    private final View.OnClickListener confirmCreateBidOnClickListener = new View.OnClickListener() {
        @RequiresApi(api = Build.VERSION_CODES.O)
        @Override
        public void onClick(View v) {
//            confirmCreateBidClicked();
            postBid();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bidding_form);

        //initialise components
        num_session = findViewById(R.id.num_session);
        rate_per_session = findViewById(R.id.rate_per_session);
        toggleBid = findViewById(R.id.toggleBid);
        time_picker = findViewById(R.id.time_picker);
        confirmCreateBid = findViewById(R.id.confirmCreateBid);

        dropdownSubjList();     //operations for selecting subject from dropdown subject list
        dropdownQualList();     //operations for selecting tutor's qualifications

        //operations for selecting sessions per week
        dropdownSessionList();
        ArrayAdapter<Integer> arrayAdapter3 = spinnerAdapterInt(getSessions());
        num_session.setAdapter(arrayAdapter3);
        num_session.setOnItemSelectedListener(spinnerSelect);

        //operations for selecting rates per session
        dropdownRateList();
        ArrayAdapter<Integer> arrayAdapter4 = spinnerAdapterInt(getRates());
        rate_per_session.setAdapter(arrayAdapter4);
        rate_per_session.setOnItemSelectedListener(spinnerSelect);

        //operations for selecting time
        time_picker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment timePicker = new TimePickerFragment();
                timePicker.show(getSupportFragmentManager(), "time picker");
            }
        });

        checkBoxSelect();   //operations for selecting day

        toggleBid.setOnCheckedChangeListener(toggleBidOnCheckChangeListener);   //operations for clicking on open-close toggle button

        confirmCreateBid.setOnClickListener(confirmCreateBidOnClickListener);   //operations for clicking on 'confirm' button
    }

    /**
     * This method adds the checked box value to a list of strings.
     */
    public void checkBoxSelect(){
        checkMonday = findViewById(R.id.checkMonday);
        checkTuesday = findViewById(R.id.checkTuesday);
        checkWednesday = findViewById(R.id.checkWednesday);
        checkThursday = findViewById(R.id.checkThursday);
        checkFriday = findViewById(R.id.checkFriday);

        checkMonday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkMonday.isChecked()){ checkBoxList.add("Monday"); }
                else{ checkBoxList.remove("Monday"); }
            }
        });
        checkTuesday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkTuesday.isChecked()){ checkBoxList.add("Tuesday"); }
                else{ checkBoxList.remove("Tuesday"); }
            }
        });
        checkWednesday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkWednesday.isChecked()){ checkBoxList.add("Wednesday"); }
                else{ checkBoxList.remove("Wednesday"); }
            }
        });
        checkThursday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkThursday.isChecked()){ checkBoxList.add("Thursday"); }
                else{ checkBoxList.remove("Thursday"); }
            }
        });
        checkFriday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkFriday.isChecked()){ checkBoxList.add("Friday"); }
                else{ checkBoxList.remove("Friday"); }
            }
        });
        setCheckBoxList(checkBoxList);
    }

    /**
     * This method takes the time picked by the user and add it to a textview
     * which is displayed below the 'select time' button.
     * @param view
     * @param hourOfDay
     * @param minute
     */
    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        textView11 = findViewById(R.id.textView11);
        String min;
        if (minute < 10){ min = "0" + String.valueOf(minute); }
        else { min = String.valueOf(minute); }
        String t = hourOfDay + ":" + min;
        textView11.setText(t);
    }

    /**
     * Getter for the attribute: subjects
     * @return a list of subjects
     */
    public List<String> getSubjects(){ return subjects; }

    /**
     * Setter for the attribute: qualifications
     * @param subjs list of strings
     */
    public void setSubjects(List<String> subjs){ this.subjects = subjs; }

    /**
     * Getter for the attribute: subjectsId
     * @return a list of strings of the subject ids
     */
    public List<String> getSubjectsId(){ return subjectsId; }

    /**
     * Setter for the attribute: subjectsId
     * @param subjId list of strings
     */
    public void setSubjectsId(List<String> subjId){ this.subjectsId = subjId; }

    /**
     * Getter for the attribute: theSubjId
     * @return a string of the selected subject's id
     */
    public static String getTheSubjId(){ return theSubjId; }

    /**
     * Setter for the attribute: theSubjId
     * @param tsid string
     */
    public void setTheSubjId(String tsid){ this.theSubjId = tsid;}

    /**
     * Getter for the attribute: qualifications
     * @return a list containing tutor's qualifications
     */
    public List<String> getQualifications(){ return qualifications; }

    /**
     * Setter for the attribute: qualifications
     * @param qualifs list of strings
     */
    public void setQualifications(List<String> qualifs){ this.qualifications = qualifs; }

    /**
     * Getter for the attribute: sessions
     * @return a list of number of sessions
     */
    public List<Integer> getSessions(){ return sessions; }

    /**
     * Setter for the attribute: sessions
     * @param sess list of integers
     */
    public void setSessions(List<Integer> sess){ this.sessions = sess; }

    /**
     * Getter for the attribute: rates
     * @return  a list of integers
     */
    public List<Integer> getRates(){ return rates; }

    /**
     * Setter for the attribute: rates
     * @param rts list of integers
     */
    public void setRates(List<Integer> rts){ this.rates = rts; }

    /**
     * Getter for the attribute: toggle
     * @return a boolean value
     */
    public boolean getToggle(){ return toggle; }

    /**
     * Setter for the attribute: toggle
     * @param tog boolean
     */
    public void setToggle(boolean tog){ this.toggle = tog; }

    /**
     * Getter for the attribute: checkBoxList
     * @return a list of strings
     */
    public List<String> getCheckBoxList(){ return checkBoxList; }

    /**
     * Setter for the attribute: checkBoxList
     * @param cbl list of strings
     */
    public void setCheckBoxList(List<String> cbl){ this.checkBoxList = cbl; }

    /**
     * This method returns a collection of objects specified by the list provided
     * in the argument so that it can be used for the spinner.
     * @param aList list of strings
     * @return a collection of objects
     */
    public ArrayAdapter<String> spinnerAdapterStr(List<String> aList){
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, aList);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        return arrayAdapter;
    }

    /**
     * This method returns a collection of objects specified by the list provided
     * in the argument so that it can be used for the spinner.
     * @param bList list of integers
     * @return a collection of objects
     */
    public ArrayAdapter<Integer> spinnerAdapterInt(List<Integer> bList){
        ArrayAdapter<Integer> arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, bList);
        arrayAdapter.setDropDownViewResource((android.R.layout.simple_spinner_dropdown_item));
        return arrayAdapter;
    }

    /**
     * This method retrieves the JSON array using GET /subject endpoint
     * and the API key, so as to fill up the drop-down list with the
     * subjects/lessons that the student seeks for the bid-request.
     */
    public void dropdownSubjList(){
        RequestQueue mQueue = Volley.newRequestQueue(this);
        String subjectUrl = rootUrl + "/subject";
        //json objects bounded by {} are found inside the json array bounded by []
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, subjectUrl, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            for (int i=0 ; i < response.length() ; i++) {
                                //retrieve each json object {} inside the json array
                                JSONObject eachSubject = response.getJSONObject(i);
                                String subjectName = eachSubject.getString("name");
                                String subjectDesc = eachSubject.getString("description");
                                String subjectId = eachSubject.getString("id");
                                subjects.add(subjectName + ": " + subjectDesc);
                                subjectsId.add(subjectId);

                            }
                            setSubjects(subjects);
                            setSubjectsId(subjectsId);
                            ArrayAdapter<String> arrayAdapter1 = spinnerAdapterStr(getSubjects());
                            bidSubject = findViewById(R.id.bidSubject);
                            bidSubject.setAdapter(arrayAdapter1);
                            bidSubject.setOnItemSelectedListener(spinnerSelect);

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
     * This method retrieves the JSON array using GET /qualifications endpoint
     * and the API key, so as to fill up the drop-down list with the
     * tutor's qualifications that the student seeks for the bid-request.
     */
    public void dropdownQualList(){
        RequestQueue mQueue = Volley.newRequestQueue(this);
        String qualUrl = rootUrl + "/qualification";
        //json objects bounded by {} are found inside the json array bounded by []
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, qualUrl, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            for (int i=0 ; i < response.length() ; i++) {
                                //retrieve each json object {} inside the json array
                                JSONObject eachQual = response.getJSONObject(i);
                                String title = eachQual.getString("title");
                                String description = eachQual.getString("description");
                                String temp = title + ": " + description;
                                if (!(getQualifications().contains(temp))){
                                    qualifications.add(temp);
                                }
                                setQualifications(qualifications);
                                ArrayAdapter<String> arrayAdapter2 = spinnerAdapterStr(getQualifications());
                                qualif_dropdown = findViewById(R.id.qualif_dropdown);
                                qualif_dropdown.setAdapter(arrayAdapter2);
                                qualif_dropdown.setOnItemSelectedListener(spinnerSelect);
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

    /**
     * This method adds different number of sessions per week to the sessions list
     * so that the student can select from.
     */
    public void dropdownSessionList(){
        for (int i=1 ; i <= 3 ; i++){
            sessions.add(i);
        }
        setSessions(sessions);
    }

    /**
     * This method adds different rates per session to the rates list
     * so that the student can select from.
     */
    public void dropdownRateList(){
        for (int i=20 ; i <= 100 ; i+=20){
            rates.add(i);
        }
        setRates(rates);
    }

    /**
     * This method sets the value of the toggle button.
     * @param isChecked boolean value
     */
    public void toggleBidChecked(boolean isChecked){ setToggle(isChecked); }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void postBid(){
        String selectedBid = toggleBid.getText().toString().toLowerCase();

        String initiatorId = LoginPage.getStudId();

        String dateTimeUtc = Instant.now().toString();

        String selectedSubject = bidSubject.getSelectedItem().toString();
        String selectedSubjId = getSubjectsId().get(getSubjects().indexOf(selectedSubject));
        setTheSubjId(selectedSubjId);

        String bidPostUrl = rootUrl + "/bid";
        String jsonString = "{" +
                "\"type\":\"" + selectedBid + "\"," +
                "\"initiatorId\":\"" + initiatorId + "\","  +
                "\"dateCreated\":\"" + dateTimeUtc + "\","  +
                "\"subjectId\":\"" + selectedSubjId + "\"," +
                "\"additionalInfo\":\"" + "{}" + "\"" +
                "}";

        RequestBody body = RequestBody.create(jsonString, JSON);
        OkHttpClient client = new OkHttpClient();
        okhttp3.Request request = new okhttp3.Request.Builder()
                .url(bidPostUrl)
                .header("Authorization", myApiKey)
                .post(body)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull okhttp3.Response response) throws IOException {
                Log.d("theTag", String.valueOf(response.code()));
                if (response.isSuccessful()) {
                    //confirmed this does NOT work: error code 400 - Request body could not be parsed or contains invalid fields.
                    //TODO: MUST FIX THIS
                    Log.d("myTag", "jjjjjjjjjjjjjj");
                    StudBiddingForm.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            confirmCreateBidClicked(selectedBid, selectedSubject);
                        }
                    });
                }
            }
        });
    }


    /**
     * This method is called when the 'confirm' button is clicked so that
     * the student is taken to from the Bidding Form page to either the
     * Open Bidding page or the Close bidding page, depending on his bid-request choice.
     */
    public void confirmCreateBidClicked(String chosenBid, String chosenSubject){

        //retrieve choices
        String selectedBid = toggleBid.getText().toString().toLowerCase();
        String selectedSubject = bidSubject.getSelectedItem().toString();
        String selectedSubjId = getSubjectsId().get(getSubjects().indexOf(selectedSubject));
        setTheSubjId(selectedSubjId);

        String selectedQualif = qualif_dropdown.getSelectedItem().toString();
        String selectedSession = num_session.getSelectedItem().toString();
        String selectedRate = rate_per_session.getSelectedItem().toString();
        String selectedTime = textView11.getText().toString();

        StringBuilder sDays = new StringBuilder();
        for (String each: getCheckBoxList()){ sDays.append(each).append(","); }
        String selectedDays = sDays.deleteCharAt(sDays.length()-1).toString();

        Intent intent;
        //go to open or close bids page
        if (getToggle()) { intent = new Intent(StudBiddingForm.this, StudViewOpenBids.class); }
        else {   intent = new Intent(StudBiddingForm.this, StudViewCloseBids.class); }

//        System.out.println("this is working");

        //all choices are added to intent so they can be used in other activities
        intent.putExtra(EXTRA_TOGGLE, chosenBid);
        intent.putExtra(EXTRA_SUBJECT, chosenSubject);
//        intent.putExtra(EXTRA_TOGGLE, selectedBid);
//        intent.putExtra(EXTRA_SUBJECT, selectedSubject);
        intent.putExtra(EXTRA_QUALIF, selectedQualif);
        intent.putExtra(EXTRA_SESSION, selectedSession);
        intent.putExtra(EXTRA_RATE, selectedRate);
        intent.putExtra(EXTRA_TIME, selectedTime);
        intent.putExtra(EXTRA_DAYS, selectedDays);
        StudBiddingForm.this.startActivity(intent);
    }
}
