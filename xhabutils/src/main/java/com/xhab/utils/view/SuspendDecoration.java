package com.xhab.utils.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.text.TextPaint;
import android.view.View;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import androidx.annotation.ColorInt;
import androidx.annotation.IntDef;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


/**
 * Created by DAB on 2016/12/29 13:25.
 * 自定义的RecyclerView的ItemDecoration,实现挤动悬浮的效果
 */

public abstract class SuspendDecoration extends RecyclerView.ItemDecoration {
    @IntDef({TitleGravity.TITLE_GRAVITY_CENTER, TitleGravity.TITLE_GRAVITY_LEFT, TitleGravity.TITLE_GRAVITY_RIGHT})
    @Retention(RetentionPolicy.SOURCE)
    public @interface TitleGravity {
        int TITLE_GRAVITY_CENTER = 0;
        int TITLE_GRAVITY_LEFT = 1;
        int TITLE_GRAVITY_RIGHT = 2;
    }

    private static final String TAG = "SuspendDecoration";
    private int offset = 3;//像数抵消
    private int mTitleHeight;//悬浮窗的高度
    private Paint mBackgroundPaint;//悬浮窗的画笔
    private TextPaint mTextPaint;//悬浮窗内容的画笔
    @TitleGravity
    private int mTitleGravity = TitleGravity.TITLE_GRAVITY_LEFT;//悬浮窗的内容显示位置(左,中,右)
    private float mTextOffsetX, mTextOffsetY;

    private RectF mRect;//悬浮窗的的矩形(动态改变的)
//    private RectF mRect;//悬浮窗的的矩形(动态改变的)

    public SuspendDecoration setTitleHeight(int titleHeight) {
        mTitleHeight = titleHeight;
        return this;
    }

    public SuspendDecoration setTitleSize(int titleSize) {
        mTextPaint.setTextSize(titleSize);
        return this;
    }

    public SuspendDecoration setTitleGravity(@TitleGravity int titleGravity) {
        mTitleGravity = titleGravity;
        return this;
    }

    /**
     * @param titleHeight  悬浮窗的的高度
     * @param titleSize    悬浮窗的字体大小
     * @param titleGravity 悬浮窗的显示位置
     */
    public SuspendDecoration(int titleHeight, int titleSize, @TitleGravity int titleGravity) {
        init();
        setTitleHeight(titleHeight);
        setTitleSize(titleSize);
        setTitleGravity(titleGravity);
    }

    /**
     * 进行一些默认参数设置
     *
     * @param context
     */
    public SuspendDecoration(Context context) {
        init();
        setTitleHeight(dp2px(context, 30));
        setTitleSize(dp2px(context, 14));
        setTitleGravity(TitleGravity.TITLE_GRAVITY_LEFT);
        setBackgroundColor(0xfff5f5f5);
        setTextColor(0xff999999);
        setTextOffsetX(dp2px(context, 16));
    }

    private void init() {
        mBackgroundPaint = new Paint();
        //设置悬浮栏中文本的画笔
        mTextPaint = new TextPaint();
        mTextPaint.setAntiAlias(true);
        mRect = new RectF();
    }

