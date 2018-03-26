package com.toscoin.wallet.ui;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

/**
 * @author John L. Jegutanis
 */
public class Dialogs {
    public static final String MESSAGE = "message";

    public static DialogFragment setMessage(DialogFragment newDialog, String message) {
        Bundle args = newDialog.getArguments();
        if (args == null) {
            newDialog.setArguments(new Bundle());
        }
        newDialog.getArguments().putString(MESSAGE, message);
        return newDialog;
    }

    public static class ProgressDialogFragment extends DialogFragment {
        public static ProgressDialogFragment newInstance(String message) {
            return (ProgressDialogFragment) setMessage(new ProgressDialogFragment(), message);
        }

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            ProgressDialog dialog = new ProgressDialog(getActivity());
            dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            dialog.setMessage(getArguments().getString(MESSAGE));
            dialog.setCancelable(false);
            dialog.setCanceledOnTouchOutside(false);
            return dialog;
        }
    }
}
