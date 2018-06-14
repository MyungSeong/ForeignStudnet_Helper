package kr.ac.dongseo.foreignstudnet_helper;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.LocalActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.gson.JsonElement;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.NameValuePair;

public class MainActivity extends Activity implements ActivityAdapterInterface {
    HttpConnectionManager httpConnectionManager;

    MemberData memberData;
    MemberData memberDataInst;

    private View mHeader;
    private ListView mListView;
    private ListView mListView_myEventList;
    private EditText etTitle;
    private EditText etContent;
    private Button btnSend;
    private Button btnComplete;

    //private int currentTab;

    private long pressedTime;

    private final int PERMISSIONS_ACCESS_FINE_LOCATION = 1000;
    private final int PERMISSIONS_ACCESS_COARSE_LOCATION = 1001;
    private boolean isAccessFineLocation = false;
    private boolean isAccessCoarseLocation = false;
    private boolean isPermission = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom);

        httpConnectionManager = new HttpConnectionManager(getApplicationContext());
        memberData = (MemberData) getApplication();
        memberDataInst = MemberData.getInstance();

        //mHeader = getLayoutInflater().inflate(R.layout.dialog_request_complete, null, false);
        LayoutInflater inflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.dialog_request_complete, null);

        btnSend = (Button) findViewById(R.id.send_btn);
        btnComplete = (Button) view.findViewById(R.id.item_complete_ok);
        etTitle = (EditText) findViewById(R.id.title_edit);
        etContent = (EditText) findViewById(R.id.content_edit);

        final TabHost tabHost = (TabHost) findViewById(R.id.tab_host);
        tabHost.setup();

        // Tab1 Setting
        final TabSpec tabSpec1 = tabHost.newTabSpec("Tab1");
        tabSpec1.setIndicator("내 이벤트"); // Tab Subject
        tabSpec1.setContent(R.id.tab_view1); // Tab Content
        tabHost.addTab(tabSpec1);

        // Tab2 Setting
        final TabSpec tabSpec2 = tabHost.newTabSpec("Tab2");
        tabSpec2.setIndicator("이벤트 리스트"); // Tab Subject
        tabSpec2.setContent(R.id.tab_view2); // Tab Content
        tabHost.addTab(tabSpec2);

        // Tab3 Setting
        final TabSpec tabSpec3 = tabHost.newTabSpec("Tab3");
        tabSpec3.setIndicator("도움 요청"); // Tab Subject
        tabSpec3.setContent(R.id.tab_view3); // Tab Content
        tabHost.addTab(tabSpec3);

        /*if (memberDataInst.getIsHelper() == 1) {
            tabHost.getTabWidget().removeView(tabHost.getTabWidget().getChildTabViewAt(2));
            tabHost.setCurrentTab(0);
            memberDataInst.setCurrentTab(0);
        } else {
            tabHost.setCurrentTab(1);
            memberDataInst.setCurrentTab(1);
        }*/
        tabHost.setCurrentTab(2);

        /* 첫 번째 탭
         *
         * 도움 요청 내용
         * http://mailmail.tistory.com/6
         */
        final String reqTag = "[Request Match] ";

        /*final MemberData memberData = MemberData.getInstance();
        final int isHelper = memberData.getIsHelper();

        if (isHelper == 1) {
            mListView = (ListView) findViewById(R.id.matchingSearch_listItem);
            mListView2 = (ListView) findViewById(R.id.matchingSearch_myList);
        } else {
            mListView = (ListView) findViewById(R.id.matchingSearch_listItem);
            mListView2 = (ListView) findViewById(R.id.matchingSearch_myList);
        }*/
        mListView = (ListView) findViewById(R.id.matchingSearch_listItem);
        mListView_myEventList = (ListView) findViewById(R.id.matchingSearch_myList);

        btnSend.setOnClickListener(new View.OnClickListener() {
            double latitude;
            double longitude;

            @Override
            public void onClick(View v) {
                try {
                    boolean checkBlank = (TextUtils.isEmpty(etTitle.getText().toString()) ||
                            TextUtils.isEmpty(etContent.getText().toString()));

                    if (checkBlank) {
                        Toast.makeText(getApplicationContext(),
                                "빈 칸이 있는지 확인해주세요", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    GPSManager gpsManager = new GPSManager(MainActivity.this);

                    if (!isPermission) {
                        callPermission();
                        return;
                    }

                    if (gpsManager.isGetLocation) {
                        latitude = gpsManager.getLatitude();
                        longitude = gpsManager.getLongitude();

                        memberDataInst.setLatitude(String.valueOf(latitude));
                        memberDataInst.setLongitude(String.valueOf(longitude));

                        Log.d("GPS DATA", "LATITUDE: " + latitude);
                        Log.d("GPS DATA", "LONGITUDE: " + longitude);
                    } else {
                        gpsManager.showSettingsAlert();
                    }

                    httpConnectionManager.requestMatch(
                            memberDataInst.getEmail(),
                            etTitle.getText().toString(),
                            etContent.getText().toString(),
                            memberDataInst.getPhone(),
                            String.valueOf(latitude), String.valueOf(longitude),
                            new JsonHttpResponseHandler() {
                                @Override
                                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                                    try {
                                        int success = response.getInt("success");

                                        if (success == 1) {
                                            Toast.makeText(getApplicationContext(), "도움 요청을 보냈습니다",
                                                    Toast.LENGTH_SHORT).show();
                                            dataSettings(); // 매칭 리스트 갱신
                                        } else {
                                            String errReason = response.getString("err");

                                            if (errReason.equals("already matching")) {
                                                Toast.makeText(getApplicationContext(), "이미 도움 요청을 했습니다",
                                                        Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                        Log.d(reqTag, "Success: " + success);
                                        Log.d(reqTag, "Msg: " + response);
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                        Log.d(reqTag, "Msg: " + response);
                                    }
                                }

                                @Override
                                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                                    Log.d(reqTag, "Request Failed " + errorResponse.toString());
                                }
                            });
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
        });

        /* 두번째 탭
         *
         * 모든 이벤트 리스트
         */
        dataSettings();

        /* 세번째 탭
         *
         * 내 이벤트 리스트
         */
        getMyEventList();

        /*final String completeTag = "[Complete Match] ";

        btnComplete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (memberDataInst.getIsHelper() == 1) {
                    try {
                        httpConnectionManager.completeMatch(memberDataInst.getEmail(), String.valueOf(memberDataInst.getIdx()), new JsonHttpResponseHandler() {
                            @Override
                            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                                int success = 0;

                                try {
                                    success = response.getInt("success");

                                    if (success == 1) {
                                        Log.d(completeTag, "Success: " + success);
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                Log.d(completeTag, response.toString());
                            }

                            @Override
                            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                                super.onFailure(statusCode, headers, throwable, errorResponse);
                            }
                        });
                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                } else {
                    Log.d(completeTag, "Is Helper? " + memberDataInst.getIsHelper());
                }
            }
        });*/

        tabHost.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
            @Override
            public void onTabChanged(String tabId) {
                if (tabId.equals("Tab1")) {
                    memberDataInst.setCurrentTab(0);
                }

                if (tabId.equals("Tab2")) {
                    memberDataInst.setCurrentTab(1);
                }

                if (tabId.equals("Tab3")) {
                    memberDataInst.setCurrentTab(2);
                }

                Log.d("DEBUG", "currentTab: " + memberDataInst.getCurrentTab());
            }
        });
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        if (pressedTime == 0) {
            Toast.makeText(this, "한번 더 누르면 종료됩니다", Toast.LENGTH_SHORT).show();
            pressedTime = System.currentTimeMillis();
        } else {
            int seconds = (int) (System.currentTimeMillis() - pressedTime);

            if (seconds > 2000) {
                pressedTime = 0;
            } else {
                finish();
            }
        }
    }

    public void dataSettings() {
        MyAdapter mMyAdapter = new MyAdapter(getApplicationContext(), new ActivityAdapterInterface() {
            @Override
            public void callGetEventList() {
                dataSettings();
                Log.d("call :", "callGetEventList()");
            }

            @Override
            public void callGetMyEventList() {
                getMyEventList();
                Log.d("call :", "callGetMyEventList()");
            }
        });

        /*for (int i = 0; i < 10; i++)
        {
            mMyAdapter.addItem(ContextCompat.getDrawable(
                    getApplicationContext(), R.drawable.eventlist_profile),
                    "Sundar Pichai_" + i, "I need help with public affairs");
        }*/

        getAllMatch(mMyAdapter);

        mListView.setAdapter(mMyAdapter);
    }

    public void getMyEventList() {
        MyAdapter mMyAdapter = new MyAdapter(getApplicationContext(), new ActivityAdapterInterface() {
            @Override
            public void callGetEventList() {
                dataSettings();
                Log.d("call :", "callGetEventList()");
            }

            @Override
            public void callGetMyEventList() {
                getMyEventList();
                Log.d("call :", "callGetMyEventList()");
            }
        });

        getSelectedMatch(mMyAdapter);

        mListView_myEventList.setAdapter(mMyAdapter);
    }

    public void getAllMatch(final MyAdapter myAdapter) {
        HttpConnectionManager httpConnectionManager;
        httpConnectionManager = new HttpConnectionManager(getApplicationContext());

        final String TAG = "[GET ALL MATCH LIST] ";

        try {
            httpConnectionManager.getAllMatch(new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    try {
                        int rows = response.getInt("rows");
                        int idx = 0;
                        String eventIdx = null;
                        String helper = null;
                        String helpee = null;
                        String title = null;
                        String content = null;
                        String state = null;
                        String helpeePhone = null;
                        String helperPhone = null;
                        String latitude = null;
                        String longitude = null;

                        for (int i = 0; i < rows; i++)
                        {
                            JSONObject jsonObject = response.optJSONObject("" + i);
                            idx = jsonObject.getInt("idx");
                            helper = jsonObject.getString("helper");
                            helpee = jsonObject.getString("helpee");
                            title = jsonObject.getString("title");
                            content = jsonObject.getString("content");
                            state = jsonObject.getString("state");
                            helpeePhone = jsonObject.getString("helpeePhone");
                            helperPhone = jsonObject.getString("helperPhone");
                            latitude = jsonObject.getString("latitude");
                            longitude = jsonObject.getString("longitude");

                            eventIdx = String.valueOf(idx);

                            myAdapter.addItem(ContextCompat.getDrawable(
                                    getApplicationContext(), R.drawable.eventlist_profile),
                                    title, content, state, helpeePhone, helperPhone, eventIdx);

                            memberData.setIdx(idx);

                            Log.d(TAG, "rows:" + rows);
                            Log.d(TAG, "[" + "Index: " + i + "] " + "idx:" + idx);
                            Log.d(TAG, "[" + "Index: " + i + "] " + "helper:" + helper);
                            Log.d(TAG, "[" + "Index: " + i + "] " + "helpee:" + helpee);
                            Log.d(TAG, "[" + "Index: " + i + "] " + "title:" + title);
                            Log.d(TAG, "[" + "Index: " + i + "] " + "content:" + content);
                            Log.d(TAG, "[" + "Index: " + i + "] " + "state:" + state);
                        }
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

    public void getSelectedMatch(final MyAdapter myAdapter) {
        HttpConnectionManager httpConnectionManager;
        httpConnectionManager = new HttpConnectionManager(getApplicationContext());

        final String TAG = "[GET SELECTED LIST] ";

        try {
            httpConnectionManager.getUserMatch(memberDataInst.getEmail(), memberDataInst.getIsHelper(), new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    try {
                        int success = response.getInt("success");
                        int rows = response.getInt("rows");
                        int idx = 0;
                        String eventIdx = null;
                        String helper = null;
                        String helpee = null;
                        String title = null;
                        String content = null;
                        String state = null;
                        String helpeePhone = null;
                        String helperPhone = null;

                        if (success == 1) {
                            Toast.makeText(getApplicationContext(), "내 이벤트 불러오기 성공",
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getApplicationContext(), "내 이벤트 불러오기 실패",
                                    Toast.LENGTH_SHORT).show();
                        }

                        for (int i = 0; i < rows; i++) {
                            JSONObject jsonObject = response.optJSONObject("" + i);
                            idx = jsonObject.getInt("idx");
                            helper = jsonObject.getString("helper");
                            helpee = jsonObject.getString("helpee");
                            title = jsonObject.getString("title");
                            content = jsonObject.getString("content");
                            state = jsonObject.getString("state");
                            helpeePhone = jsonObject.getString("helpeePhone");
                            helperPhone = jsonObject.getString("helperPhone");

                            eventIdx = String.valueOf(idx);

                            if (!state.equals("complete")) {
                                /*if (myAdapter.getCount() == rows) {
                                    return;
                                } else {*/
                                myAdapter.addItem(ContextCompat.getDrawable(
                                        getApplicationContext(), R.drawable.eventlist_profile),
                                        title, content, state, helpeePhone, helperPhone, eventIdx);
                            }

                            Log.d(TAG, "[" + "Index: " + i + "] " + "idx: " + idx);
                            Log.d(TAG, "[" + "Index: " + i + "] " + "helper: " + helper);
                            Log.d(TAG, "[" + "Index: " + i + "] " + "helpee: " + helpee);
                            Log.d(TAG, "[" + "Index: " + i + "] " + "title: " + title);
                            Log.d(TAG, "[" + "Index: " + i + "] " + "content: " + content);
                            Log.d(TAG, "[" + "Index: " + i + "] " + "state: " + state);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Log.d(TAG, e.getMessage());
                    }
                    Log.d(TAG, response.toString());
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                    Log.d(TAG, "Error: " + errorResponse.toString());
                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
            Log.d(TAG, e.getMessage());
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onRequestPermissionsResult ( int requestCode, String[] permissions,
                                             int[] grantResults) {
        if (requestCode == PERMISSIONS_ACCESS_FINE_LOCATION
                && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

            isAccessFineLocation = true;

        } else if (requestCode == PERMISSIONS_ACCESS_COARSE_LOCATION
                && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

            isAccessCoarseLocation = true;
        }

        if (isAccessFineLocation && isAccessCoarseLocation) {
            isPermission = true;
        }
    }

    // 전화번호 권한 요청
    private void callPermission() {
        // Check the SDK version and whether the permission is already granted or not.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
                && checkSelfPermission(android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            requestPermissions(
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_ACCESS_FINE_LOCATION);

        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
                && checkSelfPermission(android.Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            requestPermissions(
                    new String[]{android.Manifest.permission.ACCESS_COARSE_LOCATION},
                    PERMISSIONS_ACCESS_COARSE_LOCATION);
        } else {
            isPermission = true;
        }
    }

    @Override
    public void callGetEventList() {
        dataSettings();
    }

    @Override
    public void callGetMyEventList() {
        getMyEventList();
    }
}