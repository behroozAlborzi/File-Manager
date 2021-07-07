package com.behroozalborzi.filemanager;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

/**
 * Created by Behrooz on 7/4/2021.
 * https://behroozalborzi.ir
 * Android Developer
 * Thank you ... :)
 */
public class AddNewFolderDialog extends DialogFragment {

    private AddFolderCallBack addFolderCallBack;
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        addFolderCallBack = (AddFolderCallBack) context;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        View view = LayoutInflater.from(getContext()).inflate(R.layout.dialog_add_new_folder,null,false);
        TextInputEditText editTextName = view.findViewById(R.id.et_file_name);
        TextInputLayout etLayout = view.findViewById(R.id.etl_file_name);
        view.findViewById(R.id.btn_dialogAdd_create).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (editTextName.length()>0){
                    addFolderCallBack.onAddFolderClicked(editTextName.getText().toString());
                    dismiss();
                }else{
                    etLayout.setError("Folder name cannot be empty");
                }
            }
        });
        view.findViewById(R.id.btn_dialogAdd_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        return builder.setView(view).create();
    }



    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }
}
