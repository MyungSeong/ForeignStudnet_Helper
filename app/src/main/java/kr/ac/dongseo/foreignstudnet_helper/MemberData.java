package kr.ac.dongseo.foreignstudnet_helper;

import android.app.Application;

public class MemberData extends Application
{
    private static MemberData Instance;

    private String email;
    private String name;
    private String phone;
    private String country;
    private String language;
    private int isHelper;

    private String latitude; // only helpee
    private String longitude;

    private int idx;

    private int currentTab;

    public MemberData() {}

    public MemberData(String email, String name, String phone,
                      String country, String language, int isHelper) {
        this.email = email;
        this.name = name;
        this.phone = phone;
        this.country = country;
        this.language = language;
        this.isHelper = isHelper;
    }

    public static MemberData getInstance() {
        if (Instance == null) {
            Instance = new MemberData();
        }

        return Instance;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public int getIsHelper() {
        return isHelper;
    }

    public void setIsHelper(int isHelper) {
        this.isHelper = isHelper;
    }

    public int getIdx() {
        return idx;
    }

    public void setIdx(int idx) {
        this.idx = idx;
    }

    public int getCurrentTab() { return currentTab; }

    public void setCurrentTab(int currentTab) { this.currentTab = currentTab; }

    public String getLatitude() { return latitude; }

    public void setLatitude(String latitude) { this.latitude = latitude; }

    public String getLongitude() { return longitude; }

    public void setLongitude(String longitude) { this.longitude = longitude; }
}
