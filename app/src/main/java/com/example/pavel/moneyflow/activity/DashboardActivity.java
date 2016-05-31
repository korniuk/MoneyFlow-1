package com.example.pavel.moneyflow.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.pavel.moneyflow.R;
import com.example.pavel.moneyflow.dialogs.AddNewExpenseDialog;

public class DashboardActivity extends AppCompatActivity {

    Button btnExpenses;
    Button btnJoin;
    TextView tvExpenses;
    TextView tvExpensesNames;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        btnExpenses = (Button) findViewById(R.id.btnExpenses);
        btnJoin = (Button) findViewById(R.id.btnExpenses);

        tvExpenses = (TextView) findViewById(R.id.tvExpenses);
        tvExpensesNames = (TextView) findViewById(R.id.tvExpensesNames);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.item_expency:
                AddNewExpenseDialog expencyDialog = new AddNewExpenseDialog();
                expencyDialog.show(getSupportFragmentManager(), "ED");
                break;
        }
        return true;
    }

    public void onClickBtnExpenses(View view) {
        Intent intent = new Intent(this, ExpensesActivity.class);
        startActivity(intent);
    }
}
