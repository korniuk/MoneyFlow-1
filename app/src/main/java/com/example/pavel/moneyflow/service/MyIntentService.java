package com.example.pavel.moneyflow.service;

import android.app.IntentService;
import android.content.ContentValues;
import android.content.Intent;
import android.content.Context;
import android.net.Uri;

import com.example.pavel.moneyflow.util.Prefs;

import java.util.Calendar;

public class MyIntentService extends IntentService {

    private static final String ACTION_INSERT_EXPENCE = "com.example.pavel.moneyflow.service.action.INSERT_EXPENCY";

    private static final String EXTRA_INSERT_EXPENCY_NAME = "com.example.pavel.moneyflow.service.extra.INSERT_EXPENCY_NAME";
    private static final String EXTRA_INSERT_EXPENCY_VOLUME = "com.example.pavel.moneyflow.service.extra.INSERT_EXPENCY_VOLUME";

    public MyIntentService() {
        super("MyIntentService");
    }

    public static void startActionInsertExpency(Context context, String name, int volume){
        Intent intent = new Intent(context, MyIntentService.class);
        intent.setAction(ACTION_INSERT_EXPENCE);
        intent.putExtra(EXTRA_INSERT_EXPENCY_NAME, name);
        intent.putExtra(EXTRA_INSERT_EXPENCY_VOLUME, volume);
        context.startService(intent);
    }


    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            switch (action){
                case ACTION_INSERT_EXPENCE:
                    String name = intent.getStringExtra(EXTRA_INSERT_EXPENCY_NAME);
                    int volume = intent.getIntExtra(EXTRA_INSERT_EXPENCY_VOLUME, 0);
                    handleActionInsertExpense(name, volume);
                    break;
                default:
                    throw new UnsupportedOperationException("This action isn't supported -> " + action);
            }

        }
    }

    private void handleActionInsertExpense(String name, int volume) {

        ContentValues cvExpenseName = new ContentValues();
        cvExpenseName.put(Prefs.EXPENSE_NAMES_FIELDS_NAME, name);
        Uri insertUri = getContentResolver().insert(Prefs.URI_EXPENSE_NAME, cvExpenseName);

        long insertedId = Long.parseLong(insertUri.getLastPathSegment());
        String date = String.valueOf(Calendar.getInstance().getTimeInMillis());

        ContentValues cvExpense = new ContentValues();
        cvExpense.put(Prefs.EXPENSE_FIELD_ID_PASSIVE, insertedId);
        cvExpense.put(Prefs.EXPENSE_FIELD_DATE, date);
        cvExpense.put(Prefs.EXPENSE_FIELD_VOLUME, volume);

        getContentResolver().insert(Prefs.URI_EXPENSE, cvExpense);
    }


}
