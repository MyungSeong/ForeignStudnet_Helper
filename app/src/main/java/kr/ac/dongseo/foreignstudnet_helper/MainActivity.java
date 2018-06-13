package kr.ac.dongseo.foreignstudnet_helper;

import android.app.Activity;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Log;
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
import com.google.gson.JsonElement;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.NameValuePair;

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

        /*AsyncHttpClient client = new AsyncHttpClient("http://example.com");

        List<NameValuePair> params = new ArrayList<>();
        params.add(new NameValuePair("key", "value"));

        Headers headers = Headers.of("Header", "value");

        client.get("api/v1/", params, headers, new JsonResponseHandler()
        {
            @Override public void onSuccess()
            {
                JsonElement result = getContent();
            }
        });*/

        getAllMatch();


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

    public void getAllMatch() {
        HttpConnectionManager httpConnectionManager;
        httpConnectionManager = new HttpConnectionManager(getApplicationContext());

        final String TAG = "[GET ALL MATCH LIST] ";

        try {
            httpConnectionManager.getAllMatch(new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    /*int rows = response.getInt("rows");
                    int index = response.getInt("0.idx");
                    String mail = response.getString("mail");
                    String helper = response.getString("helper");
                    String helpee = response.getString("helpee");
                    String title = response.getString("title");
                    String content = response.getString("content");
                    String state = response.getString("state");

                    final String TAG = "[GET ALL MATCH LIST] ";

                    Log.d(TAG, response.toString());

                    Log.d(TAG, "rows:" + rows);
                    Log.d(TAG, "index:" + index);
                    Log.d(TAG, "mail:" + mail);
                    Log.d(TAG, "helper:" + helper);
                    Log.d(TAG, "helpee:" + helpee);
                    Log.d(TAG, "title:" + title);
                    Log.d(TAG, "content:" + content);
                    Log.d(TAG, "state:" + state);*/

                    try {

                        String[] arraySum = new String[20];
                        JSONArray jsonArray = new JSONObject(response.toString()).getJSONArray("0");
                        for (int i = 0; i < jsonArray.length(); i++)
                        {
                            HashMap map = new HashMap<>();
                            JSONObject jsonObject = jsonArray.getJSONObject(i);

                            String idx = jsonObject.optString("idx");

                            arraySum[i] = idx;
                        }

                        Log.d(TAG, arraySum[0]);

                        JSONObject jsonObject = response.optJSONObject("0");
                        int rows = jsonObject.getInt("rows");
                        int idx = jsonObject.getInt("idx");
                        String helper = jsonObject.getString("helper");
                        String helpee = jsonObject.getString("helpee");
                        String title = jsonObject.getString("title");
                        String content = jsonObject.getString("content");
                        String state = jsonObject.getString("state");


                        /*int rows = response.getInt("rows");
                        int idx = response.getInt("idx");
                        String helper = response.getString("helper");
                        String helpee = response.getString("helpee");
                        String title = response.getString("title");
                        String content = response.getString("content");
                        String state = response.getString("state");*/

                        Log.d(TAG, "rows:" + rows);
                        Log.d(TAG, "idx:" + idx);
                        Log.d(TAG, "helper:" + helper);
                        Log.d(TAG, "helpee:" + helpee);
                        Log.d(TAG, "title:" + title);
                        Log.d(TAG, "content:" + content);
                        Log.d(TAG, "state:" + state);
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Log.d(TAG, e.getMessage());
                    }

                    Log.d(TAG, response.toString());
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                    Log.d(TAG, errorResponse.toString());
                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
            Log.d(TAG, e.getMessage());
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            Log.d(TAG, e.getMessage());
        }
    }
}