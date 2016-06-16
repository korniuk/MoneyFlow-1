package com.example.pavel.moneyflow.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.example.pavel.moneyflow.util.Prefs;

public class DBHelper extends SQLiteOpenHelper {

    /*
    Table expenses:
    - _id
    - id_passive (id from table passive)
    - volume (volume of money)
    - date (date when expenses made)
     */
    private static final String CREATE_TABLE_EXPENSES = String.format(
            "create table %s ( %s integer primary key autoincrement, %s integer, %s integer, %s text);",
            Prefs.TABLE_EXPENSES, Prefs.FIELD_ID, Prefs.EXPENSE_FIELD_ID_PASSIVE, Prefs.EXPENSE_FIELD_VOLUME,
            Prefs.EXPENSE_FIELD_DATE);
    /*
    Table incomes:
    - _id
    - id_income_name (id from table income_name)
    - volume (volume of money)
    - date (date when income made)
     */
    private static final String CREATE_TABLE_INCOMES = String.format(
            "create table %s ( %s integer primary key autoincrement, %s integer, %s integer, %s text);",
            Prefs.TABLE_INCOMES, Prefs.FIELD_ID, Prefs.INCOMES_FIELD_ID_INCOME_NAME, Prefs.INCOMES_FIELD_VOLUME,
            Prefs.INCOMES_FIELD_DATE);

    /*
    Table expense_name
    - _id
    - name (name of expense)
     */
    private static final String CREATE_TABLE_EXPENSE_NAME = String.format(
            "create table %s ( %s integer primary key autoincrement, %s text);",
            Prefs.TABLE_EXPENSES_NAMES, Prefs.FIELD_ID, Prefs.EXPENSE_NAMES_FIELDS_NAME);

    /*
    Table income_names
    - _id
    - name (name of expense)
     */
    private static final String CREATE_TABLE_INCOME_NAMES = String.format(
            "create table %s ( %s integer primary key autoincrement, %s text);",
            Prefs.TABLE_INCOME_NAMES, Prefs.FIELD_ID, Prefs.INCOME_NAMES_FIELDS_NAME);

    private static final String CREATE_TABLE_MONTHLY_CASH = String.format(
            "create table %s ( %s integer primary key autoincrement, " +
                    "%s integer, " + // month
                    "%s integer, " + // year
                    "%s integer, " + // cash_flow
                    "%s integer, " + // incomes
                    "%s integer, " + // expenses
                    "%s integer, " + // cash_balance
                    "%s integer, " + // i_plan
                    "%s integer);",  // e_plan
            Prefs.TABLE_MONTHLY_CASH_NAME, Prefs.FIELD_ID,
            Prefs.MONTHLY_CASH_FIELD_MONTH,
            Prefs.MONTHLY_CASH_FIELD_YEAR,
            Prefs.MONTHLY_CASH_FIELD_CASH_FLOW,
            Prefs.MONTHLY_CASH_FIELD_INCOMES,
            Prefs.MONTHLY_CASH_FIELD_EXPENSE,
            Prefs.MONTH_CASH_FIELD_BALANCE,
            Prefs.MONTH_CASH_FIELD_I_PLAN,
            Prefs.MONTH_CASH_FIELD_E_PLAN);


    public DBHelper(Context context, int version) {
        super(context, Prefs.DB_NAME, null, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_EXPENSES);
        db.execSQL(CREATE_TABLE_EXPENSE_NAME);
        db.execSQL(CREATE_TABLE_INCOMES);
        db.execSQL(CREATE_TABLE_INCOME_NAMES);
        db.execSQL(CREATE_TABLE_MONTHLY_CASH);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
