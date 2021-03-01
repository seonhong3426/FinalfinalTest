package com.example.finalfinaltest;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.media.Image;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.lang.reflect.Array;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.SimpleTimeZone;

public class PlayActivity extends AppCompatActivity {

    ListView playList;
    ArrayList<WorkItem> arrayList;
    WorkOutAdapter adapter;
    Button storeBtn, exitBtn;
    SQLiteDatabase db;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play);
        init();

        long now = System.currentTimeMillis();
        Date mDate = new Date(now);

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String getTIme = simpleDateFormat.format(mDate);

        adapter = new WorkOutAdapter(getApplicationContext(), R.layout.playworkitem, arrayList);
        playList.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        //입력상자
        final EditText dateTxt = (EditText) findViewById(R.id.inputDate);
        final EditText titleTxt = (EditText) findViewById(R.id.inputTitle);
        final EditText contentTxt = (EditText) findViewById(R.id.inputContents);
        dateTxt.setText(getTIme);

        storeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //대화상자 구현하기
                //데이터베이스 정보 입력
                String date = dateTxt.getText().toString().trim();
                String title = titleTxt.getText().toString().trim();
                String content = contentTxt.getText().toString().trim();
                String work = "";
                for(int i = 0; i < arrayList.size(); i++)
                {
                    work += arrayList.get(i).workName + " ";
                }
                try {
                    db = openOrCreateDatabase("myDiary", Activity.MODE_PRIVATE, null);
                    db.execSQL("Insert into Diary (work, date, title, content) values" +
                            " ('" + work + "','" + date + "', '" + title + "', '" + content + "')");
                    db.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }

                Intent intnet = new Intent(getApplicationContext(), RecordActivity.class);
                startActivity(intnet);
            }
        });

        exitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PlayActivity.this.finish();
            }
        });

    }

    public void init(){

        arrayList = new ArrayList<WorkItem>();
        arrayList = getIntent().getParcelableArrayListExtra("key");
        System.out.println("받은 데이터 : " + arrayList);
        //리스트 뷰
        playList = (ListView) findViewById(R.id.doList);


        //버튼
        storeBtn = (Button) findViewById(R.id.storeButton);
        exitBtn = (Button) findViewById(R.id.exitButton);


    }

    public class WorkOutAdapter extends BaseAdapter {
        public ArrayList<WorkItem> items;
        Context context;
        int mLayout;

        public WorkOutAdapter(Context context, int layout, ArrayList<WorkItem> array){
            this.context = context;
            this.mLayout = layout;
            this.items = array;
            System.out.println("아이템 리스트 사이즈 : " + items.size());
        }

        public void setListViewHeightBasedOnChildren(ListView listView){
            ListAdapter listAdapter = listView.getAdapter();
            if(listAdapter == null){
                return;
            }
            int totalHeight = 0;
            for(int i = 0; i < listAdapter.getCount(); i++){
                View listItem = listAdapter.getView(i,null,listView);
                listItem.measure(0,0);
                totalHeight += listItem.getMeasuredHeight();
            }

            ViewGroup.LayoutParams params = listView.getLayoutParams();
            params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount()-1));
            listView.setLayoutParams(params);
            listView.requestLayout();
        }

        @Override
        public int getCount() {
            return items.size();
        }

        @Override
        public Object getItem(int position) {
            return items.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if(convertView == null){
                LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = inflater.inflate(mLayout,null);

            }

            ImageView imageView = (ImageView) convertView.findViewById(R.id.imageView);
            imageView.setImageResource(items.get(position).getResId());
            TextView workName = (TextView) convertView.findViewById(R.id.playName);
            workName.setText(items.get(position).workName);
            TextView bodyPart = (TextView) convertView.findViewById(R.id.playBody);
            bodyPart.setText(items.get(position).bodyPart);

            return convertView;
        }

    }

}