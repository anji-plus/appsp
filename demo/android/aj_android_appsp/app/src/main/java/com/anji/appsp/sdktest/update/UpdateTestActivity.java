package com.anji.appsp.sdktest.update;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.anji.appsp.sdk.AppSpConfig;
import com.anji.appsp.sdk.version.service.IAppSpVersionCallback;
import com.anji.appsp.sdk.http.AppSpRespCode;
import com.anji.appsp.sdk.model.AppSpModel;
import com.anji.appsp.sdk.model.AppSpVersion;
import com.anji.appsp.sdktest.R;

/**
 * Copyright © 2018 anji-plus
 * 安吉加加信息技术有限公司
 * http://www.anji-plus.com
 * All rights reserved.
 *
 * 版本更新-更新测试页面
 */
public class UpdateTestActivity extends AppCompatActivity implements View.OnClickListener {
    //appkey在移动服务平台创建项目时生成
    private final int REQUEST_CODE_ASK_PERMISSIONS = 11;
    private boolean permissionAllowed = true;

    enum UpdateType {
        Normal,//正常更新，不改造数据
        Force,//改造数据，模拟强制更新效果
        NotForce,//改造数据，模拟非强制更新效果
        ForceH5,//改造数据，模拟强制更新时跳转到外部网页下载的效果
        NotForceH5,//改造数据，模拟非强制更新时跳转到外部网页下载的效果
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish(); // back button
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_test);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        findViewById(R.id.version_btn).setOnClickListener(this);
        findViewById(R.id.version_force_btn).setOnClickListener(this);
        findViewById(R.id.version_not_force_btn).setOnClickListener(this);
        findViewById(R.id.version_force_h5_btn).setOnClickListener(this);
        findViewById(R.id.version_not_force_h5_btn).setOnClickListener(this);
        askPermission();
    }

    private void askPermission() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                permissionAllowed = false;
                requestPermissions(new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_CODE_ASK_PERMISSIONS);
            } else {
                permissionAllowed = true;
            }
        }
    }

    @Override
    public void onClick(View v) {
        if (!permissionAllowed) {
            askPermission();
            return;
        }
        UpdateType updateType = UpdateType.Normal;
        switch (v.getId()) {
            case R.id.version_btn:
                updateType = UpdateType.Normal;
                break;
            case R.id.version_force_btn:
                updateType = UpdateType.Force;
                break;
            case R.id.version_not_force_btn:
                updateType = UpdateType.NotForce;
                break;
            case R.id.version_force_h5_btn:
                updateType = UpdateType.ForceH5;
                break;
            case R.id.version_not_force_h5_btn:
                updateType = UpdateType.NotForceH5;
                break;
            default:
                break;

        }
        checkVersion(updateType);
    }

    private void versionUISuccess(final UpdateType updateType, final AppSpModel<AppSpVersion> spModel) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                AppSpVersion updateModel = spModel.getRepData();
                //注意判空
                if (updateModel == null) {
                    Toast.makeText(UpdateTestActivity.this, "当前为最新版本", Toast.LENGTH_LONG).show();
                    return;
                }
                String errorMsg = null;
                if (!AppSpRespCode.SUCCESS.equals(spModel.getRepCode())) {
                    errorMsg = spModel.getRepMsg();
                }

                if (errorMsg != null) {
                    Toast.makeText(UpdateTestActivity.this, errorMsg, Toast.LENGTH_LONG).show();
                } else {
                    //版本更新
                    handleUpdate(updateModel, updateType);
                }
            }
        });

    }

    private void versionUIError(String code, final String msg) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (msg != null) {
                    Toast.makeText(UpdateTestActivity.this, msg, Toast.LENGTH_LONG).show();
                }
            }
        });

    }

    private void checkVersion(final UpdateType updateType) {
        AppSpConfig.getInstance().getVersion(new IAppSpVersionCallback() {
            @Override
            public void update(AppSpModel<AppSpVersion> spModel) {
                //因为是异步，注意当前窗口是否活跃
                if (!isActive()) {
                    return;
                }
                //当前在子线程，注意
                versionUISuccess(updateType, spModel);
            }

            @Override
            public void error(String code, String msg) {
                versionUIError(code, msg);
            }
        });
    }

    private void handleUpdate(AppSpVersion updateModel, UpdateType updateType) {
        if (updateModel == null) {
            return;
        }
        if (updateType != null) {
            switch (updateType) {
                case Normal:
                    //无需改造数据，用服务器返回数据，下面的都是模拟的数据
                    break;
                case Force:
                    //强制更新
                    updateModel.setMustUpdate(true);
                    break;
                case NotForce:
                    updateModel.setMustUpdate(false);
                    break;
                case ForceH5:
                    //跳转到H5，强制
                    updateModel.setDownloadUrl("https://shouji.baidu.com/software/27007946.html");
                    updateModel.setMustUpdate(true);
                    break;
                case NotForceH5:
                    //跳转到H5，非强制
                    updateModel.setDownloadUrl("https://shouji.baidu.com/software/27007946.html");
                    updateModel.setMustUpdate(false);
                    break;
                default:
                    break;
            }
        }
        if (!updateModel.showUpdate) {
            Toast.makeText(this, "当前为最新版本", Toast.LENGTH_LONG).show();
            return;
        }
        showUpdateDialog(updateModel);
    }

    private void showUpdateDialog(AppSpVersion updateModel) {
        Bundle bundler = new Bundle();
        bundler.putSerializable("update", updateModel);
        UpdateDialogFragment updateDialogFragment = new UpdateDialogFragment();
        updateDialogFragment.setArguments(bundler);
        updateDialogFragment.show(getSupportFragmentManager(), "update");
    }

    protected boolean isActive() {
        boolean active = true;
        Activity curContext = this;
        if (curContext == null) {
            return false;
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            active = !(curContext.isDestroyed() || curContext.isFinishing());
        } else {
            active = !curContext.isFinishing();
        }
        return active;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_CODE_ASK_PERMISSIONS:
                if (grantResults != null && grantResults.length > 0) {
                    if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                        permissionAllowed = true;
                    }
                    break;
                }
        }
    }
}
