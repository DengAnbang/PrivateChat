package com.abxh.chatui;

import com.abxh.chatui.emoji.EmojiDao;
import com.abxh.chatui.utils.GlideUtils;

/**
 * Created by dab on 2021/3/30 19:25
 */
public class ChatUi {

    public static void init(String API_HOST, String fileRootPath) {
        GlideUtils.KEY_API_HOST = API_HOST;
        System.out.println();
        EmojiDao.FILE_ROOT_PATH = fileRootPath;
    }
}
