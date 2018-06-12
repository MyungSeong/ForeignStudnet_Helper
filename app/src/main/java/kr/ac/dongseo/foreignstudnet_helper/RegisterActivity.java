package kr.ac.dongseo.foreignstudnet_helper;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.TextureView;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import cz.msebera.android.httpclient.Header;

public class RegisterActivity extends Activity
{
    private SessionManager smgr;
    private HttpConnectionManager httpConnectionManager;

    EditText etEmail;
    EditText etPassword;
    EditText etPasswordConfirm;
    EditText etName;
    EditText etPhone;
    CheckBox chkHelper;

    Spinner spCountry;
    Spinner spLanguage;

    CheckBox cbAgree;
    Button btnOK;

    final String[] country = { "South Korea", "USA", "China" };
    final String[] language = { "Korean", "English", "Chinese" };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        smgr = new SessionManager(getApplicationContext());
        smgr.clearSession();

        httpConnectionManager = new HttpConnectionManager(getApplicationContext());

        etEmail = (EditText) findViewById(R.id.user_Email);
        etPassword = (EditText) findViewById(R.id.user_passWord);
        etPasswordConfirm = (EditText) findViewById(R.id.user_passWordAgain);
        etName = (EditText) findViewById(R.id.user_name);
        etPhone = (EditText) findViewById(R.id.user_Phone);
        chkHelper = (CheckBox) findViewById(R.id.chkHelper);

        spCountry = (Spinner) findViewById(R.id.spinner_Country);
        spLanguage = (Spinner) findViewById(R.id.spinner_Language);

        cbAgree = (CheckBox) findViewById(R.id.personalInfo);
        btnOK = (Button) findViewById(R.id.btn_agreeJoin);

        etPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String password = etPassword.getText().toString();
                String confirm = etPasswordConfirm.getText().toString();

                if (password.equals(confirm))
                {
                    etPassword.setBackgroundColor(Color.GREEN);
                    etPasswordConfirm.setBackgroundColor(Color.GREEN);
                }
                else
                {
                    etPassword.setBackgroundColor(Color.RED);
                    etPasswordConfirm.setBackgroundColor(Color.RED);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        // 국적, 언어 스피너 드롭다운
        ArrayAdapter<String> countryAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_dropdown_item, country);
        spCountry.setAdapter(countryAdapter);

        ArrayAdapter<String> languageAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_dropdown_item, language);
        spLanguage.setAdapter(languageAdapter);

        btnOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ( cbAgree.isChecked() )
                {
                    v.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (etEmail.getText().toString().length() == 0)
                            {
                                Toast.makeText(RegisterActivity.this,
                                        "이메일을 입력하세요!", Toast.LENGTH_SHORT).show();
                                etEmail.requestFocus();
                                return;
                            }

                            if (etPassword.getText().toString().length() == 0)
                            {
                                Toast.makeText(RegisterActivity.this,
                                        "비밀번호를 입력하세요!", Toast.LENGTH_SHORT).show();
                                etPassword.requestFocus();
                                return;
                            }

                            if (etPasswordConfirm.getText().toString().length() == 0)
                            {
                                Toast.makeText(RegisterActivity.this,
                                        "비밀번호 확인을 입력하세요!", Toast.LENGTH_SHORT).show();
                                etPasswordConfirm.requestFocus();
                                return;
                            }

                            if (!etPassword.getText().toString().equals(etPasswordConfirm.getText().toString()))
                            {
                                Toast.makeText(RegisterActivity.this,
                                        "비밀번호가 일치하지 않습니다!", Toast.LENGTH_SHORT).show();
                                etPassword.setText("");
                                etPasswordConfirm.setText("");
                                etPassword.requestFocus();
                                return;
                            }

                            // 서버로 전송
                            attemptRegister();
                        }
                    });
                }
                else
                {
                    Toast.makeText(RegisterActivity.this,
                            "약관 동의 버튼을 체크하세요!", Toast.LENGTH_SHORT).show();
                    cbAgree.requestFocus();
                    return;
                }
            }
        });

        /**
         * Shows the progress UI and hides the login form.
         */
        /*@TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
        protected void showProgress(final boolean show) {
            // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
            // for very easy animations. If available, use these APIs to fade-in
            // the progress spinner.
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
                int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

                mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                        show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                    }
                });

                mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                mProgressView.animate().setDuration(shortAnimTime).alpha(
                        show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                    }
                });
            } else {
                // The ViewPropertyAnimator APIs are not available, so simply show
                // and hide the relevant UI components.
                mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            }
        }*/
    }

    private void attemptRegister()
    {
        final String email = etEmail.getText().toString();
        String password = etPassword.getText().toString();
        final String name = etName.getText().toString();
        String phone = etPhone.getText().toString();
        String country = spCountry.getSelectedItem().toString();
        String language = spLanguage.getSelectedItem().toString();
        int isHelper = chkHelper.isChecked() ? 1 : 0;

        final boolean checkValidate = !(TextUtils.isEmpty(email) & TextUtils.isEmpty(password) &
                TextUtils.isEmpty(name) & TextUtils.isEmpty(phone) &
                TextUtils.isEmpty(country) & TextUtils.isEmpty(language));

        httpConnectionManager.register(
                email,
                password,
                name,
                phone,
                country,
                language,
                isHelper,
                new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        try {
                            int success = response.getInt("success");

                            if (success == 1) {
                                smgr.createSession(email, name);
                                Intent intent = new Intent(getApplicationContext(),
                                        MainActivity.class);
                                startActivity(intent);
                                finish();
                            } else {
                                String errReason = response.getString("err");

                                if (errReason.equals("invalid mail")) {
                                    Toast.makeText(getApplicationContext(),
                                            "중복된 이메일이 있습니다", Toast.LENGTH_SHORT).show();
                                    etEmail.requestFocus();
                                }

                                if (errReason.equals("Please check blank"))
                                {
                                    Toast.makeText(getApplicationContext(),
                                            "빈 칸이 있는지 확인해주세요", Toast.LENGTH_SHORT).show();
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(getApplicationContext(),
                                    "회원가입에 실패했습니다", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}
