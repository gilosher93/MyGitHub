package com.example.gil.mymanagerapp;

import android.app.DialogFragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;

import java.util.ArrayList;

/**
 * Created by Gil on 16/08/2015.
 */
public class WorkerOptionFragment extends DialogFragment {
    WorkerOptionListener listener;
    ArrayList<Worker> activeWorkers;
    ArrayAdapter<Worker> adapter;
    int position;
    Button btnRemoveFromShift;
    Button btnInfo;
    Button btnShowWorkersOrderArray;

    public void setFragment (ArrayAdapter<Worker> adapter,ArrayList<Worker> activeWorkers,int position){
        this.activeWorkers = activeWorkers;
        this.position = position;
        this.adapter = adapter;

    }

    public static interface WorkerOptionListener{
        boolean removeFromShift(int id);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_worker_options,container);
        btnRemoveFromShift = (Button)view.findViewById(R.id.btnRemoveFromShift);
        btnInfo= (Button) view.findViewById(R.id.btnInfo);
        btnShowWorkersOrderArray= (Button) view.findViewById(R.id.btnShowOrders);
        btnRemoveFromShift.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activeWorkers.remove(activeWorkers.get(position));
                adapter.notifyDataSetChanged();
                dismiss();
            }
        });
        btnInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getFragmentManager();
                ShowDetailsDialogFragment showDetailsDialogFragment = new ShowDetailsDialogFragment();
                showDetailsDialogFragment.setFragment(activeWorkers, position);
                showDetailsDialogFragment.show(fragmentManager, "show details");
                dismiss();
            }
        });
        return view;
    }
}
