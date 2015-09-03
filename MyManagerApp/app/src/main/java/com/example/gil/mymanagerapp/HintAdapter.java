package com.example.gil.mymanagerapp;

import android.content.Context;
import android.widget.ArrayAdapter;

import java.util.ArrayList;

/**
 * Created by Gil on 17/08/2015.
 */
public class HintAdapter extends ArrayAdapter<Worker> {
        private Context context;
        private ArrayList<Worker> activeWorkers;
        private int resource;

        public HintAdapter(Context theContext,int resource, ArrayList<Worker> activeWorkers) {
            super(theContext, resource, activeWorkers);
        }

        @Override
    public int getCount() {
        // don't display last item. It is used as hint.
        int count = super.getCount();
        return count > 0 ? count - 1 : count;
    }
}