package com.example.pavel.moneyflow.dialogs;

import android.app.Activity;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Adapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;

import com.example.pavel.moneyflow.R;
import com.example.pavel.moneyflow.service.MyIntentService;
import com.example.pavel.moneyflow.util.Prefs;

public class AddNewIncomeDialog extends AbstractAddItemDialog {


    @Override
    public Bundle getDialogArguments() {
        Bundle bundle = new Bundle();
        bundle.putString(AbstractAddItemDialog.BUNDLE_TITLE,
                getResources().getString(R.string.title_add_new_incomes_dialog));
        bundle.putString(AbstractAddItemDialog.BUNDLE_MESSAGE,
                getResources().getString(R.string.message_add_new_incomes));
        bundle.putString(AbstractAddItemDialog.BUNDLE_POSITIVE_BUTTON_TITLE,
                getResources().getString(R.string.positive_dialog_button_title));
        bundle.putString(AbstractAddItemDialog.BUNDLE_NEGATIVE_BUTTON_TITLE,
                getResources().getString(R.string.negative_dialog_button_title));
        bundle.putString(AbstractAddItemDialog.BUNDLE_LOADER_URI,
                Prefs.URI_INCOME_NAMES.toString());
        return bundle;
    }

    @Override
    public void onClickNegativeButton() {
        dismiss();
    }

    @Override
    public void onClickPositiveButton() {
        if (etVolume.getText() == null || TextUtils.isEmpty(etVolume.getText())){
            tilVolume.setError(getContext().getString(R.string.title_input_layout_volume_error));

        } else {
            String name = acName.getText().toString();
            int volume = Integer.parseInt(etVolume.getText().toString());

            MyIntentService.startActionInsertIncome(activity, name, volume);
            dismiss();
        }
    }
}
