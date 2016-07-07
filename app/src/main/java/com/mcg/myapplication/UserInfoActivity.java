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
import fragment.SettingFragment;

/**
 * Created by 成刚 on 2016/4/3.
 */
public class UserInfoActivity extends AppCompatActivity implements View.OnClickListener {

    private ProgressDialog dialog;

    private RelativeLayout rl_back;
    private EditText et_nickname;
    private EditText et_selfintroduce;
    private EditText et_school;
    private EditText et_major;
    private EditText et_email;
    private Button btn_send;

    private Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);
        initView();
    }

    public void initView(){

        rl_back = (RelativeLayout)findViewById(R.id.rl_user_info_back);
        rl_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        et_nickname = (EditText)findViewById(R.id.et_user_info_nickname);
        et_selfintroduce = (EditText)findViewById(R.id.et_user_info_selfintroduce);
        et_school = (EditText)findViewById(R.id.et_user_info_school);
        et_major = (EditText)findViewById(R.id.et_user_info_major);
        et_email = (EditText)findViewById(R.id.et_user_info_email);

        btn_send = (Button)findViewById(R.id.btn_user_info_send);
        btn_send.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.rl_user_info_back:
                finish();
                break;
            case R.id.btn_user_info_send:
                if(et_nickname.getText().toString().equals("")){
                    CustomToast.showToast(this,"nickname不能为空哦",1000);
                    break;
                }else sendUserInfo();
                break;
            default:
                break;
        }
    }

    public void sendUserInfo(){
        //提示框
        dialog = new ProgressDialog(this);
        dialog.setMessage("正在提交，请稍后...");
        dialog.setCancelable(false);
        dialog.show();

        new Thread(new Runnable() {
            @Override
            public void run() {
                SharedPreferences pref = getSharedPreferences("user_password",MODE_PRIVATE);
                String phone = pref.getString("phone", "");
                String nickname = et_nickname.getText().toString();
                String introduce = et_selfintroduce.getText().toString();
                String major = et_major.getText().toString();
                String email = et_email.getText().toString();
                try {
                    nickname = URLEncoder.encode(nickname,"UTF-8");
                    nickname = URLEncoder.encode(nickname,"UTF-8");
                    introduce = URLEncoder.encode(introduce,"UTF-8");
                    introduce = URLEncoder.encode(introduce,"UTF-8");
                    major = URLEncoder.encode(major,"UTF-8");
                    major = URLEncoder.encode(major,"UTF-8");
                    email = URLEncoder.encode(email,"UTF-8");
                    email = URLEncoder.encode(email,"UTF-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                String response = WebService.executeHttpUserInfo(phone, nickname, introduce, major, email);
                Message message = new Message();
                if(response != null){
                    if(response.equals("1")){
                        message.what =1;          //添加成功
                        SharedPreferences.Editor editor = pref.edit();
                        editor.putString("nickname",et_nickname.getText().toString());
                        SettingFragment.setUserNickname(et_nickname.getText().toString());
                    }else message.what = 0;
                }else message.what = 2;         //添加失败
                handler.sendMessage(message);
            }
        }).start();

        handler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what){
                    case 0:
                        dialog.dismiss();
                        CustomToast.showToast(getApplicationContext(), "用户信息提交失败", 1500);
                        break;
                    case 1:
                        dialog.dismiss();

                        et_nickname.setText("");
                        et_email.setText("");
                        et_selfintroduce.setText("");
                        et_major.setText("");
                        et_school.setText("");

                        CustomToast.showToast(getApplicationContext(), "用户信息提交成功", 1500);
                        finish();
                        break;
                    case 2:
                        dialog.dismiss();
                        CustomToast.showToast(getApplicationContext(), "连接服务器失败", 1500);
                        break;
                    default:
                        break;
                }
            }
        };
    }
}
