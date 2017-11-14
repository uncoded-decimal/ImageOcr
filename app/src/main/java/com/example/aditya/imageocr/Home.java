package com.example.aditya.imageocr;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class Home extends AppCompatActivity {
    Button b;
    RecyclerView list;
    DBHelper db;
    ArrayList<type> arr;
    listadapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        b=(Button) findViewById(R.id.button);
        list = (RecyclerView)findViewById(R.id.list);

        b.setText("ADD A NEW FILE");
        b.setTextSize(32.0f);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i =new Intent(getApplicationContext(), MainActivity.class);
                startActivity(i);
            }
        });
        db=new DBHelper(this);
        arr=db.getData();
        
        list.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        updateUI();
    }

    private void updateUI() {
        arr = db.getData();
        adapter=new listadapter(arr);
        list.setAdapter(adapter);
    }

    private class listholder extends RecyclerView.ViewHolder {
        TextView t;
        TextView d;
        public listholder(View itemView) {
            super(itemView);
            t=(TextView)findViewById(R.id.title);
            d=(TextView)findViewById(R.id.data);
        }
    }

    private class listadapter extends RecyclerView.Adapter<listholder>{

        private ArrayList<type> list;
        public listadapter(ArrayList<type> ll){
            list=ll;
        }

        @Override
        public listholder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater= LayoutInflater.from(getParent());
            View view=layoutInflater.inflate(R.layout.list_layout,parent,false);
            return new listholder(view);
        }

        @Override
        public void onBindViewHolder(listholder holder, int position) {
            type t=list.get(position);
            holder.t.setText(t.getTitle());
            holder.d.setText(t.getData());
        }

        @Override
        public int getItemCount() {
            return list.size();
        }
    }
}
