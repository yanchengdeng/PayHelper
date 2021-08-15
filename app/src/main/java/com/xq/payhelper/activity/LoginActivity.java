package com.xq.payhelper.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.xq.payhelper.HelperApplication;
import com.xq.payhelper.R;
import com.xq.payhelper.common.PreferenceUtil;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {


    private EditText userNameEditText;
    private EditText userPwdEditText;
    private Button loginButton;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("登录");
        setContentView(R.layout.activity_login);
        userNameEditText = findViewById(R.id.et_user_name);
        userPwdEditText = findViewById(R.id.et_user_pwd);
        loginButton = findViewById(R.id.btn_login);
        loginButton.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_login) {
            //todo login in server

            String user_name = userNameEditText.getText().toString().trim();
            String user_pass = userPwdEditText.getText().toString().trim();
            if (TextUtils.isEmpty(user_name)) {
                Toast.makeText(HelperApplication.getContext(), "请输入账号", Toast.LENGTH_LONG).show();
                return;
            }
            if (TextUtils.isEmpty(user_pass)) {
                Toast.makeText(HelperApplication.getContext(), "请输入密码", Toast.LENGTH_LONG).show();
                return;
            }

            PreferenceUtil.getInstance().userId(100);
            startActivity(new Intent(LoginActivity.this, MainActivity.class));
            finish();


        }
    }
}
