package com.sravan.efactorapp.UI.Fragments.Gateway;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.InsetDrawable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.sravan.efactorapp.Interfaces.DialogSubmitListener;
import com.sravan.efactorapp.R;
import com.sravan.efactorapp.utils.Utilities;

public class MessageDialog {

    public static void showErrorDialogWithOkBtn(Context context, String defaultText, DialogOkListener dialogOkListener) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (inflater != null) {
            View deleteDialogView = inflater.inflate(R.layout.dialog_error_okay, (ViewGroup) null, false);
            AlertDialog dialog = new AlertDialog.Builder(context).create();
            dialog.setView(deleteDialogView);
            dialog.setCancelable(true);
            dialog.setCanceledOnTouchOutside(true);
            ((TextView) deleteDialogView.findViewById(R.id.txt_message)).setText(defaultText);
            ((Button) deleteDialogView.findViewById(R.id.dialog_btn_ok)).setOnClickListener(new View.OnClickListener() {


                public final void onClick(View view) {
                    MessageDialog.lambda$showErrorDialogWithOkBtn$2(dialog, dialogOkListener, view);
                }
            });
            InsetDrawable inset = new InsetDrawable((Drawable) new ColorDrawable(0), (int) Utilities.convertDpToPixel(40.0f));
            if (dialog.getWindow() != null) {
                dialog.getWindow().setBackgroundDrawable(inset);
            }
            dialog.show();
        }
    }
    static /* synthetic */ void lambda$showErrorDialogWithOkBtn$2(AlertDialog dialog, DialogOkListener dialogOkListener, View v) {
        dialog.dismiss();
        if (dialogOkListener != null) {
            dialogOkListener.onOkClicked();
        }
    }

    public static void showDialogWithEditText(Context context, String title, String defaultText, String hintText, int inputType, DialogSubmitListener submitListener) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (inflater != null) {
            View deleteDialogView = inflater.inflate(R.layout.dialog_edittext_submit, (ViewGroup) null, false);
            AlertDialog dialog = new AlertDialog.Builder(context).create();
            dialog.setView(deleteDialogView);
            dialog.setCancelable(true);
            dialog.setCanceledOnTouchOutside(true);
            EditText editText = (EditText) deleteDialogView.findViewById(R.id.edt_name);
            Button dialogBtnSubmit = (Button) deleteDialogView.findViewById(R.id.dialog_btn_submit);
            ((TextView) deleteDialogView.findViewById(R.id.txt_title)).setText(title);
            if (!TextUtils.isEmpty(defaultText)) {
                editText.setText(defaultText);
            }
            if (!TextUtils.isEmpty(hintText)) {
                editText.setHint(hintText);
            }
            editText.setInputType(inputType);
            dialogBtnSubmit.setOnClickListener(new View.OnClickListener() {


                public final void onClick(View view) {
                    MessageDialog.lambda$showDialogWithEditText$0(dialog, submitListener, editText, view);
                }
            });
            InsetDrawable inset = new InsetDrawable((Drawable) new ColorDrawable(0), (int) Utilities.convertDpToPixel(40.0f));
            if (dialog.getWindow() != null) {
                dialog.getWindow().setBackgroundDrawable(inset);
            }
            dialog.show();
        }
    }

    private static void lambda$showDialogWithEditText$0(AlertDialog dialog, DialogSubmitListener submitListener, EditText editText, View view) {
        dialog.dismiss();
        if (submitListener != null) {
            submitListener.onSubmitClicked(editText.getText().toString().trim());
        }
    }
}