    public static int dp2px(Context context, int dpValue) {
        float density = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * density + 0.5f);
    }

    /**
     * 将sp转换为px
     */
    public static int sp2px(Context context, float spValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }

    /**
     * 设置字体的颜色
     *
     * @param color
     */
    public SuspendDecoration setTextColor(@ColorInt int color) {
        mTextPaint.setColor(color);
        return this;
    }

    /**
     * 设置文字在X轴的偏移
     *
     * @param textOffsetX
     */
    public SuspendDecoration setTextOffsetX(int textOffsetX) {
        mTextOffsetX = textOffsetX;
        return this;
    }

    /**
     * 设置文字在Y轴的偏移
     *
     * @param textOffsetY
     */
    public SuspendDecoration setTextOffsetY(int textOffsetY) {
        mTextOffsetY = textOffsetY;
        return this;
    }

    /**
     * 设置背景的颜色
     *
     * @param color
     */
    public SuspendDecoration setBackgroundColor(@ColorInt int color) {
        mBackgroundPaint.setColor(color);
        return this;
    }

    @Override
    public void onDraw(Canvas canvas, RecyclerView parent, RecyclerView.State state) {
        super.onDraw(canvas, parent, state);
        canvas.save();
        int firstVisibleItemPosition = getFirstVisibleItemPosition(parent);
        int lastVisibleItemPosition = getLastVisibleItemPosition(parent);
        for (int i = firstVisibleItemPosition; i <= lastVisibleItemPosition; i++) {
            View child = parent.getLayoutManager().findViewByPosition(i);
            if (child == null) return;
            //判断是否需要显示悬浮窗
            try {
                if (shouldShowTitle(i)) {
                    //悬浮窗的矩形边框
                    mRect.set(child.getLeft(), child.getTop() - mTitleHeight, child.getRight(), child.getBottom() - child.getHeight());
                    canvas.drawRect(mRect, mBackgroundPaint);
                    //悬浮窗的内容
                    try {
                        canvas.drawText(showTitle(i), getBaseLineX(mRect), getBaseLineY(mRect), mTextPaint);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        canvas.restore();
    }

    private int getLastVisibleItemPosition(RecyclerView parent) {
        int lastVisibleItemPosition = -10;
        RecyclerView.LayoutManager layoutManager = parent.getLayoutManager();
        if (layoutManager instanceof LinearLayoutManager) {
            LinearLayoutManager linearLayoutManager = (LinearLayoutManager) layoutManager;
            lastVisibleItemPosition = linearLayoutManager.findLastVisibleItemPosition();
        }
        if (lastVisibleItemPosition == -10) {
            throw new IllegalArgumentException("需要重写getLastVisibleItemPosition()方法,提供最后一个可见item的Position");
        }
        return lastVisibleItemPosition;
    }

    private int getFirstVisibleItemPosition(RecyclerView parent) {
        int firstVisibleItemPosition = -10;
        RecyclerView.LayoutManager layoutManager = parent.getLayoutManager();
        if (layoutManager instanceof LinearLayoutManager) {
            LinearLayoutManager linearLayoutManager = (LinearLayoutManager) layoutManager;
            firstVisibleItemPosition = linearLayoutManager.findFirstVisibleItemPosition();
        }
        if (firstVisibleItemPosition == -10) {
            throw new IllegalArgumentException("需要重写getFirstVisibleItemPosition()方法,提供第一个可见item的Position");
        }
        return firstVisibleItemPosition;
    }

    private int getFirstCompletelyVisibleItemPosition(RecyclerView parent) {
        int firstVisibleItemPosition = -10;
        RecyclerView.LayoutManager layoutManager = parent.getLayoutManager();
        if (layoutManager instanceof LinearLayoutManager) {
            LinearLayoutManager linearLayoutManager = (LinearLayoutManager) layoutManager;
            firstVisibleItemPosition = linearLayoutManager.findFirstCompletelyVisibleItemPosition();
        }
        if (firstVisibleItemPosition == -10) {
            throw new IllegalArgumentException("需要重写getFirstVisibleItemPosition()方法,提供第一个可见item的Position");
        }
        return firstVisibleItemPosition;
    }

    //是否需要显示悬浮窗
    private boolean shouldShowTitle(int position) throws Exception {
        if (position <= 0) return true;
        boolean sameGroup = isSameGroup(position - 1, position);
//        if (MainActivity.isDebug) Log.e(TAG, "shouldShowTitle: "+position + "***" + sameGroup);
        return !sameGroup;


//        return position >= 0 && (position == 0 || !isSameGroup(position - 1, position));
    }


    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        //拿到当前的position
        int pos = parent.getChildAdapterPosition(view);
        //判断是否需要给这个position预留显示悬浮窗的位置
        try {
            if (shouldShowTitle(pos)) {
                outRect.top = mTitleHeight - 3;
            } else {
                outRect.top = 0;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDrawOver(Canvas canvas, RecyclerView parent, RecyclerView.State state) {
        super.onDrawOver(canvas, parent, state);

        canvas.save();
        //初始化悬浮窗的高度
        float textY = mTitleHeight;
        int childCount = parent.getChildCount();
        if (childCount < 1) return;
        //通过获取布局管理器来找到当前显示的第一个position
//        LinearLayoutManager layoutManager = (LinearLayoutManager) parent.getLayoutManager();
        int firstVisibleItemPosition = getFirstVisibleItemPosition(parent);
        int lastVisibleItemPosition = getLastVisibleItemPosition(parent);
        int firstCompletelyVisibleItemPosition = getFirstCompletelyVisibleItemPosition(parent);
        for (int i = firstVisibleItemPosition; i < lastVisibleItemPosition; i++) {
            View child = parent.getLayoutManager().findViewByPosition(i);
            try {

                if (shouldShowTitle(i)) {
                    //判断当前悬浮窗是否需要被挤压,如果需要,就改变矩形的bottom
                    if (firstCompletelyVisibleItemPosition > i) {
                        continue;
                    }
                    int childTop = child.getTop();
                    if (mTitleHeight < childTop && childTop < mTitleHeight * 2) {
                        textY = childTop - mTitleHeight;
                    }
                    //不能依据当前的第一个来画悬浮
                    if (i == 0) continue;

                    mRect.set(0, textY - mTitleHeight, child.getRight(), textY);
                    canvas.drawRect(mRect, mBackgroundPaint);
                    try {
                        canvas.drawText(showTitle(firstVisibleItemPosition), getBaseLineX(mRect), getBaseLineY(mRect), mTextPaint);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    //画一个悬浮就结束循环
                    canvas.restore();
                    return;
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        //如果上面没画(就是没return),这里就补画一个
        int firstVisibleItemPosition1 = getFirstVisibleItemPosition(parent);
        View child = parent.getLayoutManager().findViewByPosition(firstVisibleItemPosition1);
        mRect.set(0, 0, child.getRight(), textY);
        if (textY <= offset) return;
        canvas.drawRect(mRect, mBackgroundPaint);
        //悬浮窗的内容
        try {
            canvas.drawText(showTitle(firstVisibleItemPosition1), getBaseLineX(mRect), getBaseLineY(mRect), mTextPaint);
        } catch (Exception e) {
            e.printStackTrace();
        }
        canvas.restore();
    }

    /**
     * 决定文字绘制在X轴左,中,右
     *
     * @param rect
     * @return
     */
    private Float getBaseLineX(RectF rect) {
        switch (mTitleGravity) {
            case TitleGravity.TITLE_GRAVITY_RIGHT:
                mTextPaint.setTextAlign(Paint.Align.RIGHT);
                return rect.right + mTextOffsetX;
            case TitleGravity.TITLE_GRAVITY_CENTER:
                mTextPaint.setTextAlign(Paint.Align.CENTER);
                return rect.centerX() - mTextOffsetX / 2;
            default:
                mTextPaint.setTextAlign(Paint.Align.LEFT);
                return rect.left + mTextOffsetX;
        }

    }

    /**
     * 文字显示在Y轴居中
     *
     * @param rect
     * @return
     */
    private Float getBaseLineY(RectF rect) {
        Paint.FontMetrics fontMetrics = mTextPaint.getFontMetrics();
        float top = fontMetrics.top;//为基线到字体上边框的距离,即上图中的top
        float bottom = fontMetrics.bottom;//为基线到字体下边框的距离,即上图中的bottom
        return (rect.centerY() - top / 2 - bottom / 2) + mTextOffsetY;
    }

    /**
     * 是否需要显示悬浮窗
     *
     * @param priorGroupId
     * @param nowGroupId
     * @return
     */
    public abstract boolean isSameGroup(int priorGroupId, int nowGroupId) throws Exception;

    /**
     * 悬浮窗的内容
     *
     * @param position
     * @return
     */
    public abstract String showTitle(int position) throws Exception;
}