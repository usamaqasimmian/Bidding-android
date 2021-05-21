package com.example.oms_2;

/**
 * This class is used for other classes to be able to instantiate objects from it.
 */
public class CardItem {

    private String mText0, mText1, mText2;      //title, subject, additionalInfo
    private String mText3, mText4, mText5, mText6;  //tutorQualification, numOfSess, ratePerSess, timeOfSess, daysOfSess
    private String mText7;
    private String ntext8, ntext9, ntext10, ntext11;

    //constructor
    public CardItem(String text0, String text1, String text2, String text3, String text4, String text5, String text6, String text7){
        mText0 = text0;
        mText1 = text1;
        mText2 = text2;
        mText3 = text3;
        mText4 = text4;
        mText5 = text5;
        mText6 = text6;
        mText7 = text7;
    }

    public CardItem(String t1, String t2, String t3, String t4, String t5, String t6, String t7, String t8, String t9, String t10, String t11){
        mText1 = t1;
        mText2 = t2;
        mText3 = t3;
        mText4 = t4;
        mText5 = t5;
        mText6 = t6;
        mText7 = t7;
        ntext8 = t8;
        ntext9 = t9;
        ntext10 = t10;
        ntext11 = t11;
    }

    //getters
    public String getmText0(){ return mText0; }
    public String getmText1(){ return mText1; }
    public String getmText2(){ return mText2; }
    public String getmText3(){ return mText3; }
    public String getmText4(){ return mText4; }
    public String getmText5(){ return mText5; }
    public String getmText6(){ return mText6; }
    public String getmText7(){ return mText7; }

    public String getNtext8() { return ntext8; }
    public String getNtext9() { return ntext9; }
    public String getNtext10() { return ntext10; }
    public String getNtext11() { return ntext11; }
}
