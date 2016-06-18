package com.example.pavel.moneyflow.provider;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;

import com.example.pavel.moneyflow.db.DBHelper;
import com.example.pavel.moneyflow.util.DateConverter;
import com.example.pavel.moneyflow.util.Prefs;

public class MyContentProvider extends ContentProvider {

    private SQLiteDatabase database;
    private DBHelper dbHelper;

    private static final int URI_CODE_EXPENSE = 1;
    private static final int URI_CODE_EXPENSE_NAME = 2;
    private static final int URI_CODE_EXPENSE_NAME_ID = 20;
    private static final int URI_CODE_EXPENSE_JOIN = 3;

    private static final int URI_CODE_INCOMES = 4;
    private static final int URI_CODE_INCOME_NAMES = 5;
    private static final int URI_CODE_INCOME_JOINED = 6;

    private static final int URI_CODE_MONTHLY_CASH = 7;

    private static UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        matcher.addURI(Prefs.AUTHORITY, Prefs.URI_TYPE_EXPENSE, URI_CODE_EXPENSE);
        matcher.addURI(Prefs.AUTHORITY, Prefs.URI_TYPE_EXPENSE_NAME, URI_CODE_EXPENSE_NAME);
        matcher.addURI(Prefs.AUTHORITY, Prefs.URI_TYPE_EXPENSES_JOINED, URI_CODE_EXPENSE_JOIN);

        matcher.addURI(Prefs.AUTHORITY, Prefs.URI_TYPE_INCOMES, URI_CODE_INCOMES);
        matcher.addURI(Prefs.AUTHORITY, Prefs.URI_TYPE_INCOME_NAMES, URI_CODE_INCOME_NAMES);
        matcher.addURI(Prefs.AUTHORITY, Prefs.URI_TYPE_INCOMES_JOINED, URI_CODE_INCOME_JOINED);

