package com.mcg.myapplication;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import java.util.ArrayList;

import Http.WebService;
import base.BaseActivity;
import base.CustomToast;
import base.UserCourseBean;
import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;

/**
 * Created by 成刚 on 2016/3/9.
 */
public class ChangePwdActivity extends BaseActivity implements View.OnClickListener {
    //用于保存用户电话号码
    private String phone;
    //标题
    private TextView tv_title;
    //用于异步处理数据
    private static Handler handler;
    private static Message message;
    //后退键
    private RelativeLayout rl_back;
    //登录键
    private Button btn_login;
    //接受和跳转
    private Intent intent;
    //手机号和新密码
    private static EditText et_phone;
    private static EditText et_pwd;
    private EditText et_pwd_again;
    //访问服务器返的信息
    private static String info = "0";
    //短信验证需要的账号和密码
    private String APPKEY = "11634345c20ee";
    private String APPSECRET = "c9bf5d2941d4727bb07a393e0f7a4185";
    //SMSSDK的回调事件
    private EventHandler eh;
    //验证码
    private EditText et_code;
    private Button btn_getCode;
    //用于获取验证码按钮的时间计时
    private MyCount mc;
    //在线获取课程
    private ArrayList<String> mCourseListFromLocal;

    public ChangePwdActivity() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_newpwd);
        //初始化短信验证SDK
        SMSSDK.initSDK(this, APPKEY, APPSECRET);
        initView();
    }

    public void initView() {
        tv_title = (TextView) findViewById(R.id.tv_newpwd_title);
        et_phone = (EditText) findViewById(R.id.et_newpwd_phone);
        et_code = (EditText) findViewById(R.id.et_newpwd_checkcode);
        btn_getCode = (Button) findViewById(R.id.btn_newpwd_checkcode);
        et_pwd = (EditText) findViewById(R.id.et_newpwd_password);
        et_pwd_again = (EditText) findViewById(R.id.et_newpwd_password_again);
        rl_back = (RelativeLayout) findViewById(R.id.rl_newpwd_back);
        btn_login = (Button) findViewById(R.id.btn_newpwd_login);

        btn_getCode.setOnClickListener(this);
        rl_back.setOnClickListener(this);
        btn_login.setOnClickListener(this);

        intent = getIntent();
        if (intent.getStringExtra("ActivityName").equals("login")) {
            tv_title.setText("忘记密码");
        } else {
            tv_title.setText("密码修改");
            et_phone.setText(getPhone());
            et_phone.setTextColor(Color.BLACK);
            et_phone.setEnabled(false);
        }

        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what) {
                    case 0:
                        break;
                    case 1:
                        CustomToast.showToast(getApplicationContext(), "修改密码成功", 1000);
                        MainActivity.actionStart(getApplicationContext(), mCourseListFromLocal);
                        finish();
                        break;
                    case 2:
                        CustomToast.showToast(getApplicationContext(), "该手机号还没有进行注册", 1000);
                        break;
                    case 3:
                        CustomToast.showToast(getApplicationContext(), "手机号不能为空哦", 1000);
                        break;
                    case 4:
                        CustomToast.showToast(getApplicationContext(), "连接服务器失败", 1000);
                        break;
                    default:
                        break;
                }
            }
        };

        eh = new EventHandler() {
            @Override
            public void afterEvent(int event, int result, Object data) {
                if (result == SMSSDK.RESULT_COMPLETE) {
                    //回调完成
                    if (event == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE) {
                        //提交验证码成功
                        ChangeNewPwd();
                    }
                } else {
                    ((Throwable) data).printStackTrace();
                }
            }
        };

    }

    public void ChangeNewPwd() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                message = Message.obtain();
                info = WebService.executeHttpNewPwd(et_phone.getText().toString(), et_pwd.getText().toString());
                if (info.equals("1")) {
                    message.what = 1;//修改成功
                    info = WebService.executeHttpGetUserCourseInfo(et_phone.getText().toString());
                    if(!info.equals("-1")){
                        Gson gson = new Gson();
                        JsonParser parser = new JsonParser();
                        JsonArray jArray = parser.parse(info).getAsJsonArray();
                        mCourseListFromLocal = new ArrayList<>();
                        for (JsonElement obj : jArray) {
                            UserCourseBean userCourseBean = gson.fromJson(obj, UserCourseBean.class);
                            mCourseListFromLocal.add(userCourseBean.getCourse_name());
                        }
                    }
                    handler.sendMessage(message);
                } else {
                    message.what = 0;//修改失败
                    handler.sendMessage(message);
                }
            }
        }).start();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_newpwd_back:
                if (intent.getStringExtra("ActivityName").equals("login")) {
                    intent = new Intent(ChangePwdActivity.this, LoginActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    finish();
                }
                break;
            case R.id.btn_newpwd_checkcode:
                //设置倒计时的时间
                btn_getCode.setClickable(false);
                mc = new MyCount(30000, 1000);
                mc.start();
                //获取验证码
                getCode();
                break;
            case R.id.btn_newpwd_login:
                /**
                 * 1.检测用户电话号码是否已经注册
                 * 2.将修改的密码插入到数据库当中
                 */
                if (et_pwd.getText().toString().equals(et_pwd_again.getText().toString())) {
                    if(et_phone.getText().toString().equals("")||et_pwd.getText().toString().equals("")){
                        CustomToast.showToast(this, "手机号或密码为空", 1000);
                    }else newPwd();
                } else CustomToast.showToast(this, "两次输入的密码不一致哦", 1000);
                break;
            default:
                break;
        }
    }

    //提交验证码
    public void getCode() {
        SMSSDK.getVerificationCode("86", et_phone.getText().toString());
        phone = et_phone.getText().toString();
    }

    //密码修改部分
    public void newPwd() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                message = Message.obtain();
                if (intent.getStringExtra("ActivityName").equals("login")) {//既要 验证手机号是否注册也要检测是否修改成功
                    info = WebService.executeHttpFindUserByPhone(et_phone.getText().toString());
                    if (info == null) {
                        info = "0";
                    }
                    if (info.equals("1")) {
                        //电话号码注册过，要获取验证码，然后进行修改
                        SMSSDK.submitVerificationCode("86", et_phone.getText().toString(), et_code.getText().toString());

                        SMSSDK.registerEventHandler(eh); //注册短信回调
                    } else if (info.equals("-1")) message.what = 2;//账号还没有注册
                    else message.what = 4;//网络连接失败
                } else {//要获取验证码再进行修改
                    SMSSDK.submitVerificationCode("86", et_phone.getText().toString(), et_code.getText().toString());
                    SMSSDK.registerEventHandler(eh); //注册短信回调
                }
                handler.sendMessage(message);
            }
        }).start();
    }

    //如果是密码修改，填写的手机号码只能是用户本身，从本地获取用户密码
    //获取本地保存的用户名和密码
    public String getPhone() {
        SharedPreferences pref = getSharedPreferences("user_password", MODE_PRIVATE);
        phone = pref.getString("phone", "");
        return phone;
    }

    //定义一个倒计时的内部类
    class MyCount extends CountDownTimer {
        public MyCount(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onFinish() {
            btn_getCode.setText("验证码");
            btn_getCode.setClickable(true);
        }

        @Override
        public void onTick(long millisUntilFinished) {
            btn_getCode.setText(millisUntilFinished / 1000 + "s");
        }
    }
}
