package com.mcg.myapplication;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;


/**
 * Created by 成刚 on 2016/3/29.
 */
public class AboutEstudyActivity extends AppCompatActivity{

    private RelativeLayout rl_back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        initview();
    }

    public void initview(){
        rl_back = (RelativeLayout)findViewById(R.id.rl_about_back);
        rl_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
