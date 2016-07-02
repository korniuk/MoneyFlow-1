package com.example.pavel.moneyflow.fragments;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.example.pavel.moneyflow.R;
import com.example.pavel.moneyflow.util.DateConverter;
import com.example.pavel.moneyflow.util.Prefs;
import com.example.pavel.moneyflow.views.RoundChart;

public class IncomesFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>{

    private RoundChart rcIncomes;
    private EditText etIncomesPlan;
    private View view;

    public IncomesFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        this.view = inflater.inflate(R.layout.fragment_incomes, container, false);
        rcIncomes = (RoundChart) view.findViewById(R.id.rcIncomes);
        etIncomesPlan = (EditText) view.findViewById(R.id.etIncomesPlan);
        etIncomesPlan.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent keyEvent) {
                if (keyEvent.getKeyCode() == KeyEvent.KEYCODE_ENTER){
                    int input = Integer.parseInt(etIncomesPlan.getText().toString());
                    ContentValues cv = new ContentValues();
                    cv.put(Prefs.MONTH_CASH_FIELD_I_PLAN, input);

                    int rowUpdated = getActivity().getContentResolver().update(Prefs.URI_MONTHLY_CASH, cv,
                            Prefs.MONTHLY_CASH_FIELD_MONTH + " = " + DateConverter.getCurrentMonth() + " AND " +
                                    Prefs.MONTHLY_CASH_FIELD_YEAR + " = " + DateConverter.getCurrentYear(), null);
                    if (rowUpdated == 0) {
                        cv.put(Prefs.MONTHLY_CASH_FIELD_MONTH, DateConverter.getCurrentMonth());
                        cv.put(Prefs.MONTHLY_CASH_FIELD_YEAR, DateConverter.getCurrentYear());
                        getActivity().getContentResolver().insert(Prefs.URI_MONTHLY_CASH, cv);
                    }
                    etIncomesPlan.clearFocus();

                    InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                    Log.d(Prefs.LOG_TAG, "Monthly cash updated! New values - " + cv.toString());
                    return true;
                }
                return false;
            }
        });
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        getActivity().getSupportLoaderManager().initLoader(2, null, this);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(getActivity(), Prefs.URI_MONTHLY_CASH, null,
                Prefs.MONTHLY_CASH_FIELD_MONTH + " = " + DateConverter.getCurrentMonth() +
        " AND " + Prefs.MONTHLY_CASH_FIELD_YEAR + " = " + DateConverter.getCurrentYear(), null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        Log.d(Prefs.LOG_TAG, "onLoadFinished: cursor count = " + cursor.getCount());
        if (cursor.moveToFirst()){
            int current = cursor.getInt(cursor.getColumnIndex(Prefs.MONTHLY_CASH_FIELD_INCOMES));
            int plan = cursor.getInt(cursor.getColumnIndex(Prefs.MONTH_CASH_FIELD_I_PLAN));

            if (plan != 0) {
                etIncomesPlan.setText(String.valueOf(plan));
                int percent = (current * 100)/plan;
                rcIncomes.setValues(percent);
                rcIncomes.beginChartAnimation();
            }

        } else {
            rcIncomes.setValues(0);
            rcIncomes.beginChartAnimation();
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }
}
