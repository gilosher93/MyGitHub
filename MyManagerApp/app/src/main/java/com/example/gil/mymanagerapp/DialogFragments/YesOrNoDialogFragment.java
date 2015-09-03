package com.example.gil.mymanagerapp.DialogFragments;

import android.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.gil.mymanagerapp.R;

/**
 * Created by Ariel on 9/1/2015.
 */
public class YesOrNoDialogFragment extends DialogFragment {
    Button btnYes,btnNo;
    TextView txtTitle;
    boolean yesOrNo = false;
    String userName,password,title;
    YesOrNoDialogListener listener;

    public void setFragment (YesOrNoDialogListener listener, String userName, String password,String title){
        this.userName = userName;
        this.password = password;
        this.listener = listener;
        this.title = title;

    }
    public static interface YesOrNoDialogListener{
        void answer(boolean answer);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_yes_or_no,container);
        txtTitle = (TextView) view.findViewById(R.id.txtTitle);
        txtTitle.setText(title+"");
        btnYes = (Button)view.findViewById(R.id.btnYes);
        btnNo= (Button) view.findViewById(R.id.btnNo);
        btnYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.answer(true);
                dismiss();
            }
        });
        btnNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.answer(false);
                dismiss();
            }
        });
        return view;
    }
}
