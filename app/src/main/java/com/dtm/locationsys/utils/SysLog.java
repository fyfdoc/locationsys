package com.dtm.locationsys.utils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import android.util.Log;
/**
 * app日志类
 */

public class SysLog {

    // 日志总开关
    private static Boolean LOG_SWITCH = true;
    // 日志写入文件开关
    private static Boolean LOG_WRITE_TO_FILE = false;
    // sd卡中日志文件的最多保存天数
    private static int SDCARD_LOG_FILE_SAVE_DAYS = 30;
    // 日志文件名称
    private static String LOGFILENAME = "_LocLog.txt";
    // 日志文件存储路径
    private static String LOG_PATH_SDCARD_DIR = "/sdcard/111Log";
    // 日志时间输出格式
    private static SimpleDateFormat LogSdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    // 日志文件名称格式
    private static SimpleDateFormat logfile = new SimpleDateFormat("yyyy-MM-dd");

    public static void w(String tag, String text) {
        log(tag, text, ' ');
    }

    public static void e(String tag, String text) {
        log(tag, text, 'e');
    }

    public static void d(String tag, String text) {
        log(tag, text, 'd');
    }

    public static void i(String tag, String text) {
        log(tag, text, 'i');
    }

    public static void v(String tag, String text) {
        log(tag, text, 'v');
    }

    /**
     * 根据tag, msg和等级，输出日志
     *
     * @param tagStr
     * @param msgStr
     * @param level
     * @return void
     * @since v 1.0
     */
    private static void log(String tagStr, String msgStr, char level) {
        if (LOG_SWITCH) {
            String[] contents = wrapperContent(tagStr, msgStr);
            String tag = contents[0];
            String msg = contents[1];
            String headString = contents[2];

            if ('i' == level) {
                Log.i(tag, headString + msg);
            } else if ('e' == level) {
                Log.e(tag, headString + msg);
            } else if ('w' == level) {
                Log.w(tag, headString + msg);
            } else if ('d' == level) {
                Log.d(tag, headString + msg);
            } else {
                Log.v(tag, headString + msg);
            }

            if (LOG_WRITE_TO_FILE) {
                writeLogtoFile(String.valueOf(level), tag, headString + msg);
            }
        }
    }

    /**
     * 打开日志文件并写入日志
     *
     * @return
     * **/
    private static void writeLogtoFile(String mylogtype, String tag, String text) {
        try {
            Date nowTime = new Date();
            String needWriteFile = logfile.format(nowTime);
            String needWriteMessage = LogSdf.format(nowTime) + " " + mylogtype + " " + tag + " " + text;

            File file = new File(LOG_PATH_SDCARD_DIR, needWriteFile + LOGFILENAME);

            if (!file.exists()) {
                // Make sure we have a path to the file
                file.getParentFile().mkdirs();
                file.createNewFile();
            }

            // 后面这个参数代表是不是要接上文件中原来的数据，不进行覆盖
            FileWriter filerWriter = new FileWriter(file, true);
            BufferedWriter bufWriter = new BufferedWriter(filerWriter);
            bufWriter.write(needWriteMessage);
            bufWriter.newLine();
            bufWriter.close();
            filerWriter.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 删除制定的日志文件
     * */
    public static void delFile() {
        String needDelFile = logfile.format(getDateBefore());
        File file = new File(LOG_PATH_SDCARD_DIR, needDelFile + LOGFILENAME);
        if (file.exists()) {
            file.delete();
        }
    }

    /**
     * 得到现在时间前的几天日期，用来得到需要删除的日志文件名
     * */
    private static Date getDateBefore() {
        Date nowTime = new Date();
        Calendar now = Calendar.getInstance();
        now.setTime(nowTime);
        now.set(Calendar.DATE, now.get(Calendar.DATE) - SDCARD_LOG_FILE_SAVE_DAYS);
        return now.getTime();
    }

    /**
     * 格式化日志信息
     * @param tag
     * @param objects
     * @return
     */
    private static String[] wrapperContent(String tag, Object... objects) {

        StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
        StackTraceElement targetElement = stackTrace[5];
        String className = targetElement.getClassName();
        String[] classNameInfo = className.split("\\.");
        if (classNameInfo.length > 0) {
            className = classNameInfo[classNameInfo.length - 1] + ".java";
        }
        String methodName = targetElement.getMethodName();
        int lineNumber = targetElement.getLineNumber();
        if (lineNumber < 0) {
            lineNumber = 0;
        }
        String methodNameShort = methodName.substring(0, 1).toUpperCase() + methodName.substring(1);
        String msg = (objects == null) ? "Log with null object" : getObjectsString(objects);
        String headString = "[(" + className + ":" + lineNumber + ")#" + methodNameShort + " ] ";
        return new String[] { tag, msg, headString };
    }

    private static String getObjectsString(Object... objects) {

        if (objects.length > 1) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("\n");
            for (int i = 0; i < objects.length; i++) {
                Object object = objects[i];
                if (object == null) {
                    stringBuilder.append("param").append("[").append(i).append("]").append(" = ").append("null").append("\n");
                }
                else {
                    stringBuilder.append("param").append("[").append(i).append("]").append(" = ").append(object.toString()).append("\n");
                }
            }
            return stringBuilder.toString();
        }
        else {
            Object object = objects[0];
            return object == null ? "null" : object.toString();
        }
    }

}
