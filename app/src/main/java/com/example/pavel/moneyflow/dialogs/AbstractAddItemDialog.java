package com.example.pavel.moneyflow.dialogs;

import android.app.Activity;
import android.app.Dialog;
import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.DialogFragment;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Adapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;

import com.example.pavel.moneyflow.R;
import com.example.pavel.moneyflow.service.MyIntentService;
import com.example.pavel.moneyflow.util.Prefs;

public abstract class AbstractAddItemDialog extends DialogFragment implements DialogInterface.OnShowListener, LoaderManager.LoaderCallbacks<Cursor> {

    public static final String BUNDLE_TITLE = "title";
    public static final String BUNDLE_MESSAGE = "message";
    public static final String BUNDLE_POSITIVE_BUTTON_TITLE = "positive_btn_title";
    public static final String BUNDLE_NEGATIVE_BUTTON_TITLE = "negative_btn_title";
    public static final String BUNDLE_LOADER_URI = "uri";

    protected TextInputLayout tilVolume;
    protected EditText etVolume;
    protected AutoCompleteTextView acName;
    protected SimpleCursorAdapter simpleCursorAdapter;
    protected Activity activity;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        activity = getActivity();

        View view = LayoutInflater.from(activity).inflate(R.layout.dialog_add_records, null, true);
        //TODO set adapter for AutoCompleateTextView

        tilVolume = (TextInputLayout) view.findViewById(R.id.input_layout_volume);

        etVolume = (EditText) view.findViewById(R.id.etVolume);
        acName = (AutoCompleteTextView) view.findViewById(R.id.acName);

        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setView(view)
                .setMessage(getDialogArguments().getString(BUNDLE_MESSAGE))
                .setTitle(getDialogArguments().getString(BUNDLE_TITLE))
                .setPositiveButton(getDialogArguments().getString(BUNDLE_POSITIVE_BUTTON_TITLE), null)
                .setNegativeButton(getDialogArguments().getString(BUNDLE_NEGATIVE_BUTTON_TITLE), null);

        AlertDialog dialog = builder.create();

        dialog.setOnShowListener(this);

        activity.getLoaderManager().initLoader(Prefs.ID_LOADER_EXPENSES_NAMES, null, this);

        return dialog;
    }

    public abstract Bundle getDialogArguments();

    public abstract void onClickNegativeButton();

    public abstract void onClickPositiveButton();


    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        return new CursorLoader(activity, Uri.parse(getDialogArguments().getString(BUNDLE_LOADER_URI)), null, null, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, final Cursor data) {
        simpleCursorAdapter = new SimpleCursorAdapter(
                activity,
                android.R.layout.simple_dropdown_item_1line,
                data,
                new String[]{Prefs.EXPENSE_NAMES_FIELDS_NAME},
                new int[]{android.R.id.text1},
                Adapter.NO_SELECTION);
        acName.setAdapter(simpleCursorAdapter);

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        Log.d(Prefs.LOG_TAG, "Reset Loader");
    }

    @Override
    public void onShow(DialogInterface dialogInterface) {

        AlertDialog dialog = (AlertDialog) getDialog();

        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickPositiveButton();
            }
        });

        dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickNegativeButton();
            }
        });
    }


}
