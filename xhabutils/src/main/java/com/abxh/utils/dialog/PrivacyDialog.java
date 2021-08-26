package com.abxh.utils.dialog;

import android.app.Dialog;
import android.content.Context;

import com.abxh.utils.R;

/**
 * 用户协议的弹窗
 */
public class PrivacyDialog extends Dialog {

    public PrivacyDialog(Context context) {
        super(context, R.style.PrivacyThemeDialog);

        setContentView(R.layout.dialog_privacy);

        setCancelable(false);
        setCanceledOnTouchOutside(false);
    }
}
