package com.kulpin.project.travelcompanion.interactive;

import android.app.Activity;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.kulpin.project.travelcompanion.BasicActivity;
import com.kulpin.project.travelcompanion.R;
import com.kulpin.project.travelcompanion.dto.Link;


public class NewLinkDialogFragment extends DialogFragment {

    private EditText editTitle;
    private EditText editAddress;

    private NewLinkDialogListener newLinkDialogListener;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        LayoutInflater inflater = getActivity().getLayoutInflater();
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        View view = inflater.inflate(R.layout.dialog_link_create, null);

        editTitle = (EditText) view.findViewById(R.id.dialog_link_title);
        editAddress = (EditText) view.findViewById(R.id.dialog_link_address);

        builder.setView(view).setTitle(getString(R.string.dialog_link_title))
                .setNegativeButton(R.string.dialog_cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        NewLinkDialogFragment.this.getDialog().cancel();
                    }
                })
                .setPositiveButton(R.string.dialog_apply, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });



        final AlertDialog dialog = builder.create();
        return dialog;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            newLinkDialogListener = (BasicActivity) activity;
        }catch (ClassCastException e){e.printStackTrace();}
    }

    @Override
    public void onStart() {
        super.onStart();
        final AlertDialog dialog = (AlertDialog)getDialog();
        if(dialog != null)
        {
            dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Link newLink = new Link();
                    if (editAddress.getText().toString().isEmpty() ||
                            !Patterns.WEB_URL.matcher(editAddress.getText().toString()).matches()){
                        editAddress.setError(getString(R.string.dialog_invalid_address));
                        return;
                    }
                    newLink.setTitle(editTitle.getText().toString());
                    newLink.setAddress(editAddress.getText().toString());
                    newLinkDialogListener.onDialogPositiveClick(NewLinkDialogFragment.this, newLink);
                    dialog.dismiss();
                }
            });
        }
    }

    public interface NewLinkDialogListener{
        public void onDialogPositiveClick(DialogFragment dialog, Link link);
        public void onDialogNegativeClick(DialogFragment dialog);
    }
}
