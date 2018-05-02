package com.mcg.myapplication;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;

import java.util.Calendar;

import broadcast.AlarmReceiver;

/**
 * Created by 成刚 on 2016/4/1.
 */
public class ExamAlarmActivity extends AppCompatActivity {
    private RelativeLayout rl_back;
    private Button btn_set;
    private TextView tV_time;
    private int year, monthOfYear, dayOfMonth, hourOfDay, minute;
    private Calendar calendar;
    private String s;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_remind);

        initView();
    }

    public void initView() {
        rl_back = (RelativeLayout) findViewById(R.id.rl_remind_back);
        rl_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        monthOfYear = calendar.get(Calendar.MONTH);
        dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
        hourOfDay = calendar.get(Calendar.HOUR_OF_DAY);
        minute = calendar.get(Calendar.MINUTE);

        tV_time = (TextView) findViewById(R.id.tv_alarmclock_time);

        btn_set = (Button) findViewById(R.id.btn_alarmclock_set);
        btn_set.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //显示DatePickerDialog,设置日期
                DatePickerDialog datePickerDialog = new DatePickerDialog(ExamAlarmActivity.this
                        , new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        s = "日期：" + year + "年" + (monthOfYear + 1) + "月" + dayOfMonth;
                        calendar.set(Calendar.YEAR, year);
                        calendar.set(Calendar.MONTH, monthOfYear);
                        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                        //显示TimePickerDialog，设置时间
                        TimePickerDialog timePickerDialog = new TimePickerDialog(ExamAlarmActivity.this
                                , new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                tV_time.setText(s + "日" + hourOfDay + "时" + minute + "分");
                                calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                                calendar.set(Calendar.MINUTE, minute);
                                calendar.set(Calendar.SECOND, 0);
                                calendar.set(Calendar.MILLISECOND, 0);

                            }
                        }, hourOfDay, minute, true);
                        //timePickerDialog.show();
                    }
                }, year, monthOfYear, dayOfMonth);
                datePickerDialog.setButton(TimePickerDialog.BUTTON_POSITIVE, getString(R.string.dialog_ok), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        getTimePicekerDialog().show();
                    }
                });
                datePickerDialog.show();
            }
        });
    }
    //设置小时和分钟
    public TimePickerDialog getTimePicekerDialog(){
        TimePickerDialog timePickerDialog;
        timePickerDialog = new TimePickerDialog(ExamAlarmActivity.this
                , new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                tV_time.setText(s + "日" + hourOfDay + "时" + minute + "分");
                calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                calendar.set(Calendar.MINUTE, minute);
                calendar.set(Calendar.SECOND, 0);
                calendar.set(Calendar.MILLISECOND, 0);
                Intent intent = new Intent(ExamAlarmActivity.this, AlarmReceiver.class);
                PendingIntent sender = PendingIntent.getBroadcast(ExamAlarmActivity.this, 0, intent, 0);
                AlarmManager am = (AlarmManager) getSystemService(ALARM_SERVICE);
                am.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), sender);
            }
        }, hourOfDay, minute, true);
        return timePickerDialog;
    }
}
