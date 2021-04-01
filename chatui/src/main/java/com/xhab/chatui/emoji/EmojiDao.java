package com.xhab.chatui.emoji;

import android.app.Application;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.xhab.chatui.R;
import com.xhab.chatui.bean.EmojiBean;
import com.xhab.chatui.utils.FileUtils;
import com.xhab.chatui.voiceCalls.JuphoonUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;


/**
 * describe: 表情数据库操作
 * author: Went_Gone
 * create on: 2016/10/27
 */
public class EmojiDao {
    private static final String TAG = "EmojiDao";
    public static String FILE_ROOT_PATH = "";
    private static String path;
    private static EmojiDao dao;
    private static boolean isInit;
    private static Application application;

    public static EmojiDao getInstance() {
        if (dao == null) {
            synchronized (EmojiDao.class) {
                if (dao == null) {
                    dao = new EmojiDao();
                }
            }
        }
        return dao;
    }

    private EmojiDao() {
        if (!isInit) {
            throw new NullPointerException("EmojiDao 未初始化!,调用init()方法初始化");
        }
    }


    public static void init(Application application1) {
        isInit = true;
        application = application1;
        initEmojiDao(application);
        JuphoonUtils.get().initialize(application, "2f28a4e830fb84d0da705096");
    }

    private static String initEmojiDao(Application application) {
        path = FileUtils.copyFilesFromRaw(application, R.raw.emoji, "emoji.db", FILE_ROOT_PATH + "/databases/");
        return path;
//        try {
//
////            path = CopySqliteFileFromRawToDatabases("emoji.db", application);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
    }

    private String copyEmojiDao(Application application) {
        path = FileUtils.copyFilesFromRaw(application, R.raw.emoji, "emoji.db", FILE_ROOT_PATH + "/databases/");
        return path;
    }

    public List<EmojiBean> getEmojiBean() {
        List<EmojiBean> emojiBeanList = new ArrayList<EmojiBean>();
        File file = new File(path);
        if (!file.exists()) {
            path = copyEmojiDao(application);
        }
        SQLiteDatabase db = SQLiteDatabase.openDatabase(path, null, SQLiteDatabase.OPEN_READONLY);
        Cursor cursor = db.query("emoji", new String[]{"unicodeInt", "_id"}, null, null, null, null, null);
        while (cursor.moveToNext()) {
            EmojiBean bean = new EmojiBean();
            int unicodeInt = cursor.getInt(0);
            int id = cursor.getInt(1);
            bean.setUnicodeInt(unicodeInt);
            bean.setId(id);
            emojiBeanList.add(bean);
        }
        return emojiBeanList;
    }

}
