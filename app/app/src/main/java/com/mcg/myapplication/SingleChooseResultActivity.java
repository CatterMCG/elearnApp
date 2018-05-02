package com.mcg.myapplication;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

import base.UserTestBean;
import cn.pedant.SweetAlert.SweetAlertDialog;
import fragment.SingleChooseFragment;

import static com.mcg.myapplication.R.drawable.red_button_background;

/**
 * Created by 成刚 on 2016/4/19.
 */
public class SingleChooseResultActivity extends AppCompatActivity implements View.OnClickListener{
    private RelativeLayout rl_back;
    //显示用户本次测试结果
    private TextView tv_course;
    private TextView tv_time;
    private TextView tv_hard;
    private TextView tv_scores;

    private ArrayList<Button> buttons = new ArrayList<>();
    private Button button;
    //题量
    private int count;
    //本次测试结果
    private String course;
    private String time;
    private String scores;
    private int hard;
    //正确答案和用户答案
    private String[] answer_correct;
    private String[] answer_user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_singlechooseresult);
        getData();
        initView();
    }

    public void initView() {
        rl_back = (RelativeLayout)findViewById(R.id.rl_singlechooseresult_back);
        rl_back.setOnClickListener(this);

        answer_correct = SingleChooseActivity.getAnswer_Correct();
        answer_user = SingleChooseActivity.getAnswer_User();
        for (int i = 0; i < count; i++) {
            switch (i){
                case 0:
                    button = (Button) findViewById(R.id.btn_singlechooseresult_1);
                    break;
                case 1:
                    button = (Button) findViewById(R.id.btn_singlechooseresult_2);
                    break;
                case 2:
                    button = (Button) findViewById(R.id.btn_singlechooseresult_3);
                    break;
                case 3:
                    button = (Button) findViewById(R.id.btn_singlechooseresult_4);
                    break;
                case 4:
                    button = (Button) findViewById(R.id.btn_singlechooseresult_5);
                    break;
                case 5:
                    button = (Button) findViewById(R.id.btn_singlechooseresult_6);
                    break;
                case 6:
                    button = (Button) findViewById(R.id.btn_singlechooseresult_7);
                    break;
                case 7:
                    button = (Button) findViewById(R.id.btn_singlechooseresult_8);
                    break;
                case 8:
                    button = (Button) findViewById(R.id.btn_singlechooseresult_9);
                    break;
                case 9:
                    button = (Button) findViewById(R.id.btn_singlechooseresult_10);
                    break;
                case 10:
                    button = (Button) findViewById(R.id.btn_singlechooseresult_11);
                    break;
                case 11:
                    button = (Button) findViewById(R.id.btn_singlechooseresult_12);
                    break;
                case 12:
                    button = (Button) findViewById(R.id.btn_singlechooseresult_13);
                    break;
                case 13:
                    button = (Button) findViewById(R.id.btn_singlechooseresult_14);
                    break;
                case 14:
                    button = (Button) findViewById(R.id.btn_singlechooseresult_15);
                    break;
                case 15:
                    button = (Button) findViewById(R.id.btn_singlechooseresult_16);
                    break;
                case 16:
                    button = (Button) findViewById(R.id.btn_singlechooseresult_17);
                    break;
                case 17:
                    button = (Button) findViewById(R.id.btn_singlechooseresult_18);
                    break;
                case 18:
                    button = (Button) findViewById(R.id.btn_singlechooseresult_19);
                    break;
                case 19:
                    button = (Button) findViewById(R.id.btn_singlechooseresult_20);
                    break;
            }

            button.setOnClickListener(this);
            button.setVisibility(View.VISIBLE);
            //如果答案正确，那么按钮的颜色为绿色；如果答案错误，那么按钮的颜色为红色
            if(answer_user[i].equals(answer_correct[i])){
                button.setBackgroundResource(R.drawable.bg_btn_test_result_right);
            }else button.setBackgroundResource(R.drawable.bg_btn_test_result_wrong);
            buttons.add(button);
        }

        tv_course = (TextView)findViewById(R.id.tv_test_result_course);
        tv_time = (TextView)findViewById(R.id.tv_test_result_time);
        tv_hard = (TextView)findViewById(R.id.tv_test_result_hard);
        tv_scores = (TextView)findViewById(R.id.tv_test_result_scores);

        tv_course.setText("课程：" + course);
        tv_time.setText("时间：" + time);
        tv_hard.setText("难度：" + hard);
        tv_scores.setText("正确率：" + scores);
    }

    public void getData() {
        count = SingleChooseActivity.getCount();

        course = "数据结构";
        time = SingleChooseFragment.getTime();
        hard = SingleChooseActivity.getHard();
        scores = SingleChooseActivity.getScores();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.rl_singlechooseresult_back:
                setEvent();
                break;
            case R.id.btn_singlechooseresult_1:
                SingleChooseActivity.setmCurrentId(0);
                finish();
                break;
            case R.id.btn_singlechooseresult_2:
                SingleChooseActivity.setmCurrentId(1);
                finish();
                break;
            case R.id.btn_singlechooseresult_3:
                SingleChooseActivity.setmCurrentId(2);
                finish();
                break;
            case R.id.btn_singlechooseresult_4:
                SingleChooseActivity.setmCurrentId(3);
                finish();
                break;
            case R.id.btn_singlechooseresult_5:
                SingleChooseActivity.setmCurrentId(4);
                finish();
                break;
            case R.id.btn_singlechooseresult_6:
                SingleChooseActivity.setmCurrentId(5);
                finish();
                break;
            case R.id.btn_singlechooseresult_7:
                SingleChooseActivity.setmCurrentId(6);
                finish();
                break;
            case R.id.btn_singlechooseresult_8:
                SingleChooseActivity.setmCurrentId(7);
                finish();
                break;
            case R.id.btn_singlechooseresult_9:
                SingleChooseActivity.setmCurrentId(8);
                finish();
                break;
            case R.id.btn_singlechooseresult_10:
                SingleChooseActivity.setmCurrentId(9);
                finish();
                break;
            case R.id.btn_singlechooseresult_11:
                SingleChooseActivity.setmCurrentId(10);
                finish();
                break;
            case R.id.btn_singlechooseresult_12:
                SingleChooseActivity.setmCurrentId(11);
                finish();
                break;
            case R.id.btn_singlechooseresult_13:
                SingleChooseActivity.setmCurrentId(12);
                finish();
                break;
            case R.id.btn_singlechooseresult_14:
                SingleChooseActivity.setmCurrentId(13);
                finish();
                break;
            case R.id.btn_singlechooseresult_15:
                SingleChooseActivity.setmCurrentId(14);
                finish();
                break;
            case R.id.btn_singlechooseresult_16:
                SingleChooseActivity.setmCurrentId(15);
                finish();
                break;
            case R.id.btn_singlechooseresult_17:
                SingleChooseActivity.setmCurrentId(16);
                finish();
                break;
            case R.id.btn_singlechooseresult_18:
                SingleChooseActivity.setmCurrentId(17);
                finish();
                break;
            case R.id.btn_singlechooseresult_19:
                SingleChooseActivity.setmCurrentId(18);
                finish();
                break;
            case R.id.btn_singlechooseresult_20:
                SingleChooseActivity.setmCurrentId(19);
                finish();
                break;
            default:
                break;
        }
    }

    //当用户点击返回键的时候，会弹出一个dialog提示用户是否真的想要退出
    public void setEvent(){
        new SweetAlertDialog(SingleChooseResultActivity.this, SweetAlertDialog.WARNING_TYPE)
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
                SingleChooseActivity.mInstance.finish();
                finish();
            }
        }).show();
    }
}
