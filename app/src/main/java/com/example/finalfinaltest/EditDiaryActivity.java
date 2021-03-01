package com.example.finalfinaltest;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class EditDiaryActivity extends AppCompatActivity {


    Button storeBtn, exitBtn;
    SQLiteDatabase db;
    String title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_diary);

        init();
        final EditText dateTxt = (EditText) findViewById(R.id.inputDate2);
        final EditText titleTxt = (EditText) findViewById(R.id.inputTitle2);
        final EditText contentTxt = (EditText) findViewById(R.id.inputContents2);

        try{
            db = openOrCreateDatabase("myDiary", Activity.MODE_PRIVATE, null);
            Cursor c1 = db.rawQuery("select work, date, title, content from Diary where title = '" + title +"'", null);
            while(c1.moveToNext()){

                String work = c1.getString(0);
                String date = c1.getString(1);
                String title1 = c1.getString(2);
                String content = c1.getString(3);
                dateTxt.setText(date);
                titleTxt.setText(title1);
                contentTxt.setText(content);

                System.out.println(work+date+title1+content);

            }


            c1.close();
            db.close();
        }catch (Exception e){
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), "exception", Toast.LENGTH_LONG).show();
        }

        storeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String date = dateTxt.getText().toString().trim();
                String title2 = titleTxt.getText().toString().trim();
                String content = contentTxt.getText().toString().trim();
                try {
                    db = openOrCreateDatabase("myDiary", Activity.MODE_PRIVATE, null);
                    db.execSQL("Update Diary Set date ='" + date + "', title = '" + title2 + "', content ='" + content +
                                    "'where title = '" + title +"'");
                    db.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }

                Intent intent = new Intent(getApplicationContext(), RecordActivity.class);
                startActivity(intent);
            }
        });
        exitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditDiaryActivity.this.finish();
            }
        });

    }

    public void init(){


        //버튼
        storeBtn = (Button) findViewById(R.id.storeButton2);
        exitBtn = (Button) findViewById(R.id.exitButton2);
        //데이터수신
        Intent intent = getIntent();
        title = intent.getStringExtra("title");
    }



}