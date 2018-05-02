package com.mcg.myapplication;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SearchView;
import android.widget.TextView;

import java.text.Collator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import adapter.SearchCourseItemAdapter;
import base.CustomToast;
import base.SearchCourseItem;

/**
 * Created by 成刚 on 2016/3/12.
 */
public class SearchActivity extends Activity implements SearchView.OnQueryTextListener, AdapterView.OnItemClickListener {

    private RelativeLayout rl_back;
    private SearchCourseItemAdapter adapter;
    private SearchView searchView;
    private ListView listView;
    private List<SearchCourseItem> searchCourseItemList;
    //从fragment里面穿过来的课程list用于判断有没有选择之前选到的课程
    private static ArrayList<String> itemSelectedList;
    //用于新添加的课程
    private ArrayList<String> AddList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        getItemSelectedList();
        initSearchCourses();//初始化课程数据
        initView();
    }

    public void getItemSelectedList() {
        Intent intent = getIntent();
        itemSelectedList = new ArrayList<>(intent.getStringArrayListExtra("itemSelectedList"));
    }

    public void initView() {

        rl_back = (RelativeLayout) findViewById(R.id.rl_search_back);
        rl_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.putStringArrayListExtra("AddList", AddList);
                setResult(RESULT_OK, intent);
                finish();
            }
        });

        searchView = (SearchView) findViewById(R.id.search_view);
        searchView.setFocusable(false);
        searchView.setOnQueryTextListener(this);
        searchView.setSubmitButtonEnabled(false);
        //修改search里面的字体大小和位置
        int id = searchView.getContext().getResources().getIdentifier("android:id/search_src_text", null, null);
        TextView textView = (TextView) searchView.findViewById(id);
        textView.setTextSize(14);
        android.widget.LinearLayout.LayoutParams layoutParams = (android.widget.LinearLayout.LayoutParams) textView.getLayoutParams();
        layoutParams.bottomMargin = -3;
        layoutParams.leftMargin = -10;
        textView.setLayoutParams(layoutParams);

        listView = (ListView) findViewById(R.id.lv_searchcourse);
        listView.setAdapter(adapter);
        listView.setItemsCanFocus(true);
        listView.setOnItemClickListener(this);
    }

    public void initSearchCourses() {
        searchCourseItemList = new ArrayList<>();
        searchCourseItemList.add(new SearchCourseItem("数据结构"));
        searchCourseItemList.add(new SearchCourseItem("计算机组成原理"));
        searchCourseItemList.add(new SearchCourseItem("计算机网络"));
        searchCourseItemList.add(new SearchCourseItem("操作系统"));
        searchCourseItemList.add(new SearchCourseItem("软件工程"));
        searchCourseItemList.add(new SearchCourseItem("高等数学"));
        searchCourseItemList.add(new SearchCourseItem("离散数学"));
        searchCourseItemList.add(new SearchCourseItem("Linux"));
        searchCourseItemList.add(new SearchCourseItem("人工智能与导论"));
        searchCourseItemList.add(new SearchCourseItem("嵌入式基础"));
        searchCourseItemList.add(new SearchCourseItem("计算机体系结构"));
        searchCourseItemList.add(new SearchCourseItem("算法设计"));
        searchCourseItemList.add(new SearchCourseItem("编译原理"));
        searchCourseItemList.add(new SearchCourseItem("windows编程"));
        searchCourseItemList.add(new SearchCourseItem("数据库原理"));
        searchCourseItemList.add(new SearchCourseItem("数据挖掘"));
        searchCourseItemList.add(new SearchCourseItem("通信原理"));
        //对所有的课程进行排序
        List<String> strings = new ArrayList<>();
        for (SearchCourseItem searchCourseItem : searchCourseItemList) {
            strings.add(searchCourseItem.getItemCourse());
        }
        Comparator comparator = Collator.getInstance(java.util.Locale.CHINA);
        Collections.sort(strings, comparator);
        searchCourseItemList.clear();

        for (String s : strings) {
            searchCourseItemList.add(new SearchCourseItem(s));
        }

        adapter = new SearchCourseItemAdapter(SearchActivity.this,
                R.layout.item_searchcourse, searchCourseItemList);
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        List obj = searchItem(newText);
        updateLayout(obj);
        return false;
    }

    public void updateLayout(List<SearchCourseItem> obj) {
        adapter = new SearchCourseItemAdapter(SearchActivity.this, R.layout.item_searchcourse, obj);
        listView.setAdapter(adapter);
    }

    public List searchItem(String name) {
        List<SearchCourseItem> mSearchList = new ArrayList<>();
        for (int i = 0; i < searchCourseItemList.size(); i++) {
            int index = searchCourseItemList.get(i).getItemCourse().indexOf(name);
            // 存在匹配的数据
            if (index != -1) {
                mSearchList.add(new SearchCourseItem(searchCourseItemList.get(i).getItemCourse()));
            }
        }
        return mSearchList;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Boolean flag = true;
        TextView tv_course = (TextView) view.findViewById(R.id.tv_search_item);
        for (int i = 0; i < itemSelectedList.size(); i++) {
            if (tv_course.getText().toString().equals(itemSelectedList.get(i).trim())) {
                flag = false;
                break;
            }
        }
        if (flag) {
            AddList.add(tv_course.getText().toString());
            itemSelectedList.add(tv_course.getText().toString());
            CustomToast.showToast(getBaseContext(), "成功添加" + tv_course.getText().toString(), 1500);
        } else {
            CustomToast.showToast(getBaseContext(), "已添加过", 1500);
        }
    }

    //按返回键与按“后退”键的效果是一样的
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent();
        intent.putStringArrayListExtra("AddList", AddList);
        setResult(RESULT_OK, intent);
        finish();
    }
}
