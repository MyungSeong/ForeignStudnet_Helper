package kr.ac.dongseo.foreignstudnet_helper;

import android.app.Activity;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MainActivity extends Activity implements OnMapReadyCallback {
    private ListView mListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TabHost tabHost = (TabHost) findViewById(R.id.tab_host);
        tabHost.setup();

        // Tab1 Setting
        TabSpec tabSpec1 = tabHost.newTabSpec("Tab1");
        tabSpec1.setIndicator("도움 요청"); // Tab Subject
        tabSpec1.setContent(R.id.tab_view1); // Tab Content
        tabHost.addTab(tabSpec1);

        // Tab2 Setting
        TabSpec tabSpec2 = tabHost.newTabSpec("Tab2");
        tabSpec2.setIndicator("도우미 리스트"); // Tab Subject
        tabSpec2.setContent(R.id.tab_view2); // Tab Content
        tabHost.addTab(tabSpec2);

        // Tab3 Setting
        TabSpec tabSpec3 = tabHost.newTabSpec("Tab3");
        tabSpec3.setIndicator("도움 요청 확인"); // Tab Subject
        tabSpec3.setContent(R.id.tab_view3); // Tab Content
        tabHost.addTab(tabSpec3);

        tabHost.setCurrentTab(0);

        /* 첫 번째 탭
         *
         * 도움 요청 내용
         * http://mailmail.tistory.com/6
         */
        mListView = (ListView) findViewById(R.id.matchingSearch_listItem);
        dataSettings();

        /* 두번째 탭
         *
         * 도우미 리스트 내용
         */
        FragmentManager fragmentManager = getFragmentManager();
        MapFragment mapFragment = (MapFragment)fragmentManager
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        /* 세번째 탭
         *
         * 도움 요청 확인 내용
         */

    }

    private void dataSettings()
    {
        MyAdapter mMyAdapter = new MyAdapter();

        for (int i = 0; i < 10; i++)
        {
            mMyAdapter.addItem(ContextCompat.getDrawable(
                    getApplicationContext(), R.drawable.dummy_profile),
                    "Sundar Pichai_" + i, "I need help with public affairs");
        }

        mListView.setAdapter(mMyAdapter);
    }

    @Override
    public void onMapReady(final GoogleMap googleMap) {
        LatLng SEOUL = new LatLng(37.56, 126.97);

        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(SEOUL);
        markerOptions.title("서울");
        markerOptions.snippet("한국의 수도");
        googleMap.addMarker(markerOptions);

        googleMap.moveCamera(CameraUpdateFactory.newLatLng(SEOUL));
        googleMap.animateCamera(CameraUpdateFactory.zoomTo(10));
    }
}