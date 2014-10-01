package com.neux.proj.insurance.utility;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import com.neux.proj.insurance.R;

/**
 * Created with IntelliJ IDEA.
 * User: titan
 * Date: 2014/1/14
 * Time: ¤U¤È 4:22
 * To change this template use File | Settings | File Templates.
 */
public class DialogUtils {
    public static void openDialog(Context context,String message) {
        try{
            new AlertDialog.Builder(context).setTitle(R.string.app_name)
                    .setMessage(message)
                    .setPositiveButton(android.R.string.ok, new AlertDialog.OnClickListener()
                    {
                        @Override
                        public void onClick(DialogInterface dialog, int which)
                        {
                            dialog.dismiss();
                        }
                    }).setCancelable(false).create().show();
        }catch(Exception e)
        {
            Log.e("DEBUG", "onJsAlert Error e = " + e.toString());
        }
    }
}
