package com.example.finalfinaltest;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.media.Image;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class EditWorkActivity extends AppCompatActivity {

    Button addBtn, delBtn, exitButton;
    ListView editListView;
    SQLiteDatabase db;
    WorkOutAdapter adapter;
    final ArrayList<WorkItem> arrayList = new ArrayList<WorkItem>();
    boolean mcheck[] = new boolean[arrayList.size()];
    boolean filterCheck = false;
    EditText searchView ;






    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_work);

        init();
        //initDB();
        selectDB();

        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), InputWorkOutActivity.class);
                startActivity(intent);
                EditWorkActivity.this.finish();
            }
        });
        exitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditWorkActivity.this.finish();
            }
        });

        editListView.setAdapter(adapter);

        editListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                System.out.println("리스트뷰 번지수 : " + i);
                adapter.setCheck(i);
                adapter.notifyDataSetChanged();
            }
        });
        delBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean bCheck;
                for(int i = adapter.getCount() -1; i>=0; i--){
                    bCheck = adapter.getChecked(i);
                    if(bCheck == true){
                        if(filterCheck == false){
                            deleteDB(adapter.items.get(i).getWorkName());
                            arrayList.remove(i);
                        }else{
                            deleteDB(adapter.filteredItems.get(i).getWorkName());
                            arrayList.remove(adapter.filteredItems.remove(i));

                        }

                    }
                }
                adapter.setAllCheck(false);
                adapter.notifyDataSetChanged();
            }
        });
        searchView.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable editable) {
                filterCheck = true;
                String filterText = editable.toString();
                ((WorkOutAdapter)editListView.getAdapter()).getFilter().filter(filterText) ;
            }
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }


        });


    }
    public void init(){
        addBtn = (Button) findViewById(R.id.addWork);
        delBtn = (Button) findViewById(R.id.delWork);

        editListView = (ListView) findViewById(R.id.editListView);
        searchView = (EditText) findViewById(R.id.searchText);
        exitButton = (Button) findViewById(R.id.exitButton_editWork);

    }

    public void initDB(){
        try{
            db = openOrCreateDatabase("myDiary", Activity.MODE_PRIVATE, null);
            db.execSQL("create table if not exists PLAY (id integer PRIMARY KEY autoincrement, image int, name text not null, body text not null);");
            db.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void deleteDB(String name){
        try{
            db = openOrCreateDatabase("myDiary", Activity.MODE_PRIVATE, null);
            db.execSQL("delete from PLAY where name ='" + name + "'");
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
                int resId = c1.getInt(0);
                String name = c1.getString(1);
                String body = c1.getString(2);
                System.out.println(resId + name + body);

                WorkItem workItem = new WorkItem(resId, name, body);

                arrayList.add(workItem);
                mcheck = new boolean[arrayList.size()];

            }

            c1.close();
            db.close();
        }catch (Exception e){
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), "exception", Toast.LENGTH_LONG).show();
        }
        adapter = new WorkOutAdapter(getApplicationContext(), R.layout.work_item, arrayList, mcheck);
        editListView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }



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