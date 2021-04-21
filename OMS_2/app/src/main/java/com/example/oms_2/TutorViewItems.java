package com.example.oms_2;

public class TutorViewItems {
    private String tutorName;
    private String tutorQualification;
    private String tutorLevel;
    private String tutorId;

    public String getTutorId() {
        return tutorId;
    }

    public void setTutorId(String tutorId) {
        this.tutorId = tutorId;
    }

    public TutorViewItems(String tutorName, String tutorQualification, String tutorLevel, String tutorId){
        this.tutorName = tutorName;
        this.tutorQualification = tutorQualification;
        this.tutorLevel = tutorLevel;
        this.tutorId = tutorId;
    }

    public void setTutorName(String tutorName) {
        this.tutorName = tutorName;
    }

    public String getTutorName() {
        return tutorName;
    }

    public String getTutorQualification() {
        return tutorQualification;
    }

    public String getTutorLevel() {
        return tutorLevel;
    }

    public void setTutorQualification(String tutorQualification) {
        this.tutorQualification = tutorQualification;
    }

    public void setTutorLevel(String tutorLevel) {
        this.tutorLevel = tutorLevel;
    }


}
