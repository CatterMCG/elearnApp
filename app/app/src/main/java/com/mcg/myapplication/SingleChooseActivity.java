package com.mcg.myapplication;

import android.content.Context;
import android.content.Intent;
import android.os.CountDownTimer;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import adapter.SingleChooseFragmentPagerAdapter;
import base.Course;
import cn.pedant.SweetAlert.SweetAlertDialog;
import fragment.SingleChooseFragment;

/**
 * Created by 成刚 on 2016/4/2.
 */
public class SingleChooseActivity extends AppCompatActivity {
    //直接到某一个界面
    public static int mCurrentId;
    //获得这个单项选择界面的实例
    public static SingleChooseActivity mInstance = null;
    //答案
    private static String[] answer_correct;
    private static String[] answer_user;
    private static boolean flag = false;
    //容器 fragment
    private ViewPager pager;
    //用于显示单选的界面
    private ArrayList<SingleChooseFragment> fragmentList;
    private SingleChooseFragmentPagerAdapter adapter;
    //返回back
    private RelativeLayout rl_back;
    //计时器
    private TextView tv_timer;
    private static MyCount mc;
    //数据结构的单选试题
    private static ArrayList<Course> mcourse;
    //单选数量
    private static int count;
    //单选难度
    private static int hard;
    //从SelfTestFragment获取课程单项选择题目
    public static void actionStart(Context ctx,ArrayList<Course> s,int c,int h){
        Intent intent = new Intent(ctx,SingleChooseActivity.class);
        intent.putExtra("exercises", s);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        mcourse = new ArrayList<>(s);
        count = c;
        hard = h;
        ctx.startActivity(intent);
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_singlechoose);
        mInstance = this;
        initView();
        //设置时间为题数*60s
        mc = new MyCount(count*60*1000,1000);
        mc.start();
    }

    @Override
    protected void onResume() {
        super.onResume();
        pager.setCurrentItem(mCurrentId);
    }

    public static void setmCurrentId(int position){
        mCurrentId = position;
    }
    public void initView(){

        answer_user = new String[count];
        answer_correct = new String[count];

        tv_timer = (TextView)findViewById(R.id.tv_singlechoose_timer);

        rl_back = (RelativeLayout)findViewById(R.id.rl_singlechoose_back);
        rl_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(flag){
                    Intent intent = new Intent(getApplicationContext(),SingleChooseResultActivity.class);
                    startActivity(intent);

                }else {
                    setEvent();
                }
            }
        });

        pager = (ViewPager) findViewById(R.id.viewpager_singlechoose);
        fragmentList = new ArrayList<>();

        //用一个循环添加是个题目的fragment，然后添加到viewpager里面
        int a[] = getRandomCount();
        for(int i=0;i<count;i++){
            SingleChooseFragment singleChooseFragment = new SingleChooseFragment();
            singleChooseFragment.setData(mcourse.get(a[i]).getTitle()
                    , mcourse.get(a[i]).getA()
                    , mcourse.get(a[i]).getB()
                    , mcourse.get(a[i]).getC()
                    , mcourse.get(a[i]).getD()
                    , mcourse.get(a[i]).getAnswer()
                    ,mcourse.get(a[i]).getAnalysis());
            fragmentList.add(singleChooseFragment);
            //设置正确答案
            answer_correct[i] = mcourse.get(a[i]).getAnswer();
            //给用户的答案初始化为错误的
            answer_user[i] = mcourse.get(a[i]).getAnswer() +"1";
        }

        adapter = new SingleChooseFragmentPagerAdapter(getSupportFragmentManager(),fragmentList);
        //给viewPager设置配置器
        pager.setAdapter(adapter);
    }

    //获取30以内的count个不相同的随机数
    public int[]  getRandomCount(){
        int a[] = new int[count];//初始化数组
        Random random = new Random();
        int k = 0;//记录有效的随机数个数
        while(k < count){
            boolean flag = true;//用来标志的变量
            int r = random.nextInt(30);
            for(int i=0;i<count;i++){
                if(r == a[i]){
                    flag = false;
                    break;
                }
            }
            if(flag){
                a[k] = r;
                k++;
            }
        }
        return a;
    }

    //当用户点击返回键的时候，会弹出一个dialog提示用户是否真的想要退出
    public void setEvent(){
        new SweetAlertDialog(SingleChooseActivity.this, SweetAlertDialog.WARNING_TYPE)
                .setTitleText("EXIT")
                .setContentText("你确定要退出当前测试吗？")
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

    //定义一个倒计时的内部类
    class MyCount extends CountDownTimer {
        public MyCount(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }
        @Override
        public void onFinish() {
            tv_timer.setText("00:00");
            new SweetAlertDialog(SingleChooseActivity.this, SweetAlertDialog.WARNING_TYPE)
                    .setTitleText("时间到！")
                .setContentText("是否选择继续测试?")
                    .setCancelText("取消")
                    .setConfirmText("确定")
                    .showCancelButton(true)
                    .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sDialog) {
                           SingleChooseFragment singleChooseFragment = new SingleChooseFragment();
                            singleChooseFragment.showScores();
                        }
                    }).setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                @Override
                public void onClick(SweetAlertDialog sweetAlertDialog) {
                    sweetAlertDialog.cancel();
                }
            }).show();
        }
        @Override
        public void onTick(long millisUntilFinished) {
            tv_timer.setText(millisUntilFinished / 60000 + ":" + (millisUntilFinished - 60000 * (millisUntilFinished / 60000)) / 1000);
        }
    }

    //让倒计时功能停止
    public static  void mcshutDown(){
        mc.cancel();
    }

    //用户自己的答案
    public static void setAnswer_user(int position,String answer){
        answer_user[position] = answer;
    }

    public static String getScores(){
        int k=0;
        for(int i=0 ; i < count;i++){
            if(answer_user[i].equals(answer_correct[i])){
                k++;
            }
        }
        return k+"/"+count;
    }

    public static String[] getAnswer_User(){
        return answer_user;
    }
    public static String[] getAnswer_Correct(){
        return answer_correct;
    }
    public static int getHard(){
        return hard;
    }
    public static int getCount(){
        return count;
    }

    //设置flag，当flag为true的时候则不会显示答案分析，如果为false则为显示答案分析
    public static boolean setFlag(){
        flag = true;
        return  flag;
    }
    public static boolean getFlag(){
        return  flag;
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        mCurrentId = 0;
        flag = false;
    }
}

