package com.example.attendence.Model;

import android.graphics.Bitmap;

import java.io.Serializable;

public class Information_Attend  implements Serializable {
    Bitmap bitmap;
    boolean Report,Check_Attend;
    String note;
    String studentCode;

    public String getStudentCode() {
        return studentCode;
    }

    public void setStudentCode(String studentCode) {
        this.studentCode = studentCode;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    public boolean isReport() {
        return Report;
    }

    public void setReport(boolean report) {
        Report = report;
    }

    public boolean isCheck_Attend() {
        return Check_Attend;
    }

    public void setCheck_Attend(boolean check_Attend) {
        Check_Attend = check_Attend;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public Information_Attend() {
    }

    public Information_Attend(Bitmap bitmap, boolean report, boolean check_Attend, String note) {
        this.bitmap = bitmap;
        Report = report;
        Check_Attend = check_Attend;
        this.note = note;
    }
}
