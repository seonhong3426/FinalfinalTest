package com.example.finalfinaltest;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class InputWorkOutActivity extends AppCompatActivity {
    SQLiteDatabase db;
    Button extraButton, finButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input_work_out);

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int height = dm.heightPixels;

        getWindow().setLayout((int) (width * 0.8) , (int) (height * 0.5));

        extraButton = (Button) findViewById(R.id.extraWork);
        finButton = (Button) findViewById(R.id.finExtra);
        final EditText ed1 = (EditText) findViewById(R.id.inputName);
        final EditText ed2 = (EditText) findViewById(R.id.inputBody);

        extraButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = ed1.getText().toString().trim();
                String body = ed2.getText().toString().trim();
                int resId;
                if(name.equals("")|| body.equals("")){
                    Toast.makeText(getApplicationContext(),"빈 칸 없이 입력해주세요.", Toast.LENGTH_LONG).show();
                    return;
                }
                else{
                    if(name.equals("축구")){
                        resId = R.drawable.soccer;
                        Toast.makeText(getApplicationContext(), resId, Toast.LENGTH_LONG).show();
                    }
                    else if(name.equals("농구")){
                        resId = R.drawable.basketball;
                        Toast.makeText(getApplicationContext(), resId, Toast.LENGTH_LONG).show();
                    }
                    else if(name.equals("배구")){
                        resId = R.drawable.volleyball;
                        Toast.makeText(getApplicationContext(), resId, Toast.LENGTH_LONG).show();
                    }
                    else if(name.equals("탁구")){
                        resId = R.drawable.tabletennis;
                        Toast.makeText(getApplicationContext(), resId, Toast.LENGTH_LONG).show();
                    }
                    else if(name.equals("볼링")){
                        resId = R.drawable.balling;
                        Toast.makeText(getApplicationContext(), resId, Toast.LENGTH_LONG).show();
                    }
                    else if(name.equals("배드민턴")){
                        resId = R.drawable.badminton;
                        Toast.makeText(getApplicationContext(), resId, Toast.LENGTH_LONG).show();
                    }
                    else if(name.equals("복싱") || name.equals("권투")){
                        resId = R.drawable.boxing;
                        Toast.makeText(getApplicationContext(), resId, Toast.LENGTH_LONG).show();
                    }
                    else if(name.equals("펜싱")){
                        resId = R.drawable.fennsing;
                        Toast.makeText(getApplicationContext(), resId, Toast.LENGTH_LONG).show();
                    }
                    else
                        resId = R.drawable.ic_launcher_foreground;
                    try {
                        db = openOrCreateDatabase("myDiary", Activity.MODE_PRIVATE, null);
                        db.execSQL("Insert into PLAY (image, name, body) values ('" + resId + "','"+ name + "', '" + body + "')");
                        db.close();
                       Toast.makeText(getApplicationContext(),name + "운동을 추가하셨습니다.", Toast.LENGTH_LONG).show();

                        ed1.setText(null);
                        ed2.setText(null);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

            }
        });
        finButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), EditWorkActivity.class);
                startActivity(intent);
                InputWorkOutActivity.this.finish();
            }
        });




    }

}