package base;

import android.app.ProgressDialog;
import android.content.Context;

/**
 * Created by 成刚 on 2016/4/17.
 */
public class MyProgressDialog {
    private ProgressDialog dialog;

    public void show(Context context,String message){
        dialog = new ProgressDialog(context);
        dialog.setMessage(message);
        dialog.setCancelable(false);
        dialog.show();
    }

    public void dismiss(){
        dialog.dismiss();
    }
}
