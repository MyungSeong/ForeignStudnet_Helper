package kr.ac.dongseo.foreignstudnet_helper;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

import cz.msebera.android.httpclient.Header;

public class LoginActivity extends Activity {
    private SessionManager smgr;
    private HttpConnectionManager httpConnectionManager;
    private SharedPreferences sharedPreferences;

    EditText etEmail;
    EditText etPassword;
    CheckBox cbLoginRemember;

    boolean saveLoginData;
    String email;
    String password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        httpConnectionManager = new HttpConnectionManager(getApplicationContext());
        smgr = new SessionManager(getApplicationContext());
        sharedPreferences = getSharedPreferences("appData", MODE_PRIVATE);

        etEmail = (EditText) findViewById(R.id.login_email);
        etPassword = (EditText) findViewById(R.id.login_pw);
        cbLoginRemember = (CheckBox) findViewById(R.id.login_remeber);

        loadLoginPerf();

        if (saveLoginData) {
            etEmail.setText(email);
            etPassword.setText(password);
            cbLoginRemember.setChecked(saveLoginData);
        }

        Button loginButton = (Button) findViewById(R.id.login_btn);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    attemptLogin();
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

        Button signUpButton = (Button) findViewById(R.id.sign_up);
        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getBaseContext(), RegisterActivity.class);
                startActivity(i);
                finish();
            }
        });
    }

    private void attemptLogin() throws UnsupportedEncodingException, JSONException {
        String email = etEmail.getText().toString();
        String password = etPassword.getText().toString();

        if (!TextUtils.isEmpty(email) && !TextUtils.isEmpty(password)) {
            //showProgess(true);

            httpConnectionManager.login(email, password, new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    try {
                        int success = response.getInt("success");

                        if (success == 1) {
                            String email = etEmail.getText().toString(); //String email = response.getString("mail");
                            String name = response.getString("name");

                            smgr.createSession(email, name);
                            saveLoginPref();
                            Intent intent = new Intent(getBaseContext(), MainActivity.class);
                            startActivity(intent);
                            finish();
                        } else {
                            String errReason = response.getString("err");

                            if (errReason.equals("Check mail & password")) {
                                Toast.makeText(getApplicationContext(),
                                        "이메일이나 비밀번호를 다시 확인해 주세요",
                                        Toast.LENGTH_SHORT).show();
                                etEmail.requestFocus();
                            }

                            if (errReason.equals("not found mail")) {
                                Toast.makeText(getApplicationContext(),
                                        "존재하지 않는 이메일 입니다",
                                        Toast.LENGTH_SHORT).show();
                                etEmail.requestFocus();
                            }

                            if (errReason.equals("Please check blank")) {
                                Toast.makeText(getApplicationContext(),
                                        "빈 칸이 있는지 확인해주세요",
                                        Toast.LENGTH_SHORT).show();
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(getApplicationContext(), "로그인을 실패하였습니다",
                                Toast.LENGTH_SHORT).show();
                        //showProgress(false);
                    }
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                    super.onFailure(statusCode, headers, throwable, errorResponse);
                }
            });
        }
    }

    private void saveLoginPref() {
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putBoolean("SAVE_LOGIN_DATA", cbLoginRemember.isChecked());
        editor.putString("EMAIL", etEmail.getText().toString().trim());
        editor.putString("PASSWORD", etPassword.getText().toString().trim());

        editor.apply();
    }

    private void loadLoginPerf() {
        saveLoginData = sharedPreferences.getBoolean("SAVE_LOGIN_DATA", false);
        email = sharedPreferences.getString("EMAIL", "");
        password = sharedPreferences.getString("PASSWORD", "");
    }
}
