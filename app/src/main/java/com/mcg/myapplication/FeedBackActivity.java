package com.mcg.myapplication;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import Http.WebService;
import base.CustomToast;
import base.MyProgressDialog;
import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * Created by 成刚 on 2016/4/1.
 */
public class FeedBackActivity extends AppCompatActivity implements View.OnClickListener{
    private MyProgressDialog dialog = new MyProgressDialog();

    private Button btn_send;
    private RelativeLayout rl_back;
    private EditText et_content;
    private Handler handler;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);

        initView();
    }

    public void initView(){
        et_content = (EditText)findViewById(R.id.et_feedback);

        rl_back = (RelativeLayout)findViewById(R.id.rl_feedback_back);
        rl_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btn_send = (Button) findViewById(R.id.btn_feedback);
        btn_send.setOnClickListener(this);

        handler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what){
                    case 0:
                        dialog.dismiss();
                        CustomToast.showToast(getApplicationContext(),"很遗憾，发送失败",1500);
                        break;
                    case 1:
                        dialog.dismiss();
                        new SweetAlertDialog(FeedBackActivity.this,SweetAlertDialog.NORMAL_TYPE)
                                .setTitleText("")
                                .setContentText("感谢您宝贵的意见，我们会在最短的时间内做出行动")
                                .setConfirmText("确定")
                                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                    @Override
                                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                                        sweetAlertDialog.dismiss();
                                        et_content.setText("");
                                    }
                                }).show();
                        break;
                    case 2:
                        dialog.dismiss();
                        CustomToast.showToast(getApplicationContext(),"连接服务器失败",1500);
                        break;
                    default:
                        break;
                }
            }
        };
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.btn_feedback){
            if(!et_content.getText().toString().equals("")){
                //显示提示框
                dialog.show(FeedBackActivity.this,"正在提交，请稍后...");
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        String suggestion = et_content.getText().toString();
                        Message message = new Message();
                        SharedPreferences sharedPreferences = getSharedPreferences("user_password", MODE_PRIVATE);
                        String username = sharedPreferences.getString("phone","");
                        String info = "0";
                        try {
                            suggestion = URLEncoder.encode(suggestion,"UTF-8");
                            suggestion = URLEncoder.encode(suggestion,"UTF-8");
                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        }
                        info = WebService.executeHttpFeedBack(username, suggestion);
                        if(info != null){
                            if(info.equals("1")){
                                message.what = 1;
                            }else message.what = 0;
                        }else message.what = 2;
                        handler.sendMessage(message);
                    }
                }).start();
            }else CustomToast.showToast(this,"意见不能空哦",1500);

        }
    }
}
