package com.mcg.myapplication;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;

import Http.WebService;
import base.CustomToast;
import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;

/**
 * Created by 成刚 on 2016/3/11.
 */
public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {
    //保存用户提交验证的手机号，防止之后用户进行了修改
    private String phone;
    //用于异步处理时间
    private Handler handler = new Handler();
    //注册成功返回的数据
    private String info;
    //注册等待dialog
    private ProgressDialog dialog;
    //用于获取验证码按钮的时间计时
    private MyCount mc;
    //手机号
    private EditText et_phone;
    //密码
    private EditText et_pwd;
    private EditText et_pwd_again;
    //验证码
    private EditText et_code;
    //返回按钮
    private RelativeLayout rl_back;
    //获取验证码按钮
    private Button btn_getCode;
    //登录跳转按钮
    private Button btn_login;
    //实现跳转的intent
    private Intent intent;
    //短信验证需要的账号和密码
    private String APPKEY = "11634345c20ee";
    private String APPSECRET = "c9bf5d2941d4727bb07a393e0f7a4185";
    //SMSSDK的回调事件
    private EventHandler eh;

    public RegisterActivity() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        //初始化短信验证SDK
        SMSSDK.initSDK(this, APPKEY, APPSECRET);
        //配置信息
        initView();
    }

    public void initView() {
        et_phone = (EditText) findViewById(R.id.et_register_phone);
        et_pwd = (EditText) findViewById(R.id.et_register_password);
        et_pwd_again = (EditText) findViewById(R.id.et_register_password_again);
        et_code = (EditText) findViewById(R.id.et_register_checkcode);

        rl_back = (RelativeLayout) findViewById(R.id.rl_register_back);
        btn_getCode = (Button) findViewById(R.id.btn_register_checkcode);
        btn_login = (Button) findViewById(R.id.btn_register_login);
        rl_back.setOnClickListener(this);
        btn_getCode.setOnClickListener(this);
        btn_login.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_register_back:
                intent = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
                break;
            case R.id.btn_register_checkcode://点击按钮之后会倒计时60s，之内不允许再次点击
                //设置倒计时的时间
                btn_getCode.setClickable(false);
                mc = new MyCount(30000, 1000);
                mc.start();
                //获取验证码
                getCode();
                break;
            case R.id.btn_register_login:
                //提交验证的手机号
                if(!et_code.getText().toString().equals("")){//验证码部分不能为空
                    if(et_pwd.getText().toString().equals(et_pwd_again.getText().toString())){//保证两次密码输入的相同
                        SMSSDK.submitVerificationCode("86", et_phone.getText().toString(), et_code.getText().toString());
                        eh = new EventHandler() {
                            @Override
                            public void afterEvent(int event, int result, Object data) {
                                if (result == SMSSDK.RESULT_COMPLETE) {
                                    //回调完成
                                    if (event == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE) {
                                        //提交验证码成功
                                        handler.post(new Runnable() {
                                            @Override
                                            public void run() {
                                                register();
                                            }
                                        });
                                    }
                                } else {
                                    ((Throwable) data).printStackTrace();
                                }
                            }
                        };
                        SMSSDK.registerEventHandler(eh); //注册短信回调
                        break;
                    }else {
                        CustomToast.showToast(this,"两次密码不同",1000);
                        break;
                    }
                }else {
                    CustomToast.showToast(this,"验证码没有填写哦",1000);
                    break;
                }
            default:
                break;
        }
    }

    //服务器进行注册
    public void register() {
        // 提示框
        dialog = new ProgressDialog(this);
        dialog.setTitle("提示");
        dialog.setMessage("正在登陆，请稍后...");
        dialog.setCancelable(false);
        dialog.show();
        // 创建子线程，分别进行Get和Post传输
        new Thread(new MyThread()).start();
    }

    // 子线程接收数据，主线程修改数据
    public class MyThread implements Runnable {
        @Override
        public void run() {
            //用Get方式登录用户
            info = WebService.executeHttpRegister(phone, et_pwd.getText().toString().replace(" ", ""));
            handler.post(new Runnable() {
                @Override
                public void run() {
                    if (info.equals("1")) {//插入数据库，实现跳转
                        CustomToast.showToast(getApplicationContext(), "注册成功", 2000);
                        savePhoneAndPass();
                        Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                        startActivity(intent);
                        SMSSDK.unregisterEventHandler(eh);
                        finish();
                    } else CustomToast.showToast(getApplicationContext(), "注册失败", 2000);
                    dialog.dismiss();
                }
            });
        }
    }

    //提交验证码
    public void getCode() {
        SMSSDK.getVerificationCode("86", et_phone.getText().toString());
        phone = et_phone.getText().toString();
    }

    //保存用户名和密码，用于自动登录
    public boolean savePhoneAndPass() {
        SharedPreferences pref = getSharedPreferences("user_password", MODE_PRIVATE);
        SharedPreferences.Editor meditor = pref.edit();

        //保证每次只保存当前用户
        meditor.clear();
        meditor.putString("yes", "yes");
        meditor.putString("phone", et_phone.getText().toString());
        meditor.putString("password", et_pwd.getText().toString());

        return meditor.commit();
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
