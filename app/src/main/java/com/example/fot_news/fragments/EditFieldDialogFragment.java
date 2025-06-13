package com.example.fot_news.fragments; // Make sure this path is correct or adjust

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.fot_news.R;

public class EditFieldDialogFragment extends DialogFragment {

    public static final String TAG = "EditFieldDialogFragment";
    private static final String ARG_TITLE = "title";
    private static final String ARG_CURRENT_VALUE = "current_value";
    private static final String ARG_FIELD_NAME = "field_name";
    private static final String ARG_INPUT_TYPE = "input_type";

    public interface EditFieldDialogListener {
        void onSaveField(String fieldName, String newValue);
    }

    private EditFieldDialogListener listener;
    private EditText dialogEditText;
    private String fieldName;
    private int inputType;

    public static EditFieldDialogFragment newInstance(String title, String currentValue, String fieldName, int inputType) {
        EditFieldDialogFragment fragment = new EditFieldDialogFragment();
        Bundle args = new Bundle();
        args.putString(ARG_TITLE, title);
        args.putString(ARG_CURRENT_VALUE, currentValue);
        args.putString(ARG_FIELD_NAME, fieldName);
        args.putInt(ARG_INPUT_TYPE, inputType);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            listener = (EditFieldDialogListener) getTargetFragment(); // For fragment communication
            if (listener == null) { // If target fragment is not set, try activity
                listener = (EditFieldDialogListener) context;
            }
        } catch (ClassCastException e) {
            throw new ClassCastException("Calling context must implement EditFieldDialogListener");
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_edit_field, container, false);

        TextView dialogTitle = view.findViewById(R.id.dialog_title);
        dialogEditText = view.findViewById(R.id.dialog_edit_text);
        Button btnSave = view.findViewById(R.id.btn_save);
        Button btnCancel = view.findViewById(R.id.btn_cancel);

        if (getArguments() != null) {
            dialogTitle.setText(getArguments().getString(ARG_TITLE));
            dialogEditText.setText(getArguments().getString(ARG_CURRENT_VALUE));
            fieldName = getArguments().getString(ARG_FIELD_NAME);
            inputType = getArguments().getInt(ARG_INPUT_TYPE);
            dialogEditText.setInputType(inputType);
            dialogEditText.setSelection(dialogEditText.getText().length()); // Place cursor at end
        }

        btnSave.setOnClickListener(v -> {
            if (listener != null) {
                listener.onSaveField(fieldName, dialogEditText.getText().toString().trim());
            }
            dismiss();
        });

        btnCancel.setOnClickListener(v -> dismiss());

        // Request focus and show keyboard
        dialogEditText.requestFocus();
        if (getDialog() != null && getDialog().getWindow() != null) {
            getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        }

        return view;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        return dialog;
    }
}