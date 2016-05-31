package com.example.pavel.moneyflow.util;

import android.net.Uri;

public class Prefs {

    public static final String LOG_TAG = "MoneyFlow";

    //------------------------Provider----------------------------
    public static final String AUTHORITY = "com.example.pavel.moneyflow.provider";
    //___________EXPENSES______________
    public static String URI_TYPE_EXPENSE = "expenses";
    public static String URI_TYPE_EXPENSE_NAME = "expenses_name";
    public static String URI_TYPE_EXPENSES_JOINED = "expenses_joined";
    public static final Uri URI_EXPENSE = Uri.parse("content://" + AUTHORITY + "/" + URI_TYPE_EXPENSE);
    public static final Uri URI_EXPENSE_NAME = Uri.parse("content://" + AUTHORITY + "/" + URI_TYPE_EXPENSE_NAME);
    public static final Uri URI_EXPENSE_JOINED = Uri.parse("content://" + AUTHORITY + "/" + URI_TYPE_EXPENSES_JOINED);
    //___________INCOMES________________
    public static String URI_TYPE_INCOMES = "incomes";
    public static String URI_TYPE_INCOME_NAMES = "income_names";
    public static String URI_TYPE_INCOMES_JOINED = "incomes_joined";
    public static final Uri URI_INCOMES = Uri.parse("content://" + AUTHORITY + "/" + URI_TYPE_INCOMES);
    public static final Uri URI_INCOME_NAMES = Uri.parse("content://" + AUTHORITY + "/" + URI_TYPE_INCOME_NAMES);
    public static final Uri URI_INCOMES_JOINED = Uri.parse("content://" + AUTHORITY + "/" + URI_TYPE_INCOMES_JOINED);

    //--------------------DB Constants-----------------------------------
    public static final String DB_NAME = "MoneyFlowDB";
    public static final int DB_CURRENT_VERSION = 2;
    public static final String FIELD_ID = "_id";

    //___________________EXPENSES TABLE ____________________________
    public static final String TABLE_EXPENSES = "expenses";
    public static final String EXPENSE_FIELD_ID_PASSIVE = "id_passive";
    public static final String EXPENSE_FIELD_VOLUME = "volume";
    public static final String EXPENSE_FIELD_DATE = "date";

    //_________________EXPENSES NAMES TABLE__________________________
    public static final String TABLE_EXPENSES_NAMES = "expenses_names";
    public static final String EXPENSE_NAMES_FIELDS_NAME = "name";

    //_________________INCOMES NAMES TABLE____________________________
    public static final String TABLE_INCOME_NAMES = "income_names";
    public static final String INCOME_NAMES_FIELDS_NAME = "name";

    //___________________INCOMES TABLE _______________________________
    public static final String TABLE_INCOMES = "incomes";
    public static final String INCOMES_FIELD_ID_INCOME_NAME = "id_income_name";
    public static final String INCOMES_FIELD_VOLUME = "volume";
    public static final String INCOMES_FIELD_DATE = "date";

    //________________LOADER_CONSTANTS_____________________________
    public static final int ID_LOADER_EXPENSES_NAMES = 1;

}
