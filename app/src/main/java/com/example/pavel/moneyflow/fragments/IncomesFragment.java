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

public class IncomesFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>{

    private RoundChart rcIncomes;

    public IncomesFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_incomes, container, false);
        rcIncomes = (RoundChart) view.findViewById(R.id.rcIncomes);

        return view;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(getActivity(), Prefs.URI_MONTHLY_CASH, null,
                Prefs.MONTHLY_CASH_FIELD_MONTH + " = " + DateConverter.getCurrentMonth() +
        " AND " + Prefs.MONTHLY_CASH_FIELD_YEAR + " = " + DateConverter.getCurrentYear(), null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        if (cursor.getCount() != 0){
            cursor.moveToFirst();
            int currentValue = cursor.getInt(cursor.getColumnIndex(Prefs.MONTHLY_CASH_FIELD_INCOMES));
            int plan = cursor.getInt(cursor.getColumnIndex(Prefs.MONTH_CASH_FIELD_I_PLAN));

            int percent = (currentValue *100)/plan;

            rcIncomes.setValues(percent);
            rcIncomes.invalidate();
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }
}
