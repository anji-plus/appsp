package com.anji.appsp.sdktest;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.anji.appsp.sdktest.notice.NoticeTestActivity;
import com.anji.appsp.sdktest.update.UpdateTestActivity;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TextView cur_version = (TextView) findViewById(R.id.cur_version);
        cur_version.setText("当前版本为：" + BuildConfig.VERSION_NAME + "  版本号: " + BuildConfig.VERSION_CODE);
        findViewById(R.id.version).setOnClickListener(this);
        findViewById(R.id.notice).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.version:
                Intent intent = new Intent(this, UpdateTestActivity.class);
                startActivity(intent);
                break;
            case R.id.notice:
                intent = new Intent(this, NoticeTestActivity.class);
                startActivity(intent);
                break;
            default:
                break;

        }
    }

}
