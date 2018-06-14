package kr.ac.dongseo.foreignstudnet_helper;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.EventListener;

public class CustomDialog extends Dialog {
    private TextView mTitleView;
    private TextView mContentView;
    private TextView mCountryView;
    private TextView mLanguageView;
    private TextView mEventIndexView;
    private Button mLeftButton;
    private Button mRightButton;
    private String mTitle;
    private String mContent;
    private String mCountry;
    private String mLanguage;
    private String mEventIndex;

    private Button mMapViewButton;

    private View.OnClickListener mLeftClickListener;
    private View.OnClickListener mRightClickListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 다이얼로그 외부 화면 흐리게 표현
        WindowManager.LayoutParams lpWindow = new WindowManager.LayoutParams();
        lpWindow.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        lpWindow.dimAmount = 0.8f;
        getWindow().setAttributes(lpWindow);

        MemberData memberData = MemberData.getInstance();
        int isHelper = memberData.getIsHelper();

        if (isHelper == 1) {
            setContentView(R.layout.dialog_request_decide);

            mTitleView = findViewById(R.id.item_decide_title);
            mContentView = findViewById(R.id.item_decide_content);
            mCountryView = findViewById(R.id.item_decide_country);
            mLanguageView = findViewById(R.id.item_decide_language);
            mLeftButton = findViewById(R.id.item_decide_ok);
            mRightButton = findViewById(R.id.item_decide_cancle);
            mEventIndexView = findViewById(R.id.item_decide_eventIndex);

            mMapViewButton = findViewById(R.id.item_decide_mapview);
            mMapViewButton.setVisibility(View.VISIBLE);

            mMapViewButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(v.getContext(), MapActivity.class);
                    v.getContext().startActivity(intent);
                }
            });
        } else {
            setContentView(R.layout.dialog_request_help);

            mTitleView = findViewById(R.id.item_true_title);
            mContentView = findViewById(R.id.item_true_content);
            mCountryView = findViewById(R.id.item_true_country);
            mLanguageView = findViewById(R.id.item_true_language);
            mLeftButton = findViewById(R.id.item_true_ok);
            mRightButton = findViewById(R.id.item_true_cancle);
            mEventIndexView = findViewById(R.id.item_true_eventIndex);
        }

        if (memberData.getCurrentTab() == 0) {
            setContentView(R.layout.dialog_request_complete);

            mTitleView = findViewById(R.id.item_complete_title);
            mContentView = findViewById(R.id.item_complete_content);
            mCountryView = findViewById(R.id.item_complete_country);
            mLanguageView = findViewById(R.id.item_complete_language);
            mLeftButton = findViewById(R.id.item_complete_ok);
            mEventIndexView = findViewById(R.id.item_complete_eventIndex);
        }

        Log.d("Is Helper? ", "Msg: " + isHelper);

        // 제목과 내용을 생성자에서 셋팅한다.
        mTitleView.setText(mTitle);
        mContentView.setText(mContent);
        mCountryView.setText(mCountry);
        mLanguageView.setText(mLanguage);
        mEventIndexView.setText(mEventIndex);

        /*if (memberData.getCurrentTab() == 0) {
            mTitleView.setText("도움을 완료하시겠습니까?");
        }*/

        // 클릭 이벤트 셋팅
        if (mLeftClickListener != null && mRightClickListener != null) {
            mLeftButton.setOnClickListener(mLeftClickListener);
            mRightButton.setOnClickListener(mRightClickListener);
        } else if (mLeftClickListener != null
                && mRightClickListener == null) {
            mLeftButton.setOnClickListener(mLeftClickListener);
        } else {

        }
    }

    // 클릭버튼 X
    public CustomDialog(Context context, String title,
                        String country, String language, String eventIndex) {
        super(context, android.R.style.Theme_Translucent_NoTitleBar);
        this.mTitle = title;
        this.mCountry = country;
        this.mLanguage = language;
        this.mEventIndex = eventIndex;
    }

    // 클릭버튼이 하나일때 생성자 함수로 클릭이벤트를 받는다.
    public CustomDialog(Context context, String title,
                        String country, String language, String eventIndex,
                        View.OnClickListener singleListener) {
        super(context, android.R.style.Theme_Translucent_NoTitleBar);
        this.mTitle = title;
        this.mCountry = country;
        this.mLanguage = language;
        this.mEventIndex = eventIndex;
        this.mLeftClickListener = singleListener;
    }

    // 클릭버튼이 확인과 취소 두개일때 생성자 함수로 이벤트를 받는다
    public CustomDialog(Context context, String title,
                        String country, String language, String eventIndex,
                        View.OnClickListener leftListener,
                        View.OnClickListener rightListener) {
        super(context, android.R.style.Theme_Translucent_NoTitleBar);
        this.mTitle = title;
        this.mCountry = country;
        this.mLanguage = language;
        this.mEventIndex = eventIndex;
        this.mLeftClickListener = leftListener;
        this.mRightClickListener = rightListener;
    }
}