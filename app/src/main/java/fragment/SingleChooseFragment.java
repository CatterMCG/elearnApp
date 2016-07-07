package fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.text.format.Time;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import com.mcg.myapplication.R;
import com.mcg.myapplication.SingleChooseActivity;
import com.mcg.myapplication.SingleChooseResultActivity;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Currency;
import java.util.Date;
import java.util.TimeZone;

import Http.WebService;
import base.CustomToast;
import base.UserTestBean;
import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * Created by 成刚 on 2016/4/2.
 */
public class SingleChooseFragment extends Fragment implements View.OnClickListener {//单项选择界面统一的一个界面模式

    private Handler handler;
    private ImageView imgv_divider;

    private TextView tv_test_id;//题号
    private TextView tv_test_topic;//题目
    private String title;
    private String answer;
    private Button btn_commit;
    private TextView tv_test_analysis_contetn;//答案解析类容
    private String analysis;

    private static String mtime;

    //选项A B C D以及选项的内容
    private RadioButton btn_A;
    private String content_A;
    private RadioButton btn_B;
    private String content_B;
    private RadioButton btn_C;
    private String content_C;
    private RadioButton btn_D;
    private String content_D;

    //获取当前fragment是第几个
    private int position;
    private int length;

    //用于传入数据的构造函数
    public void setData(String title, String content_A, String content_B, String content_C, String content_D, String answer,String analysis) {
        this.title = title;
        this.content_A = content_A;
        this.content_B = content_B;
        this.content_C = content_C;
        this.content_D = content_D;
        this.answer = answer;
        this.analysis = analysis;
    }

    public static SingleChooseFragment newInstance(SingleChooseFragment singleChooseFragment, int length, int position) {
        SingleChooseFragment fragment = singleChooseFragment;

        fragment.length = length;
        fragment.position = position;
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_singlechoose, container, false);
        //设置题号
        tv_test_id = (TextView)view.findViewById(R.id.tv_singlechoose_id);
        tv_test_id.setText((position+1) + "/" + SingleChooseActivity.getCount());

        imgv_divider = (ImageView) view.findViewById(R.id.imgv_test_diver);
        tv_test_topic = (TextView) view.findViewById(R.id.tv_test_title);
        btn_commit = (Button) view.findViewById(R.id.btn_test_commit);
        tv_test_analysis_contetn = (TextView) view.findViewById(R.id.tv_test_analysis_content);
        //设置题目和分析
        tv_test_topic.setText(title);
        tv_test_analysis_contetn.setText("正确答案："+answer+"\n答案解析：\n" + analysis);

        btn_A = (RadioButton) view.findViewById(R.id.btn_answer_A);
        btn_B = (RadioButton) view.findViewById(R.id.btn_answer_B);
        btn_C = (RadioButton) view.findViewById(R.id.btn_answer_C);
        btn_D = (RadioButton) view.findViewById(R.id.btn_answer_D);
        //设置选项和分析
        btn_A.setText(content_A);
        btn_B.setText(content_B);
        btn_C.setText(content_C);
        btn_D.setText(content_D);

        //设置点击事件
        btn_commit.setOnClickListener(this);
        btn_A.setOnClickListener(this);
        btn_B.setOnClickListener(this);
        btn_C.setOnClickListener(this);
        btn_D.setOnClickListener(this);

        //设置点击 提交按钮 之后的事件
        if(position == (length - 1)){
            btn_commit.setVisibility(View.VISIBLE);
        }else btn_commit.setVisibility(View.GONE);

        //如果提交了Flag就是true
        if(SingleChooseActivity.getFlag()){
            btn_commit.setVisibility(View.GONE);
            //radiobutton设置为不可点击
            btn_A.setClickable(false);
            btn_B.setClickable(false);
            btn_C.setClickable(false);
            btn_D.setClickable(false);

            tv_test_analysis_contetn.setVisibility(View.VISIBLE);
        }

        handler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what){
                    case 0://发送到数据库失败
                        break;
                    case 1://发送到数据库成功
                        break;
                    case 2://连接服务器失败
                        break;
                    default:
                        break;
                }
            }
        };

        return view;
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.btn_answer_A:
                CustomToast.showToast(getActivity().getApplicationContext(), "你选择了A选项", 1500);
                SingleChooseActivity.setAnswer_user(position, "A");
                break;
            case R.id.btn_answer_B:
                CustomToast.showToast(getActivity().getApplicationContext(), "你选择了B选项", 1500);
                SingleChooseActivity.setAnswer_user(position, "B");
                break;
            case R.id.btn_answer_C:
                CustomToast.showToast(getActivity().getApplicationContext(), "你选择了C选项", 1500);
                SingleChooseActivity.setAnswer_user(position, "C");
                break;
            case R.id.btn_answer_D:
                CustomToast.showToast(getActivity().getApplicationContext(), "你选择了D选项", 1500);
                SingleChooseActivity.setAnswer_user(position, "D");
                break;
            case R.id.btn_test_commit:
                SingleChooseActivity.setFlag();
                btn_commit.setVisibility(View.GONE);
                tv_test_analysis_contetn.setVisibility(View.VISIBLE);
                imgv_divider.setVisibility(View.VISIBLE);
                //设置选项不可点击无效
                btn_A.setClickable(false);
                btn_B.setClickable(false);
                btn_C.setClickable(false);
                btn_D.setClickable(false);
                //让主界面的倒计时功能停止
                SingleChooseActivity.mcshutDown();
                //显示得分情况
                showScores();
                //将测试结果添加到数据库
                sendSelfTestInfo();
                break;
            default:
                break;
        }
    }

    public void sendSelfTestInfo(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                SharedPreferences sharedPreferences = getActivity().getSharedPreferences("user_password", Context.MODE_PRIVATE);
                String username = sharedPreferences.getString("phone","");
                String course_name = "数据结构";
                String time = getCurrentTime();
                mtime = time;
                int hard = SingleChooseActivity.getHard();
                String scores = SingleChooseActivity.getScores();
                try {
                    course_name = URLEncoder.encode(course_name,"UTF-8");
                    course_name = URLEncoder.encode(course_name,"UTF-8");
                    time = URLEncoder.encode(time,"UTF-8");
                    time = URLEncoder.encode(time,"UTF-8");
                    scores = URLEncoder.encode(scores,"UTF-8");
                    scores = URLEncoder.encode(scores,"UTF-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }

                String response = WebService.executeHttpSendUserTestInfo(username,course_name,time,hard,scores);
                Message message = new Message();
                if(response != null){
                    if(response.equals("1")){
                        message.what =1;          //添加成功

                    }else message.what = 0;
                }else message.what = 2;         //添加失败
                handler.sendMessage(message);
            }
        }).start();
    }

    public String getCurrentTime(){
        SimpleDateFormat formatter = new SimpleDateFormat ("yyyy年MM月dd日 HH:mm:ss ");
        formatter.setTimeZone(TimeZone.getTimeZone("GMT+08"));
        Date curDate = new Date(System.currentTimeMillis());//获取当前时间
        String str = formatter.format(curDate);
        return str;
    }

    public void showScores(){
        new SweetAlertDialog(getActivity(), SweetAlertDialog.NORMAL_TYPE)
                .setTitleText("score")
                .setContentText("你本次测试的正确率为：" + SingleChooseActivity.getScores())
                .setConfirmText("ok")
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        sweetAlertDialog.cancel();
                        Intent intent = new Intent(getActivity().getApplicationContext(), SingleChooseResultActivity.class);
                        startActivity(intent);
                    }
                }).show();
    }

    public static String  getTime(){
        return mtime;
    }

}
