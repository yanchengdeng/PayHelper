package com.xq.payhelper.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.Process;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.ActivityOptionsCompat;
import androidx.lifecycle.Observer;

import com.blankj.utilcode.util.AppUtils;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.google.gson.Gson;
import com.kaopiz.kprogresshud.KProgressHUD;
import com.king.zxing.CameraScan;
import com.king.zxing.CaptureActivity;
import com.xq.payhelper.R;
import com.xq.payhelper.common.Constants;
import com.xq.payhelper.common.VariableData;
import com.xq.payhelper.entity.Result;
import com.xq.payhelper.entity.ServiceNoticeInfo;
import com.xq.payhelper.service.HelperNotificationListenerService;
import com.xq.payhelper.utils.AppUtil;

import java.util.List;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;
import rxhttp.wrapper.param.RxHttp;


public class MainActivity extends AppCompatActivity implements EasyPermissions.PermissionCallbacks {
    private TextView tvServiceTips;
    private ImageView ivScan;
    private EditText etInput;
    private Button btnActionListener;
    private TextView tvActionApiResult;
    private TextView tvVersion;


    public static final int RC_CAMERA = 0X01;
    public static final String KEY_TITLE = "key_title";
    public static final String KEY_IS_CONTINUOUS = "key_continuous_scan";

    public static final int REQUEST_CODE_SCAN = 0X01;

