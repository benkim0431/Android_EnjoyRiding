package com.skh.enjoyriding.utils;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

public class CustomDialog extends DialogFragment {
    String message;
    String title;
    String positivieBtnStr;
    String negativeBtnStr;

    public CustomDialog(String message, String title, String positiveBtn, String negativeBtn) {
        this.message = message;
        this.title = title;
        this.positivieBtnStr = positiveBtn;
        this.negativeBtnStr = negativeBtn;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        if (negativeBtnStr != null && negativeBtnStr.length() > 1) {
            builder.setMessage(message)
                    .setTitle(title)
                    .setPositiveButton(positivieBtnStr, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //Do nothing
                        }
                    }).setNegativeButton(negativeBtnStr, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //Do nothing;
                        }
                    });
        } else {
            builder.setMessage(message)
                    .setTitle(title)
                    .setPositiveButton(positivieBtnStr, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //Do nothing
                        }
                    });
        }
        return builder.create();
    }
}
