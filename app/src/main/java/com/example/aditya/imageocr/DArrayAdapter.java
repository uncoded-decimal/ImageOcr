package com.example.aditya.imageocr;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by andi on 10/19/2017.
 */

class DArrayAdapter extends ArrayAdapter<type> {
    Context context;
    ArrayList<type> arrayList;
    public DArrayAdapter(Context context, int resource, ArrayList<type> arayList) {
        super(context, resource, arayList);
        this.context=context;
        arrayList=arayList;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.list_layout, parent, false);

        TextView t=(TextView)view.findViewById(R.id.title);
        TextView d=(TextView)view.findViewById(R.id.data);

        t.setText(arrayList.get(position).title);
        d.setText(arrayList.get(position).data);

        return view;
    }
}
