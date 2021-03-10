package com.example.attendence.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RequestImage {
    @SerializedName("code_student")
    @Expose
    private String codeStudent;
    @SerializedName("code_course")
    @Expose
    private String codeCourse;
    @SerializedName("date_attend")
    @Expose
    private String dateAttend;

    public String getCodeStudent() {
        return codeStudent;
    }

    public void setCodeStudent(String codeStudent) {
        this.codeStudent = codeStudent;
    }

    public String getCodeCourse() {
        return codeCourse;
    }

    public void setCodeCourse(String codeCourse) {
        this.codeCourse = codeCourse;
    }

    public String getDateAttend() {
        return dateAttend;
    }

    public void setDateAttend(String dateAttend) {
        this.dateAttend = dateAttend;
    }

}
