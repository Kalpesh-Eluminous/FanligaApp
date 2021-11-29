package com.fanliga.models;

import java.io.Serializable;
import java.util.ArrayList;

public class ProfileData implements Serializable {
    String first_name,last_name,user_email,mobile_no,location,age,home_club_id,profile_image,profile_image_path;

    ArrayList<Sports> active_sports;
    ArrayList<Leagues> bookmark_leagues;
    ArrayList<Moderators> bookmark_moderators;
    ArrayList<Categories> bookmark_categories;
    ArrayList<Clubs> bookmark_clubs;

    HomeClub homeClub;

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public String getUser_email() {
        return user_email;
    }

    public void setUser_email(String user_email) {
        this.user_email = user_email;
    }

    public String getMobile_no() {
        return mobile_no;
    }

    public void setMobile_no(String mobile_no) {
        this.mobile_no = mobile_no;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getHome_club_id() {
        return home_club_id;
    }

    public void setHome_club_id(String home_club_id) {
        this.home_club_id = home_club_id;
    }

    public String getProfile_image() {
        return profile_image;
    }

    public void setProfile_image(String profile_image) {
        this.profile_image = profile_image;
    }

    public String getProfile_image_path() {
        return profile_image_path;
    }

    public void setProfile_image_path(String profile_image_path) {
        this.profile_image_path = profile_image_path;
    }


    public ArrayList<Sports> getActive_sports() {
        return active_sports;
    }

    public void setActive_sports(ArrayList<Sports> active_sports) {
        this.active_sports = active_sports;
    }

    public ArrayList<Leagues> getBookmark_leagues() {
        return bookmark_leagues;
    }

    public void setBookmark_leagues(ArrayList<Leagues> bookmark_leagues) {
        this.bookmark_leagues = bookmark_leagues;
    }

    public ArrayList<Moderators> getBookmark_moderators() {
        return bookmark_moderators;
    }

    public void setBookmark_moderators(ArrayList<Moderators> bookmark_moderators) {
        this.bookmark_moderators = bookmark_moderators;
    }

    public ArrayList<Categories> getBookmark_categories() {
        return bookmark_categories;
    }

    public void setBookmark_categories(ArrayList<Categories> bookmark_categories) {
        this.bookmark_categories = bookmark_categories;
    }

    public ArrayList<Clubs> getBookmark_clubs() {
        return bookmark_clubs;
    }

    public void setBookmark_clubs(ArrayList<Clubs> bookmark_clubs) {
        this.bookmark_clubs = bookmark_clubs;
    }

    public HomeClub getHomeClub() {
        return homeClub;
    }

    public void setHomeClub(HomeClub homeClub) {
        this.homeClub = homeClub;
    }
}
