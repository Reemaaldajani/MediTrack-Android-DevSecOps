package com.example.meditrack;

import android.app.Dialog;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

public class DeleteConfirmationDialog extends DialogFragment {

    public interface OnDeleteConfirmListener {
        void onConfirm();
    }

    private OnDeleteConfirmListener listener;

    public DeleteConfirmationDialog(OnDeleteConfirmListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Delete Record")
                .setMessage("Are you sure you want to delete this record?")
                .setPositiveButton("Delete", (dialog, which) -> {
                    if (listener != null) listener.onConfirm();
                })
                .setNegativeButton("Cancel", (dialog, which) -> dismiss());
        return builder.create();
    }
}