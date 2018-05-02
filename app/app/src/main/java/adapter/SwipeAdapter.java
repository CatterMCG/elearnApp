package adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mcg.myapplication.MainActivity;
import com.mcg.myapplication.R;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import base.CourseItem;

/**
 * Created by 成刚 on 2016/3/28.
 */
public class SwipeAdapter extends BaseAdapter{
    /**
     * 上下文对象
     */
    private Context mContext = null;

    /**
     *
     */
    private int mRightWidth = 0;

    /**
     * 单击事件监听器
     */
    private IOnItemRightClickListener mListener = null;

    public interface IOnItemRightClickListener {
        void onRightClick(View v, int position);
    }

    /**
     * @param
     */
    //获取图片
    private HashMap<String,Bitmap> mbitmaps;
    //数据源与数据适配器进行关联
    private ArrayList<CourseItem> mlist = new ArrayList<>();

    public SwipeAdapter(Context ctx, int rightWidth,ArrayList<CourseItem> list,HashMap<String,Bitmap> bitmap ,IOnItemRightClickListener l) {
        mContext = ctx;
        mRightWidth = rightWidth;
        mListener = l;
        mlist = list;
        mbitmaps = bitmap;
    }

    @Override
    public int getCount() {
        return mlist.size();
    }

    @Override
    public CourseItem getItem(int position) {
        return mlist.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder item;
        CourseItem courseItem = getItem(position);
        final int thisPosition = position;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_course_swipe, parent, false);
            item = new ViewHolder();
            item.item_left = (RelativeLayout)convertView.findViewById(R.id.item_left);
            item.item_right = (RelativeLayout)convertView.findViewById(R.id.item_right);
            item.item_left_imgv = (ImageView)convertView.findViewById(R.id.item_left_imgv);
            item.item_left_txt = (TextView)convertView.findViewById(R.id.item_left_txt);
            item.item_right_txt = (TextView)convertView.findViewById(R.id.item_right_txt);
            convertView.setTag(item);
        } else {// 有直接获得ViewHolder
            item = (ViewHolder)convertView.getTag();
        }

        LinearLayout.LayoutParams lp1 = new LayoutParams(LayoutParams.MATCH_PARENT,
                LayoutParams.MATCH_PARENT);
        item.item_left.setLayoutParams(lp1);
        LinearLayout.LayoutParams lp2 = new LayoutParams(mRightWidth, LayoutParams.MATCH_PARENT);
        item.item_right.setLayoutParams(lp2);
        item.item_left_imgv.setImageBitmap(makeRoundCorner(mbitmaps.get(courseItem.getCouserName().trim())));
        item.item_left_txt.setText(courseItem.getCouserName());
        item.item_right_txt.setText("删除");
        item.item_right.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null) {
                    mListener.onRightClick(v, thisPosition);
                }
            }
        });
        return convertView;
    }

    private class ViewHolder {
        RelativeLayout item_left;
        RelativeLayout item_right;
        TextView item_left_txt;
        TextView item_right_txt;
        ImageView item_left_imgv;
    }

    //设置圆形图片
    public static Bitmap makeRoundCorner(Bitmap bitmap)
    {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        int left = 0, top = 0, right = width, bottom = height;
        float roundPx = height/2;
        if (width > height) {
            left = (width - height)/2;
            top = 0;
            right = left + height;
            bottom = height;
        } else if (height > width) {
            left = 0;
            top = (height - width)/2;
            right = width;
            bottom = top + width;
            roundPx = width/2;
        }

        Bitmap output = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas;
        canvas = new Canvas(output);
        int color = 0xff424242;
        Paint paint = new Paint();
        Rect rect = new Rect(left, top, right, bottom);
        RectF rectF = new RectF(rect);

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);
        return output;
    }

}
