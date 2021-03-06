package ru.firstset.whereisuser.util;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import ru.firstset.whereisuser.MainActivity;
import ru.firstset.whereisuser.MyMapFragment;
import ru.firstset.whereisuser.R;

public class DialogFragment extends android.app.DialogFragment implements DialogInterface.OnClickListener {

        private int resultDialog = 0;
        final String LOG_TAG = "myDialogLogs";

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            resultDialog = 0;
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setTitle(R.string.alert_dialog_title);
            builder.setMessage(R.string.alert_dialog_message)
                    .setPositiveButton(R.string.alert_dialog_positive_button_text, this)
                    .setNegativeButton(R.string.alert_dialog_negative_button_text, this);
            return builder.create();
        }

        @Override
        public void onClick(DialogInterface dialog, int which) {
            switch (which) {
                case Dialog.BUTTON_POSITIVE:
                    //удаляем
                    resultDialog = 1;
                    break;
                case Dialog.BUTTON_NEGATIVE:
                    resultDialog = 2;
                    Log.d(LOG_TAG, "resultDialog = 2");

                    break;
            }
        }


        public void onDismiss(DialogInterface dialog) {
            super.onDismiss(dialog);
        }

        public void onCancel(DialogInterface dialog) {
            super.onCancel(dialog);
        }
    }


