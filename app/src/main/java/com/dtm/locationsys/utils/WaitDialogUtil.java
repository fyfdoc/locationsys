package com.dtm.locationsys.utils;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.dtm.locationsys.R;

/**
 * 等待窗口的工具类，不能按Back键取消
 * 1 调用showWaitDialog(),显示等待窗口，并提示文字
 * 2 updateWaitDialogText(),更新窗口文字
 * 3 showOrUpdateWaitDialog(),当不确定窗口是不是已经打开时
 * 4 dismissWaitDialog() 关闭等待窗口
 * 5 isShowing() 判断窗口是否正在显示
 */

public class WaitDialogUtil {
    public static AlertDialog waitDialog = null;
    public static TextView waitDialogPopInfo;

    public static void showWaitDialog(Context ctx, String contentStr)
    {
        try
        {
            if (waitDialog == null) {
                waitDialog = new AlertDialog.Builder(ctx).create();
            }else if (waitDialog.isShowing()) {
                waitDialog.dismiss();
            }

            LayoutInflater inflater = LayoutInflater.from(ctx);
            View backupExpandHeader = inflater.inflate(R.layout.wait_dialog, null);
            waitDialogPopInfo = (TextView) backupExpandHeader.findViewById(R.id.popDialogInfo);
            waitDialogPopInfo.setText(contentStr);

            waitDialog.setCancelable(true);
            waitDialog.show();
            waitDialog.setContentView(backupExpandHeader);

        } catch (Exception e) {
            SysLog.e("WaitDiaLogUtil", e.getMessage());
        }
    }

    public static void showOrUpdateWaitDialog(Context ctx,String text){
        if (waitDialog == null)
            showWaitDialog(ctx, text);
        else{
            updateWaitDialogText(text);
        }
    }

    public static void updateWaitDialogText(String text){
        if ((waitDialog != null) && waitDialog.isShowing())
        {
            waitDialogPopInfo.setText(text);
        }
    }

    public static void dismissWaitDialog()
    {
        try
        {
            if ((waitDialog != null) && waitDialog.isShowing())
            {
                waitDialog.dismiss();
                waitDialog = null;
            }
        } catch (Exception e)
        {
           SysLog.e("WaitDiaLogUtil", e.getMessage());
        }
    }

    public static boolean isShowing(){
        if ((waitDialog != null) && waitDialog.isShowing())
        {
            return true;
        }
        return false;
    }
}
