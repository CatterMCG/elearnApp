package com.mcg.myapplication;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.RadioButton;
import android.widget.TextView;

import java.util.ArrayList;

import base.BaseActivity;
import fragment.CourseFragment;
import fragment.SelfTestFragment;
import fragment.SettingFragment;

/**
 * Created by 成刚 on 2016/3/11.
 */
public class MainActivity extends BaseActivity implements View.OnClickListener {

    private TextView title;
    private RadioButton rbtn_course;
    private RadioButton rbtn_selftest;
    private RadioButton rbtn_setting;

    private CourseFragment mCourse;
    private SelfTestFragment mSelfTest;
    private SettingFragment mSetting;

    private ArrayList<String> mCourseList = new ArrayList<>();//新添加的课程
    private ArrayList<String> mCourseListFromFragment = new ArrayList<>() ;//从courselist获取课程
    private static ArrayList<String> mCourseListFromLocal ;

    //从login获取本地数据
    public static void actionStart(Context ctx,ArrayList<String> s){
        Intent intent = new Intent(ctx,MainActivity.class);
        intent.putStringArrayListExtra("mCourseListFromLocal", s);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        mCourseListFromLocal = new ArrayList<>(s);
        ctx.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        setDefaultFragment();
    }

    //设置默认的fragment
    public void setDefaultFragment(){
        FragmentManager fm = getFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        mCourse = new CourseFragment();
        transaction.add(R.id.fl_fragment_content,mCourse, "tag1");
        transaction.commit();
        setTextColorAndTitle(R.id.btn_main_course);
    }

    //初始化布局和数据
    public void initView(){

        //初始化title和RadioGroup里面的按钮
        title = (TextView) findViewById(R.id.tv_main_title);
        rbtn_course = (RadioButton) findViewById(R.id.btn_main_course);
        rbtn_course.setOnClickListener(this);
        rbtn_selftest = (RadioButton) findViewById(R.id.btn_main_selftest);
        rbtn_selftest.setOnClickListener(this);
        rbtn_setting = (RadioButton) findViewById(R.id.btn_main_setting);
        rbtn_setting.setOnClickListener(this);
    }

    //用于CourseFragment从MainActivity里面获取local CourseList
    public ArrayList<String> getCourseListFromLocal(){
        return mCourseListFromLocal;
    }

    //用于设置title名称和下面buttonde显示高亮
    public void setTextColorAndTitle(int id){
        switch (id){
            case R.id.btn_main_course:
                rbtn_course.setTextColor(Color.WHITE);
                rbtn_selftest.setTextColor(Color.BLACK);
                rbtn_setting.setTextColor(Color.BLACK);
                title.setText("我的课程");
                break;
            case R.id.btn_main_selftest:
                rbtn_course.setTextColor(Color.BLACK);
                rbtn_selftest.setTextColor(Color.WHITE);
                rbtn_setting.setTextColor(Color.BLACK);
                title.setText("自我测试");
                break;
            case R.id.btn_main_setting:
                rbtn_course.setTextColor(Color.BLACK);
                rbtn_selftest.setTextColor(Color.BLACK);
                rbtn_setting.setTextColor(Color.WHITE);
                title.setText("我的易学");
                break;
        }
    }

    //用于fragment的切换
    @Override
    public void onClick(View view) {
        FragmentManager fm = getFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        switch (view.getId()){
            case R.id.btn_main_course:
                if(mCourse.isAdded()){
                    transaction.hide(getCurrentFragment()).show(mCourse);
                }
                setTextColorAndTitle(R.id.btn_main_course);
                break;
            case  R.id.btn_main_selftest:
                if(mSelfTest == null){
                    mSelfTest = new SelfTestFragment();
                    transaction.add(R.id.fl_fragment_content,mSelfTest, "tag2");
                }
                transaction.hide(getCurrentFragment()).show(mSelfTest);
                setTextColorAndTitle(R.id.btn_main_selftest);
                break;
            case R.id.btn_main_setting:
                if(mSetting == null){
                    mSetting = new SettingFragment();
                    transaction.add(R.id.fl_fragment_content,mSetting, "tag3");
                }
                transaction.hide(getCurrentFragment()).show(mSetting);
                setTextColorAndTitle(R.id.btn_main_setting);
                break;
            default:
                break;
        }
        transaction.commit();
    }

    //获得当前的fragment
    public Fragment getCurrentFragment(){
        if(mSelfTest != null && mSelfTest.isVisible()){
            return mSelfTest;
        }else if(mSetting != null && mSetting.isVisible()){
            return mSetting;
        }else return mCourse;
    }


    //从SearchActivity返回新添加的数据
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode){
            case 0:
                if(resultCode == RESULT_OK){
                    mCourseList = data.getStringArrayListExtra("AddList");
                    CourseFragment.setCourseList(mCourseList);
                }
        }
    }

    //保存本地数据,在onPause中进行保存，为了突然结束app时候无法保存用户数据
    @Override
    protected void onPause() {
        super.onPause();
        saveCourseList();//数据存储
    }

    //数据保存过程
    public  boolean saveCourseList(){
        SharedPreferences pref_course = getSharedPreferences("courselist", MODE_PRIVATE);
        SharedPreferences.Editor meditor_course = pref_course.edit();

        //退出的时候会调用fragment中的方法获取当前的courselist，并把之前的数据删除，保存最新的。
        mCourseListFromFragment = mCourse.getCourseListFragment();
        meditor_course.clear();
        meditor_course.putInt("courselist_size", mCourseListFromFragment.size());
        if(mCourseListFromFragment.size() != 0){
            for(int i = 0;i<mCourseListFromFragment.size();i++){
                meditor_course.putString("courselist_" + i,mCourseListFromFragment.get(i));
            }
        }

        mCourseListFromLocal = mCourseListFromFragment;

        return meditor_course.commit();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        SharedPreferences pref_course = getSharedPreferences("courselist", MODE_PRIVATE);
        SharedPreferences.Editor meditor_course = pref_course.edit();

        SharedPreferences pref_user = getSharedPreferences("user_password", MODE_PRIVATE);
        //如果是用户自己退出的，就直接清除用户选择的本地课程信息
        if(pref_user.getString("yes","").equals("")){
            meditor_course.clear();
            //由于mCourseListFromLocal是静态的，如果资源够这个进程没有结束的时候还是会存在的，所以同时
            //要将mCourseListFromLocal给这是为空
            mCourseListFromLocal.clear();
        }
        meditor_course.commit();
    }
}
