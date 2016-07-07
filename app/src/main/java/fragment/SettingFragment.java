package fragment;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.mcg.myapplication.AboutEstudyActivity;
import com.mcg.myapplication.AskActivity;
import com.mcg.myapplication.ChangePwdActivity;
import com.mcg.myapplication.ExamAlarmActivity;
import com.mcg.myapplication.FeedBackActivity;
import com.mcg.myapplication.LoginActivity;
import com.mcg.myapplication.R;
import com.mcg.myapplication.UserInfoActivity;
import com.mcg.myapplication.VersionActivity;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import Http.WebService;
import base.CustomToast;
import base.MyProgressDialog;
import base.UserCourseBean;
import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * Created by 成刚 on 2016/3/11.
 */
public class SettingFragment extends Fragment implements View.OnClickListener {
    private MyProgressDialog dialog = new MyProgressDialog();

    private Handler handler;

    private String jsonstr;
    private Gson gson;

    private static TextView tv_nickname;
    private ImageView imageView;
    private RelativeLayout LL1;
    /*private RelativeLayout LL2;*/
    private RelativeLayout LL3;
    private RelativeLayout LL4;
    private RelativeLayout LL5;
    private RelativeLayout LL6;
    private RelativeLayout LL7;
    private RelativeLayout LL8;
    private Button btn_exit;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_setting, container, false);
        tv_nickname = (TextView) view.findViewById(R.id.tv_setting_nickname);
        setNickName();
        imageView = (ImageView) view.findViewById(R.id.imgv_setting_user);

        LL1 = (RelativeLayout) view.findViewById(R.id.rl_setting_1);
        /*LL2 = (RelativeLayout) view.findViewById(R.id.rl_setting_2);*/
        LL3 = (RelativeLayout) view.findViewById(R.id.rl_setting_3);
        LL4 = (RelativeLayout) view.findViewById(R.id.rl_setting_4);
        LL5 = (RelativeLayout) view.findViewById(R.id.rl_setting_5);
        LL6 = (RelativeLayout) view.findViewById(R.id.rl_setting_6);
        LL7 = (RelativeLayout) view.findViewById(R.id.rl_setting_7);
        LL8 = (RelativeLayout) view.findViewById(R.id.rl_setting_8);
        btn_exit = (Button) view.findViewById(R.id.btn_setting_exit);

        btn_exit.setOnClickListener(this);
        imageView.setOnClickListener(this);
        LL1.setOnClickListener(this);
        /*LL2.setOnClickListener(this);*/
        LL3.setOnClickListener(this);
        LL4.setOnClickListener(this);
        LL5.setOnClickListener(this);
        LL6.setOnClickListener(this);
        LL7.setOnClickListener(this);
        LL8.setOnClickListener(this);
        return view;
    }

    public void setNickName() {
        SharedPreferences preferences = getActivity().getSharedPreferences("user_password", Context.MODE_PRIVATE);
        String nickname = preferences.getString("nickname", "");
        //如果本地存储的nickname不问空的话，则将本地的Nickname读出来，否则会使用默认的
        if (!nickname.toString().equals("")) {
            tv_nickname.setText(preferences.getString("nickname", ""));
        }
    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {
            case R.id.imgv_setting_user:
                setUserImag();
                break;
            case R.id.rl_setting_1://资料完善
                intent = new Intent(getActivity().getApplicationContext(), UserInfoActivity.class);
                startActivity(intent);
                break;
            /*case R.id.rl_setting_2://你问我答
                intent = new Intent(getActivity().getApplicationContext(), AskActivity.class);
                startActivity(intent);
                break;*/
            case R.id.rl_setting_3://密码修改
                intent = new Intent(getActivity().getApplicationContext(), ChangePwdActivity.class);
                intent.putExtra("ActivityName", "main");
                startActivity(intent);
                break;
            case R.id.rl_setting_4://考试提醒
                intent = new Intent(getActivity().getApplicationContext(), ExamAlarmActivity.class);
                startActivity(intent);
                break;
            case R.id.rl_setting_5://软件版本
                intent = new Intent(getActivity().getApplicationContext(), VersionActivity.class);
                startActivity(intent);
                break;
            case R.id.rl_setting_6://意见反馈
                intent = new Intent(getActivity().getApplicationContext(), FeedBackActivity.class);
                startActivity(intent);
                break;
            case R.id.rl_setting_7://关于我们
                intent = new Intent(getActivity().getApplicationContext(), AboutEstudyActivity.class);
                startActivity(intent);
                break;
            case R.id.rl_setting_8://信息同步
                SyncUserCourses();
                break;
            case R.id.btn_setting_exit:
                UserExit();
            default:
                break;
        }
    }

    public void SyncUserCourses() {
        //显示提示框
        dialog.show(getActivity(),"正在同步，请稍后...");

        CourseFragment courseFragment = new CourseFragment();

        final SharedPreferences pref_user = getActivity().getSharedPreferences("user_password", Context.MODE_PRIVATE);
        ArrayList<String> course_list = courseFragment.getCourseListFragment();

        List<UserCourseBean> userCourseBeans = new ArrayList<>();
        String course_name;
        String username;
        for (int i = 0; i < course_list.size(); i++) {
            course_name = course_list.get(i);
            username = pref_user.getString("phone", "");
            UserCourseBean userCourseBean = new UserCourseBean();
            userCourseBean.setUsername(username);
            try {
                course_name = URLEncoder.encode(course_name, "UTF-8");
                course_name = URLEncoder.encode(course_name, "UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            userCourseBean.setCourse_name(course_name);
            userCourseBeans.add(userCourseBean);
        }
        gson = new Gson();
        jsonstr = gson.toJson(userCourseBeans);
        Log.d("TAG", jsonstr);
        new Thread(new Runnable() {
            @Override
            public void run() {
                Message message = new Message();
                String response = WebService.executeHttpSendUserCourseInfo(pref_user.getString("phone", ""), jsonstr);
                if (response != null) {
                    message.what = 1;//同步成功
                } else message.what = 0;//同步失败
                handler.sendMessage(message);
            }
        }).start();

        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what) {
                    case 0:
                        dialog.dismiss();
                        CustomToast.showToast(getActivity().getApplicationContext(), "连接服务器失败", 1000);
                        break;
                    case 1:
                        dialog.dismiss();
                        CustomToast.showToast(getActivity().getApplicationContext(), "同步成功", 1000);
                        break;
                    default:
                        break;
                }
            }
        };
    }

    public static void setUserNickname(String nickname) {
        tv_nickname.setText(nickname);
    }

    //从相册中选择图片
    public void setUserImag() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, 1);
    }

    /**
     * 剪切图片
     * @param uri
     */
    private void crop(Uri uri) {
        // 裁剪图片意图
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        intent.putExtra("crop", "true");
        // 裁剪框的比例，1：1
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        // 裁剪后输出图片的尺寸大小
        intent.putExtra("outputX", 250);
        intent.putExtra("outputY", 250);

        intent.putExtra("outputFormat", "JPEG");// 图片格式
        intent.putExtra("noFaceDetection", true);// 取消人脸识别
        intent.putExtra("return-data", true);
        // 开启一个带有返回值的Activity，请求码为PHOTO_REQUEST_CUT
        startActivityForResult(intent, 2);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode,resultCode,data);
        if(requestCode == 1){//从相册中选取图片
            if (data != null){
                Uri uri = data.getData();
                crop(uri);
            }
        }else if(requestCode == 2){//剪切选取的图片
            if(data != null){
                Bitmap bitmap = data.getParcelableExtra("data");
                this.imageView.setImageBitmap(bitmap);
            }
        }
    }

    public void UserExit() {
        new SweetAlertDialog(getActivity(), SweetAlertDialog.WARNING_TYPE)
                .setTitleText("EXIT")
                .setContentText("确认要退出当前用户?")
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
                SharedPreferences pref = getActivity().getSharedPreferences("user_password", Context.MODE_PRIVATE);
                SharedPreferences.Editor meditor = pref.edit();
                meditor.clear();
                meditor.putString("yes", "");
                meditor.putString("phone", "");
                meditor.putString("password", "");
                meditor.commit();

                //跳转到登录界面
                Intent intent = new Intent(getActivity().getApplicationContext(), LoginActivity.class);
                startActivity(intent);
                getActivity().finish();
            }
        }).show();
    }

}
