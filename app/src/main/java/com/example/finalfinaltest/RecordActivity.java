package com.example.finalfinaltest;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
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
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class RecordActivity extends AppCompatActivity {

    ListView listView;
    RecordAdapter adapter;
    Button delButton, goWriteButton, exitButton;
    SQLiteDatabase db;
    EditText searchView;
    boolean filterCheck = false;

    final ArrayList<RecordItem> arrayList = new ArrayList<RecordItem>();
    boolean checkAr[] = new boolean[arrayList.size()];
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record);
        listView = (ListView) findViewById(R.id.listView);
        delButton = (Button) findViewById(R.id.deleteButton);
        goWriteButton = (Button) findViewById(R.id.goWriteDiary);
        exitButton = (Button) findViewById(R.id.exitButton_record);
        searchView = (EditText) findViewById(R.id.serachText_record);

        //initDB();
        selectDB();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                adapter.setCheck(i);
                adapter.notifyDataSetChanged();

            }
        });

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(getApplicationContext(),EditDiaryActivity.class);
                intent.putExtra("title",adapter.items.get(i).getTitle());
                Toast.makeText(getApplicationContext(),"전송 : " + adapter.items.get(i).getTitle(), Toast.LENGTH_LONG).show();

                startActivity(intent);
                return true;
            }
        });

        delButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                showDialog();

            }
        });

        goWriteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), StartWorkOutActivity.class);
                startActivity(intent);
            }
        });
        exitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RecordActivity.this.finish();
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
                ((RecordAdapter)listView.getAdapter()).getFilter().filter(filterText) ;
            }
        });

    }

    public void initDB(){
        try{
            db = openOrCreateDatabase("myDiary", Activity.MODE_PRIVATE, null);
            db.execSQL("create table if not exists Diary (id integer PRIMARY KEY autoincrement, work text not null, date text not null, title text not null, content text not null);");
            db.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    public void selectDB(){
        try{
            db = openOrCreateDatabase("myDiary", Activity.MODE_PRIVATE, null);
            Cursor c1 = db.rawQuery("select work, date, title, content from Diary", null);

            while(c1.moveToNext()){
                String work = c1.getString(0);
                String date = c1.getString(1);
                String title = c1.getString(2);
                String content = c1.getString(3);

                RecordItem rI = new RecordItem(work, date, title, content);
                //test
                //RecordItem r1 = new RecordItem("풀업", "12월 25일", "그날의추억", "블라블라");

                arrayList.add(rI);
                checkAr = new boolean[arrayList.size()];
            }

            c1.close();
            db.close();
        }catch (Exception e){
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), "exception", Toast.LENGTH_LONG).show();
        }
        adapter = new RecordAdapter(getApplicationContext(), R.layout.record_item, arrayList, checkAr);
        listView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }
    public void deleteDB(String title){
        try{
            db = openOrCreateDatabase("myDiary", Activity.MODE_PRIVATE, null);
            db.execSQL("delete from Diary where title ='" + title + "'");
            db.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    public void showDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("경고").setMessage("정말 삭제하시겠습니까?");
        builder.setPositiveButton("네", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int j) {
                boolean bCheck;
                for(int i = adapter.getCount() -1; i>=0; i--){
                    bCheck = adapter.getChecked(i);
                    if(bCheck == true){
                        if(filterCheck == true){
                            deleteDB(adapter.filteredItems.get(i).getTitle());
                            arrayList.remove(adapter.filteredItems.remove(i));
                        }else{
                            deleteDB(adapter.items.get(i).getTitle());
                            arrayList.remove(i);
                        }
                    }
                }
                adapter.setAllCheck(false);
                adapter.notifyDataSetChanged();
            }
        });
        builder.setNegativeButton("아니오", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
            }
        });

        AlertDialog alertDialog = builder.create();

        alertDialog.show();
    }

    public class RecordAdapter extends BaseAdapter implements Filterable {
        public ArrayList<RecordItem> items;
        Context context;
        int mLayout;
        boolean check[] ;
        private ArrayList<RecordItem> filteredItems;

        Filter filter;
        public Filter getFilter(){
            if(filter == null){
                filter = new ListFilter();
            }
            return filter;
        }

        public RecordAdapter(Context context, int mLayout, ArrayList<RecordItem> array, boolean checkAr[]){
            this.context = context;
            this.mLayout = mLayout;
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

        public void addItem(RecordItem item){
            items.add(item);
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

            RecordItem recordItem = filteredItems.get(position);

            TextView workText = (TextView) convertView.findViewById(R.id.workText);
            //workText.setText(items.get(position).getWork());
            workText.setText(recordItem.getWork());
            TextView dateText = (TextView) convertView.findViewById(R.id.dateText);
            //dateText.setText(items.get(position).getDate());
            dateText.setText(recordItem.getDate());
            TextView titleText = (TextView) convertView.findViewById(R.id.titleText);
            //titleText.setText(items.get(position).getTitle());
            titleText.setText(recordItem.getTitle());
            TextView contentText = (TextView) convertView.findViewById(R.id.contentText);
            //contentText.setText(items.get(position).getContents());
            contentText.setText(recordItem.getContents());
            CheckBox box = (CheckBox) convertView.findViewById(R.id.checkBox1);
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
                    ArrayList<RecordItem> itemList = new ArrayList<RecordItem>();

                    for(RecordItem item : items){
                        if(item.getWork().toUpperCase().contains(charSequence.toString().toUpperCase())){
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
                filteredItems = (ArrayList<RecordItem>) filterResults.values;

                if(filterResults.count > 0){
                    notifyDataSetChanged();
                }else{
                    notifyDataSetInvalidated();
                }

            }
        }
    }
}