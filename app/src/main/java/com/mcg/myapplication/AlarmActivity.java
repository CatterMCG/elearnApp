package com.mcg.myapplication;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * Created by 成刚 on 2016/4/3.
 */
public class AlarmActivity extends AppCompatActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        new SweetAlertDialog(this, SweetAlertDialog.NORMAL_TYPE)
                .setTitleText("数据结构")
                .setContentText("马上要考试啦，你准备好了吗？")
                .setConfirmText("嗯，我知道啦")
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                sweetAlertDialog.cancel();
                finish();
            }}).show();
    }
}
