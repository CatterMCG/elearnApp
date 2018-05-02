package com.mcg.myapplication;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

public class LoginActivity extends BaseActivity implements View.OnClickListener {
    private Button btn_login;
    private TextView tv_freregister;
    private TextView tv_forget;
    private static EditText et_phone;
    private static EditText et_pwd;
    private Intent intent;
    private Bundle bundle;
    // 返回主线程更新数据
    private static Handler handler ;
    // 返回成功判断
    private static String info;

    private static ArrayList<String> mCourseListFromLocal = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initView();
        bundle = getPhoneAndPass();
        //下次登录的时候会自动登录，那么关注的课程信息要从本地读取
        // 如果用户自己退出了，不会自动登录，关注的课程要从网上读取
        if (bundle.getString("yes").equals("yes")) {
            mCourseListFromLocal = getCourseListFromLocal();
            MainActivity.actionStart(this, mCourseListFromLocal);
            finish();
        }
    }

    private void initView() {
        btn_login = (Button) findViewById(R.id.btn_login);
        btn_login.setOnClickListener(this);
        tv_freregister = (TextView) findViewById(R.id.tv_login_freregister);
        tv_freregister.setOnClickListener(this);
        tv_forget = (TextView) findViewById(R.id.tv_login_forget);
        tv_forget.setOnClickListener(this);
        et_phone = (EditText) findViewById(R.id.et_login_phone);
        et_pwd = (EditText) findViewById(R.id.et_login_password);
    }

    //课程信息数据取出过程
    public ArrayList<String> getCourseListFromLocal() {
        SharedPreferences pref = getSharedPreferences("courselist", MODE_PRIVATE);
        int size = pref.getInt("courselist_size", 0);

        String s;
        //由于mCourseListFromLocal是static的，先将mCourseListFromLocal设置为空
        mCourseListFromLocal.clear();
        for (int i = 0; i < size; i++) {
            s = pref.getString("courselist_" + i, "");
            mCourseListFromLocal.add(s);
        }
        return mCourseListFromLocal;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_login:
                // 检测网络，无法检测wifi
                if (et_phone.getText().toString().equals("") || et_pwd.getText().toString().equals("")) {
                    CustomToast.showToast(this, "不允许账号或者密码为空哦，请重新输入", 1000);
                    break;
                } else {
                    login();
                    handler = new Handler(){
                        @Override
                        public void handleMessage(Message msg) {
                            super.handleMessage(msg);
                            switch (msg.what){
                                case 0:
                                    CustomToast.showToast(getApplicationContext(), "手机号或密码错误", 1000);
                                    break;
                                case 1:
                                    CustomToast.showToast(getApplicationContext(), "登录成功", 1000);
                                    savePhoneAndPass();
                                    MainActivity.actionStart(getApplicationContext(), mCourseListFromLocal);
                                    finish();
                                    break;
                                 case 2:
                                    CustomToast.showToast(getApplicationContext(), "连接服务器失败", 1000);
                                    break;
                                default:
                                    break;
                            }
                        }
                    };
                    break;
                }
            case R.id.tv_login_freregister:
                intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
                finish();
                break;
            case R.id.tv_login_forget:
                intent = new Intent(LoginActivity.this, ChangePwdActivity.class);
                intent.putExtra("ActivityName","login");
                startActivity(intent);
                finish();
            default:
                break;
        }
    }

    //在线检测用户登录
    public void login() {
        // 创建子线程，分别进行Get和Post传输
        new Thread(new Runnable() {
            @Override
            public void run() {
                Message message = Message.obtain();
                info = WebService.executeHttpGet(et_phone.getText().toString(), et_pwd.getText().toString());
                if(info != null){
                    if(info.equals("1")){
                        message.what = 1;//成功登录
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
                    }else message.what =0;//登录失败
                }else message.what = 2;
                handler.sendMessage(message);
            }
        }).start();
    }

    //获取本地保存的用户名和密码
    public Bundle getPhoneAndPass() {
        Bundle bundle = new Bundle();
        SharedPreferences pref = getSharedPreferences("user_password", MODE_PRIVATE);
        bundle.putString("yes", pref.getString("yes", ""));
        return bundle;
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
}
