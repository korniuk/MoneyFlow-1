package com.example.pavel.moneyflow.adapters;

import android.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.example.pavel.moneyflow.R;

public class ExpensesAndIncomesViewHolder extends RecyclerView.ViewHolder {

    protected TextView tvName;
    protected TextView tvVolume;
    protected TextView tvDate;

    public ExpensesAndIncomesViewHolder(View view) {
        super(view);
        tvName = (TextView) view.findViewById(R.id.tvNameItemExpenses);
        tvVolume = (TextView) view.findViewById(R.id.tvVolumeItemExpenses);
        tvDate = (TextView) view.findViewById(R.id.tvDateItemExpenses);
    }
}
