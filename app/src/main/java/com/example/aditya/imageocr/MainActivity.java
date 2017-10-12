package com.example.aditya.imageocr;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.SparseArray;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.text.TextBlock;
import com.google.android.gms.vision.text.TextRecognizer;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import static com.example.aditya.imageocr.R.id.imageView;

public class MainActivity extends AppCompatActivity {

    StringBuilder detectedText;
    EditText edit;
    private static final int PICK_IMAGE=100;
    Uri imageUri;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Bitmap bmp=null;
        openGallery();

        try{
        bmp= BitmapFactory.decodeStream(getContentResolver().openInputStream(imageUri));}
        catch (FileNotFoundException e){
            e.printStackTrace();
        }
        detectedText= new StringBuilder();

            Bitmap bitmap = Bitmap.createBitmap(bmp,0,0,bmp.getWidth(),bmp.getHeight()/2+40);
            gettingTextFromBitmap(bitmap);

        bitmap = Bitmap.createBitmap(bmp,0,bmp.getHeight()/2,bmp.getWidth(),bmp.getHeight()/2);
        gettingTextFromBitmap(bitmap);
        edit = (EditText) findViewById(R.id.me);
        edit.setText(detectedText,EditText.BufferType.EDITABLE);
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
    private void openGallery(){
        Intent gallery= new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(gallery,PICK_IMAGE);
    }

    @Override
    protected void onActivityResult(int requestCode,int resultCode, Intent data){
        super.onActivityResult(requestCode,resultCode,data);
        ImageView imageView = (ImageView)findViewById(R.id.imageView);
        if(requestCode==RESULT_OK || requestCode==PICK_IMAGE){
            imageUri=data.getData();
            imageView.setImageURI(imageUri);
        }
    }
}