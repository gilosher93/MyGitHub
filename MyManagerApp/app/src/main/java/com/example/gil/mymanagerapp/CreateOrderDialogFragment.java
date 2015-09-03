package com.example.gil.mymanagerapp;

import android.app.DialogFragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

/**
 * Created by Gil on 30/07/2015.
 */
public class CreateOrderDialogFragment extends DialogFragment {
    ArrayList<OrderObj> myOrders;
    EditText txtRstName;
    EditText txtAddressStr;
    EditText txtAddressNum;
    EditText txtTimeToPickUp;
    Button btnCreateOrder;
    CreateOrderListener listener;
    ArrayList<RstObj> myRsts;
    
    AutoCompleteTextView textView;

    public void setFragment (CreateOrderListener listener,ArrayList<OrderObj> myOrders,ArrayList<RstObj> myRsts){
        this.listener = listener;
        this.myOrders = myOrders;
        this.myRsts=myRsts;
    }

    public static interface CreateOrderListener {
        void onFinishCreateOrder(int id);
        void invalidCreate();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_create_order,container);
        btnCreateOrder = (Button)view.findViewById(R.id.btnCreateOrder);
        //Log.d("Gil","rasturants: " + myRsts);
        ArrayAdapter<RstObj> adapter = new ArrayAdapter<RstObj>(view.getContext(),android.R.layout.simple_dropdown_item_1line,myRsts);
        AutoCompleteTextView textView = (AutoCompleteTextView) view.findViewById(R.id.txtRstName);
        textView.setThreshold(1);
        textView.setAdapter(adapter);
        txtRstName = (EditText) view.findViewById(R.id.txtRstName);
        txtRstName.setText(HomeActivity.currentUser);
        txtAddressStr = (EditText) view.findViewById(R.id.txtAddressStr);
        txtAddressNum = (EditText) view.findViewById(R.id.txtAddressNum);
        txtTimeToPickUp = (EditText) view.findViewById(R.id.txtTimeToPickUp);
        btnCreateOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (txtRstName.getText().toString().length() == 0 || txtAddressStr.getText().toString().length() == 0 ||
                        txtAddressNum.getText().toString().length()==0 || txtTimeToPickUp.getText().toString().length()==0) {
                    listener.invalidCreate();
                    return;
                }
                final String rstName = txtRstName.getText().toString();
                final String addressStr = txtAddressStr.getText().toString();
                final int addressNum = Integer.valueOf(txtAddressNum.getText().toString());
                final int timeToPickUp = Integer.valueOf(txtTimeToPickUp.getText().toString());
                myOrders.add(new OrderObj(rstName, addressStr, addressNum, timeToPickUp));
                listener.onFinishCreateOrder(myOrders.size()-1);
                dismiss();
            }
        });
        txtRstName.requestFocus();
        getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);


        return view;
    }
}
