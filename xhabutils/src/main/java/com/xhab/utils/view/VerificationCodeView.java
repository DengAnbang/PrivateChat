package com.xhab.utils.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xhab.utils.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dab on 2021/3/25 20:05
 */
public class VerificationCodeView extends RelativeLayout {
    private Context context;
    private TextView tv_code;
    private View lineView;
    private EditText mEtCode;
    private List<String> codes = new ArrayList<>();
    private int textColor;
    private int codeNumber;
    private float textSize;
    private boolean showPlaintext;
    private LinearLayout ll_content;
    private int lineColorDefault;
    private int lineColorFocus;

    public EditText getEtCode() {
        return mEtCode;
    }

    public VerificationCodeView(Context context) {
        this(context, null);
    }

    public VerificationCodeView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public VerificationCodeView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        loadView(attrs);
    }


    private void loadView(AttributeSet attrs) {
        View view = LayoutInflater.from(context).inflate(R.layout.verification_code, this);
        ll_content = view.findViewById(R.id.ll_code_content);
        mEtCode = view.findViewById(R.id.et_code);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.VerificationCodeView);
        textColor = typedArray.getColor(R.styleable.VerificationCodeView_code_text_color, getResources().getColor(R.color.just_color_000000));
        lineColorDefault = typedArray.getColor(R.styleable.VerificationCodeView_line_color_default, getResources().getColor(R.color.just_color_dedede));
        lineColorFocus = typedArray.getColor(R.styleable.VerificationCodeView_line_color_focus, getResources().getColor(R.color.just_color_19b595));
        codeNumber = typedArray.getInt(R.styleable.VerificationCodeView_code_number, 6);
        textSize = typedArray.getDimension(R.styleable.VerificationCodeView_code_text_size, getResources().getDimension(R.dimen.text_20));
        showPlaintext = typedArray.getBoolean(R.styleable.VerificationCodeView_show_plaintext, true);
        typedArray.recycle();
        initEvent();
        initView();
    }

    private void initView() {
        for (int i = 0; i < codeNumber; i++) {
            LinearLayout.LayoutParams layout_param = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1.0f);
            View item_view = LayoutInflater.from(context).inflate(R.layout.verifation_code_item, null);
            tv_code = item_view.findViewById(R.id.tv_code);
            tv_code.setTextColor(textColor);
            tv_code.setTextSize(textSize);
            lineView = item_view.findViewById(R.id.view);
            lineView.setBackgroundColor(lineColorDefault);
            ll_content.addView(item_view, layout_param);
        }

        mEtCode.setFocusable(true);
        mEtCode.setFocusableInTouchMode(true);
        mEtCode.requestFocus();
        showKeyboard(mEtCode);
    }

    private void initEvent() {
        mEtCode.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable != null && editable.length() > 0) {
                    mEtCode.setText("");
                    if (codes.size() < codeNumber) {
                        codes.add(editable.toString());
                        showCode(editable.toString());
                    }
                }
            }
        });

        // 监听验证码删除按键
        mEtCode.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int keyCode, KeyEvent keyEvent) {
                if (keyCode == KeyEvent.KEYCODE_DEL && keyEvent.getAction() == KeyEvent.ACTION_DOWN && codes.size() > 0) {
                    if (codes.isEmpty()) return false;
                    removeCode(codes.size() - 1);
                    codes.remove(codes.size() - 1);
                    return true;
                }
                return false;
            }
        });
    }


    /**
     * 显示输入的验证码
     */
    private void showCode(String code) {
        LinearLayout child = (LinearLayout) ll_content.getChildAt(codes.size() - 1);
        TextView tvCode = (TextView) child.getChildAt(0);
        View lineView = child.getChildAt(1);
        if (!showPlaintext) {
            code = "*";
        }
        tvCode.setText(code);
        lineView.setBackgroundColor(lineColorFocus);
        callBack();//回调
    }


    private void removeCode(int code) {
        LinearLayout child = (LinearLayout) ll_content.getChildAt(code);
        TextView tvCode = (TextView) child.getChildAt(0);
        View lineView = child.getChildAt(1);
        tvCode.setText("");
        lineView.setBackgroundColor(lineColorDefault);
    }


    /**
     * 回调
     */
    private void callBack() {
        if (onInputListener == null) {
            return;
        }
        if (codes.size() == codeNumber) {
            onInputListener.onSucess(getPhoneCode());
        } else {
            onInputListener.onInput();
        }
    }

    //定义回调
    public interface OnInputListener {
        void onSucess(String code);

        void onInput();
    }

    private OnInputListener onInputListener;

    public void setOnInputListener(OnInputListener onInputListener) {
        this.onInputListener = onInputListener;
    }

    /**
     * 获得手机号验证码
     *
     * @return 验证码
     */
    public String getPhoneCode() {
        StringBuilder sb = new StringBuilder();
        for (String code : codes) {
            sb.append(code);
        }
        return sb.toString();
    }

    private void showKeyboard(EditText editText) {
        InputMethodManager inputMethodManager = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.showSoftInput(editText, InputMethodManager.RESULT_SHOWN);
        inputMethodManager.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
    }
}
