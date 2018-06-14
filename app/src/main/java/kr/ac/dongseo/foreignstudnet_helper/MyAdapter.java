package kr.ac.dongseo.foreignstudnet_helper;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Member;
import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class MyAdapter extends BaseAdapter {
    private Context context = null;
    private ActivityAdapterInterface mActivityAdapterInterface;

    private HttpConnectionManager httpConnectionManager;

    private CustomDialog mCustomDialog;
    private ArrayList<MatchingItemData> mItems = new ArrayList<>();

    private MemberData memberData = MemberData.getInstance();
    private int isHelper = memberData.getIsHelper();

    private String eventIndex;

    public MyAdapter(Context context, ActivityAdapterInterface activityAdapterInterface) {
        this.context = context;
        mActivityAdapterInterface = activityAdapterInterface;
    }

    public void deleteItems() { mItems.removeAll(mItems); }

    @Override
    public int getCount() {
        return mItems.size();
    }

    @Override
    public MatchingItemData getItem(int position) {
        return mItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        Context context = parent.getContext();

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(
                    Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.listitem_matching_search, parent,
                    false);
        }

        final ImageView ivImg = (ImageView) convertView.findViewById(R.id.ivImg);
        final TextView tvName = (TextView) convertView.findViewById(R.id.tvName);
        final TextView tvContents = (TextView) convertView.findViewById(R.id.tvContents);
        final TextView tvState = (TextView) convertView.findViewById(R.id.tvState);
        final TextView tvHelpeePhone = (TextView) convertView.findViewById(R.id.tvHelpeePhone);
        final TextView tvHelperPhone = (TextView) convertView.findViewById(R.id.tvHelperPhone);

        final MatchingItemData matchingItemData = getItem(position);

        ivImg.setImageDrawable(matchingItemData.getIcon());
        ivImg.setScaleType(ImageView.ScaleType.FIT_XY);
        tvName.setText(matchingItemData.getName());
        tvContents.setText(matchingItemData.getContents());
        tvState.setText(matchingItemData.getState());
        tvState.setVisibility(View.VISIBLE);
        tvHelpeePhone.setText(matchingItemData.getHelpeephone());
        tvHelperPhone.setText(matchingItemData.getHelperphone());

        eventIndex = matchingItemData.getIdx();

        ivImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCustomDialog = new CustomDialog(v.getContext(), isHelper == 1 ? "도움을 수락하시겠습니까?" : "도움을 요청하시겠습니까?",
                        tvName.getText().toString(), tvContents.getText().toString(), matchingItemData.getIdx(), leftListener, rightListener);
                mCustomDialog.show();

                getSelectMatchIdx(position);

                /*Log.d("GET MEMBER DATA: ", "Is Helper?: " + new DummyAdapter().getMemberData().getIsHelper());
                Log.d("GET MEMBER DATA: ", "Name: " + new DummyAdapter().getMemberData().getName());*/
            }
        });

        tvName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCustomDialog = new CustomDialog(v.getContext(), isHelper == 1 ? "도움을 수락하시겠습니까?" : "도움을 요청하시겠습니까?",
                        tvName.getText().toString(), tvContents.getText().toString(), matchingItemData.getIdx(), leftListener, rightListener);
                mCustomDialog.show();
            }
        });

        tvContents.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCustomDialog = new CustomDialog(v.getContext(), isHelper == 1 ? "도움을 수락하시겠습니까?" : "도움을 요청하시겠습니까?",
                        tvName.getText().toString(), tvContents.getText().toString(), matchingItemData.getIdx(), leftListener, rightListener);
                mCustomDialog.show();
            }
        });

        Log.d("DEBUG", "getCurrentTab: " + memberData.getCurrentTab());

        if (memberData.getCurrentTab() == 0) {
            ivImg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mCustomDialog = new CustomDialog(v.getContext(), isHelper == 1 ? "도움을 완료하시겠습니까?" : "",
                            tvName.getText().toString(), tvContents.getText().toString(), matchingItemData.getIdx(), completeListener);
                    mCustomDialog.show();
                }
            });

            tvName.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mCustomDialog = new CustomDialog(v.getContext(), isHelper == 1 ? "도움을 완료하시겠습니까?" : "",
                            tvName.getText().toString(), tvContents.getText().toString(), matchingItemData.getIdx(), completeListener);
                    mCustomDialog.show();
                }
            });

            tvContents.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mCustomDialog = new CustomDialog(v.getContext(), isHelper == 1 ? "도움을 완료하시겠습니까?" : "",
                            tvName.getText().toString(), tvContents.getText().toString(), matchingItemData.getIdx(), completeListener);
                    mCustomDialog.show();
                }
            });

            Log.d("DEBUG", "currentTab: " + memberData.getCurrentTab());
        }

        if (memberData.getCurrentTab() == 1) {
            tvHelperPhone.setVisibility(View.GONE);
        }

        return convertView;
    }

    public void addItem(Drawable img, String name, String contents, String state,
                        String helpeePhone, String helperPhone, String idx)
    {
        MatchingItemData mItem = new MatchingItemData();

        mItem.setIcon(img);
        mItem.setName(name);
        mItem.setContents(contents);
        mItem.setState(state);
        mItem.setHelpeephone(helpeePhone);
        mItem.setHelperphone(helperPhone);
        mItem.setIdx(idx);

        mItems.add(mItem);
    }

    // Custom Dialog
    private View.OnClickListener leftListener = new View.OnClickListener()
    {
        public void onClick(final View v)
        {
            httpConnectionManager = new HttpConnectionManager(context);

            final String TAG = "[DECIDE MATCH] ";

            if (isHelper == 1) {
                try {
                    httpConnectionManager.decideMatch(memberData.getEmail(), Integer.parseInt(eventIndex), memberData.getPhone(), new JsonHttpResponseHandler() {
                        @Override
                        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                            try {
                                int success = response.getInt("success");

                                if (success == 1) {
                                    Log.d(TAG, "Success: " + success);
                                    mActivityAdapterInterface.callGetEventList();
                                    mActivityAdapterInterface.callGetMyEventList();
                                    Toast.makeText(v.getContext(), "해당 유저의 도움을 수락했습니다",
                                            Toast.LENGTH_SHORT).show();
                                } else {
                                    String errReason = response.getString("err");
                                    Log.d(TAG, "Error: " + errReason);
                                    Toast.makeText(v.getContext(), "해당 유저의 도움을 수락하지 못했습니다",
                                            Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                            Log.d(TAG, "Error: " + errorResponse);
                        }
                    });
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
            /*Toast.makeText(v.getContext(), isHelper == 1 ? "해당 유저의 도움을 수락했습니다" : "해당 유저에게 도움을 신청했습니다",
                    Toast.LENGTH_SHORT).show();*/
            mCustomDialog.dismiss();
        }
    };

    private View.OnClickListener rightListener = new View.OnClickListener()
    {
        public void onClick(View v)
        {
            Toast.makeText(v.getContext(), isHelper == 1 ? "해당 유저의 도움을 수락했습니다" : "해당 유저에게 도움을 신청했습니다",
                    Toast.LENGTH_SHORT).show();
            mCustomDialog.dismiss();
        }
    };

    private View.OnClickListener completeListener = new View.OnClickListener() {
        final String completeTag = "[Complete Match] ";

        @Override
        public void onClick(final View v) {
            httpConnectionManager = new HttpConnectionManager(context);

            if (memberData.getIsHelper() == 1) {
                try {
                    httpConnectionManager.completeMatch(memberData.getEmail(), Integer.parseInt(eventIndex), new JsonHttpResponseHandler() {
                        @Override
                        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                            int success = 0;

                            try {
                                success = response.getInt("success");

                                if (success == 1) {
                                    mActivityAdapterInterface.callGetMyEventList();
                                    v.getContext().startActivity(new Intent(v.getContext(), MainActivity.class));
                                }

                                Toast.makeText(v.getContext(), success == 1 ? "도움을 완료했습니다" : "도움 완료에 실패했습니다",
                                        Toast.LENGTH_SHORT).show();
                                Log.d(completeTag, "Success: " + success);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            Log.d(completeTag, response.toString());
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                            Log.d(completeTag, "Error: " + errorResponse);
                        }
                    });
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
            mCustomDialog.dismiss();
        }
    };

    public void getSelectMatchIdx(final int row) {
        HttpConnectionManager httpConnectionManager;
        httpConnectionManager = new HttpConnectionManager(context);

        final String TAG = "[GET SELETE MATCH LIST] ";

        try {
            httpConnectionManager.getAllMatch(new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    try {
                        int rows = response.getInt("rows");
                        int idx = 0;
                        String helper = null;
                        String helpee = null;
                        String title = null;
                        String content = null;
                        String state = null;
                        String helpeePhone = null;
                        String helperPhone = null;
                        String latitude; // only helpee
                        String longitude;

                        JSONObject jsonObject = response.optJSONObject("" + row);
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

                        Log.d(TAG, "[" + "Index: " + row + "] " + "idx:" + idx);
                        Log.d(TAG, "[" + "Index: " + row + "] " + "helper:" + helper);
                        Log.d(TAG, "[" + "Index: " + row + "] " + "helpee:" + helpee);
                        Log.d(TAG, "[" + "Index: " + row + "] " + "title:" + title);
                        Log.d(TAG, "[" + "Index: " + row + "] " + "content:" + content);
                        Log.d(TAG, "[" + "Index: " + row + "] " + "state:" + state);
                        Log.d(TAG, "[" + "Index: " + row + "] " + "helpeePhone:" + helpeePhone);
                        Log.d(TAG, "[" + "Index: " + row + "] " + "helperPhone:" + helperPhone);

                        memberData.setIdx(idx);
                        Log.d(TAG, "[" + "Index: " + row + "] " + "MemberData idx:" + memberData.getIdx());
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

    /*public void getSelectMatch(final int row) {
        HttpConnectionManager httpConnectionManager;
        httpConnectionManager = new HttpConnectionManager(context);

        final String TAG = "[GET SELETE MATCH LIST] ";

        try {
            httpConnectionManager.getAllMatch(new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    try {
                        int rows = response.getInt("rows");
                        int idx = 0;
                        String helper = null;
                        String helpee = null;
                        String title = null;
                        String content = null;
                        String state = null;

                        JSONObject jsonObject = response.optJSONObject("" + row);
                        idx = jsonObject.getInt("idx");
                        helper = jsonObject.getString("helper");
                        helpee = jsonObject.getString("helpee");
                        title = jsonObject.getString("title");
                        content = jsonObject.getString("content");
                        state = jsonObject.getString("state");

                        Log.d(TAG, "[" + "Index: " + row + "] " + "idx:" + idx);
                        Log.d(TAG, "[" + "Index: " + row + "] " + "helper:" + helper);
                        Log.d(TAG, "[" + "Index: " + row + "] " + "helpee:" + helpee);
                        Log.d(TAG, "[" + "Index: " + row + "] " + "title:" + title);
                        Log.d(TAG, "[" + "Index: " + row + "] " + "content:" + content);
                        Log.d(TAG, "[" + "Index: " + row + "] " + "state:" + state);
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
    }*/
}
