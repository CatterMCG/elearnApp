package adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.mcg.myapplication.R;

import java.util.List;

import base.UserTestBean;

/**
 * Created by 成刚 on 2016/4/18.
 */
public class UserTestInfoAdapter extends ArrayAdapter<UserTestBean> {
    private List<UserTestBean> mList;
    private Context mContext;
    private int resourseId;

    public UserTestInfoAdapter(Context context, int resource, List<UserTestBean> objects) {
        super(context, resource, objects);
        mContext = context;
        mList = objects;
        resourseId = resource;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
       /* ViewHolder item;
        UserTestBean userTestBean = getItem(position);
        if(convertView == null){
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_test_info,parent,false);
            item = new ViewHolder();
            item.course = (TextView)convertView.findViewById(R.id.tv_test_info_course);
            item.time = (TextView)convertView.findViewById(R.id.tv_test_info_time);
            item.hard = (TextView)convertView.findViewById(R.id.tv_test_info_hard);
            item.scores = (TextView)convertView.findViewById(R.id.tv_test_info_scores);
        }else {
            item = (ViewHolder)convertView.getTag();
        }
        item.course.setText(userTestBean.getCourse());
        item.time.setText(userTestBean.getTime());
        item.hard.setText(userTestBean.getHard()+"");
        item.scores.setText(userTestBean.getScores());*/
        UserTestBean userTestBean = getItem(position);
        View view = LayoutInflater.from(mContext).inflate(resourseId,null);

        TextView tv_course = (TextView)view.findViewById(R.id.tv_test_info_course);
        TextView tv_time = (TextView)view.findViewById(R.id.tv_test_info_time);
        TextView tv_hard = (TextView)view.findViewById(R.id.tv_test_info_hard);
        TextView tv_scores = (TextView)view.findViewById(R.id.tv_test_info_scores);

        tv_course.setText("课程：" + userTestBean.getCourse());
        tv_time.setText("时间："+userTestBean.getTime());
        tv_hard.setText("难度："+userTestBean.getHard());
        tv_scores.setText("正确率："+userTestBean.getScores());
        return view;
    }

    /*private class ViewHolder{
        TextView course;
        TextView time;
        TextView hard;
        TextView scores;
    }*/
}
