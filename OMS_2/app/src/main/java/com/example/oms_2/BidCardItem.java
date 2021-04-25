package com.example.oms_2;

/**
 * This class is used for other classes to be able to instantiate objects from it.
 * The objects are the open bids details.
 */
public class BidCardItem {

    private String mText0, mText1, mText2;      //title, subject, additionalInfo
    private String mText3, mText4, mText5, mText6;  //tutorQualification, numOfSess, ratePerSess, timeOfSess, daysOfSess
    private String mText7;

    //constructor
    public BidCardItem(String text0, String text1, String text2, String text3, String text4, String text5, String text6, String text7){
        mText0 = text0;
        mText1 = text1;
        mText2 = text2;
        mText3 = text3;
        mText4 = text4;
        mText5 = text5;
        mText6 = text6;
        mText7 = text7;
    }

    //getters
    public String getmText0(){ return mText0; }
    public String getmText1(){ return mText1; }
    public String getmText2(){ return mText2; }
    public String getmText3(){ return mText3; }
    public String getmText4(){ return mText4; }
    public String getmText5(){ return mText5; }
    public String getmText6(){ return mText6; }
    public String getmText7() {
        return mText7;
    }

}
