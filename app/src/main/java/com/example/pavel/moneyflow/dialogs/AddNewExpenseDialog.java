package com.example.pavel.moneyflow.dialogs;


import android.os.Bundle;
import android.text.TextUtils;

import com.example.pavel.moneyflow.R;
import com.example.pavel.moneyflow.service.MyIntentService;
import com.example.pavel.moneyflow.util.Prefs;

public class AddNewExpenseDialog extends AbstractAddItemDialog{

    @Override
    public Bundle getDialogArguments() {
        Bundle bundle = new Bundle();
        bundle.putString(AbstractAddItemDialog.BUNDLE_TITLE,
                getResources().getString(R.string.title_add_new_expency_dialog));
        bundle.putString(AbstractAddItemDialog.BUNDLE_MESSAGE,
                getResources().getString(R.string.message_add_new_expenses));
        bundle.putString(AbstractAddItemDialog.BUNDLE_POSITIVE_BUTTON_TITLE,
                getResources().getString(R.string.positive_dialog_button_title));
        bundle.putString(AbstractAddItemDialog.BUNDLE_NEGATIVE_BUTTON_TITLE,
                getResources().getString(R.string.negative_dialog_button_title));
        bundle.putString(AbstractAddItemDialog.BUNDLE_LOADER_URI,
                Prefs.URI_EXPENSE_NAME.toString());
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

            MyIntentService.startActionInsertExpense(activity, name, volume);
            dismiss();
        }
    }
}
