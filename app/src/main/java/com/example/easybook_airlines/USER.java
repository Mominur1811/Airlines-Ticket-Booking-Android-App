package com.example.easybook_airlines;

public class USER {
    String fullname,email,gender,birth_day;

    public USER() {
    }

    public USER(String fullname, String email, String gender, String birth_day) {
        this.fullname = fullname;
        this.email = email;
        this.gender = gender;
        this.birth_day = birth_day;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getBirth_day() {
        return birth_day;
    }

    public void setBirth_day(String birth_day) {
        this.birth_day = birth_day;
    }
}
