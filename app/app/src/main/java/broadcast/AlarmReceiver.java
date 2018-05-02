package broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.mcg.myapplication.AlarmActivity;

/**
 * Created by 成刚 on 2016/4/3.
 */
public class AlarmReceiver extends BroadcastReceiver{
    @Override
    public void onReceive(Context context, Intent intent) {
        Intent i=new Intent(context, AlarmActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(i);
    }
}
