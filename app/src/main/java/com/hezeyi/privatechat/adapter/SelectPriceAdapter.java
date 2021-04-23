package com.hezeyi.privatechat.adapter;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hezeyi.privatechat.R;
import com.hezeyi.privatechat.bean.SelectPriceBean;
import com.xhab.utils.inteface.OnItemClickListener;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Created by dab on 2021/4/13 17:21
 */
public class SelectPriceAdapter extends RecyclerView.Adapter<SelectPriceAdapter.ViewHolder> {
    private List<SelectPriceBean> mSelectPriceBeans;
    private SelectPriceBean mSelectPriceBean;

    public SelectPriceBean getSelectPriceBean() {
        return mSelectPriceBean;
    }

    public void clear() {
        mSelectPriceBean = null;
    }

    public void setSelectPriceBeans(List<SelectPriceBean> selectPriceBeans) {
        mSelectPriceBeans = selectPriceBeans;
        Collections.sort(selectPriceBeans);
        notifyDataSetChanged();
    }

    private OnItemClickListener<SelectPriceBean> mOnItemClickListener;

    public void setOnItemClickListener(OnItemClickListener<SelectPriceBean> onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }

    @NonNull
    @Override
    public SelectPriceAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_select_price, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull SelectPriceAdapter.ViewHolder holder, int position) {

        SelectPriceBean selectPriceBean = mSelectPriceBeans.get(position);
        selectPriceBean.setPosition(position);

        holder.day.setText(selectPriceBean.getDay() + "天");
        holder.money.setText(selectPriceBean.getMoney() + "元");
        if (TextUtils.isEmpty(selectPriceBean.getGiving_day()) || selectPriceBean.getGiving_day().equals("0")) {
            holder.giving_day.setVisibility(View.GONE);
        } else {
            holder.giving_day.setVisibility(View.VISIBLE);
            holder.giving_day.setText("赠送" + selectPriceBean.getGiving_day() + "天");
        }
        if (Objects.equals(selectPriceBean.getId(), "1")) {
            selectPriceBean.setGiving_day("0");
            holder.giving_day.setVisibility(View.VISIBLE);
            holder.giving_day.setText("仅首次充值");
        }


        holder.mView.setBackgroundColor(ContextCompat.getColor(holder.mView.getContext(), selectPriceBean.isChoose() ? R.color.just_color_ff8c00 : R.color.just_color_ffffff));
        holder.mView.setOnClickListener(v -> {
            if (mSelectPriceBean != null) {
                mSelectPriceBean.choose();
                notifyItemChanged(mSelectPriceBean.getPosition(), mSelectPriceBean);
            }
            mSelectPriceBean = selectPriceBean;
            selectPriceBean.choose();
            holder.mView.setBackgroundColor(ContextCompat.getColor(holder.mView.getContext(), selectPriceBean.isChoose() ? R.color.just_color_ff8c00 : R.color.just_color_ffffff));
            if (mOnItemClickListener != null) {
                mOnItemClickListener.onItemClick(v, position, selectPriceBean);
            }
        });
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position, @NonNull List<Object> payloads) {
        super.onBindViewHolder(holder, position, payloads);
        if (payloads.size() > 0) {
            Object o = payloads.get(0);
            if (o instanceof SelectPriceBean) {
                holder.mView.setBackgroundColor(ContextCompat.getColor(holder.mView.getContext(), ((SelectPriceBean) o).isChoose() ? R.color.just_color_ff8c00 : R.color.just_color_ffffff));

            }
        }
    }

    @Override
    public int getItemCount() {
//        return 10;
        return mSelectPriceBeans == null ? 0 : mSelectPriceBeans.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        View mView;
        TextView money;
        TextView giving_day;
        TextView day;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mView = itemView;
            money = itemView.findViewById(R.id.tv_money);
            giving_day = itemView.findViewById(R.id.tv_giving_day);
            day = itemView.findViewById(R.id.tv_day);
        }
    }
}
