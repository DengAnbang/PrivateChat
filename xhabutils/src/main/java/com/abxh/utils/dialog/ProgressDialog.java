package com.abxh.utils.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.abxh.utils.R;
import com.abxh.utils.utils.RxUtils;

/**
 * 自定义进度框
 *
 * @author Chony
 * @time 2017/2/9 9:add_file
 */

/**
 *
 */
public class ProgressDialog extends Dialog {

    TextView mTvProgress;

    public ProgressDialog(Context context) {
        super(context, R.style.dlg);
        setContentView(R.layout.dialog_update_progress);

        //WindowManager.LayoutParams params = getWindow().getAttributes();
        //params.width = (int) AppUtil.getPixelsFromDp(context, 80);
        //getWindow().setAttributes(params);
        setCanceledOnTouchOutside(false);

        mTvProgress = (TextView) getWindow().findViewById(R.id.tv_progress);
    }

    public void setProgress(final String progress) {
        RxUtils.runOnUiThread(() -> {
            if (mTvProgress.getVisibility() != View.VISIBLE) {
                mTvProgress.setVisibility(View.VISIBLE);
            }
            mTvProgress.setText(progress);
        });

    }

}
