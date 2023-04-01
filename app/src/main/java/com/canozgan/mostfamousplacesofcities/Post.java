package com.canozgan.mostfamousplacesofcities;

public class Post {
    String email;
    String comment;
    String downloadUrl;
    String placeName;
    String city;
    Double latitute;
    Double longitute;

    public Post(String email, String comment, String downloadUrl, String placeName, String city, Double latitute, Double longitute) {
        this.email = email;
        this.comment = comment;
        this.downloadUrl = downloadUrl;
        this.placeName = placeName;
        this.city = city;
        this.latitute = latitute;
        this.longitute = longitute;
    }
}