    private Observer<ServiceNoticeInfo> serviceMsgObserver = new Observer<ServiceNoticeInfo>() {
        @Override
        public void onChanged(ServiceNoticeInfo o) {
            tvServiceTips.setVisibility(View.VISIBLE);
            tvServiceTips.setText(o.getMsg());
        }
    };


    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler(){
        @SuppressLint("HandlerLeak")
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            if (msg.what == 1){
                    btnActionListener.setText("正在监听");
                    btnActionListener.setEnabled(false);
                    btnActionListener.setClickable(false);
                    btnActionListener.setBackground(getDrawable(R.color.c_cccccc));
                    ivScan.setVisibility(View.GONE);
            }
        }
    };




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tvServiceTips = findViewById(R.id.tvServiceTip);
        ivScan = findViewById(R.id.ivScan);
        etInput = findViewById(R.id.etInput);
        btnActionListener = findViewById(R.id.btnAction);
        tvActionApiResult = findViewById(R.id.tvListenerMsg);
        tvVersion = findViewById(R.id.tvVersion);

        tvVersion.setText(AppUtils.getAppVersionName());

        //扫描
        ivScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkCameraPermissions();
            }
        });

        //开启监听

        btnActionListener.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startAction();
            }
        });


        //推送服务状态监听
        VariableData.serviceMsg.observe(this, serviceMsgObserver);

        if (!TextUtils.isEmpty(SPUtils.getInstance().getString(Constants.BASE_URL_KEY))) {
            etInput.setText(SPUtils.getInstance().getString(Constants.BASE_URL_KEY));
        }
    }


    private void startAction() {
        if (TextUtils.isEmpty(SPUtils.getInstance().getString(Constants.BASE_URL_KEY))) {
            ToastUtils.showLong(R.string.input_url);
            return;
        }

        KProgressHUD progressHUD = KProgressHUD.create(MainActivity.this)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setLabel(getString(R.string.loading))
                .setCancellable(true)
                .setAnimationSpeed(2)
                .setDimAmount(0.5f)
                .show();
        try {
            MediaType JSON = MediaType.parse("application/json; charset=utf-8");
            RequestBody body = RequestBody.create(JSON, "{\"a\":\"open\"}");

            RxHttp.postBody(SPUtils.getInstance().getString(Constants.BASE_URL_KEY))
                    .setBody(body)
                    .asClass(Result.class)
                    .subscribe(data -> {
                        progressHUD.dismiss();
                        setListenerUI(data);
                    }, throwable -> {
                        progressHUD.dismiss();
                        tvActionApiResult.setText(throwable.getLocalizedMessage());
                        ToastUtils.showShort("监听失败");
                    });
        } catch (Exception e) {
            e.printStackTrace();
            tvActionApiResult.setText(e.getLocalizedMessage());
            progressHUD.dismiss();
        }
    }

    private void setListenerUI(Result data) {
        tvActionApiResult.setText(new Gson().toJson(data));
        if (data.getStatus() == 0) {
            Message msg = handler.obtainMessage();
            msg.what = 1;
            handler.sendMessage(msg);
        }
    }

    @Override
    public void onPermissionsGranted(int requestCode, @NonNull List<String> perms) {

    }

    @Override
    public void onPermissionsDenied(int requestCode, @NonNull List<String> perms) {

    }

    /**
     * 检测拍摄权限
     */
    @AfterPermissionGranted(RC_CAMERA)
    private void checkCameraPermissions() {
        String[] perms = {Manifest.permission.CAMERA};
        if (EasyPermissions.hasPermissions(this, perms)) {//有权限
            startScan();
        } else {
            // Do not have permissions, request them now
            EasyPermissions.requestPermissions(this, getString(R.string.permission_camera),
                    RC_CAMERA, perms);
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        // Forward results to EasyPermissions
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    /**
     * 扫码
     */
    private void startScan() {
        ActivityOptionsCompat optionsCompat = ActivityOptionsCompat.makeCustomAnimation(this, R.anim.in, R.anim.out);
        Intent intent = new Intent(this, CaptureActivity.class);
        intent.putExtra(KEY_TITLE, "扫码");
        intent.putExtra(KEY_IS_CONTINUOUS, false);
        ActivityCompat.startActivityForResult(this, intent, REQUEST_CODE_SCAN, optionsCompat.toBundle());
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && data != null) {
            switch (requestCode) {
                case REQUEST_CODE_SCAN:
                    String result = CameraScan.parseScanResult(data);
                    if (!TextUtils.isEmpty(result)) {
                        etInput.setText(result);
                        SPUtils.getInstance().put(Constants.BASE_URL_KEY, result);
                    } else {
                        Toast.makeText(this, "未识别出信息", Toast.LENGTH_LONG).show();
                    }
                    break;
            }

        }
    }


    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        String url = SPUtils.getInstance().getString(Constants.BASE_URL_KEY);
        LogUtils.d("onNewIntent---" + url);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        VariableData.serviceMsg.removeObserver(serviceMsgObserver);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (AppUtil.isAvailable(getApplicationContext(), Constants.LISTENING_TARGET_PKG_ALi) || AppUtil.isAvailable(getApplicationContext(), Constants.LISTENING_TARGET_PKG_TENCENT)) {
            ensureCollectorRunning();
        } else {
            tvServiceTips.setText("未安装支付宝或微信应用,服务无法开启!");
        }
    }

    private void ensureCollectorRunning() {
        tvServiceTips.setVisibility(View.VISIBLE);
        tvServiceTips.setText("检查服务启动情况...");
        ComponentName collectorComponent = new ComponentName(this, HelperNotificationListenerService.class);
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        boolean collectorRunning = false;
        List<ActivityManager.RunningServiceInfo> runningServices = manager.getRunningServices(Integer.MAX_VALUE);
        if (runningServices == null) {
            return;
        }
        for (ActivityManager.RunningServiceInfo service : runningServices) {
            if (service.service.equals(collectorComponent)) {
                if (service.pid == Process.myPid()) {
                    collectorRunning = true;
                }
            }
        }
        if (collectorRunning) {
            tvServiceTips.setText("服务正在运行...");
            return;
        }
        toggleNotificationListenerService();
    }

    private void toggleNotificationListenerService() {
        tvServiceTips.setText("准备启动监听服务...");
        PackageManager pm = getPackageManager();
        pm.setComponentEnabledSetting(
                new ComponentName(this, HelperNotificationListenerService.class),
                PackageManager.COMPONENT_ENABLED_STATE_DISABLED, PackageManager.DONT_KILL_APP);

        pm.setComponentEnabledSetting(
                new ComponentName(this, HelperNotificationListenerService.class),
                PackageManager.COMPONENT_ENABLED_STATE_ENABLED, PackageManager.DONT_KILL_APP);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_history:
                startActivity(new Intent(this, HistoryActivity.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}