        matcher.addURI(Prefs.AUTHORITY, Prefs.URI_TYPE_MONTHLY_CASH, URI_CODE_MONTHLY_CASH);
    }

    public MyContentProvider() {
    }

    @Override
    public boolean onCreate() {
        dbHelper = new DBHelper(getContext(), Prefs.DB_CURRENT_VERSION);
        return true;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        int result = 0;
        switch (matcher.match(uri)){
            case URI_CODE_EXPENSE:
                database = dbHelper.getWritableDatabase();
                result = database.delete(Prefs.TABLE_EXPENSES, selection, selectionArgs);
                break;
            case URI_CODE_EXPENSE_NAME:
                database = dbHelper.getWritableDatabase();
                result = database.delete(Prefs.TABLE_EXPENSES_NAMES, selection, selectionArgs);
                break;
            case URI_CODE_INCOMES:
                database = dbHelper.getWritableDatabase();
                result = database.delete(Prefs.TABLE_INCOMES, selection, selectionArgs);
                break;
            case URI_CODE_INCOME_NAMES:
                database = dbHelper.getWritableDatabase();
                result = database.delete(Prefs.TABLE_INCOME_NAMES, selection, selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Unsupported uri -> " + uri);
        }
        return result;
    }

    @Override
    public String getType(Uri uri) {
        // TODO: Implement this to handle requests for the MIME type of the data
        // at the given URI.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        database = dbHelper.getWritableDatabase();
        Uri insertUri = null;
        long id;

        switch (matcher.match(uri)){
            case URI_CODE_EXPENSE:
                id = database.insert(Prefs.TABLE_EXPENSES, null, values);
                insertUri = ContentUris.withAppendedId(uri, id);
                updateMonthlyCash(uri, values);
                getContext().getContentResolver().notifyChange(Prefs.URI_EXPENSE_JOINED, null);
                getContext().getContentResolver().notifyChange(Prefs.URI_MONTHLY_CASH, null);
                getContext().getContentResolver().notifyChange(uri, null);
                return insertUri;
            case URI_CODE_EXPENSE_NAME:
                id = database.insert(Prefs.TABLE_EXPENSES_NAMES, null, values);
                insertUri = ContentUris.withAppendedId(uri, id);
                getContext().getContentResolver().notifyChange(Prefs.URI_EXPENSE_JOINED, null);
                getContext().getContentResolver().notifyChange(uri, null);
                return insertUri;
            case URI_CODE_INCOMES:
                id = database.insert(Prefs.TABLE_INCOMES, null, values);
                insertUri = ContentUris.withAppendedId(uri, id);
                updateMonthlyCash(uri, values);
                getContext().getContentResolver().notifyChange(Prefs.URI_INCOMES_JOINED, null);
                getContext().getContentResolver().notifyChange(Prefs.URI_MONTHLY_CASH, null);
                getContext().getContentResolver().notifyChange(uri, null);
                return insertUri;
            case URI_CODE_INCOME_NAMES:
                id = database.insert(Prefs.TABLE_INCOME_NAMES, null, values);
                insertUri = ContentUris.withAppendedId(uri, id);
                getContext().getContentResolver().notifyChange(Prefs.URI_INCOMES_JOINED, null);
                getContext().getContentResolver().notifyChange(uri, null);
                return insertUri;
            case URI_CODE_MONTHLY_CASH:
                id = database.insert(Prefs.TABLE_MONTHLY_CASH_NAME, null, values);
                insertUri = ContentUris.withAppendedId(uri, id);
                getContext().getContentResolver().notifyChange(uri, null);
                return insertUri;
            default:
                throw new IllegalArgumentException("Unsupported uri -> " + uri);
        }
    }

    private void updateMonthlyCash(Uri uri, ContentValues values){

        ContentValues cvToUpdate = new ContentValues();
        int updateValue = 0;

        Cursor c = query(Prefs.URI_MONTHLY_CASH, null,
                Prefs.MONTHLY_CASH_FIELD_MONTH + " = " + DateConverter.getCurrentMonth(), null, null);

        switch (matcher.match(uri)){
            case URI_CODE_EXPENSE:
                updateValue = values.getAsInteger(Prefs.EXPENSE_FIELD_VOLUME);
                if (c.getCount() == 0){
                    cvToUpdate.put(Prefs.MONTHLY_CASH_FIELD_MONTH, DateConverter.getCurrentMonth());
                    cvToUpdate.put(Prefs.MONTHLY_CASH_FIELD_YEAR, DateConverter.getCurrentYear());
                    cvToUpdate.put(Prefs.MONTHLY_CASH_FIELD_EXPENSE, updateValue);
                    insert(Prefs.URI_MONTHLY_CASH, cvToUpdate);
                } else {
                    c.moveToFirst();
                    int currentValue = c.getInt(c.getColumnIndex(Prefs.MONTHLY_CASH_FIELD_EXPENSE));
                    cvToUpdate.put(Prefs.MONTHLY_CASH_FIELD_EXPENSE, currentValue + updateValue);
                    update(Prefs.URI_MONTHLY_CASH, cvToUpdate,
                            Prefs.MONTHLY_CASH_FIELD_MONTH + " = " + DateConverter.getCurrentMonth(), null);
                }
                break;
            case URI_CODE_INCOMES:
                cvToUpdate.put(Prefs.MONTHLY_CASH_FIELD_INCOMES, values.getAsString(Prefs.INCOMES_FIELD_VOLUME));
                break;
        }




    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        database = dbHelper.getWritableDatabase();
        Cursor cursor = null;
        switch (matcher.match(uri)){
            case URI_CODE_EXPENSE:
                cursor = database.query(Prefs.TABLE_EXPENSES, projection, selection,
                        selectionArgs, null, null, sortOrder);
                break;
            case URI_CODE_EXPENSE_NAME:
                cursor = database.query(Prefs.TABLE_EXPENSES_NAMES, projection, selection, selectionArgs,
                        null, null, sortOrder);

                break;
            case URI_CODE_EXPENSE_JOIN:
                SQLiteQueryBuilder expenseQueryBuilder = new SQLiteQueryBuilder();
                expenseQueryBuilder.setTables(Prefs.TABLE_EXPENSES + " INNER JOIN " + Prefs.TABLE_EXPENSES_NAMES + " ON (" +
                        Prefs.TABLE_EXPENSES_NAMES + "." + Prefs.FIELD_ID + " = "
                        + Prefs.TABLE_EXPENSES + "." + Prefs.EXPENSE_FIELD_ID_PASSIVE + ")");
                cursor = expenseQueryBuilder.query(database, projection, selection, selectionArgs, null, null, sortOrder);
                break;
            case URI_CODE_INCOMES:
                cursor = database.query(Prefs.TABLE_INCOMES, projection, selection,
                        selectionArgs, null, null, sortOrder);
                break;
            case URI_CODE_INCOME_NAMES:
                cursor = database.query(Prefs.TABLE_INCOME_NAMES, projection, selection, selectionArgs,
                        null, null, sortOrder);
                break;
            case URI_CODE_INCOME_JOINED:
                SQLiteQueryBuilder incomesQueryBuilder = new SQLiteQueryBuilder();
                incomesQueryBuilder.setTables(Prefs.TABLE_INCOMES + " INNER JOIN " + Prefs.TABLE_INCOME_NAMES + " ON (" +
                        Prefs.TABLE_INCOME_NAMES + "." + Prefs.FIELD_ID + " = "
                        + Prefs.TABLE_INCOMES + "." + Prefs.INCOMES_FIELD_ID_INCOME_NAME + ")");
                cursor = incomesQueryBuilder.query(database, projection, selection, selectionArgs, null, null, sortOrder);
                break;
            case URI_CODE_MONTHLY_CASH:
                cursor = database.query(Prefs.TABLE_MONTHLY_CASH_NAME, projection, selection, selectionArgs,
                        null, null, sortOrder);
                break;
            default:
                throw new IllegalArgumentException("Unsupported uri -> " + uri);
        }
        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        switch (matcher.match(uri)){
            case URI_CODE_MONTHLY_CASH:
                return database.update(Prefs.TABLE_MONTHLY_CASH_NAME, values, selection, selectionArgs);
            default:
                throw new IllegalArgumentException("Unsupported uri -> " + uri);
        }
    }


}
