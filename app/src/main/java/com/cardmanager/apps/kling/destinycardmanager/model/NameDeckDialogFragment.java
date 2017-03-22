package com.cardmanager.apps.kling.destinycardmanager.model;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.widget.EditText;

import com.cardmanager.apps.kling.destinycardmanager.R;

/**
 * The dialog fragment handling naming new decks.
 * Created by danie on 2016-12-18.
 */

public class NameDeckDialogFragment extends DialogFragment {

    /* The activity that creates an instance of this dialog fragment must
     * implement this interface in order to receive event callbacks.
     * Each method passes the DialogFragment in case the host needs to query it. */
    public interface NoticeDialogListener {
        public void onNameEntered(String name);
    }

    // Use this instance of the interface to deliver action events
    NoticeDialogListener listener;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        // Verify that the host activity implements the callback interface
        try {
            // Instantiate the NoticeDialogListener so we can send events to the host
            listener = (NoticeDialogListener) getActivity();
        } catch (ClassCastException e) {
            // The activity doesn't implement the interface, throw exception
            throw new ClassCastException(getActivity().toString()
                    + " must implement NoticeDialogListener");
        }
    }

    @Override
    public Dialog onCreateDialog(final Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction

        LayoutInflater inflater = getActivity().getLayoutInflater();

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Enter deck name").setView(inflater.inflate(R.layout.name_deck_dialog, null)).
                setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        EditText etDeckName = (EditText) ((AlertDialog) dialog).findViewById(R.id.etDeckName);
                        listener.onNameEntered(etDeckName.getText().toString());
                    }
                });

        // Create the AlertDialog object and return it
        return builder.create();
    }
}
