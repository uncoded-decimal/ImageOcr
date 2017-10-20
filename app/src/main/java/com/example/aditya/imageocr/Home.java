package com.example.aditya.imageocr;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;

public class Home extends AppCompatActivity {
    Button b;
    ListView list;
    DBHelper db;
    ArrayList<type> arr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        b=(Button) findViewById(R.id.button);
        list = (ListView)findViewById(R.id.list);

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
        DArrayAdapter adapter=new DArrayAdapter(this,R.layout.list_layout,arr);
        list.setAdapter(adapter);
    }
}
