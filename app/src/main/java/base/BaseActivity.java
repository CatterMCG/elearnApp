package base;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 成刚 on 2016/3/10.
 */
public class BaseActivity extends AppCompatActivity {

    private List<BaseActivity> list = new ArrayList<>();
    private long exittime = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK ){
            exit();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    public void exit(){
        if(System.currentTimeMillis()- exittime > 2000 ){
            Toast.makeText(getApplicationContext(),"再按一次退出程序",Toast.LENGTH_SHORT).show();
            exittime = System.currentTimeMillis();
        }else {
            finish();
        }
    }
}
