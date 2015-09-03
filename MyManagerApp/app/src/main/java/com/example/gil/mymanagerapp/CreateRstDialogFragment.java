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
 * Created by Gil on 2/08/2015.
 */
public class CreateRstDialogFragment extends DialogFragment {
    ArrayList<RstObj> myRsts;
    EditText txtUserName;
    EditText txtPassword;
    Button btnCreateRst;
    CreateRstListener listener;

    public void setFragment (CreateRstListener listener, ArrayList<RstObj> myRsts){
        this.listener = listener;
        this.myRsts = myRsts;
    }

    public static interface CreateRstListener {
        void onFinishCreateRst(RstObj rstObj);
        void invalidCreate();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_create_rst,container);
        btnCreateRst = (Button)view.findViewById(R.id.btnCreateRst);
        txtUserName = (EditText)view.findViewById(R.id.txtUserName);
        txtPassword = (EditText)view.findViewById(R.id.txtPassword);
        btnCreateRst.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (txtUserName.getText().toString().length() == 0 || txtPassword.getText().toString().length() == 0) {
                    listener.invalidCreate();
                    return;
                }
                final String userName = txtUserName.getText().toString();
                final String password = txtPassword.getText().toString();

                //myRsts.add(new RstObj(new User(userName,password)));

                listener.onFinishCreateRst(myRsts.get(0));
                dismiss();
            }
        });
        txtUserName.requestFocus();

        //make the keyboard appear automatically
        getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);

        return view;
    }
}
