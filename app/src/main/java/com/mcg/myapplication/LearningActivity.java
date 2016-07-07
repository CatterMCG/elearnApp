package com.mcg.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

import adapter.CourselearningAdapter;
import base.CustomToast;
import base.MyProgressDialog;
import cn.pedant.SweetAlert.SweetAlertDialog;
import fragment.CourseFragment;

/**
 * Created by 成刚 on 2016/4/14.
 */
public class LearningActivity extends AppCompatActivity implements View.OnClickListener {
    private ViewPager viewPager;
    private ArrayList<View> mlists;
    private CourselearningAdapter adapter;

    private RelativeLayout rl_back;
    private TextView tv_back_home;

    private RelativeLayout rl_Important_question;
    private RelativeLayout rl_Intensive_memory;
    private RelativeLayout rl_download;
    //下载链接
    private LinearLayout rl_download_1;
    private LinearLayout rl_download_2;
    private LinearLayout rl_download_3;

    //private static String IP = "http://10.0.3.2:8080";
    private static String IP = "192.168.43.251:8080";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_learn);
        CourseFragment.ProgressDialogDismiss();

        initView();
    }

    public void initView() {
        tv_back_home = (TextView)findViewById(R.id.tv_learn_back_home);
        tv_back_home.setOnClickListener(this);

        LayoutInflater inflater = getLayoutInflater();

        //获取相应view中的控件，进行监听
        View view = inflater.inflate(R.layout.fragment_course_data_structure_0, null);
        rl_Important_question = (RelativeLayout) view.findViewById(R.id.rl_data_structure_Important_questions);
        rl_Important_question.setOnClickListener(this);
        rl_Intensive_memory = (RelativeLayout) view.findViewById(R.id.rl_data_structure_Intensive_memory);
        rl_Intensive_memory.setOnClickListener(this);
        rl_download = (RelativeLayout) view.findViewById(R.id.rl_data_structure_download);
        rl_download.setOnClickListener(this);

        View view1 = inflater.inflate(R.layout.fragment_course_data_structure_5, null);
        rl_download_1 = (LinearLayout) view1.findViewById(R.id.rl_download_data_structure_1);
        rl_download_1.setOnClickListener(this);
        rl_download_2 = (LinearLayout) view1.findViewById(R.id.rl_download_data_structure_2);
        rl_download_2.setOnClickListener(this);
        rl_download_3 = (LinearLayout) view1.findViewById(R.id.rl_download_data_structure_3);
        rl_download_3.setOnClickListener(this);

        viewPager = (ViewPager) findViewById(R.id.viewpager_learn);
        mlists = new ArrayList<>();
        Intent intent = getIntent();
        if (intent.getStringExtra("course_name").equals("数据结构")) {
            mlists.add(view);
            mlists.add(inflater.inflate(R.layout.fragment_course_data_structure_1, null));
            mlists.add(inflater.inflate(R.layout.fragment_course_data_structure_2, null));
            mlists.add(inflater.inflate(R.layout.fragment_course_data_structure_3, null));
            mlists.add(inflater.inflate(R.layout.fragment_course_data_structure_4, null));
            mlists.add(view1);
        }
        adapter = new CourselearningAdapter(mlists);
        viewPager.setAdapter(adapter);

        rl_back = (RelativeLayout) findViewById(R.id.rl_learn_back);
        rl_back.setOnClickListener(this);
    }

    //当用户点击返回键的时候，会弹出一个dialog提示用户是否真的想要退出
    public void setEvent() {
        new SweetAlertDialog(LearningActivity.this, SweetAlertDialog.WARNING_TYPE)
                .setTitleText("EXIT")
                .setContentText("你确定要退出本次学习吗？")
                .setCancelText("取消")
                .setConfirmText("确定")
                .showCancelButton(true)
                .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {
                        sDialog.cancel();
                    }
                }).setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
            @Override
            public void onClick(SweetAlertDialog sweetAlertDialog) {
                finish();
            }
        }).show();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_learn_back://退出课程学习
                setEvent();
                break;
            case R.id.tv_learn_back_home://回到首页
                viewPager.setCurrentItem(0);
                break;
            case R.id.rl_data_structure_Important_questions://重要题型
                viewPager.setCurrentItem(1);
                break;
            case R.id.rl_data_structure_Intensive_memory://强化记忆
                viewPager.setCurrentItem(4);
                break;
            case R.id.rl_data_structure_download://资料下载
                viewPager.setCurrentItem(5);
                break;
            case R.id.rl_download_data_structure_1://资料下载1
                //直接传入
                DownLoadActivity.actionStart(getApplicationContext(),IP+"/SCUEC_data_structure.zip");
                break;
            case R.id.rl_download_data_structure_2://资料下载2
                DownLoadActivity.actionStart(getApplicationContext(),IP+"/WHU_data_structure.zip");
                break;
            case R.id.rl_download_data_structure_3://资料下载3
                DownLoadActivity.actionStart(getApplicationContext(),IP+"/books_data_structure.zip");
                break;
            default:
                break;
        }
    }

}
