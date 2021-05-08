package com.xhab.chatui.emoji;


import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.xhab.chatui.R;
import com.xhab.chatui.bean.EmojiBean;

import java.util.List;

public class EmojiAdapter extends BaseQuickAdapter<EmojiBean, BaseViewHolder> {


    public EmojiAdapter(@Nullable List<EmojiBean> data, int index, int pageSize) {
         super(R.layout.item_emoji,  data);
     }

    @Override
    protected void convert(BaseViewHolder helper, EmojiBean item) {
        //判断是否为最后一个item
        if (item.getId()==0) {
             helper.getView(R.id.et_emoji ).setBackgroundResource(R.mipmap.rc_icon_emoji_delete);
        } else {
             helper.setText(R.id.et_emoji,item.getUnicodeInt() );
        }



    }


}
