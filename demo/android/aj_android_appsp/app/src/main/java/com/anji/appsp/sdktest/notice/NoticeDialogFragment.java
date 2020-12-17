package com.anji.appsp.sdktest.notice;

import android.content.DialogInterface;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.anji.appsp.sdk.model.AppSpNoticeModelItem;
import com.anji.appsp.sdktest.AppContext;
import com.anji.appsp.sdktest.R;

/**
 * Copyright © 2018 anji-plus
 * 安吉加加信息技术有限公司
 * http://www.anji-plus.com
 * All rights reserved.
 * <p>
 * 公告管理-公告测试页面
 */
public class NoticeDialogFragment extends DialogFragment implements View.OnClickListener {
    private TextView tvTitle;
    private TextView tvContent;
    private TextView tvOk;
    private View delete;
    private AppSpNoticeModelItem updateModel;

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_ok:
            case R.id.delete:
                dismiss();
                break;
        }

    }

    @Override
    public void onDismiss(@NonNull DialogInterface dialog) {
        super.onDismiss(dialog);
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_dialog_notice, null);
        if (getDialog() == null) {
            return view;
        }
        initView(view);
        getDialog().setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    if (event.getAction() == KeyEvent.ACTION_UP) {
                        dismiss();
                    }
                }
                return false;
            }
        });

        return view;
    }

    private void initView(View view) {
        tvTitle = view.findViewById(R.id.tv_title);
        tvContent = view.findViewById(R.id.tv_content);
        tvOk = view.findViewById(R.id.tv_ok);
        delete = view.findViewById(R.id.delete);

        tvOk.setOnClickListener(this);
        delete.setOnClickListener(this);

        GradientDrawable backgroundDrawable = new GradientDrawable();
        backgroundDrawable.setColor(0xFFFFFFFF);
        float scale = getResources().getDisplayMetrics().density;
        backgroundDrawable.setCornerRadius((int) (4 * scale + 0.5f));
        Window window = getDialog().getWindow();
        int width = getResources().getDisplayMetrics().widthPixels;
        window.getDecorView().setPadding(0, 0, 0, 0);
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.width = width - getResources().getDimensionPixelSize(R.dimen.dialog_margin) * 2;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setAttributes(lp);

        updateModel = (AppSpNoticeModelItem) getArguments().getSerializable("notice");
        if (updateModel == null) {
            Toast.makeText(AppContext.getInstance(), "没有公告信息", Toast.LENGTH_LONG).show();
            return;
        }
        afterView();
    }

    private void afterView() {
        tvTitle.setText(updateModel.getTitle());
        tvContent.setText(updateModel.getDetails());
    }
}
