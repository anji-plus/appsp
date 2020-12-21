package com.anji.appsp.sdktest.update;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;
import androidx.fragment.app.DialogFragment;

import com.anji.appsp.sdk.model.AppSpVersion;
import com.anji.appsp.sdktest.AppContext;
import com.anji.appsp.sdktest.BuildConfig;
import com.anji.appsp.sdktest.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DecimalFormat;

import static com.anji.appsp.sdktest.DeviceUtil.getScreenWidth;


/**
 * Copyright © 2018 anji-plus
 * 安吉加加信息技术有限公司
 * http://www.anji-plus.com
 * All rights reserved.
 *
 * 版本更新-更新测试页面
 */
public class UpdateDialogFragment extends DialogFragment implements View.OnClickListener {
    private TextView tvContent;
    private TextView tvOk;
    private View close;
    private ProgressBar progress;

    private AppSpVersion updateModel;
    private File file;
    private String fileName;
    private boolean isInteriorSdCardPrivate;
    private boolean isShowPer;
    private ApkDownloadTask apkDownloadTask;
    private int updateCount = 0;//当前进度

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(STYLE_NO_FRAME, R.style.dialog);
    }

    @Override
    public void onDismiss(@NonNull DialogInterface dialog) {
        super.onDismiss(dialog);
        if (apkDownloadTask != null) {
            apkDownloadTask.cancel(true);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_dailog_update, null);
        if (getDialog() == null) {
            return view;
        }
        initView(view);
        getDialog().setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    if (event.getAction() == KeyEvent.ACTION_UP) {
                        if (updateModel != null) {
                            if (updateModel.isMustUpdate()) {
                                close.performClick();
                                return true;
                            }
                        }
                        dismiss();
                    }
                }
                return false;
            }
        });

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        if (!isShowPer && updateModel != null) {
            isShowPer = true;
            getDialog().getWindow().setLayout((int) (getScreenWidth() * 0.9), getDialog().getWindow().getAttributes().height);
            fileName = BuildConfig.APPLICATION_ID + "_" + System.currentTimeMillis() + ".apk";
            if (Environment.getExternalStorageState().equals(
                    Environment.MEDIA_MOUNTED)) {
                file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), fileName);
            } else {
                isInteriorSdCardPrivate = true;
                file = new File(getActivity().getFilesDir(), fileName);
            }
            if (file != null && file.exists()) {
                file.delete();
            }
        }
        tvOk.setTag(String.valueOf(updateModel.getDownloadUrl()));
    }

    private void initView(View view) {
//        tvProgress = view.findViewById(R.id.tvProgress);
        tvContent = view.findViewById(R.id.tv_content);
        tvOk = view.findViewById(R.id.tv_ok);
        close = view.findViewById(R.id.close);
        progress = view.findViewById(R.id.progress);

        tvOk.setOnClickListener(this);
        close.setOnClickListener(this);

        updateModel = (AppSpVersion) getArguments().getSerializable("update");
        if (updateModel == null) {
            Toast.makeText(AppContext.getInstance(), "没有更新信息", Toast.LENGTH_LONG).show();
            return;
        }
        tvOk.setTag(updateModel.getDownloadUrl());
        String releaseLog = updateModel.getUpdateLog();
        setCancelable(false);
        tvContent.setText(releaseLog);
        if (updateModel.isMustUpdate()) {
            close.setVisibility(View.GONE);
        } else {
            close.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.close:
                if (updateModel != null) {
                    if (updateModel.isMustUpdate()) {
                        Toast.makeText(getContext(), "当前版本必须强制更新，应用即将退出", Toast.LENGTH_LONG).show();
                        view.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                System.exit(0);
                            }
                        }, 3000);
                    } else {
                        dismiss();
                    }
                }
                break;
            case R.id.tv_ok:
                boolean externalUrl = false;
                if (updateModel != null && updateModel.getDownloadUrl() != null) {
                    externalUrl = !updateModel.getDownloadUrl().contains(".apk");
                }
                //强制更新，必须下载，否则退出APP
                if (updateModel != null) {
                    if (updateModel.isMustUpdate()) {
                        if (externalUrl) {
                            //跳转到外部H5下载
                            launchH5(updateModel.getDownloadUrl());
                        } else {
                            //内部下载地址
                            //安裝
                            progress.setVisibility(View.VISIBLE);
                            loadApk(view.getTag().toString());
                        }
                    } else {
                        if (externalUrl) {
                            //跳转到外部H5下载
                            launchH5(updateModel.getDownloadUrl());
                        } else {
                            //内部下载地址
                            //安裝
                            progress.setVisibility(View.VISIBLE);
                            loadApk(view.getTag().toString());
                        }
                    }
                }
                break;
        }
    }

    /**
     * @param url
     */
    private void launchH5(String url) {
        Uri uri = Uri.parse(url);
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    private void installApk(File file) {
        Intent intent = new Intent(Intent.ACTION_VIEW);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            Uri contentUri = FileProvider.getUriForFile(getContext(), BuildConfig.APPLICATION_ID + ".fileprovider", file);
            intent.setDataAndType(contentUri, "application/vnd.android.package-archive");
        } else {
            intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        if (getActivity() != null) {
            getActivity().startActivity(intent);
        }
    }


    /**
     * 检测是否同包名
     *
     * @param remoteApkPath apk包的绝对路径
     */
    private boolean isSamePackage(String remoteApkPath) {
        boolean samePackage = true;
        if (getActivity() == null) {
            return samePackage;
        }
        PackageManager pm = getActivity().getPackageManager();
        PackageInfo pkgInfo = pm.getPackageArchiveInfo(remoteApkPath, PackageManager.GET_ACTIVITIES);
        if (pkgInfo != null) {
            ApplicationInfo appInfo = pkgInfo.applicationInfo;
            String packageName = appInfo.packageName; // 得到包名
            String curPackageName = null;
            try {
                PackageInfo curPkgInfo = pm.getPackageInfo(BuildConfig.APPLICATION_ID, 0);
                if (curPkgInfo != null) {
                    curPackageName = curPkgInfo.packageName;
                }
            } catch (PackageManager.NameNotFoundException exception) {

            }
            if (packageName != null && curPackageName != null) {
                //非同包名apk
                if (!packageName.equals(curPackageName)) {
                    samePackage = false;
                }
            }
        }
        return samePackage;
    }

    private void showWarningAlert(final File file) {
        final AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
        alert.setTitle("包名不一致");
        //增加描述信息
        alert.setMessage("检测到当前包名和服务器apk包名不一致，是否继续？");
        alert.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                installApk(file);
            }
        });

        alert.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dismiss();
            }
        });
        alert.create().show();
    }

    private class ApkDownloadTask extends AsyncTask<String, Float, Boolean> {
        @Override
        protected Boolean doInBackground(String... params) {
            try {
                URL url = new URL(params[0]);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                InputStream in = connection.getInputStream();
                int count = 0;
                int len;
                byte[] b = new byte[1024];
                FileOutputStream out;
                if (isInteriorSdCardPrivate) {
                    out = AppContext.getInstance().openFileOutput(fileName, Context.MODE_WORLD_READABLE | Context.MODE_WORLD_WRITEABLE);
                } else {
                    out = new FileOutputStream(file);
                }
                while ((len = in.read(b)) != -1) {
                    out.write(b, 0, len);
                    count += len;
                    publishProgress(1.0f * count / connection.getContentLength());
                }
                out.close();
                in.close();
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
            if (file.exists() && file.isFile()) {
                return true;
            }
            return false;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
//                dismiss();
            if (aBoolean) {
                if (!isSamePackage(file.getAbsolutePath())) {
                    showWarningAlert(file);
                } else {
                    installApk(file);
                }
            } else {
                Toast.makeText(AppContext.getInstance(), "下载失败", Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        protected void onProgressUpdate(Float... values) {
            progress.setProgress(values[0].intValue());
//            tvProgress.setText(decimalFormat.format(values[0] * 100) + "%");
            updateCount = (int) (values[0] * 100);
            progress.setProgress(updateCount);

        }
    }

    private void loadApk(String url) {
        if (file == null) {
            return;
        }
        if (file.exists() && file.isFile()) {

            if (!isSamePackage(file.getAbsolutePath())) {
                showWarningAlert(file);
            } else {
                installApk(file);
            }
            return;
        }
        if (apkDownloadTask == null) {
            apkDownloadTask = new ApkDownloadTask();
        }
        apkDownloadTask.execute(url);
    }

}
