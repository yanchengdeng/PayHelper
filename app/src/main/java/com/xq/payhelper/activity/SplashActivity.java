package com.xq.payhelper.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationManagerCompat;

import com.xq.payhelper.HelperApplication;
import com.xq.payhelper.R;

public class SplashActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
    }

    @Override
    protected void onResume() {
        super.onResume();
        boolean isAuthor = isNotificationServiceEnable();
        if (!isAuthor) {
            Toast.makeText(HelperApplication.getContext(), "请授予通知助手使用权", Toast.LENGTH_SHORT).show();
            startActivity(new Intent("android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS"));
        } else {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    startActivity(new Intent(SplashActivity.this, MainActivity.class));
                    finish();
                }
            },1000);
        }
    }

    private boolean isNotificationServiceEnable() {
        return NotificationManagerCompat.getEnabledListenerPackages(this).contains(getPackageName());
    }
}
