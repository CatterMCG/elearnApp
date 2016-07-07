package adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.mcg.myapplication.R;

import java.util.List;

import base.SearchCourseItem;

/**
 * Created by 成刚 on 2016/3/12.
 */
public class SearchCourseItemAdapter extends ArrayAdapter<SearchCourseItem> {
    private int resourceId;

    public SearchCourseItemAdapter(Context context, int textViewResource, List<SearchCourseItem> objects) {
        super(context, textViewResource, objects);
        resourceId = textViewResource;
    }

    @Override
    public View getView(int position,View convertView,ViewGroup parent){
        SearchCourseItem searchCourseItem = getItem(position);
        View view = LayoutInflater.from(getContext()).inflate(resourceId, null);
        TextView tv_search_item = (TextView) view.findViewById(R.id.tv_search_item);
        tv_search_item.setText(searchCourseItem.getItemCourse());
        return view;
    }
}




