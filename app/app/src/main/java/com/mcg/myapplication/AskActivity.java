package com.mcg.myapplication;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import base.CustomToast;

/**
 * Created by 成刚 on 2016/4/1.
 */
public class AskActivity extends AppCompatActivity{
    private EditText et_question;
    private Button btn_send;
    private RelativeLayout rl_back;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question);

        initView();
    }

    public void initView(){
        et_question = (EditText)findViewById(R.id.et_question);

        btn_send = (Button)findViewById(R.id.btn_question);
        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CustomToast.showToast(AskActivity.this,et_question.getText().toString(),1500);
                et_question.setText("");
            }
        });

        rl_back = (RelativeLayout)findViewById(R.id.rl_question_back);
        rl_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

}
