package com.app.secretdiary;

public class Users {
    String signuptype,email,number,date,gender,id,about,token,addedimage,addedimagepostkey;
    String title,descroption,postkey,realtime,alarmtime,notetime,lastupdate,toptime,firsttime;
    Long time;

    public Users(String firsttime, Long time) {
        this.firsttime = firsttime;
        this.time = time;
    }


    public Users(String signuptype, String email, String number) {
        this.signuptype = signuptype;
        this.email = email;
        this.number = number;
    }

    public Users(String addedimage, String title, String descroption, String postkey, String notetime, String lastupdate, String toptime) {
        this.addedimage = addedimage;
        this.title = title;
        this.descroption = descroption;
        this.postkey = postkey;
        this.notetime = notetime;
        this.lastupdate = lastupdate;
        this.toptime = toptime;
    }


    public Users() {
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public Long getTime() {
        return time;
    }

    public void setTime(Long time) {
        this.time = time;
    }

    public String getFirsttime() {
        return firsttime;
    }

    public void setFirsttime(String firsttime) {
        this.firsttime = firsttime;
    }

    public String getAddedimage() {
        return addedimage;
    }

    public void setAddedimage(String addedimage) {
        this.addedimage = addedimage;
    }

    public String getAddedimagepostkey() {
        return addedimagepostkey;
    }

    public void setAddedimagepostkey(String addedimagepostkey) {
        this.addedimagepostkey = addedimagepostkey;
    }

    public String getToptime() {
        return toptime;
    }

    public void setToptime(String toptime) {
        this.toptime = toptime;
    }

    public String getSignuptype() {
        return signuptype;
    }

    public void setSignuptype(String signuptype) {
        this.signuptype = signuptype;
    }


    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAbout() {
        return about;
    }

    public void setAbout(String about) {
        this.about = about;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescroption() {
        return descroption;
    }

    public void setDescroption(String descroption) {
        this.descroption = descroption;
    }

    public String getPostkey() {
        return postkey;
    }

    public void setPostkey(String postkey) {
        this.postkey = postkey;
    }

    public String getRealtime() {
        return realtime;
    }

    public void setRealtime(String realtime) {
        this.realtime = realtime;
    }


    public String getAlarmtime() {
        return alarmtime;
    }

    public void setAlarmtime(String alarmtime) {
        this.alarmtime = alarmtime;
    }

    public String getNotetime() {
        return notetime;
    }

    public void setNotetime(String notetime) {
        this.notetime = notetime;
    }

    public String getLastupdate() {
        return lastupdate;
    }

    public void setLastupdate(String lastupdate) {
        this.lastupdate = lastupdate;
    }
}
