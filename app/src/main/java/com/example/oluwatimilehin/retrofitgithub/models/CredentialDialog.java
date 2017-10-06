package com.example.oluwatimilehin.retrofitgithub.models;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import com.example.oluwatimilehin.retrofitgithub.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Oluwatimilehin on 05/10/2017.
 * oluwatimilehinadeniran@gmail.com.
 */

public class CredentialDialog extends DialogFragment {

    @BindView(R.id.nameEditText)
    EditText usernameField;
    @BindView(R.id.passwordTv)
    EditText passwordField;
    CredentialsDialogListener listener;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();

        View view = inflater.inflate(R.layout.dialog_credentials, null);
        ButterKnife.bind(this, view);

        usernameField.setText(getArguments().getString("username"));
        passwordField.setText(getArguments().getString("password"));

        builder.setView(view)
                .setTitle("Credentials")
                .setNegativeButton("Cancel", null)
                .setPositiveButton("Continue", ((dialog, which) -> {
                    if (listener != null) {
                        listener.onDialogPositiveClick(usernameField.getText().toString(),
                                passwordField.getText().toString());
                    }
                }));

        return builder.create();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if (getActivity() instanceof CredentialsDialogListener) {
            listener = (CredentialsDialogListener) getActivity();
        }
    }

    public interface CredentialsDialogListener {
        void onDialogPositiveClick(String username, String password);
    }
}
