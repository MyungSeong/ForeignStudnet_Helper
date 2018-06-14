package kr.ac.dongseo.foreignstudnet_helper;

import android.graphics.drawable.Drawable;

public class MatchingItemData
{
    private Drawable icon;
    private String name;
    private String contents;
    private String state;

    private String helpeephone;
    private String helperphone;

    private String latitude;
    private String longitude;

    private String idx;

    public Drawable getIcon() {
        return icon;
    }

    public void setIcon(Drawable icon) {
        this.icon = icon;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContents() {
        return contents;
    }

    public void setContents(String contents) {
        this.contents = contents;
    }

    public String getIdx() { return idx; }

    public void setIdx(String idx) { this.idx = idx; }

    public String getState() { return state; }

    public void setState(String state) { this.state = state; }

    public String getHelpeephone() { return helpeephone; }

    public void setHelpeephone(String helpeephone) { this.helpeephone = helpeephone; }

    public String getHelperphone() { return helperphone; }

    public void setHelperphone(String helperphone) { this.helperphone = helperphone; }

    public String getLatitude() { return latitude; }

    public void setLatitude(String latitude) { this.latitude = latitude; }

    public String getLongitude() { return longitude; }

    public void setLongitude(String longitude) { this.longitude = longitude; }
}
