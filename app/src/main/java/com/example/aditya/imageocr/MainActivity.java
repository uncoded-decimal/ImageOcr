package com.example.aditya.imageocr;

import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.SparseArray;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.text.TextBlock;
import com.google.android.gms.vision.text.TextRecognizer;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import static com.example.aditya.imageocr.R.id.imageView;

public class MainActivity extends AppCompatActivity {

    StringBuilder detectedText;
    EditText edit, title;
    private static final int PICK_IMAGE=100;
    Uri imageUri;
    ImageView imageView;
    Bitmap bmp=null;
    DBHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        edit = (EditText) findViewById(R.id.me);
        title = (EditText)findViewById(R.id.title);
        imageView=(ImageView)findViewById(R.id.imageView);
        Intent gallery = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(gallery, PICK_IMAGE);

        db = new DBHelper(this);

        Button save = (Button)findViewById(R.id.save);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String t = title.getText().toString();
                String d = detectedText.toString();
                db.addData(t,d);
                Toast.makeText(getApplicationContext(),"Successfully Added!",Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }


    private void gettingTextFromBitmap(Bitmap bitmap) {

        TextRecognizer textRecognizer = new TextRecognizer.Builder(this).build();
        try {
            if (!textRecognizer.isOperational()) {
                new AlertDialog.
                        Builder(this).
                        setMessage("Text recognizer could not be set up on your device").show();
                return;
            }

            Frame frame = new Frame.Builder().setBitmap(bitmap).build();
            SparseArray<TextBlock> origTextBlocks = textRecognizer.detect(frame);
            List<TextBlock> textBlocks = new ArrayList<>();
            for (int i = 0; i < origTextBlocks.size(); i++) {
                TextBlock textBlock = origTextBlocks.valueAt(i);
                textBlocks.add(textBlock);
            }
            Collections.sort(textBlocks, new Comparator<TextBlock>() {
                @Override
                public int compare(TextBlock o1, TextBlock o2) {
                    int diffOfTops = o1.getBoundingBox().top - o2.getBoundingBox().top;
                    int diffOfLefts = o1.getBoundingBox().left - o2.getBoundingBox().left;
                    if (diffOfTops != 0) {
                        return diffOfTops;
                    }
                    return diffOfLefts;
                }
            });

            for (TextBlock textBlock : textBlocks) {
                if (textBlock != null && textBlock.getValue() != null) {
                    detectedText.append(textBlock.getValue());
                    detectedText.append("\n");
                }
            }

        }  finally {
        textRecognizer.release();
    }
    bitmap.recycle();
}

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==RESULT_OK||requestCode==PICK_IMAGE){
            imageUri=data.getData();
            imageView.setImageURI(imageUri);
                getresult();
        }
        else if(requestCode==RESULT_CANCELED){
            finish();
        }
    }

    void getresult(){
        try {
            bmp=MediaStore.Images.Media.getBitmap(this.getContentResolver(),imageUri);
        } catch (IOException e) {
            e.printStackTrace();
        }
        catch ( NullPointerException f){
            f.printStackTrace();
        }
        detectedText=new StringBuilder();

        Bitmap bitmap = Bitmap.createBitmap(bmp,0,0,bmp.getWidth(),bmp.getHeight()/2+40);
        gettingTextFromBitmap(bitmap);

        bitmap = Bitmap.createBitmap(bmp,0,bmp.getHeight()/2,bmp.getWidth(),bmp.getHeight()/2);
        gettingTextFromBitmap(bitmap);
        edit.setText(detectedText);
        edit.setLines(10);
    }
}