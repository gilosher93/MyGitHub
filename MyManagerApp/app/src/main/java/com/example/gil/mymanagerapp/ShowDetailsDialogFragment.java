package com.example.gil.mymanagerapp;

import android.app.DialogFragment;
import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Gil on 23/08/2015.
 */
public class ShowDetailsDialogFragment extends DialogFragment {
    ArrayList<Worker> workers;
    int position;
    TextView txtName,txtPhone;
    Button btnEditWorker;

    public void setFragment (ArrayList<Worker> workers,int position){
        this.workers = workers;
        this.position = position;

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_show_details,container);
        txtName = (TextView)view.findViewById(R.id.txtShowName);
        txtPhone = (TextView)view.findViewById(R.id.txtShowPhone);
        btnEditWorker = (Button)view.findViewById(R.id.btnEdit);
        txtName.setText(workers.get(position).getFirstName().toString() +
                " " + workers.get(position).getLastName().toString());
        txtPhone.setText(workers.get(position).getPhone().toString());
        btnEditWorker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getFragmentManager();
                EditDialogFragment editDialogFragment = new EditDialogFragment();
                editDialogFragment.setFragment(workers,position);
                editDialogFragment.show(fragmentManager, "edit worker");
                dismiss();
            }
        });
        return view;
    }
}
