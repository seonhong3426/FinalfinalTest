package com.example.finalfinaltest;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
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
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class StartWorkOutActivity extends AppCompatActivity {
    Button playBtn, exitButton;
    ListView selectList;
    EditText searchView;
    final ArrayList<WorkItem> arrayList = new ArrayList<WorkItem>();
    final ArrayList<WorkItem> sendList = new ArrayList<WorkItem>();
    WorkOutAdapter adapter;
    SQLiteDatabase db;
    boolean mcheck[];
    boolean filterCheck = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_work_out);

        playBtn = (Button) findViewById(R.id.playWork);
        selectList = (ListView) findViewById(R.id.selectListView);
        searchView = (EditText) findViewById(R.id.searchText2);
        exitButton = (Button) findViewById(R.id.exitButton_start);

        //initDB();
        selectDB();

        selectList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                System.out.println("리스트뷰 번지수 : " + i);
                adapter.setCheck(i);
                adapter.notifyDataSetChanged();
            }
        });
        playBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean bCheck;
                for(int i = adapter.getCount() -1; i>=0; i--){
                    bCheck = adapter.getChecked(i);
                    if(bCheck == true){
                        //플레이버튼을 눌렸을 때 하는 동작
                        //선택된 데이터를 보낼 어레이리스트에 저장
                        if(filterCheck == true){
                            sendList.add(adapter.filteredItems.get(i));
                        }else{
                            sendList.add(arrayList.get(i));
                        }

                    }
                }
                adapter.setAllCheck(false);
                adapter.notifyDataSetChanged();
                Intent intent = new Intent(getApplicationContext(), PlayActivity.class);
                intent.putParcelableArrayListExtra("key",sendList);
                startActivity(intent);

            }

        });
        searchView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                filterCheck = true;
                String filterText = editable.toString();
                ((WorkOutAdapter)selectList.getAdapter()).getFilter().filter(filterText) ;
            }
        });
        exitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                StartWorkOutActivity.this.finish();
            }
        });
    }

    public void initDB(){
        try{
            db = openOrCreateDatabase("myDiary", Activity.MODE_PRIVATE, null);
            db.execSQL("create table if not exists PLAY (id integer PRIMARY KEY autoincrement, name text not null, body text not null);");
            db.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }


    public void selectDB(){
        try{
            db = openOrCreateDatabase("myDiary", Activity.MODE_PRIVATE, null);
            Cursor c1 = db.rawQuery("select image, name, body from PLAY", null);

            while(c1.moveToNext()){
                int image = c1.getInt(0);
                String name = c1.getString(1);
                String body = c1.getString(2);
                System.out.println(name + body);
                WorkItem workItem = new WorkItem(image, name, body);

                arrayList.add(workItem);
                mcheck = new boolean[arrayList.size()];
                System.out.println("배열 길이 : " + mcheck.length);

            }

            c1.close();
            db.close();
        }catch (Exception e){
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), "exception", Toast.LENGTH_LONG).show();
        }
        adapter = new WorkOutAdapter(getApplicationContext(), R.layout.work_item, arrayList, mcheck);
        selectList.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    /*public class WorkOutAdapter extends BaseAdapter {
        public ArrayList<WorkItem> items;
        boolean check[] ;
        Context context;
        int mLayout;

        public WorkOutAdapter(Context context, int layout, ArrayList<WorkItem> array, boolean checkAr[]){
            this.context = context;
            this.mLayout = layout;
            this.items = array;
            this.check = checkAr;

        }



        public boolean getChecked(int position){
            return check[position];
        }
        public void setCheck(int position){
            check[position] = !check[position];

        }
        public void setAllCheck(boolean value){
            for(int i = 0; i < check.length;i ++){
                check[i] = value;
            }
        }

        @Override
        public int getCount() {
            return items.size();
        }

        public void addItem(WorkItem item) {
            items.add(item);
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

            TextView workName = (TextView) convertView.findViewById(R.id.workName);
            workName.setText(items.get(position).workName);

            TextView bodyPart = (TextView) convertView.findViewById(R.id.bodyPart);
            bodyPart.setText(items.get(position).bodyPart);

            CheckBox box = (CheckBox) convertView.findViewById(R.id.checkBox);
            box.setChecked(check[position]);

            return convertView;
        }
    }*/
    public class WorkOutAdapter extends BaseAdapter implements Filterable {
        public ArrayList<WorkItem> items;
        private ArrayList<WorkItem> filteredItems;
        boolean check[] ;
        Context context;
        int mLayout;

        Filter filter;

        public Filter getFilter(){
            if(filter == null){
                filter = new ListFilter();
            }
            return filter;
        }

        public WorkOutAdapter(Context context, int layout, ArrayList<WorkItem> array, boolean checkAr[]){
            this.context = context;
            this.mLayout = layout;
            this.items = array;
            this.check = checkAr;
            this.filteredItems = items;
        }



        public boolean getChecked(int position){
            return check[position];
        }
        public void setCheck(int position){
            check[position] = !check[position];

        }
        public void setAllCheck(boolean value){
            for(int i = 0; i < check.length;i ++){
                check[i] = value;
            }
        }

        @Override
        public int getCount() {
            return filteredItems.size();
        }


        @Override
        public Object getItem(int position) {
            return filteredItems.get(position);
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

            WorkItem workItem = filteredItems.get(position);

            ImageView imageView = (ImageView) convertView.findViewById(R.id.imageView);
            //imageView.setImageResource(items.get(position).getResId());
            imageView.setImageResource(workItem.getResId());

            TextView workName = (TextView) convertView.findViewById(R.id.workName);
            //workName.setText(items.get(position).getWorkName());
            workName.setText(workItem.getWorkName());

            TextView bodyPart = (TextView) convertView.findViewById(R.id.bodyPart);
            //.setText(items.get(position).getBodyPart());
            bodyPart.setText(workItem.getBodyPart());

            CheckBox box = (CheckBox) convertView.findViewById(R.id.checkBox);
            box.setChecked(check[position]);


            return convertView;
        }

        private class ListFilter extends Filter{


            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                FilterResults results = new FilterResults() ;

                if(charSequence == null || charSequence.length() == 0){
                    results.values = items;
                    results.count = items.size();
                }else{
                    ArrayList<WorkItem> itemList = new ArrayList<WorkItem>();

                    for(WorkItem item : items){
                        if(item.getBodyPart().toUpperCase().contains(charSequence.toString().toUpperCase())){
                            itemList.add(item);
                        }
                    }

                    results.values = itemList;
                    results.count = itemList.size();
                }

                return results;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                filteredItems = (ArrayList<WorkItem>) filterResults.values;

                if(filterResults.count > 0){
                    notifyDataSetChanged();
                }else{
                    notifyDataSetInvalidated();
                }

            }
        }
    }


}