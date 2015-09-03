package com.example.gil.mymanagerapp;

import android.app.DialogFragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import java.util.ArrayList;

/**
 * Created by Gil on 31/07/2015.
 */
public class CreateWorkerDialogFragment extends DialogFragment {
    ArrayList<Worker> workers;
    EditText txtFirstName;
    EditText txtLastName;
    EditText txtDate;
    EditText txtPhone;
    Button btnAddWorker;
    CreateWorkerListener listener;

    public void setFragment (CreateWorkerListener listener, ArrayList<Worker> workers){
        this.listener = listener;
        this.workers = workers;
    }

    public static interface CreateWorkerListener {
        void onFinishCreateWorker(int id);
        void invalidCreate();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_create_worker,container);
        btnAddWorker = (Button)view.findViewById(R.id.btnAddWorker);
        txtFirstName = (EditText)view.findViewById(R.id.txtFirstName);
        txtLastName = (EditText) view.findViewById(R.id.txtLastName);
        txtDate = (EditText) view.findViewById(R.id.txtDate);
        txtPhone = (EditText) view.findViewById(R.id.txtPhone);
        btnAddWorker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String firstName = txtFirstName.getText().toString();
                final String lastName = txtLastName.getText().toString();
                final String date = txtDate.getText().toString();
                final String phone= txtPhone.getText().toString();
                if(firstName.length() == 0 || lastName.length() == 0 || date.length()==0 || phone.length()==0){
                    listener.invalidCreate();
                    return;
                }
                workers.add(new Worker(firstName,lastName,phone));
                listener.onFinishCreateWorker(workers.size()-1);
                dismiss();
            }
        });
        txtFirstName.requestFocus();
        getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        return view;
    }
}
