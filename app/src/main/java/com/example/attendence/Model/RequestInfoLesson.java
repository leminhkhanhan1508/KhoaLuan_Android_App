package com.example.attendence.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RequestInfoLesson {
    @SerializedName("code_course")
    @Expose
    private String codeCourse;
    @SerializedName("date_attend")
    @Expose
    private String dateAttend;

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
