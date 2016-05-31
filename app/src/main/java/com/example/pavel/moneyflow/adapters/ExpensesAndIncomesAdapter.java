package com.example.pavel.moneyflow.adapters;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.pavel.moneyflow.R;
import com.example.pavel.moneyflow.util.DateConverter;
import com.example.pavel.moneyflow.util.Prefs;

public class ExpensesAndIncomesAdapter extends AbstractCursorRecyclerAdapter<ExpensesAndIncomesViewHolder> {

    private Context context;

    public ExpensesAndIncomesAdapter(Context context, Cursor c) {
        super(c);
        this.context = context;
    }

    @Override
    public void onBindViewHolder(ExpensesAndIncomesViewHolder holder, Cursor cursor) {
        holder.tvName.setText(cursor.getString(cursor.getColumnIndex(Prefs.EXPENSE_NAMES_FIELDS_NAME)));
        holder.tvVolume.setText(cursor.getString(cursor.getColumnIndex(Prefs.EXPENSE_FIELD_VOLUME)));
        holder.tvDate.setText(
                DateConverter.convertToString(cursor.getString(cursor.getColumnIndex(Prefs.EXPENSE_FIELD_DATE))));

    }

    @Override
    public ExpensesAndIncomesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_incomes_and_expense_adapter, parent, false);
        return new ExpensesAndIncomesViewHolder(view);
    }
}
