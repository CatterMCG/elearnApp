package base;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

/**
 * Created by 成刚 on 2016/4/10.
 */
public class MyDatabaseHelper extends SQLiteOpenHelper {
    private Context mContext;

    //创建数据结构选择题
    public static final String CREATE_COURSE_DATASTRUCTURE = "create table data_structure ("
            +"id integer primary key autoincrement,"
            +"hard integer,"
            +"title text,"
            +"A text,"
            +"B text,"
            +"C text,"
            +"D text,"
            +"answer text,"
            +"analysis text)";

    public MyDatabaseHelper(Context context,String name,SQLiteDatabase.CursorFactory factory,int version){
        super(context,name,factory,version);
        mContext = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_COURSE_DATASTRUCTURE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
