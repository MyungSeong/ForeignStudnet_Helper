package kr.ac.dongseo.foreignstudnet_helper;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class RegisterActivity extends AppCompatActivity
{
    String userEmail;
    String userPassword;
    String userCountry;
    String userLanguage;
    Integer userIsHelper;

    final EditText etEmail = (EditText) findViewById(R.id.user_Email);
    final EditText etPassword = (EditText) findViewById(R.id.user_passWord);
    final EditText etPasswordConfirm = (EditText) findViewById(R.id.user_passWordAgain);
    final CheckBox chkHelper = (CheckBox) findViewById(R.id.chkHelper);

    Spinner spCountry = (Spinner) findViewById(R.id.spinner_Country);
    Spinner spLanguage = (Spinner) findViewById(R.id.spinner_Language);

    final CheckBox cbAgree = (CheckBox) findViewById(R.id.personalInfo);
    Button btnOK = (Button) findViewById(R.id.btn_agreeJoin);

    final String[] country = { "South Korea", "USA", "China" };
    final String[] language = { "Korean", "English", "Chinese" };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

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
                            userEmail = etEmail.getText().toString();
                            userPassword = etPassword.getText().toString();
                            userCountry = spCountry.getSelectedItem().toString();
                            userLanguage = spLanguage.getSelectedItem().toString();

                            if (chkHelper.isChecked())
                            {
                                userIsHelper = 1;
                            }
                            else
                            {
                                userIsHelper = 0;
                            }

                            /*if (!condition)
                            {
                                Toast.makeText(RegisterActivity.this,
                                    "서버로 전송이 실패했습니다!", Toast.LENGTH_SHORT).show();
                                return;
                            }*/

                            /*Intent result = new Intent();
                            result.putExtra("email", etEmail.getText().toString());
                            setResult(RESULT_OK, result);
                            finish();*/

                            Intent i = new Intent(getBaseContext(), MainActivity.class);
                            startActivity(i);
                            finish();
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
    }
}
