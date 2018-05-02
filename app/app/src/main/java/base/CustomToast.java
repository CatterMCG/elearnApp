package base;

import android.content.Context;
import android.os.Handler;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import com.mcg.myapplication.R;

import java.util.zip.Inflater;

/**
 * Created by 成刚 on 2016/3/21.
 */
public class CustomToast {
    private static Toast mToast;
    private static Handler mHandler = new Handler();
    private static Runnable r = new Runnable() {
        public void run() {
            mToast.cancel();
        }
    };

    public static void showToast(Context mContext, String text, int duration) {

        mHandler.removeCallbacks(r);
        if (mToast != null)
            mToast.setText(text);
        else
            mToast = Toast.makeText(mContext, text, Toast.LENGTH_SHORT);
        mHandler.postDelayed(r, duration);
        /*View view = LayoutInflater.from(mContext).inflate(R.layout.my_toast,null);
        mToast.setView(view);*/
        mToast.setGravity(Gravity.CENTER,0,0);
        mToast.show();
    }

    public static void showToast(Context mContext, int resId, int duration) {
        showToast(mContext, mContext.getResources().getString(resId), duration);
    }

}
