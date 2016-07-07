package fragment;

import android.app.Fragment;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;

import com.mcg.myapplication.LearningActivity;
import com.mcg.myapplication.MainActivity;
import com.mcg.myapplication.R;
import com.mcg.myapplication.SearchActivity;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;

import adapter.SwipeAdapter;
import base.CourseItem;
import base.CustomToast;
import base.MyProgressDialog;
import cn.pedant.SweetAlert.SweetAlertDialog;
import widget.SwipeListView;

/**
 * Created by 成刚 on 2016/3/11.
 */
public class CourseFragment extends Fragment implements AdapterView.OnItemClickListener {
    private static ArrayList<CourseItem> itemList = new ArrayList<>();//显示的课程
    private static ArrayList<String> itemSelectedList = new ArrayList<>();//已经添加的
    private static ArrayList<String> AddList = new ArrayList<>();//新添加的课程

    private static MyProgressDialog myProgressDialog = new MyProgressDialog();

    private SwipeListView mlistView;
    private SwipeAdapter adapter;
    private TextView tv_search;
    private TextView tv_hint;
    private MainActivity mainActivity = new MainActivity();

    //设置新添加的list数据
    public static void setCourseList(ArrayList<String> addlist) {
        //将穿过来的新添加的课程给Addlist
        AddList = addlist;
    }

    //获得本地的bitmap
    public HashMap<String, Bitmap> getBitmaps() {
        HashMap<String, Bitmap> bitmaps = new HashMap<>();
        for (int i = 0; i < itemSelectedList.size(); i++) {
            Bitmap bitmap = null;
            InputStream is = null;
            String course_name = itemSelectedList.get(i).toString().trim();
            switch (course_name) {
                case "数据结构":
                    is = getResources().openRawResource(R.mipmap.course_img_data_structure);
                    bitmap = BitmapFactory.decodeStream(is);
                    break;
                case "操作系统":
                    is = getResources().openRawResource(R.mipmap.course_img_os);
                    bitmap = BitmapFactory.decodeStream(is);
                    break;
                case "计算机网络":
                    is = getResources().openRawResource(R.mipmap.course_img_network);
                    bitmap = BitmapFactory.decodeStream(is);
                    break;
                case "计算机组成原理":
                    is = getResources().openRawResource(R.mipmap.course_img_organization);
                    bitmap = BitmapFactory.decodeStream(is);
                    break;
                default://其他课程
                    is = getResources().openRawResource(R.mipmap.course_default_img);
                    bitmap = BitmapFactory.decodeStream(is);
            }
            bitmaps.put(course_name, bitmap);
        }
        return bitmaps;
    }

    @Override
    public void onResume() {
        super.onResume();
        itemSelectedList = mainActivity.getCourseListFromLocal();
        itemSelectedList.addAll(AddList);
        for (int i = 0; i < itemSelectedList.size(); i++) {
            itemList.add(new CourseItem(R.drawable.course_default_img, itemSelectedList.get(i)));
        }

        //当用户没有添加任何课程的时候，会显示提示
        if (itemList.size() == 0) {
            tv_hint.setVisibility(View.VISIBLE);
        } else tv_hint.setVisibility(View.GONE);

        AddList.clear();
        setAdapter();
    }

    public void setAdapter() {
        adapter = new SwipeAdapter(getActivity().getApplicationContext(), mlistView.getRightViewWidth(), itemList, getBitmaps(),
                new SwipeAdapter.IOnItemRightClickListener() {
                    @Override
                    public void onRightClick(View v, int position) {
                        // TODO Auto-generated method stub
                        ItemRemove(position);//删除元素
                    }
                });
        mlistView.setAdapter(adapter);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container
            , Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_course, container, false);
        tv_search = (TextView) view.findViewById(R.id.tv_search);
        tv_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity().getApplicationContext(), SearchActivity.class);
                intent.putStringArrayListExtra("itemSelectedList", itemSelectedList);
                getActivity().startActivityForResult(intent, 0);
            }
        });

        tv_hint = (TextView) view.findViewById(R.id.tv_course_hint);

        mlistView = (SwipeListView) view.findViewById(R.id.lv_course);
        setAdapter();
        mlistView.setItemsCanFocus(true);
        mlistView.setOnItemClickListener(this);
        return view;
    }

    //删除元素
    public void ItemRemove(int position) {
        final int index = position;

        //删除过程中也要检测一下是否 没有任何课程了，也需要提示一下
        if (itemList.size() == 0) {
            tv_hint.setVisibility(View.VISIBLE);
        } else tv_hint.setVisibility(View.GONE);

        //让用户确认是否要删除该课程
        new SweetAlertDialog(getActivity(),SweetAlertDialog.WARNING_TYPE)
                .setTitleText("删除").setContentText("确认要删除"+itemSelectedList.get(index)).setCancelText("取消").setConfirmText("确定")
        .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
            @Override
            public void onClick(SweetAlertDialog sweetAlertDialog) {
                mlistView.hiddeRightAfterDelete();
                sweetAlertDialog.dismiss();
            }
        }).setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
            @Override
            public void onClick(SweetAlertDialog sweetAlertDialog) {
                itemList.remove(index);
                itemSelectedList.remove(index);
                mlistView.hiddeRightAfterDelete();
                adapter.notifyDataSetChanged();
                sweetAlertDialog.dismiss();
            }
        }).show();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (itemList.get(position).getCouserName().trim().equals("数据结构")) {
            myProgressDialog.show(getActivity(),"正在加载...");
            Intent intent = new Intent(getActivity().getApplicationContext(), LearningActivity.class);
            intent.putExtra("course_name",itemList.get(position).getCouserName().trim());
            startActivity(intent);
        } else CustomToast.showToast(getActivity().getApplicationContext()
                , itemList.get(position).getCouserName() + "还没有课程学习功能哦，请期待", 1500);
    }

    public static void ProgressDialogDismiss(){
        myProgressDialog.dismiss();
    }

    //给Mainactivity传递courselist用于退出的时候进行保存
    public  ArrayList<String> getCourseListFragment() {
        itemList.clear();
        return itemSelectedList;
    }
    //给自测fragment传递数据
    public  static ArrayList<String> getCourseList() {
        return itemSelectedList;
    }

}
