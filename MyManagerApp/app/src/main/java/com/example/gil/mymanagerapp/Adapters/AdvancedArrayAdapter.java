package com.example.gil.mymanagerapp.Adapters;

import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.gil.mymanagerapp.Activities.ManagerActivity;
import com.example.gil.mymanagerapp.ObjectClasses.OrderObj;
import com.example.gil.mymanagerapp.ObjectClasses.Worker;

import java.util.ArrayList;

/**
 * Created by Gil on 10/08/2015.
 */
public class AdvancedArrayAdapter extends ArrayAdapter<OrderObj>{
    private String BusinessName, Address;
    private int orderId;
    private ArrayList<OrderObj> myOrders;
    private ArrayList<Worker> activeWorkers;
    private Activity activity;
    private int resource, textOrderResourceId,textBusinessResourceId,textAddressResourceId,textTimeResourceId, spnMessengerResourceId,textPickUpTimeResourceId;


    public AdvancedArrayAdapter(Activity context, ArrayList<OrderObj> myOrders,ArrayList<Worker> activeWorkers, int resource, int textOrderResourceId,int textPickUpTimeResourceId,int textTimeResourceId,
                                int textBusinessResourceId, int textAddressResourceId, int spnMessengerResourceId) {
        super(context, resource, myOrders);
        this.textOrderResourceId = textOrderResourceId;
        this.textTimeResourceId = textTimeResourceId;
        this.textBusinessResourceId = textBusinessResourceId;
        this.textAddressResourceId = textAddressResourceId;
        this.spnMessengerResourceId = spnMessengerResourceId;
        this.textPickUpTimeResourceId = textPickUpTimeResourceId;
        activity = context;
        this.resource = resource;
        this.myOrders = myOrders;
        this.activeWorkers = activeWorkers;
    }

    static class ViewContainer{
        TextView txtOrder;
        TextView txtTime;
        TextView txtBusiness;
        TextView txtAddress;
        Spinner spnMessenger;
        TextView txtPickUpTime;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewContainer viewContainer;
        View rowView = convertView;
        if (rowView == null) {
            Log.d("Gil","AdvancedArrayAdapter - new");
            viewContainer = new ViewContainer();
            LayoutInflater inflater = activity.getLayoutInflater();
            rowView = inflater.inflate(resource, null, true);
            viewContainer.txtOrder = (TextView) rowView.findViewById(textOrderResourceId);
            viewContainer.txtTime = (TextView) rowView.findViewById(textTimeResourceId);
            viewContainer.txtBusiness = (TextView) rowView.findViewById(textBusinessResourceId);
            viewContainer.txtAddress = (TextView) rowView.findViewById(textAddressResourceId);
            viewContainer.spnMessenger = (Spinner) rowView.findViewById(spnMessengerResourceId);
            viewContainer.txtPickUpTime = (TextView)rowView.findViewById(textPickUpTimeResourceId);
            rowView.setTag(viewContainer);

        } else {
            Log.d("Gil", "AdvancedArrayAdapter - recycling");
            viewContainer = (ViewContainer) rowView.getTag();
        }
        if (myOrders != null && position < myOrders.size()){
            viewContainer.txtOrder.setText(myOrders.get(position).getOrderId()+"");
            viewContainer.txtBusiness.setText(myOrders.get(position).getRstName());
            viewContainer.txtAddress.setText(myOrders.get(position).getAddressStr() + " " + myOrders.get(position).getAddressNum());
            long time = myOrders.get(position).getOrderTime();
            String stringTime = ManagerActivity.whatTimeIsIt(time);
            viewContainer.txtTime.setText(stringTime);
            HintAdapter spinnerAdapter = new HintAdapter(rowView.getContext(),android.R.layout.simple_spinner_dropdown_item,activeWorkers);
            viewContainer.spnMessenger.setAdapter(spinnerAdapter);
            viewContainer.spnMessenger.setSelection(spinnerAdapter.getCount());
            int pickUp = myOrders.get(position).getTimeToPickUp();
            String pickUpStr = pickUp+"";
            if(myOrders.get(position).getTimeToPickUp()==0)
                pickUpStr = "Ready!";
            viewContainer.txtPickUpTime.setText(pickUpStr+"");
        }
        return rowView;
    }
}
