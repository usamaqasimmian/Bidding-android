package com.example.oms_2;

public class TutorViewItems {
    private String tutorName;
    private String tutorQualification;
    private String tutorLevel;
    private String tutorId;
    private String subjectId;

    public String getTutorId() {
        return tutorId;
    }

    public void setTutorId(String tutorId) {
        this.tutorId = tutorId;
    }

    public String getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(String subjectId) {
        this.subjectId = subjectId;
    }

    public TutorViewItems(String tutorName, String tutorQualification, String tutorLevel, String tutorId, String subjectId){
        this.tutorName = tutorName;
        this.tutorQualification = tutorQualification;
        this.tutorLevel = tutorLevel;
        this.tutorId = tutorId;

        this.subjectId = subjectId;
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
