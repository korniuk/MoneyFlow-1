package com.example.pavel.moneyflow.activity;

import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.widget.Toast;


import com.example.pavel.moneyflow.R;
import com.example.pavel.moneyflow.adapters.DashboardPagerAdapter;
import com.example.pavel.moneyflow.dialogs.AddNewExpenseDialog;
import com.example.pavel.moneyflow.dialogs.AddNewIncomeDialog;

public class MainActivity extends AppCompatActivity {

    private DashboardPagerAdapter dashboardPagerAdapter;
    private Toolbar toolbar;
    private ViewPager mViewPager;
    private FloatingActionButton fab;

    private Menu menu;
    private MenuItem listExpenses;
    private MenuItem listIncomes;
    private boolean isMenuItemExist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        dashboardPagerAdapter = new DashboardPagerAdapter(this, getSupportFragmentManager());

        mViewPager = (ViewPager) findViewById(R.id.viewPager);
        mViewPager.setAdapter(dashboardPagerAdapter);

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}

            @Override
            public void onPageSelected(int position) {
               setFragmentInfo(position);
               changeOptionMenu(position);
               if (position == DashboardPagerAdapter.FRAGMENT_CASH_FLOW){
                   fab.setVisibility(View.GONE);
               } else {
                   fab.setVisibility(View.VISIBLE);
               }
            }

            @Override
            public void onPageScrollStateChanged(int state) {}
        });

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);

        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setVisibility(View.GONE);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (mViewPager.getCurrentItem()){
                    case DashboardPagerAdapter.FRAGMENT_EXPENSES:
                        new AddNewExpenseDialog().show(getSupportFragmentManager(), "ED_Main");
                        break;
                    case DashboardPagerAdapter.FRAGMENT_INCOMES:
                        new AddNewIncomeDialog().show(getSupportFragmentManager(), "ID_Main");
                        break;
                }
            }
        });
    }

    private void setFragmentInfo(int position){

        switch (position){
            case DashboardPagerAdapter.FRAGMENT_CASH_FLOW:
                getSupportActionBar().setTitle(R.string.title_tab_cash_flow);
                break;
            case DashboardPagerAdapter.FRAGMENT_EXPENSES:
                getSupportActionBar().setTitle(R.string.title_tab_expense);
                break;
            case DashboardPagerAdapter.FRAGMENT_INCOMES:
                getSupportActionBar().setTitle(R.string.title_tab_incomes);
                break;
        }
    }

    private void changeOptionMenu(int position){
        if (!isMenuItemExist) return;
        switch (position){
            case DashboardPagerAdapter.FRAGMENT_CASH_FLOW:
                listExpenses.setVisible(false);
                listIncomes.setVisible(false);
                break;
            case DashboardPagerAdapter.FRAGMENT_EXPENSES:
                listExpenses.setVisible(true);
                listIncomes.setVisible(false);
                break;
            case DashboardPagerAdapter.FRAGMENT_INCOMES:
                listExpenses.setVisible(false);
                listIncomes.setVisible(true);
                break;
        }
    }



    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        this.menu = menu;
        listExpenses = menu.findItem(R.id.item_list_expenses);
        listExpenses.setVisible(false);

        listIncomes = menu.findItem(R.id.item_list_incomes);
        listIncomes.setVisible(false);

        isMenuItemExist = true;
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.item_user_profile) {
            Intent intent = new Intent(this, ProfileActivity.class);
            startActivity(intent);
            return true;
        } else if (id == R.id.item_list_expenses){
            Intent intent = new Intent(this, ExpensesActivity.class);
            startActivity(intent);
        } else if (id == R.id.item_list_incomes) {
            Intent intent = new Intent(this, IncomesActivity.class);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }
}
