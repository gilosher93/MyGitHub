package com.example.gil.mymanagerapp.DialogFragments;

import android.app.DialogFragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;

import com.example.gil.mymanagerapp.Activities.HomeActivity;
import com.example.gil.mymanagerapp.ObjectClasses.OrderObj;
import com.example.gil.mymanagerapp.ObjectClasses.Restaurant;
import com.example.gil.mymanagerapp.R;

import java.util.ArrayList;

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
    ArrayList<Restaurant> myRsts;
    
    AutoCompleteTextView textView;

    public void setFragment (CreateOrderListener listener,ArrayList<OrderObj> myOrders,ArrayList<Restaurant> myRsts){
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
        ArrayAdapter<Restaurant> adapter = new ArrayAdapter<Restaurant>(view.getContext(),android.R.layout.simple_dropdown_item_1line,myRsts);
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
                String rstName = txtRstName.getText().toString();
                String addressStr = txtAddressStr.getText().toString();
                int addressNum = Integer.valueOf(txtAddressNum.getText().toString());
                int timeToPickUp = Integer.valueOf(txtTimeToPickUp.getText().toString());
                if (rstName.length() == 0 || addressStr.length() == 0 || addressNum ==0 || timeToPickUp==0) {
                    listener.invalidCreate();
                    return;
                }
                listener.onFinishCreateOrder(myOrders.size()-1);
                dismiss();
            }
        });
        txtRstName.requestFocus();
        getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);


        return view;
    }
}
