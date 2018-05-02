package fragment;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.SimpleExpandableListAdapter;

import com.mcg.myapplication.R;
import com.mcg.myapplication.SingleChooseSettingActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import base.Course;
import base.CustomToast;
import base.MyDatabaseHelper;

/**
 * Created by 成刚 on 2016/3/11.
 */
public class SelfTestFragment extends Fragment {

    private ExpandableListView epdlv;
    //创建一级条目容器
    private List<Map<String, String>> groups = new ArrayList<Map<String, String>>();
    //存放内容，以便显示在列表中
    private List<List<Map<String, String>>> childs = new ArrayList<List<Map<String, String>>>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_selftest, container, false);

        epdlv = (ExpandableListView) view.findViewById(R.id.epdlv_selftest);
        setListData();

        return view;
    }

    //设置列表内容
    public void setListData() {
        //设置院系
        Map<String, String> title_1 = new HashMap<String, String>();
        title_1.put("dept", "计算机学院");
        groups.add(title_1);

        //计科院设自测课程
        ArrayList<String> course = CourseFragment.getCourseList();
        List<Map<String, String>> childs_1 = new ArrayList<Map<String, String>>();

        for (int i = 0;i<course.size();i++){
            Map<String, String> title_content = new HashMap<String, String>();
            title_content.put("course",course.get(i).toString());
            childs_1.add(title_content);
        }
        childs.add(childs_1);

        //创建ExpendableListView的Adapter容器， 1.上下文 2.一级集合 3.一级样式文件 4. 一级条目键值
        //5.一级显示控件名 6. 二级集合 7. 二级样式 8.二级条目键值 9.二级显示控件名
        final SimpleExpandableListAdapter sela = new SimpleExpandableListAdapter(getActivity().getApplicationContext()
                , groups, R.layout.expendablelistview_groups, new String[]{"dept"}
                , new int[]{R.id.tv_selftest_groups}, childs, R.layout.expendablelistview_childs, new String[]{"course"}
                , new int[]{R.id.tv_selftest_childs});

        epdlv.setAdapter(sela);
        //对子列表内容按下进行监听
        epdlv.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                Intent intent = new Intent(getActivity().getApplicationContext(), SingleChooseSettingActivity.class);
                if (sela.getChild(groupPosition, childPosition).toString().replaceAll("\\s", "").equals("{course=数据结构}")) {
                    /**
                     * 1.从本地数据库取出数据，随机选出10个出现在选择界面
                     * 2.如果本地没有数据，访问服务器，下载相应的课程
                     * 3.存入本地数据库，并在做题界面显示出来
                     */
                    intent.putExtra("course_name", "数据结构");
                    startActivity(intent);
                } else {
                    intent.putExtra("course_name", "其他课程");
                    /*CustomToast.showToast(getActivity().getApplicationContext(), sela.getChild(groupPosition, childPosition).toString().replaceAll("\\s", "") + "haibuc", 1500);*/

                    CustomToast.showToast(getActivity().getApplicationContext(),"该课程暂时还未开启自测功能",1500);
                }
                return true;
            }
        });

        //对未添加course的学院进行监听
        epdlv.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                //点击的是其他的学院的时候，会提示提示没有自测功能，如果这个时候“计算机学院”是展开的话，就让它收起来
                if (groupPosition != 0) {
                    if (epdlv.isGroupExpanded(10)) {
                        epdlv.collapseGroup(10);
                    }
                    CustomToast.showToast(getActivity().getApplicationContext(), "该学院还暂时还无法进行自我测试，敬请期待！", 2000);
                    return true;
                } else return false;
            }
        });
    }


}
