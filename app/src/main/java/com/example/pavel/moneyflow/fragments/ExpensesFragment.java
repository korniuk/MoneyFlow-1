package com.example.pavel.moneyflow.fragments;

import android.content.Context;
import android.database.ContentObserver;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.pavel.moneyflow.R;
import com.example.pavel.moneyflow.activity.ExpensesActivity;
import com.example.pavel.moneyflow.activity.MainActivity;
import com.example.pavel.moneyflow.util.Prefs;
import com.example.pavel.moneyflow.views.RoundChart;

import java.util.HashMap;

public class ExpensesFragment extends Fragment implements LoaderManager.LoaderCallbacks<HashMap<String, String>> {

    private static final String CURRENT_MONTH = "current_month";
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
    public Loader<HashMap<String, String>> onCreateLoader(int id, Bundle args) {
        return new HashMapLoader(getActivity());
    }

    @Override
    public void onLoadFinished(Loader<HashMap<String, String>> loader, HashMap<String, String> data) {
        tvExpensesSummary.setText(data.get(CURRENT_MONTH));

        roundChart.setValues(100, 130);
    }

    @Override
    public void onLoaderReset(Loader<HashMap<String, String>> loader) {

    }

    private static class HashMapLoader extends Loader<HashMap<String, String>> {

        private HashMap<String, String> result;

        public HashMapLoader(Context context) {
            super(context);
            result = new HashMap<>();
        }

        @Override
        protected void onStartLoading() {
            forceLoad();
        }

        @Override
        protected void onForceLoad() {
            super.onForceLoad();
            Cursor cursor = getContext().getContentResolver().query(Prefs.URI_EXPENSE, new String[]{Prefs.EXPENSE_FIELD_VOLUME}, null, null, null);

            cursor.moveToFirst();
            int value = 0;

            if (cursor.getCount() == 0) {
                result.put(CURRENT_MONTH, String.valueOf(value));
                deliverResult(result);
                return;
            }

            do {
                value += cursor.getInt(cursor.getColumnIndex(Prefs.EXPENSE_FIELD_VOLUME));
            } while (cursor.moveToNext());

            result.put(CURRENT_MONTH, String.valueOf(value));
            deliverResult(result);
        }
    }
}
