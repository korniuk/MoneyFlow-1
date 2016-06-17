package com.example.pavel.moneyflow.fragments;

import android.content.Context;
import android.database.ContentObserver;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.pavel.moneyflow.R;
import com.example.pavel.moneyflow.activity.ExpensesActivity;
import com.example.pavel.moneyflow.activity.MainActivity;
import com.example.pavel.moneyflow.util.DateConverter;
import com.example.pavel.moneyflow.util.Prefs;
import com.example.pavel.moneyflow.views.RoundChart;

import java.util.HashMap;

public class ExpensesFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    private int EXPENSES_PLAN = 1500;

    TextView tvExpensesSummary;
    RoundChart roundChart;

    ContentObserver contentObserver = new ContentObserver(new Handler()) {
        @Override
        public void onChange(boolean selfChange) {
            super.onChange(selfChange);
            getActivity().getSupportLoaderManager().restartLoader(1, null, ExpensesFragment.this);
            Log.d(Prefs.LOG_TAG, "Loader restart");
        }
    };

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_expenses, container, false);
        tvExpensesSummary = (TextView) view.findViewById(R.id.tvCurrentFragmentExpenses);
        roundChart = (RoundChart) view.findViewById(R.id.rcExpenses);
        getActivity().getSupportLoaderManager().initLoader(1, null, this);
        getContext().getContentResolver().registerContentObserver(Prefs.URI_EXPENSE, true, contentObserver);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        getContext().getContentResolver().registerContentObserver(Prefs.URI_EXPENSE, false, contentObserver);
    }

    @Override
    public void onStop() {
        super.onStop();
        getContext().getContentResolver().unregisterContentObserver(contentObserver);
    }


    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(getActivity(), Prefs.URI_MONTHLY_CASH, null, Prefs.MONTHLY_CASH_FIELD_MONTH +
                "=?", new String[]{DateConverter.getCurrentMonth()}, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        if (data.moveToFirst()){
            int current_plan = data.getInt(data.getColumnIndex(Prefs.MONTHLY_CASH_FIELD_EXPENSE));
            roundChart.setValues(EXPENSES_PLAN, current_plan);
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
