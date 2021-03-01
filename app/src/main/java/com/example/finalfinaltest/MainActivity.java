package com.example.finalfinaltest;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    Button recordBtn, startBtn, editBtn;
    SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recordBtn = (Button) findViewById(R.id.recordWork);
        startBtn = (Button) findViewById(R.id.startWork);
        editBtn = (Button) findViewById(R.id.editWork);
        initDB();

        TextView information = findViewById(R.id.information);
        information.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),Pop.class));
            }
        });

        editBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), EditWorkActivity.class);
                startActivity(intent);
            }
        });

        startBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //운동하기
                Intent intent = new Intent(getApplicationContext(), StartWorkOutActivity.class);
                startActivity(intent);
            }
        });
        recordBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //운동기록
                Intent intent = new Intent(getApplicationContext(), RecordActivity.class);
                startActivity(intent);
            }
        });
    }

    public void initDB(){
        try{
            db = openOrCreateDatabase("myDiary", Activity.MODE_PRIVATE, null);
            db.execSQL("create table if not exists Diary (id integer PRIMARY KEY autoincrement, work text not null, date text not null, title text not null, content text not null);");
            db.execSQL("create table if not exists PLAY (id integer PRIMARY KEY autoincrement, image int, name text not null, body text not null);");
            db.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}