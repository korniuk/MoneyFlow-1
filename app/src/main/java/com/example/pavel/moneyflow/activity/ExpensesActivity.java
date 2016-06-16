package com.example.pavel.moneyflow.activity;

import android.content.Context;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.example.pavel.moneyflow.R;
import com.example.pavel.moneyflow.adapters.ExpensesAndIncomesAdapter;
import com.example.pavel.moneyflow.dialogs.AddNewExpenseDialog;
import com.example.pavel.moneyflow.util.Prefs;

public class ExpensesActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    private RecyclerView rvExpenses;
    private ContentObserver contentObserver;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expenses);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        final FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AddNewExpenseDialog().show(getSupportFragmentManager(), "ED");
            }
        });

        rvExpenses = (RecyclerView) findViewById(R.id.rvExpenses);
        rvExpenses.setLayoutManager(new LinearLayoutManager(this));

        rvExpenses.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                Log.d(Prefs.LOG_TAG, "DX - " + dx + ", DY - " + dy);
                if (dy > 0){
                    fab.setVisibility(View.GONE);

                } else {
                    fab.setVisibility(View.VISIBLE);
                }
            }
        });

        contentObserver = new ContentObserver(new Handler()) {
            @Override
            public void onChange(boolean selfChange, Uri uri) {
                getSupportLoaderManager().getLoader(1).forceLoad();
            }
        };

        getSupportLoaderManager().initLoader(1, null, this);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new ExpensesLoader(ExpensesActivity.this, contentObserver);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        if (rvExpenses.getAdapter() == null){
            rvExpenses.setAdapter(new ExpensesAndIncomesAdapter(this, data));
        } else {
            ((ExpensesAndIncomesAdapter) rvExpenses.getAdapter()).swapCursor(data);
        }


    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }


    static class ExpensesLoader extends CursorLoader {

        private ContentObserver contentObserver;
        private Context context;

        public ExpensesLoader(Context context, ContentObserver contentObserver) {
            super(context);
            this.contentObserver = contentObserver;
            this.context = context;
        }
        @Override
        public Cursor loadInBackground() {
            Cursor c = context.getContentResolver().query(Prefs.URI_EXPENSE_JOINED, null, null, null, null);
            c.registerContentObserver(contentObserver);
            return c;
        }



    }

}
