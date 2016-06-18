package com.example.pavel.moneyflow.fragments;

import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.pavel.moneyflow.R;
import com.example.pavel.moneyflow.util.DateConverter;
import com.example.pavel.moneyflow.util.Prefs;
import com.example.pavel.moneyflow.views.RoundChart;

public class ExpensesFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    private int EXPENSES_PLAN = 1500;

    RoundChart roundChart;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_expenses, container, false);
        roundChart = (RoundChart) view.findViewById(R.id.rcExpenses);
        getActivity().getSupportLoaderManager().initLoader(1, null, this);
        return view;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(getActivity(), Prefs.URI_MONTHLY_CASH, null, Prefs.MONTHLY_CASH_FIELD_MONTH +
                "=?", new String[]{DateConverter.getCurrentMonth()}, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        if (data.moveToFirst()){
            int current_expenses = data.getInt(data.getColumnIndex(Prefs.MONTHLY_CASH_FIELD_EXPENSE));
            int expenses_plan = data.getInt(data.getColumnIndex(Prefs.MONTH_CASH_FIELD_E_PLAN));
            roundChart.setValues(EXPENSES_PLAN, current_expenses);
            roundChart.invalidate();
        } else {
            roundChart.setValues(EXPENSES_PLAN, 0);
            roundChart.invalidate();
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }
}
