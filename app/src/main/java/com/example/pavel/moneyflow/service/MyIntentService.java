package com.example.pavel.moneyflow.service;

import android.app.IntentService;
import android.content.ContentValues;
import android.content.Intent;
import android.content.Context;
import android.net.Uri;

import com.example.pavel.moneyflow.util.Prefs;

import java.util.Calendar;

public class MyIntentService extends IntentService {

    private static final String ACTION_INSERT_EXPENSE = "com.example.pavel.moneyflow.service.action.INSERT_EXPENSE";
    private static final String ACTION_INSERT_INCOME = "com.example.pavel.moneyflow.service.action.INSERT_INCOME";

    private static final String EXTRA_INSERT_EXPENSE_NAME = "com.example.pavel.moneyflow.service.extra.INSERT_EXPENSE_NAME";
    private static final String EXTRA_INSERT_EXPENSE_VOLUME = "com.example.pavel.moneyflow.service.extra.INSERT_EXPENSE_VOLUME";

    private static final String EXTRA_INSERT_INCOME_NAME = "com.example.pavel.moneyflow.service.extra.INSERT_INCOME_NAME";
    private static final String EXTRA_INSERT_INCOME_VOLUME = "com.example.pavel.moneyflow.service.extra.INSERT_INCOME_VOLUME";


    public MyIntentService() {
        super("MyIntentService");
    }

    public static void startActionInsertExpense(Context context, String name, int volume){
        Intent intent = new Intent(context, MyIntentService.class);
        intent.setAction(ACTION_INSERT_EXPENSE);
        intent.putExtra(EXTRA_INSERT_EXPENSE_NAME, name);
        intent.putExtra(EXTRA_INSERT_EXPENSE_VOLUME, volume);
        context.startService(intent);
    }

    public static void startActionInsertIncome(Context context, String name, int volume) {
        Intent intent = new Intent(context, MyIntentService.class);
        intent.setAction(ACTION_INSERT_INCOME);
        intent.putExtra(EXTRA_INSERT_INCOME_NAME, name);
        intent.putExtra(EXTRA_INSERT_INCOME_VOLUME, volume);
        context.startService(intent);
    }


    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            switch (action){
                case ACTION_INSERT_EXPENSE:
                    String expenseName = intent.getStringExtra(EXTRA_INSERT_EXPENSE_NAME);
                    int expenseVolume = intent.getIntExtra(EXTRA_INSERT_EXPENSE_VOLUME, 0);
                    handleActionInsertExpense(expenseName, expenseVolume);
                    break;
                case ACTION_INSERT_INCOME:
                    String incomeName = intent.getStringExtra(EXTRA_INSERT_INCOME_NAME);
                    int incomeVolume = intent.getIntExtra(EXTRA_INSERT_INCOME_VOLUME, 0);
                    handleActionInsertIncome(incomeName, incomeVolume);
                    break;
                default:
                    throw new UnsupportedOperationException("This action isn't supported -> " + action);
            }

        }
    }

    private void handleActionInsertIncome(String incomeName, int incomeVolume) {
        ContentValues cvIncomeName = new ContentValues();
        cvIncomeName.put(Prefs.INCOME_NAMES_FIELDS_NAME, incomeName);
        Uri insertUri = getContentResolver().insert(Prefs.URI_INCOME_NAMES, cvIncomeName);

        long insertedId = Long.parseLong(insertUri.getLastPathSegment());
        String date = String.valueOf(Calendar.getInstance().getTimeInMillis());

        ContentValues cvIncome = new ContentValues();
        cvIncome.put(Prefs.INCOMES_FIELD_ID_INCOME_NAME, insertedId);
        cvIncome.put(Prefs.INCOMES_FIELD_DATE, date);
        cvIncome.put(Prefs.INCOMES_FIELD_VOLUME, incomeVolume);

        getContentResolver().insert(Prefs.URI_INCOMES, cvIncome);
    }

    private void handleActionInsertExpense(String expenseName, int expenseVolume) {

        ContentValues cvExpenseName = new ContentValues();
        cvExpenseName.put(Prefs.EXPENSE_NAMES_FIELDS_NAME, expenseName);
        Uri insertUri = getContentResolver().insert(Prefs.URI_EXPENSE_NAME, cvExpenseName);

        long insertedId = Long.parseLong(insertUri.getLastPathSegment());
        String date = String.valueOf(Calendar.getInstance().getTimeInMillis());

        ContentValues cvExpense = new ContentValues();
        cvExpense.put(Prefs.EXPENSE_FIELD_ID_PASSIVE, insertedId);
        cvExpense.put(Prefs.EXPENSE_FIELD_DATE, date);
        cvExpense.put(Prefs.EXPENSE_FIELD_VOLUME, expenseVolume);

        getContentResolver().insert(Prefs.URI_EXPENSE, cvExpense);
    }
}
