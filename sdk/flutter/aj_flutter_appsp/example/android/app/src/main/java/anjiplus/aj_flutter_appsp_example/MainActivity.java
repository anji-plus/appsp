package anjiplus.aj_flutter_appsp_example;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;

import java.util.Map;

import io.flutter.app.FlutterActivity;
import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel;
import io.flutter.plugins.GeneratedPluginRegistrant;

public class MainActivity extends FlutterActivity {
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    GeneratedPluginRegistrant.registerWith(this);
    new MethodChannel(getFlutterView(), "aj_flutter_appspdemo_channel").setMethodCallHandler(
            new MethodChannel.MethodCallHandler() {
              @Override
              public void onMethodCall(MethodCall call, MethodChannel.Result result) {
                if (call.method.equals("apkinstallMethod")) {
                  Object parameter = call.arguments();
                  if (parameter instanceof Map) {
                    String value = (String) ((Map) parameter).get("path");
                    boolean installAllowed = true;
                    if (Build.VERSION.SDK_INT >= 26) {
                      //来判断应用是否有权限安装apk
                      installAllowed = getPackageManager().canRequestPackageInstalls();
                      //有权限
                      if (!installAllowed) {
                        //无权限 申请权限
                        Intent intent = new Intent("android.settings.MANAGE_UNKNOWN_APP_SOURCES", Uri.parse("package:" + getPackageName()));
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        VersionUpdateInstaller.installApk(getApplication(), value);
                        return;
                      }
                    }
                    if (installAllowed) {
                      VersionUpdateInstaller.installApk(getApplication(), value);
                    }
                  }
                  //版本更新
                  result.success("Android " + Build.VERSION.RELEASE);
                } else {
                  result.notImplemented();
                }
              }
            }
    );

  }
}
