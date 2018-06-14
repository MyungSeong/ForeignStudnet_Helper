package kr.ac.dongseo.foreignstudnet_helper;

import java.util.ArrayList;

public class MemberAdapter {
    private ArrayList<MemberData> mMemberData = new ArrayList<>();

    public void addItem(MemberData item) { mMemberData.add(item); }

    public MemberData getItem(int position) {
        return mMemberData.get(position);
    }

    public int getSize() {
        return mMemberData.size();
    }

    public boolean isEmpty() {
        return mMemberData.isEmpty();
    }
}