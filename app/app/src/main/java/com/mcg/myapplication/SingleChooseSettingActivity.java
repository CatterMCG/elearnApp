package com.mcg.myapplication;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import java.util.ArrayList;

import Http.WebService;
import adapter.UserTestInfoAdapter;
import base.Course;
import base.CustomToast;
import base.MyDatabaseHelper;
import base.UserTestBean;

/**
 * Created by 成刚 on 2016/4/10.
 */
public class SingleChooseSettingActivity extends AppCompatActivity {
    //本地数据库
    private MyDatabaseHelper dbHelper;
    //用于存储从服务器读取的单项选择json数据
    private String response;
    //用于异步处理UI界面
    private Handler handler;
    //返回按钮
    private RelativeLayout rl_back;
    //选择题目数量
    private RadioGroup radioGroup_count;
    private RadioButton radioButton_count;
    private int count;
    //选择题目难度
    private RadioGroup radioGroup_hard;
    private RadioButton radioButton_hard;
    private int hard;
    //开始按钮
    private Button button;
    //要传送过去的选择题信息
    private ArrayList<Course> courses;
    private String course_name;
    //
    private ArrayList<UserTestBean> usertestinfo = new ArrayList<>();
    private ListView listView;
    private UserTestInfoAdapter adapter;

    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_singlechoosesetting);
        Intent intent = getIntent();
        course_name = intent.getStringExtra("course_name");
        getUserTestInfo();
        initView();
        setListenerForView();

        //没有的是就会提示无法获取选择题
        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what) {
                    case 0://成功获取用户测试信息，显示在mlist中
                        textView.setText("自测记录");
                        adapter = new UserTestInfoAdapter(getApplicationContext(), R.layout.item_test_info, usertestinfo);
                        listView = (ListView) findViewById(R.id.lv_test_info);
                        listView.setAdapter(adapter);
                        break;
                    case 1://失败
                        CustomToast.showToast(getApplicationContext(), "在线获取课程自测题目失败", 1000);
                        break;
                    case 2://成功
                        //跳转到单选设置界面
                        CustomToast.showToast(getApplicationContext(), "在线获取课程自测题目成功", 1000);
                        SingleChooseActivity.actionStart(getApplicationContext(), courses, count, hard);
                        finish();
                    case 3://获取历次测试信息失败
                        /*textView.setText("获取自测记录失败");*/
                        break;
                    default:
                        break;
                }
            }
        };
    }

    //初始化界面
    private void initView() {
        radioGroup_count = (RadioGroup) findViewById(R.id.rg_singlechoosesetting_count);
        radioGroup_hard = (RadioGroup) findViewById(R.id.rg_singlechoosesetting_hard);
        button = (Button) findViewById(R.id.btn_singlechoosesetting);
        rl_back = (RelativeLayout) findViewById(R.id.rl_singlechoosesetting_back);

        textView = (TextView) findViewById(R.id.tv_test_info_alert);
    }

    //在线获取相应课程的自测题目
    public void getCourseDataOnline() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                response = WebService.executeHttpGetCourse(hard + "");
                if (response != null) {//返回数据不为空的时候执行
                    //用gson的方式将json转换为course对象
                    Gson gson = new Gson();
                    JsonParser parser = new JsonParser();
                    JsonArray jArray = parser.parse(response).getAsJsonArray();
                    courses = new ArrayList<>();
                    for (JsonElement obj : jArray) {
                        Course course = gson.fromJson(obj, Course.class);
                        courses.add(course);
                    }
                    Message message = new Message();
                    message.what = 2;
                    handler.sendMessage(message);
                } else {
                    Message message = new Message();
                    message.what = 1;
                    handler.sendMessage(message);
                }
            }
        }).start();
    }

    private void setListenerForView() {
        //设置radiogroup的监听事件
        radioGroup_count.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                //选择题目数量
                radioButton_count = (RadioButton) findViewById(radioGroup_count.getCheckedRadioButtonId());
                if (radioButton_count != null) {
                    count = Integer.parseInt(radioButton_count.getText().toString());
                } else count = 0;
            }
        });
        radioGroup_hard.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                //选择题目难度
                radioButton_hard = (RadioButton) findViewById(radioGroup_hard.getCheckedRadioButtonId());
                if (radioButton_hard.getText().toString().equals("一颗星")) {
                    hard = 1;
                } else if (radioButton_hard.getText().toString().equals("二颗星")) {
                    hard = 2;
                } else hard = 0;
            }
        });
        //设置button的监听事件
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (count != 0) {
                    if (hard != 0) {
                        getCourseDataOnline();
                    } else CustomToast.showToast(getApplicationContext(), "请选择题目难度哦", 2000);
                } else CustomToast.showToast(getApplicationContext(), "请选择题目数量哦", 2000);
            }
        });
        rl_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    public void getUserTestInfo() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Message message = new Message();
                SharedPreferences sharedPreferences = getSharedPreferences("user_password", MODE_PRIVATE);
                String username = sharedPreferences.getString("phone", "");

                String info = WebService.executeHttpGetUserTestInfo(username);
                if (info != null) {

                    Gson gson = new Gson();
                    JsonParser parser = new JsonParser();
                    JsonArray jArray = parser.parse(info).getAsJsonArray();
                    for (JsonElement obj : jArray) {
                        UserTestBean userTestBean = gson.fromJson(obj, UserTestBean.class);
                        usertestinfo.add(userTestBean);
                    }
                    message.what = 0;//成功获取用户测试信息

                } else message.what = 3;//连接服务器失败
                handler.sendMessage(message);
            }
        }).start();
    }
}
