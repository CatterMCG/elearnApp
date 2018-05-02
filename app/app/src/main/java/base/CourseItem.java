package base;

import android.graphics.Bitmap;
import android.media.Image;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by 成刚 on 2016/3/12.
 */
public class CourseItem {

    private int courseImage;
    private String couserName;

    public CourseItem(int courseImage,String courseName){
        this.courseImage = courseImage;
        this.couserName = courseName;
    }

    public int getCourseImage(){
        return courseImage;
    }

    public String getCouserName(){
        return couserName;
    }

}
