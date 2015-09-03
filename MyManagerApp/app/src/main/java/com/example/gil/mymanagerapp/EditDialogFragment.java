package com.example.gil.mymanagerapp;

import android.app.DialogFragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import java.util.ArrayList;

/**
 * Created by Gil on 23/08/2015.
 */
public class EditDialogFragment extends DialogFragment {
    ArrayList<Worker> workers;
    int position;
    EditText txtEditFirstName,txtEditLastName,txtEditPhone;
    Button btnOk,btnCancel;

    public void setFragment (ArrayList<Worker> workers,int position){
        this.workers = workers;
        this.position = position;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_edit_details,container);
        btnOk = (Button) view.findViewById(R.id.btnOk);
        btnCancel = (Button) view.findViewById(R.id.btnCancel);
        txtEditFirstName = (EditText)view.findViewById(R.id.txtEditFirstName);
        txtEditLastName = (EditText)view.findViewById(R.id.txtEditLastName);
        txtEditPhone = (EditText)view.findViewById(R.id.txtEditPhone);
        txtEditFirstName.setText(workers.get(position).getFirstName().toString());
        txtEditLastName.setText(workers.get(position).getLastName().toString());
        txtEditPhone.setText(workers.get(position).getPhone().toString());
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String firstName = txtEditFirstName.getText().toString();
                final String lastName = txtEditLastName.getText().toString();
                final String phone = txtEditPhone.getText().toString();
                if (firstName.length() == 0 || lastName.length() == 0 || phone.length() == 0) {
                    return;
                }
                workers.get(position).setFirstName(firstName);
                workers.get(position).setLastName(lastName);
                workers.get(position).setPhone(phone);
                dismiss();
            }
        });
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        return view;
    }
}
