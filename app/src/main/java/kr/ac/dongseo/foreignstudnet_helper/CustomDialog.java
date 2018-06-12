package kr.ac.dongseo.foreignstudnet_helper;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

public class CustomDialog extends Dialog {

    private TextView mTitleView;
    private TextView mCountryView;
    private TextView mLanguageView;
    private Button mLeftButton;
    private Button mRightButton;
    private String mTitle;
    private String mCountry;
    private String mLanguage;

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

        setContentView(R.layout.dialog_request_help);

        mTitleView = (TextView) findViewById(R.id.item_true_title);
        mCountryView = (TextView) findViewById(R.id.item_true_country);
        mLanguageView = (TextView) findViewById(R.id.item_true_language);
        mLeftButton = (Button) findViewById(R.id.item_true_ok);
        mRightButton = (Button) findViewById(R.id.item_true_cancle);

        // 제목과 내용을 생성자에서 셋팅한다.
        mTitleView.setText(mTitle);
        mCountryView.setText(mCountry);
        mLanguageView.setText(mLanguage);

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

    // 클릭버튼이 하나일때 생성자 함수로 클릭이벤트를 받는다.
    public CustomDialog(Context context, String title, String country, String language,
                        View.OnClickListener singleListener) {
        super(context, android.R.style.Theme_Translucent_NoTitleBar);
        this.mTitle = title;
        this.mLeftClickListener = singleListener;
    }

    // 클릭버튼이 확인과 취소 두개일때 생성자 함수로 이벤트를 받는다
    public CustomDialog(Context context, String title,
                        String country, String language, View.OnClickListener leftListener,
                        View.OnClickListener rightListener) {
        super(context, android.R.style.Theme_Translucent_NoTitleBar);
        this.mTitle = title;
        this.mCountry = country;
        this.mLanguage = language;
        this.mLeftClickListener = leftListener;
        this.mRightClickListener = rightListener;
    }
}