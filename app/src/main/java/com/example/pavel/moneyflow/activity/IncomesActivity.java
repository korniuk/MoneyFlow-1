package com.example.pavel.moneyflow.activity;

import android.content.Context;
import android.database.ContentObserver;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.example.pavel.moneyflow.R;
import com.example.pavel.moneyflow.adapters.ExpensesAndIncomesAdapter;
import com.example.pavel.moneyflow.dialogs.AddNewIncomeDialog;
import com.example.pavel.moneyflow.util.Prefs;

public class IncomesActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    private RecyclerView rvIncomes;
    private ContentObserver contentObserver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_incomes);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AddNewIncomeDialog().show(getSupportFragmentManager(), "ID");
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        rvIncomes = (RecyclerView) findViewById(R.id.rvIncomes);
        rvIncomes.setLayoutManager(new LinearLayoutManager(this));

        contentObserver = new ContentObserver(new Handler()) {
            @Override
            public void onChange(boolean selfChange) {
                getSupportLoaderManager().getLoader(1).forceLoad();
            }
        };

        getSupportLoaderManager().initLoader(1, null, this);
    }


    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new IncomesLoader(this, contentObserver);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        if (rvIncomes.getAdapter() == null){
            rvIncomes.setAdapter(new ExpensesAndIncomesAdapter(this, data));
        } else {
            ((ExpensesAndIncomesAdapter) rvIncomes.getAdapter()).swapCursor(data);
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

    private static class IncomesLoader extends CursorLoader {

        private ContentObserver contentObserver;
        private Context context;

        public IncomesLoader(Context context, ContentObserver contentObserver) {
            super(context);
            this.context = context;
            this.contentObserver = contentObserver;
        }

        @Override
        public Cursor loadInBackground() {
            Cursor c = context.getContentResolver().query(Prefs.URI_INCOMES_JOINED, null, null, null, null);
            c.registerContentObserver(contentObserver);
            return c;
        }
    }
}
