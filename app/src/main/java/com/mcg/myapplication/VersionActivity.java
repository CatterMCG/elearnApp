package com.mcg.myapplication;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;

import Http.WebService;
import base.CustomToast;

/**
 * Created by 成刚 on 2016/4/1.
 */
public class VersionActivity extends AppCompatActivity {
    private PackageManager manager;
    private  PackageInfo info = null;

    private Handler handler;
    private String response;

    private Button btn_version_check;
    private RelativeLayout rl_back;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_version);
        initView();
        //获取这个app的版本信息
        manager = getApplicationContext().getPackageManager();
        try {
            info = manager.getPackageInfo(getApplicationContext().getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void initView(){
        rl_back = (RelativeLayout)findViewById(R.id.rl_version_back);
        rl_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btn_version_check = (Button) findViewById(R.id.btn_version_check);
        btn_version_check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {

                        response = WebService.executeHttpGetVersion();
                        Message message = new Message();
                        if(response != null){
                            if(info.versionName.equals(response)){
                                message.what =0;//该版本已经是最新版本
                            }else message.what =1;//检测到最新版本，需要更新
                        }else message.what = 2;//联网失败

                        handler.sendMessage(message);
                    }
                }).start();
            }
        });
        handler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what){
                    case 0:
                        CustomToast.showToast(VersionActivity.this,"当前版本号为：" + info.versionName + "，是最新版本哦",1500);
                        break;
                    case 1:
                        CustomToast.showToast(VersionActivity.this,"当前版本号为：" + info.versionName + "，最新版本为：V"+response+"  请及时更新哦",1500);
                        break;
                    case 2:
                        CustomToast.showToast(VersionActivity.this,"当前版本号为："+info.versionName+"，无法连接服务器",1500);
                    default:
                        break;
                }
            }
        };
    }
}
