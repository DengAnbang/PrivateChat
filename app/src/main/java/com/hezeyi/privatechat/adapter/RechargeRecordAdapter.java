package com.hezeyi.privatechat.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hezeyi.privatechat.R;
import com.hezeyi.privatechat.bean.RechargeRecordBean;
import com.abxh.utils.utils.TimeUtils;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Created by dab on 2021/4/13 22:09
 */
public class RechargeRecordAdapter extends RecyclerView.Adapter<RechargeRecordAdapter.ViewHolder> {
    private List<RechargeRecordBean> mRechargeRecordBeans;

    public List<RechargeRecordBean> getDataBeans() {
        return mRechargeRecordBeans;
    }

    public void setRechargeRecordBeans(List<RechargeRecordBean> rechargeRecordBeans) {
        mRechargeRecordBeans = rechargeRecordBeans;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RechargeRecordAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recharge_record, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RechargeRecordAdapter.ViewHolder holder, int position) {
        RechargeRecordBean rechargeRecordBean = mRechargeRecordBeans.get(position);
        holder.tv_user_account.setText("被充值人账号:"+rechargeRecordBean.getUser_account());
        holder.tv_user_name.setText("被充值人名称:"+rechargeRecordBean.getUser_name());
        holder.tv_execution_user_account.setText("充值人账号:"+rechargeRecordBean.getExecution_user_account());
        holder.tv_execution_user_name.setText("充值人名称:"+rechargeRecordBean.getExecution_user_name());
        holder.tv_money.setText("充值金额:"+rechargeRecordBean.getMoney() + "元");
        holder.tv_day.setText("充值时间:"+rechargeRecordBean.getDay() + "天");
        holder.tv_recharge_type.setText("充值方式:"+rechargeRecordBean.getRechargeTypeShowString());
        holder.tv_created.setText("时间:"+TimeUtils.toTimeByString(rechargeRecordBean.getCreated()));
    }

    @Override
    public int getItemCount() {
        return mRechargeRecordBeans == null ? 0 : mRechargeRecordBeans.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tv_user_account;
        TextView tv_user_name;
        TextView tv_execution_user_account;
        TextView tv_execution_user_name;
        TextView tv_money;
        TextView tv_day;
        TextView tv_recharge_type;
        TextView tv_created;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_user_account = itemView.findViewById(R.id.tv_user_account);
            tv_user_name = itemView.findViewById(R.id.tv_user_name);
            tv_execution_user_account = itemView.findViewById(R.id.tv_execution_user_account);
            tv_execution_user_name = itemView.findViewById(R.id.tv_execution_user_name);
            tv_money = itemView.findViewById(R.id.tv_money);
            tv_day = itemView.findViewById(R.id.tv_day);
            tv_recharge_type = itemView.findViewById(R.id.tv_recharge_type);
            tv_created = itemView.findViewById(R.id.tv_created);

        }
    }
}
